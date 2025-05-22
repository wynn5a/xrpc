package io.github.wynn5a.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

  private RpcResponse response;

  public RpcResponse getResponse() {
    return response;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
    this.response = msg;
    // Consider using a CountDownLatch or CompletableFuture here if the client needs to wait more robustly
    // For now, NettyClientInvocationHandler will get the response after channel close
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
