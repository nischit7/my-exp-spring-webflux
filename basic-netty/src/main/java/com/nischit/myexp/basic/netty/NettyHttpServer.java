package com.nischit.myexp.basic.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
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
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Basic http server by using Netty.
 * It is just a demonstration of where to add handlers.
 * At a high level, Spring webflux underneath uses Netty similar approach.
 */
public class NettyHttpServer {

    private static final int MAX_THREAD = 10;
    private static final int SO_BACKLOG = 128;
    private static final int MAX_CONTENT_LEN = 512 * 1024;
    private static final int SERVER_PORT = 8080;

    private ChannelFuture channel;
    private final EventLoopGroup masterGroup;
    private final EventLoopGroup slaveGroup;
    private EventExecutorGroup blockingCallThreadgroup = new DefaultEventExecutorGroup(MAX_THREAD);

    public NettyHttpServer() {
        masterGroup = new NioEventLoopGroup();
        slaveGroup = new NioEventLoopGroup();
    }

    /**
     * Starts the Netty HTTP server.
     */
    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                shutdown();
            }
        });

        try {
            final ServerBootstrap bootstrap =
                    new ServerBootstrap()
                            .group(masterGroup, slaveGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ServerInitializer())
                            .option(ChannelOption.SO_BACKLOG, SO_BACKLOG)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);
            channel = bootstrap.bind(SERVER_PORT).sync();
            //channels.add(bootstrap.bind(8080).sync());
        } catch (final InterruptedException e) {
            // Nothing to do
        }
    }

    /**
     * Shuts down the http server.
     */
    public void shutdown() {
        slaveGroup.shutdownGracefully();
        masterGroup.shutdownGracefully();

        try {
            channel.channel().closeFuture().sync();
        } catch (final InterruptedException e) {
            // Nothing to do
        }
    }

    /**
     * Starter main method.
     *
     * @param args Arguments.
     */
    public static void main(final String[] args) {
        new NettyHttpServer().start();
    }

    private class ServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        public void initChannel(final SocketChannel ch) {
            ch.pipeline().addLast("codec", new HttpServerCodec());
            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(MAX_CONTENT_LEN));
            ch.pipeline().addLast("request", new MyChannelInboundHandlerAdapter());
            ch.pipeline().addLast(blockingCallThreadgroup, "another", new MyAnotherInboundHandlerAdapter());
        }
    }

    private class MyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            if (msg instanceof FullHttpRequest) {
                final FullHttpRequest request = (FullHttpRequest)msg;
                final String responseMessage = "Hello from Netty!";
                final FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(responseMessage.getBytes())
                );

                if (HttpUtil.isKeepAlive(request)) {
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                }
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, responseMessage.length());

                ctx.writeAndFlush(response);
            } else {
                super.channelRead(ctx, msg);
            }
        }

        @Override
        public void channelReadComplete(final ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.INTERNAL_SERVER_ERROR,
                    Unpooled.copiedBuffer(cause.getMessage().getBytes())
            ));
        }
    }

    private class MyAnotherInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) {

            ctx.fireChannelRead(msg);
        }
    }
}
