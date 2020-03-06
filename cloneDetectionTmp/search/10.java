private static void promiseUsage(Executor executor) throws InterruptedException, ExecutionException {
  Promise<Integer> consumedPromise=new Promise<>();
  consumedPromise.fulfillInAsync(() -> {
    Thread.sleep(1000);
    return 10;
  }
,executor).then(value -> {
    System.out.println("Consumed int value: " + value);
  }
);
  Promise<String> transformedPromise=new Promise<>();
  transformedPromise.fulfillInAsync(() -> {
    Thread.sleep(1000);
    return "10";
  }
,executor).then(value -> {
    return Integer.parseInt(value);
  }
).then(value -> {
    System.out.println("Consumed transformed int value: " + value);
  }
);
  consumedPromise.get();
  transformedPromise.get();
}
