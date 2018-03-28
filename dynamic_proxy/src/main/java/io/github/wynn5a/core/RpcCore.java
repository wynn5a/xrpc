package io.github.wynn5a.core;

/**
 * @author Fuwenming
 * @create 2018/3/27
 **/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Fuwenming
 * @create 2018/3/27
 */
public class RpcCore {

    /**
     * expose service at port
     */
    public static void export(final Object service, int port) {
        if (service == null) {
            throw new IllegalArgumentException("service instance should not be null!");
        }
        checkPort(port);
        new Thread(() -> {
            System.out.println("Export service " + service.getClass().getName() + " on port: " + port);
            ServerSocket server = null;
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            while (true) {
                try (Socket socket = server.accept()) {
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    String methodName = input.readUTF();
                    Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                    Object[] arguments = (Object[]) input.readObject();
                    try {
                        Method method = service.getClass().getMethod(methodName, parameterTypes);
                        Object result = method.invoke(service, arguments);
                        output.writeObject(result);
                    } catch (Throwable t) {
                        output.writeObject(t);
                    } finally {
                        output.close();
                        input.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * get service from remote
     */
    public static <T> T get(final Class<T> clazz, final String host, final int port) {
        if (clazz == null)
            throw new IllegalArgumentException("service class should not be null!");
        if (!clazz.isInterface())
            throw new IllegalArgumentException("The " + clazz.getName() + " should be interface class!");
        if (host == null || host.length() == 0)
            throw new IllegalArgumentException("host should not be null or empty!");
        checkPort(port);
        System.out.println("try to get remote service " + clazz.getName() + " from server " + host + ":" + port);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new RpcRemoteCaller(host, port));
    }

    private static void checkPort(int port) {
        if (port <= 0 || port > 0xFFFF) {
            throw new IllegalArgumentException("port out of range: " + port);
        }
    }

}
