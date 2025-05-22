package io.github.wynn5a.core;

import java.lang.reflect.Proxy;

public class NettyRpcCore {

  public static void export(Object service, int port) {
    try {
      new ServiceServer(port, service).start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(Class<T> serviceInterface, String host, int port) {
    return (T) Proxy.newProxyInstance(
        serviceInterface.getClassLoader(),
        new Class<?>[]{serviceInterface},
        new NettyClientInvocationHandler(host, port)
    );
  }
}
