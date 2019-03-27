package com.nischit.myexp.basic.netty;

import static io.netty.buffer.Unpooled.copiedBuffer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NettyHttpServer {
    private ChannelFuture channel;
    private final EventLoopGroup masterGroup;
    private final EventLoopGroup slaveGroup;
    private EventExecutorGroup blockingCallThreadgroup = new DefaultEventExecutorGroup(10);

    public NettyHttpServer() {
        masterGroup = new NioEventLoopGroup();
        slaveGroup = new NioEventLoopGroup();
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() { shutdown(); }
        });

        try {
            final ServerBootstrap bootstrap =
                    new ServerBootstrap()
                            .group(masterGroup, slaveGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ServerInitializer())
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);
            channel = bootstrap.bind(8080).sync();
            //channels.add(bootstrap.bind(8080).sync());
        }
        catch (final InterruptedException e) { }
    }

    public void shutdown() {
        slaveGroup.shutdownGracefully();
        masterGroup.shutdownGracefully();

        try {
            channel.channel().closeFuture().sync();
        } catch (InterruptedException e) { }
    }

    public static void main(String[] args) {
        new NettyHttpServer().start();
    }

    private class ServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        public void initChannel(final SocketChannel ch) {
            ch.pipeline().addLast("codec", new HttpServerCodec());
            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(512*1024));
            ch.pipeline().addLast("request", new MyChannelInboundHandlerAdapter());
            ch.pipeline().addLast("another", new MyAnotherInboundHandlerAdapter());
        }
    }

    private class MyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof FullHttpRequest) {
                final FullHttpRequest request = (FullHttpRequest) msg;
                final String responseMessage = "Hello from Netty!";
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        copiedBuffer(responseMessage.getBytes())
                );

                if (HttpUtil.isKeepAlive(request)) {
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                }
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, responseMessage.length());

                ctx.writeAndFlush(response);
            } else {
                super.channelRead(ctx, msg);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.INTERNAL_SERVER_ERROR,
                    copiedBuffer(cause.getMessage().getBytes())
            ));
        }
    }

    private class MyAnotherInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {

            ctx.fireChannelRead(msg);
        }
    }
}
