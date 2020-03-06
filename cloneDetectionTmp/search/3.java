private static void checkTicket(LotteryService service,Scanner scanner){
  LOGGER.info("What is the ID of the lottery ticket?");
  String id=readString(scanner);
  LOGGER.info("Give the 4 comma separated winning numbers?");
  String numbers=readString(scanner);
  try {
    String[] parts=numbers.split(",");
    Set<Integer> winningNumbers=new HashSet<>();
    for (int i=0; i < 4; i++) {
      winningNumbers.add(Integer.parseInt(parts[i]));
    }
    LotteryTicketCheckResult result=service.checkTicketForPrize(new LotteryTicketId(Integer.parseInt(id)),LotteryNumbers.create(winningNumbers));
    if (result.getResult().equals(LotteryTicketCheckResult.CheckResult.WIN_PRIZE)) {
      LOGGER.info("Congratulations! The lottery ticket has won!");
    }
 else     if (result.getResult().equals(LotteryTicketCheckResult.CheckResult.NO_PRIZE)) {
      LOGGER.info("Unfortunately the lottery ticket did not win.");
    }
 else {
      LOGGER.info("Such lottery ticket has not been submitted.");
    }
  }
 catch (  Exception e) {
    LOGGER.info("Failed checking the lottery ticket - please try again.");
  }
}
