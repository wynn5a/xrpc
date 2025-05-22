package io.github.wynn5a.provider;

import io.github.wynn5a.core.NettyRpcCore;
import io.github.wynn5a.service.HelloService;

public class ProviderApplication {

  public static void main(String[] args) {
    HelloService helloService = new HelloServiceImpl();
    NettyRpcCore.export(helloService, 2233);
    System.out.println("HelloService exported on port 2233");

    // Keep the main thread alive
    synchronized (ProviderApplication.class) {
      try {
        ProviderApplication.class.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
