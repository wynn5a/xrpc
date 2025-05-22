package io.github.wynn5a.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class NettyClientInvocationHandler implements InvocationHandler {

  private final String host;
  private final int port;

  public NettyClientInvocationHandler(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    RpcRequest request = new RpcRequest();
    request.setMethodName(method.getName());
    request.setParameterTypes(method.getParameterTypes());
    request.setArguments(args);

    RpcClientHandler clientHandler = new RpcClientHandler();
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
              ch.pipeline().addLast(
                  new ObjectEncoder(),
                  new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                  clientHandler
              );
            }
          });

      ChannelFuture future = b.connect(host, port).sync();
      future.channel().writeAndFlush(request).sync();
      future.channel().closeFuture().sync(); // Wait until channel is closed

      RpcResponse response = clientHandler.getResponse();
      if (response.getError() != null) {
        throw response.getError();
      }
      return response.getResult();
    } finally {
      group.shutdownGracefully();
    }
  }
}
