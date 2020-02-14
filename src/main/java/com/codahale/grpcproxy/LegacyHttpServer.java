/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codahale.grpcproxy;

import com.codahale.grpcproxy.helloworld.HelloReply;
import com.codahale.grpcproxy.helloworld.HelloRequest;
import io.airlift.airline.Command;
import io.airlift.airline.Option;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An HTTP/1.1 server which parses protobuf messages in request bodies and emits protobuf messages
 * in response bodies. Implements, in its own way, the {@code helloworld.Greeter} service.
 */
public class LegacyHttpServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(LegacyHttpServer.class);
  private final Server server;

  public LegacyHttpServer(int port, int threads) {
    this.server = new Server(new QueuedThreadPool(threads));
    server.setHandler(
        new AbstractHandler() {
          @Override
          public void handle(
              String target,
              Request baseRequest,
              HttpServletRequest request,
              HttpServletResponse response)
              throws IOException {
            final String method = baseRequest.getParameter("method");
            if ("helloworld.Greeter/SayHello".equals(method)) {
              baseRequest.setHandled(true);
              sayHello(baseRequest, response);
            }
          }
        });

    final ServerConnector connector = new ServerConnector(server);
    connector.setPort(port);
    server.addConnector(connector);
  }

  public void start() throws Exception {
    Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    server.start();
  }

  private void stop() {
    try {
      server.stop();
    } catch (Exception e) {
      LOGGER.error("Error shutting down server", e);
    }
  }

  private void sayHello(Request request, HttpServletResponse response) throws IOException {
    final HelloRequest req = HelloRequest.parseFrom(request.getInputStream());
    final String greeting = "Hello " + req.getName();
    final HelloReply resp = HelloReply.newBuilder().setMessage(greeting).build();
    resp.writeTo(response.getOutputStream());
  }

  @Command(name = "http", description = "Run a legacy HTTP/Protobuf HelloWorld service.")
  public static class Cmd implements Runnable {

    @Option(
      name = {"-p", "--port"},
      description = "the port to listen on"
    )
    private int port = 8080;

    @Option(
      name = {"-c", "--threads"},
      description = "the number of worker threads to use"
    )
    private int threads = 100;

    @Override
    public void run() {
      final LegacyHttpServer server = new LegacyHttpServer(port, threads);
      try {
        server.start();
      } catch (Exception e) {
        LOGGER.error("Error starting server", e);
      }
    }
  }
}
