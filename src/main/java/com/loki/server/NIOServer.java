package com.loki.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * this is a NIO server demo
 * @author  loki on 2017/9/17.
 */
public class NIOServer {

    private static final Logger logger  = LoggerFactory.getLogger(NIOServer.class);

    private Selector selector;//

    public void initServer(int port) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();

        channel.configureBlocking(false);//是否阻塞

        channel.socket().bind(new InetSocketAddress(port));//绑定 端口

        this.selector =  Selector.open();

        channel.register(selector, SelectionKey.OP_ACCEPT);//注册
    }


    public void listen() throws IOException {

        logger.info("server is listing !");

        //拿到管道 里的key 值,进行 处理
        while (true){
            logger.info("selector is waiting to handle ..");
            selector.select();
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

            if (iterator.hasNext()){
                SelectionKey  key = iterator.next();
                iterator.remove();
                handle(key);
            }
        }
    }

    public void handle(SelectionKey key) throws IOException {

        if (key.isAcceptable()){
            handleKeyAcceptable(key);
        }else if (key.isReadable()){
            handleKeyReadable(key);
        }else {
            logger.warn("key is invalid!");
        }
    }

    public void handleKeyAcceptable(SelectionKey  key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel)key.channel();
        SocketChannel channel = server.accept();
        channel.configureBlocking(false);
        channel.register(this.selector,SelectionKey.OP_READ);
    }

    public void handleKeyReadable(SelectionKey key) throws IOException {

        SocketChannel channel = (SocketChannel)key.channel();

        ByteBuffer  byteBuffer  = ByteBuffer.allocate(2046);
        int read = channel.read(byteBuffer);

        if (read > 0){
            byte[] array = byteBuffer.array();
            System.out.println(("收到的消息 ："+new String(array).trim()));

            ByteBuffer outBuffer = ByteBuffer.wrap("OK".getBytes());
            channel.write(outBuffer);
        }else {
            logger.info("client  closed !");
            key.cancel();
        }
    }


    public static void main(String[] args) throws IOException {
        NIOServer  server = new NIOServer();
        server.initServer(8000);
        server.listen();
    }
}
