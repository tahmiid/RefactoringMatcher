private static void queryLotteryAccountFunds(WireTransfers bank,Scanner scanner){
  LOGGER.info("What is the account number?");
  String account=readString(scanner);
  LOGGER.info("The account {} has {} credits.",account,bank.getFunds(account));
}
