package com.codahale.grpcproxy;

import java.io.IOException;
import org.junit.Test;

/** @Author: yaozh @Description: */
public class HelloWorldServerTest {
  @Test
  public void Test() throws IOException, InterruptedException {
    int port = 50051;
    final HelloWorldServer server = new HelloWorldServer(port, null);
    server.start();
    // new TerminationUtil().blockUntilShutdown();
  }
}
