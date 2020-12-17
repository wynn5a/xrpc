package io.github.wynn5a.service;

public class HelloService implements Hello {

  @Override
  public String hello(String name) {
    return "hello, " + name;
  }
}
