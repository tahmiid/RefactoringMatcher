/** 
 * Acquire the globalMutex lock on behalf of current and future concurrent readers. Make sure no writers currently owns the lock.
 */
private void acquireForReaders(){
synchronized (globalMutex) {
    while (doesWriterOwnThisLock()) {
      try {
        globalMutex.wait();
      }
 catch (      InterruptedException e) {
        LOGGER.info("InterruptedException while waiting for globalMutex in acquireForReaders",e);
        Thread.currentThread().interrupt();
      }
    }
    globalMutex.add(this);
  }
}
