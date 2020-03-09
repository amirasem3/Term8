import java.util.ArrayList;

class Able {
    ArrayList<Customer> customers = new ArrayList<>();
    private int idleTime;

    void serve(Customer customer) {
        customer.setServiceTime(ableServiceTime(customer.getRandomServiceDigit()));
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
        String name = "ABLE";
        customer.setServerName(name);
        customers.add(customer);

    }

    private static int ableServiceTime(int service) {
        if (service >= 1 && service <= 30) return 2;
        if (service >= 31 && service <= 58) return 3;
        if (service >= 59 && service <= 83) return 4;
        if (service >= 84 && service <= 100) return 5;
        return 0;
    }

}
