/** 
 * @return lottery ticket
 */
public static LotteryTicket createLotteryTicket(String email,String account,String phone,Set<Integer> givenNumbers){
  PlayerDetails details=PlayerDetails.create(email,account,phone);
  LotteryNumbers numbers=LotteryNumbers.create(givenNumbers);
  return LotteryTicket.create(details,numbers);
}
