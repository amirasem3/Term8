import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter  number of customers : ");
        int customerNumber = scanner.nextInt();
        Customer[] customers = new Customer[customerNumber];
        Random serviceTime = new Random();
        Random arrivalTime = new Random();
        Server server = new Server();
        int service;
        int arrive;
        for (int i = 0; i < customerNumber; i++) {
            service = serviceTime.nextInt(100);
            arrive = arrivalTime.nextInt(1000);
            customers[i] = new Customer(i + 1, arrivalTime(arrive), serviceTime(service), arrive, service);
        }
        for (Customer c : customers) {
            c.setArrivalTime(calculateArrive(c.getId(), customers));
            server.serve(c);
            c.print();
        }

        int sumOfWait = 0;
        int numberOfCustomerWait = 0;
        int totalIdle = 0;
        int totalServiceTime = 0;
        int totalTimeCustomersWaitInQueue = 0;
        int totalSpend = 0;
        for (Customer c : customers) {
            sumOfWait += c.getWaitInQueue();
            totalIdle += c.getIdleServerTime();
            totalServiceTime += c.getServiceTime();
            totalSpend += c.getTimeInSystem();
            totalTimeCustomersWaitInQueue += c.getWaitInQueue();
            if (c.getWaitInQueue() != 0) numberOfCustomerWait++;
        }
        //PAGE 23
        //calculate average waiting time for a customer
        double avgWaitInQueue = (double) sumOfWait / customerNumber;
        //calculate probably that a customer has to wait in queue
        double waitProb = (double) numberOfCustomerWait / customerNumber;
        //calculate fraction of idle time of the server
        double idleFraction = (double) totalIdle / customers[customers.length - 1].getServiceTimeEnd();
        //probably of the server being busy
        double busyProb = (double) 1 - idleFraction;

        //PAGE 24
        //calculate average service time
        double avgServiceTime = (double) totalServiceTime / customerNumber;
        //expected value
        double expectedValue = (1 * 0.1) + (2 * 0.2) + (3 * 0.3) + (4 * 0.25) + (5 * 0.1) + (6 * 0.05);

        //PAGE 25
        //average time between arrivals
        double avgTimeBetweenArrivals = (double) customers[customers.length - 1].getArrivalTime() / (customerNumber - 1);
        //discrete uniform distribution
        double discreteUniformDistribution = (double) (1 + 8) / 2;
        //average waiting time of those who wait
        double avgWaitingTime = 0.0;
        if (numberOfCustomerWait != 0) avgWaitingTime = (double) totalTimeCustomersWaitInQueue / numberOfCustomerWait;

        //PAGE 26
        //average timer a customer spends in the system
        double avgTimeCustomerSpends = (double) totalSpend / customerNumber;
        //average time customer spends in the system
        double avgTimeCustomerSpend = avgWaitInQueue + avgServiceTime;
        System.out.println("average waiting time for a customer : " + avgWaitInQueue);
        System.out.println("probably that a customer has to wait in queue  : " + waitProb);
        System.out.println("fraction of idle time of the server : " + idleFraction);
        System.out.println("probably of the server being busy : " + busyProb);
        System.out.println("average service time : " + avgServiceTime);
        System.out.println("expected value : " + expectedValue);
        System.out.println("average time between arrivals : " + avgTimeBetweenArrivals);
        System.out.println("discrete uniform distribution : " + discreteUniformDistribution);
        System.out.println("average waiting time of those who wait : " + avgWaitingTime);
        System.out.println("average time a customer spends in the system : " + avgTimeCustomerSpends);
        System.out.println("average time customer spends in the system : " + avgTimeCustomerSpend);

        HSSFWorkbook workbook  = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("100Run5");
        Map<String , Object[]> data = new HashMap<String , Object[]>();
        int counter =0;
//        data.put("0", new Object[] {"Customer", "Time Since Last Arrival", "Arrival Time" , "Service Time" , "Time Service Begin"
//        , "Time Customer Wait in Queue" , "Time Service End" , "Time Customer Spend in System" , "Idle Time of Server"});
//        counter++;
        for (Customer c : customers){
            data.put(counter+"" , new Object[]{c.getId()+"" , c.getTimeBetweenArrival()+"" , c.getArrivalTime()+""  , c.getServiceTime()+"" ,
                    c.getServiceTimeBegin()+"" , c.getWaitInQueue()+"" , c.getServiceTimeEnd()+"" , c.getTimeInSystem()+"" , c.getIdleServerTime()+""});
            counter++;
        }


        Set<String> keyset = data.keySet();
        int rownum = 0 ;
        for (String key : keyset){
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellNum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellNum++);
                if(obj instanceof Date)
                    cell.setCellValue((Date)obj);
                else if(obj instanceof Boolean)
                    cell.setCellValue((Boolean)obj);
                else if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Double)
                    cell.setCellValue((Double)obj);
            }
        }
        try {
            FileOutputStream out =
                    new FileOutputStream(new File("sample.xls"));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int serviceTime(int service) {
        if (service >= 0 && service <= 10) return 1;
        if (service >= 11 && service <= 30) return 2;
        if (service >= 31 && service <= 60) return 3;
        if (service >= 61 && service <= 85) return 4;
        if (service >= 86 && service <= 95) return 5;
        if (service >= 96 && service <= 100) return 6;
        return 0;

    }

    private static int arrivalTime(int arrival) {
        if (arrival >= 1 && arrival <= 125) return 1;
        if (arrival >= 126 && arrival <= 250) return 2;
        if (arrival >= 251 && arrival <= 375) return 3;
        if (arrival >= 376 && arrival <= 500) return 4;
        if (arrival >= 501 && arrival <= 625) return 5;
        if (arrival >= 626 && arrival <= 750) return 6;
        if (arrival >= 751 && arrival <= 875) return 7;
        if (arrival >= 876 && arrival <= 1000) return 8;
        return 0;
    }

    private static int calculateArrive(int id, Customer[] customers) {
        int sum = 0;
        for (Customer customer : customers) {
            sum += customer.getTimeBetweenArrival();
            if (customer.getId() == id) break;
        }
        return sum;
    }

}
