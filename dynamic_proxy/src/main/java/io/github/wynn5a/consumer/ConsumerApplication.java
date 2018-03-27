package io.github.wynn5a.consumer;

import io.github.wynn5a.core.RpcCore;
import io.github.wynn5a.service.HelloService;

/**
 * @author Fuwenming
 * @create 2018/3/27
 **/
public class ConsumerApplication {
    public static void main(String[] args) {
        try {
            HelloService service = RpcCore.get(HelloService.class, "127.0.0.1", 1122);
            System.out.println(service.sayHello("world"));
            System.out.println(service.sayHello("world2"));


            HelloService service2 = RpcCore.get(HelloService.class, "127.0.0.1", 1123);
            System.out.println(service2.sayHello("世界"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
