private void stop() throws InterruptedException {
  canStop.await();
  executor.shutdownNow();
}
