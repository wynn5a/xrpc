package io.github.wynn5a.core;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * serve the exposed service
 */
public class ServiceServer {

  private final int port;

  public ServiceServer(int port) {
    this.port = port;
  }

  public void start() {
    //accepts an incoming connection
    EventLoopGroup boss = new NioEventLoopGroup();
    //handles the traffic of the accepted connection
    //the boss accepts the connection and registers the accepted connection to the worker
    EventLoopGroup worker = new NioEventLoopGroup();
  }

}
