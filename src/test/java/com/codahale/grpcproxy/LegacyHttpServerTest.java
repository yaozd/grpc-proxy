package com.codahale.grpcproxy;

import org.junit.Test;

/** @Author: yaozh @Description: */
public class LegacyHttpServerTest {
  @Test
  public void Test() throws Exception {
    int port = 8080;
    int threads = 100;
    final LegacyHttpServer server = new LegacyHttpServer(port, threads);
    server.start();
    new TerminationUtil().blockUntilShutdown();
  }
}
