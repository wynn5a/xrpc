package io.github.wynn5a.provider;

import io.github.wynn5a.service.HelloService;

public class HelloServiceImpl implements HelloService {

  @Override
  public String sayHello(String name) {
    return "Hello, " + name + " (from Netty)";
  }
}
