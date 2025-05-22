package io.github.wynn5a.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

  private final Object serviceImpl;

  public RpcServerHandler(Object serviceImpl) {
    this.serviceImpl = serviceImpl;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
    RpcResponse response = new RpcResponse();
    try {
      Method method = serviceImpl.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
      Object result = method.invoke(serviceImpl, request.getArguments());
      response.setResult(result);
    } catch (Throwable t) {
      response.setError(t);
    }
    ctx.writeAndFlush(response);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
