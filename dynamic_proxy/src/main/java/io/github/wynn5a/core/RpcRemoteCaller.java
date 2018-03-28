package io.github.wynn5a.core;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * dynamic proxy for calling remote service
 *
 * @author Fuwenming
 * @create 2018/3/28
 **/
public class RpcRemoteCaller implements InvocationHandler {
    private String host;
    private int port;

    public RpcRemoteCaller(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            output.writeUTF(method.getName());
            output.writeObject(method.getParameterTypes());
            output.writeObject(args);
            try {
                Object result = input.readObject();
                if (result instanceof Throwable) {
                    throw (Throwable) result;
                }
                return result;
            } finally {
                input.close();
                output.close();
            }
        }
    }
}
