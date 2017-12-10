package com.loki.server;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author   loki on 2017/9/17.
 */
public class HelloHandler extends SimpleChannelHandler {

    private static final Logger logger = LoggerFactory.getLogger(HelloHandler.class);

    /**
     * 事件触发
     */
    @Override
    public void handleUpstream(final ChannelHandlerContext ctx, ChannelEvent evt)
        throws Exception {

        if(evt instanceof IdleStateEvent)
        {
            //No data was either received or sent for a while.
            if( ((IdleStateEvent) evt).getState() == IdleState.ALL_IDLE )
            {

                System.out.println("发送了一次心跳");
                //写数据返回个客户端
//                ChannelFuture write = ctx.getChannel().write("瓜皮一号");
                //给write方法添加一个侦听器
                //一开始，没有进行读写操作的时候，强制关闭通道。
//                write.addListener(new ChannelFutureListener() {
//
//                    @Override
//                    public void operationComplete(ChannelFuture write) throws Exception {
//
//                        // TODO Auto-generated method stub
//
//                        ctx.getChannel().close();//关闭通道
//                    }
//                });

                System.out.println("提示玩家下线");
                //写数据返回个客户端
                ChannelFuture write = ctx.getChannel().write("time out you lost");
                //给write方法添加一个侦听器
                //一开始，没有进行读写操作的时候，强制关闭通道。
                write.addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture write) throws Exception {

                        // TODO Auto-generated method stub

                        ctx.getChannel().close();//关闭通道
                    }
                });

            }
        }else{
            // TODO Auto-generated method stub
            super.handleUpstream(ctx, evt);
        }



    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        logger.info("message  received !");
        logger.info("get message info :"+e.getMessage());
        //send message
            ctx.getChannel().write("hi");
            super.messageReceived(ctx, e);
//            Thread.sleep(5000);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.warn("there  is a  exception happen !");
        super.exceptionCaught(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("channel connected !");
        ctx.getChannel().write("welcome !");
        super.channelConnected(ctx, e);

    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);
    }
}
