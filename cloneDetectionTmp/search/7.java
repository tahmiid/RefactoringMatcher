private void calculateLineCount(){
  countLines().thenAccept(count -> {
    System.out.println("Line count is: " + count);
    taskCompleted();
  }
);
}
