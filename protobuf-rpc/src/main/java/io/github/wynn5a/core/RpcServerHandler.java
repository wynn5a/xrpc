package io.github.wynn5a.core;

import io.github.wynn5a.service.protobuf.HelloRequest;
import io.github.wynn5a.service.protobuf.HelloResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;

public class RpcServerHandler extends SimpleChannelInboundHandler<HelloRequest> {

  private final Object serviceImpl;

  public RpcServerHandler(Object serviceImpl) {
    this.serviceImpl = serviceImpl;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, HelloRequest protoRequest) throws Exception {
    HelloResponse.Builder responseBuilder = HelloResponse.newBuilder();
    try {
      String name = protoRequest.getName();
      Method method = serviceImpl.getClass().getMethod("sayHello", String.class);
      Object result = method.invoke(serviceImpl, name);
      responseBuilder.setGreeting((String) result);
    } catch (Throwable t) {
      // Handle error, potentially by setting an error field in the response if your .proto supports it
      // For now, we'll just print and send an empty response or a response with an error message if defined
      System.err.println("Error processing request: " + t.getMessage());
      t.printStackTrace();
      // Example: responseBuilder.setError(t.getMessage()); // If HelloResponse had an error field
    }
    ctx.writeAndFlush(responseBuilder.build());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
