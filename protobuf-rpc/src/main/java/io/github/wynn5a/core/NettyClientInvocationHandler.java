package io.github.wynn5a.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.github.wynn5a.service.protobuf.HelloRequest; // Generated
import io.github.wynn5a.service.protobuf.HelloResponse; // Generated
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
    // Assuming the method is sayHello and args[0] is the name
    String name = (String) args[0];
    HelloRequest protoRequest = HelloRequest.newBuilder().setName(name).build();

    RpcClientHandler clientHandler = new RpcClientHandler(); // Needs to be adapted for HelloResponse
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
              ch.pipeline().addLast(
                  new ProtobufVarint32FrameDecoder(),
                  new ProtobufDecoder(HelloResponse.getDefaultInstance()), // Decode HelloResponse
                  new ProtobufVarint32LengthFieldPrepender(),
                  new ProtobufEncoder(),
                  clientHandler // RpcClientHandler will handle HelloResponse
              );
            }
          });

      ChannelFuture future = b.connect(host, port).sync();
      future.channel().writeAndFlush(protoRequest).sync();
      future.channel().closeFuture().sync(); // Wait until channel is closed

      HelloResponse protoResponse = clientHandler.getResponse(); // RpcClientHandler getResponse() must return HelloResponse
      // Error handling would ideally be part of the HelloResponse protobuf definition
      // For now, assume success if we get a response.
      return protoResponse.getGreeting();
    } finally {
      group.shutdownGracefully();
    }
  }
}
