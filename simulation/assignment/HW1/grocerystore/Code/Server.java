import java.util.ArrayList;

class Server {
    private ArrayList<Customer> customers = new ArrayList<>();
    private int idleTime = 0;

    void serve(Customer customer) {
        if (customer.getId() == 1) {
            customer.setServiceTimeBegin(0);
            idleTime = 0;
        } else {
            if (customer.getArrivalTime() > customers.get(customers.size() - 1).getServiceTimeEnd()) {
                customer.setServiceTimeBegin(customer.getArrivalTime());
                idleTime = customer.getServiceTimeBegin() - customers.get(customers.size() - 1).getServiceTimeEnd();
            }
            if (customer.getArrivalTime() <= customers.get(customers.size() - 1).getServiceTimeEnd()) {
                customer.setServiceTimeBegin(customers.get(customers.size() - 1).getServiceTimeEnd());
                idleTime = customer.getServiceTimeBegin() - customers.get(customers.size() - 1).getServiceTimeEnd();
            }
        }
        customer.setIdleServerTime(idleTime);
        customers.add(customer);
    }


}
