import java.util.ArrayList;

class Backer {
    ArrayList<Customer> customers = new ArrayList<>();
    private int idleTime;

    void serve(Customer customer) {
        if (customers.isEmpty()) {
            customer.setServiceTime(backerServiceTime(customer.getRandomServiceDigit()));
            customer.setServiceTimeBegin(customer.getArrivalTime());
            idleTime = 0;
        } else {
            if (customer.getArrivalTime() > customers.get(customers.size() - 1).getServiceTimeEnd()) {
                customer.setServiceTime(backerServiceTime(customer.getRandomServiceDigit()));
                customer.setServiceTimeBegin(customer.getArrivalTime());
                idleTime = customer.getServiceTimeBegin() - customers.get(customers.size() - 1).getServiceTimeEnd();
            }
            if (customer.getArrivalTime() <= customers.get(customers.size() - 1).getServiceTimeEnd()) {
                customer.setServiceTime(backerServiceTime(customer.getRandomServiceDigit()));
                customer.setServiceTimeBegin(customers.get(customers.size() - 1).getServiceTimeEnd());
                idleTime = customer.getServiceTimeBegin() - customers.get(customers.size() - 1).getServiceTimeEnd();
            }
        }
        customer.setIdleServerTime(idleTime);
        String name = "BACKER";
        customer.setServerName(name);
        customers.add(customer);

    }

    private static int backerServiceTime(int service) {
        if (service >= 1 && service <= 35) return 3;
        if (service >= 36 && service <= 60) return 4;
        if (service >= 61 && service <= 80) return 5;
        if (service >= 81 && service <= 100) return 6;
        return 0;
    }
}
