package io.github.wynn5a.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.github.wynn5a.service.protobuf.HelloServiceProto; // Assuming this will be generated

public class ServiceServer {

  private final int port;
  private final Object serviceImpl;

  public ServiceServer(int port, Object serviceImpl) {
    this.port = port;
    this.serviceImpl = serviceImpl;
  }

  public void start() throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
              ch.pipeline().addLast(
                  new ProtobufVarint32FrameDecoder(),
                  new ProtobufDecoder(HelloServiceProto.HelloRequest.getDefaultInstance()),
                  new ProtobufVarint32LengthFieldPrepender(),
                  new ProtobufEncoder(),
                  new RpcServerHandler(serviceImpl)
              );
            }
          });

      ChannelFuture f = b.bind(port).sync();
      System.out.println("Server started on port: " + port);
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
