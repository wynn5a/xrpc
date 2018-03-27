package io.github.wynn5a.provider;

import io.github.wynn5a.core.RpcCore;

/**
 * @author Fuwenming
 * @create 2018/3/27
 **/
public class ProviderApplication {
    public static void main(String[] args) {
        try {
            RpcCore.export(new HelloServiceImpl(), 1122);
            RpcCore.export(new HelloServiceImpl(), 1123);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {

        }
    }
}
