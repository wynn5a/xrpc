package io.github.wynn5a.provider;

import io.github.wynn5a.service.HelloService;

/**
 * @author Fuwenming
 * @create 2018/3/27
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }
}
