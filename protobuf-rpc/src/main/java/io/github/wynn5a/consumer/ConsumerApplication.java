package io.github.wynn5a.consumer;

import io.github.wynn5a.core.NettyRpcCore;
import io.github.wynn5a.service.HelloService;

public class ConsumerApplication {

  public static void main(String[] args) {
    HelloService service = NettyRpcCore.get(HelloService.class, "127.0.0.1", 2233);
    if (service != null) {
      try {
        String result = service.sayHello("Netty RPC User");
        System.out.println("RPC Call Result: " + result);
      } catch (Exception e) {
        System.err.println("RPC Call failed: " + e.getMessage());
        e.printStackTrace();
      }
    } else {
      System.err.println("Failed to get service proxy.");
    }
  }
}
