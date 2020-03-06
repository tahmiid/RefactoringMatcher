private static void submitTicket(LotteryService service,Scanner scanner){
  LOGGER.info("What is your email address?");
  String email=readString(scanner);
  LOGGER.info("What is your bank account number?");
  String account=readString(scanner);
  LOGGER.info("What is your phone number?");
  String phone=readString(scanner);
  PlayerDetails details=new PlayerDetails(email,account,phone);
  LOGGER.info("Give 4 comma separated lottery numbers?");
  String numbers=readString(scanner);
  try {
    String[] parts=numbers.split(",");
    Set<Integer> chosen=new HashSet<>();
    for (int i=0; i < 4; i++) {
      chosen.add(Integer.parseInt(parts[i]));
    }
    LotteryNumbers lotteryNumbers=LotteryNumbers.create(chosen);
    LotteryTicket lotteryTicket=new LotteryTicket(new LotteryTicketId(),details,lotteryNumbers);
    Optional<LotteryTicketId> id=service.submitTicket(lotteryTicket);
    if (id.isPresent()) {
      LOGGER.info("Submitted lottery ticket with id: {}",id.get());
    }
 else {
      LOGGER.info("Failed submitting lottery ticket - please try again.");
    }
  }
 catch (  Exception e) {
    LOGGER.info("Failed submitting lottery ticket - please try again.");
  }
}
