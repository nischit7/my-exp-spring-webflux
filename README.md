# Experiments on spring webflux

Play with various abilities of webflux.

# basic-netty

Helps to understand, how to start and run at the most basic level. It sets a premise for how webflux might be using it underneath.

# spring-async

Runs webflux with Jetty as the underlying NIO server

# webflux-netty

Runs webflux with Netty as the underlying NIO server

Execute various unit tests to get a perspective of Mono/Flux, subscribeOn, publishOn
https://github.com/nischit7/my-exp-spring-webflux/blob/master/webflux-netty/src/test/java/com/nischit/myexp/webflux/netty/api/TeamControllerTest.java

Execute a integration test and validate spring webflux behavior
https://github.com/nischit7/my-exp-spring-webflux/blob/master/webflux-netty/src/it/java/com/nischit/myexp/webflux/netty/MyAppIT.java

