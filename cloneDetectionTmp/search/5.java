private static void addFundsToLotteryAccount(WireTransfers bank,Scanner scanner){
  LOGGER.info("What is the account number?");
  String account=readString(scanner);
  LOGGER.info("How many credits do you want to deposit?");
  String amount=readString(scanner);
  bank.setFunds(account,Integer.parseInt(amount));
  LOGGER.info("The account {} now has {} credits.",account,bank.getFunds(account));
}
