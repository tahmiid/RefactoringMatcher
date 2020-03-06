/** 
 * This is a synchronized thread starter
 */
public static synchronized void startThread(){
  if (!updateThread.isAlive()) {
    updateThread.start();
    headIndex=0;
    tailIndex=0;
  }
}
