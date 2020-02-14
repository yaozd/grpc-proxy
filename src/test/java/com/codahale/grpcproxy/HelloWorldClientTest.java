package com.codahale.grpcproxy;

import javax.net.ssl.SSLException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @Author: yaozh @Description: */
public class HelloWorldClientTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldClientTest.class);

  @Test
  public void Test() throws SSLException, InterruptedException {
    String hostname = "localhost";
    int port = 50051;
    final HelloWorldClient client = new HelloWorldClient(hostname, port, null);
    String greet = client.greet(100);
    LOGGER.info("greet:{}", greet);
    client.shutdown();
  }
}
