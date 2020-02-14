package com.codahale.grpcproxy;

import java.io.IOException;
import okhttp3.HttpUrl;
import org.junit.Test;

/** @Author: yaozh @Description: */
public class ProxyRpcServerTest {
  @Test
  public void Test() throws IOException, InterruptedException {
    String upstream = "http://localhost:8080/grpc";
    int port = 50051;
    final ProxyRpcServer server = new ProxyRpcServer(port, null, HttpUrl.parse(upstream));
    server.start();
    server.blockUntilShutdown();
  }
}
