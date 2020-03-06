public void start() throws IOException {
  reactor=new NioReactor(new ThreadPoolDispatcher(2));
  LoggingHandler loggingHandler=new LoggingHandler();
  reactor.registerChannel(tcpChannel(6666,loggingHandler)).registerChannel(tcpChannel(6667,loggingHandler)).registerChannel(udpChannel(6668,loggingHandler)).start();
}
