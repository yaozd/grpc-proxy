package com.codahale.grpcproxy;

import com.codahale.grpcproxy.util.TlsContext;
import org.junit.Test;

import javax.net.ssl.SSLException;
import java.io.IOException;

/**
 * @Author: yaozh
 * @Description:
 */
public class HelloWorldServerTest {
    @Test
    public void Test() throws IOException, InterruptedException {
        int port = 50051;
        final HelloWorldServer server = new HelloWorldServer(port, null);
        server.start();
        //new TerminationUtil().blockUntilShutdown();
    }
}