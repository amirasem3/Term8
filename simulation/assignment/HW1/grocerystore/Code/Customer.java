class Customer {
    private int id;
    private int timeBetweenArrival;
    private int serviceTimeBegin;
    private int serviceTimeEnd;
    private int waitInQueue;
    private int serviceTime;
    private int timeInSystem;
    private int arrivalTime;
    private int idleServerTime;
    private int randomServiceDigit;
    private int randomArriveDigit;


    Customer(int id, int timeBetweenArrival, int serviceTime , int randomArriveDigit , int randomServiceDigit) {
        this.id = id;
        this.timeBetweenArrival = timeBetweenArrival;
        this.serviceTime = serviceTime;
        this.randomArriveDigit = randomArriveDigit;
        this.randomServiceDigit = randomServiceDigit;
        if (this.id == 1) this.timeBetweenArrival = 0;
    }

    int getId() {
        return id;
    }

    int getTimeBetweenArrival() {
        return timeBetweenArrival;
    }

    int getServiceTimeBegin() {
        return serviceTimeBegin;
    }

    int getServiceTimeEnd() {
        return serviceTimeEnd;
    }

    int getWaitInQueue() {
        return waitInQueue;
    }

    int getServiceTime() {
        return serviceTime;
    }

    int getTimeInSystem() {
        return timeInSystem;
    }

    void setServiceTimeBegin(int serviceTimeBegin) {
        this.serviceTimeBegin = serviceTimeBegin;
        setServiceTimeEnd(serviceTimeBegin + this.serviceTime);
        setWaitInQueue(serviceTimeBegin - this.arrivalTime);
        setTimeInSystem(this.waitInQueue + this.serviceTime);
    }

    private void setServiceTimeEnd(int serviceTimeEnd) {
        this.serviceTimeEnd = serviceTimeEnd;
    }

    private void setWaitInQueue(int waitInQueue) {
        this.waitInQueue = waitInQueue;
    }

    private void setTimeInSystem(int timeInSystem) {
        this.timeInSystem = timeInSystem;
    }

    void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    int getArrivalTime() {
        return arrivalTime;
    }

    void setIdleServerTime(int idleServerTime) {
        this.idleServerTime = idleServerTime;
    }

    int getIdleServerTime() {
        return idleServerTime;
    }

    void print() {
        System.out.println("CID : " + this.id);
        System.out.println("Time Since Last Arrival  : " + this.timeBetweenArrival);
        System.out.println("Arrival Time : " + this.arrivalTime);
        System.out.println("Service Time : " + this.serviceTime);
        System.out.println("Time Server Begins  : " + this.serviceTimeBegin);
        System.out.println("Time Customer waits in Queue : " + this.waitInQueue);
        System.out.println("Time Service End : " + this.serviceTimeEnd);
        System.out.println("Time Customer Spends in System : " + this.timeInSystem);
        System.out.println("Idle Time of Server : "  + this.idleServerTime);
        System.out.println("Random Digit for Service Time : " + this.randomServiceDigit);
        System.out.println("Random Digit for Arrival Time : " + this.randomArriveDigit);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
