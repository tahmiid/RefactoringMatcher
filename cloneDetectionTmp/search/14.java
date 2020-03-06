private static void performOperationsUsing(final CustomerDao customerDao) throws Exception {
  addCustomers(customerDao);
  log.info("customerDao.getAllCustomers(): ");
  try (Stream<Customer> customerStream=customerDao.getAll()){
    customerStream.forEach((customer) -> log.info(customer));
  }
   log.info("customerDao.getCustomerById(2): " + customerDao.getById(2));
  final Customer customer=new Customer(4,"Dan","Danson");
  customerDao.add(customer);
  log.info("customerDao.getAllCustomers(): " + customerDao.getAll());
  customer.setFirstName("Daniel");
  customer.setLastName("Danielson");
  customerDao.update(customer);
  log.info("customerDao.getAllCustomers(): ");
  try (Stream<Customer> customerStream=customerDao.getAll()){
    customerStream.forEach((cust) -> log.info(cust));
  }
   customerDao.delete(customer);
  log.info("customerDao.getAllCustomers(): " + customerDao.getAll());
}
