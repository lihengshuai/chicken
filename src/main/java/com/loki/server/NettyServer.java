package com.loki.server;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;

/**
 * @author loki on 2017/9/17.
 */
public class NettyServer {

  public static void main(String[] args) {

    ServerBootstrap bootstrap = new ServerBootstrap();

    ExecutorService boss = Executors.newCachedThreadPool();
    ExecutorService worker = Executors.newCachedThreadPool();

    bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
    final HashedWheelTimer timer = new HashedWheelTimer();

    bootstrap.setPipelineFactory(() -> {
      ChannelPipeline pipeline = Channels.pipeline();
      pipeline.addLast("idle", new IdleStateHandler(timer, 5, 5, 10));
      pipeline.addLast("encoder", new StringEncoder());
      pipeline.addLast("decoder", new StringDecoder());
      pipeline.addLast("Handler", new HelloHandler());
      return pipeline;
    });
    bootstrap.bind(new InetSocketAddress(8000));
    System.out.println("server open !");
  }
}
