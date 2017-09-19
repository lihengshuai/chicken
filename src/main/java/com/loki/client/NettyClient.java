package com.loki.client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author   loki on 2017/9/19.
 */
public class NettyClient {

    public static void main(String[] args) {
        ClientBootstrap bootstrap = new ClientBootstrap();

        ExecutorService worker = Executors.newCachedThreadPool();
        ExecutorService boss = Executors.newCachedThreadPool();

        bootstrap.setFactory(new NioClientSocketChannelFactory(boss,worker));

        bootstrap.setPipelineFactory( () -> {
            ChannelPipeline channelPipeline = Channels.pipeline();
            channelPipeline.addLast("decoder",new StringDecoder());
            channelPipeline.addLast("encoder",new StringEncoder());
            channelPipeline.addLast("handler",new HiHandler());
            return channelPipeline;
        });

        ChannelFuture connect = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
        Channel channel = connect.getChannel();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("请输入");
            channel.write(scanner.next());
        }
    }
}
