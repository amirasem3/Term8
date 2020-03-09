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
        System.out.println("Please Enter number of customers : ");
        int numberOfCustomer = scanner.nextInt();
        Customer[] customers = new Customer[numberOfCustomer];
        Random arrivalTime = new Random();
        Random serviceTime = new Random();
        Able able = new Able();
        Backer backer = new Backer();
        int arrive;
        int service;
        for (int i = 0; i < numberOfCustomer; i++) {
            arrive = arrivalTime.nextInt(100);
            service = serviceTime.nextInt(100);
            customers[i] = new Customer(i + 1, arrivalTime(arrive), arrive, service);
        }
        for (Customer c : customers) {
            c.setArrivalTime(calculateArrive(c.getId(), customers));
        }
        able.serve(customers[0]);

        System.out.println("ABLE CUSTOMER SIZE : " + able.customers.size());
        for (int i = 1; i < customers.length; i++) {
            if (backer.customers.size() >= 1) {
                if (customers[i].getArrivalTime() < backer.customers.get(backer.customers.size() - 1).getServiceTimeEnd()
                        && customers[i].getArrivalTime() < able.customers.get(able.customers.size() - 1).getServiceTimeEnd()) {
                    int min = Math.min(backer.customers.get(backer.customers.size() - 1).getServiceTimeEnd(),
                            able.customers.get(able.customers.size() - 1).getServiceTimeEnd());

                    if (able.customers.get(able.customers.size() - 1).getServiceTimeEnd() != backer.customers.get(backer.customers.size() - 1).getServiceTimeEnd()) {
                        if (min == able.customers.get(able.customers.size() - 1).getServiceTimeEnd())
                            able.serve(customers[i]);
                        if (min == backer.customers.get(backer.customers.size() - 1).getServiceTimeEnd())
                            backer.serve(customers[i]);
                    } else able.serve(customers[i]);
                    continue;
                }
                if (customers[i].getArrivalTime() >= backer.customers.get(backer.customers.size() - 1).getServiceTimeEnd() &&
                        customers[i].getArrivalTime() >= able.customers.get(able.customers.size() - 1).getServiceTimeEnd()) {

                    able.serve(customers[i]);
                    continue;
                }
                if (customers[i].getArrivalTime() >= backer.customers.get(backer.customers.size() - 1).getServiceTimeEnd() &&
                        customers[i].getArrivalTime() <= able.customers.get(able.customers.size() - 1).getServiceTimeEnd()) {

                    backer.serve(customers[i]);
                    continue;
                }
                if (customers[i].getArrivalTime() <= backer.customers.get(backer.customers.size() - 1).getServiceTimeEnd() &&
                        customers[i].getArrivalTime() >= able.customers.get(able.customers.size() - 1).getServiceTimeEnd()) {

                    able.serve(customers[i]);
                }

            } else {
                if (customers[i].getArrivalTime() < able.customers.get(able.customers.size() - 1).getServiceTimeEnd()) {

                    backer.serve(customers[i]);
                    continue;
                }
                if (customers[i].getArrivalTime() >= able.customers.get(able.customers.size() - 1).getServiceTimeEnd()) {

                    able.serve(customers[i]);
                }

            }
        }
        for (Customer c : customers) {
            c.print();
        }
        //ABLE
        int ableSumOfWait = 0;
        int ableNumberOfCustomerWait = 0;
        int ableTotalIdle = 0;
        int ableTotalServiceTime = 0;
        int ableTotalTimeCustomerQueue = 0;
        int ableTotalSpend = 0;
        for (Customer c : able.customers) {
            ableSumOfWait += c.getWaitInQueue();
            ableTotalIdle += c.getIdleServerTime();
            ableTotalServiceTime += c.getServiceTime();
            ableTotalSpend += c.getTimeInSystem();
            ableTotalTimeCustomerQueue += c.getWaitInQueue();
            if (c.getWaitInQueue() != 0) ableNumberOfCustomerWait++;

        }
        //calculate average Waiting time for a customer
        double ableAvgWaitInQueue = (double) ableSumOfWait / able.customers.size();
        //calculate probably that a customer has to wait in queue
        double ableWaitProb = (double) ableNumberOfCustomerWait / able.customers.size();
        //calculate fraction of idle time of the server
        double ableIdleFraction = (double) ableTotalIdle / able.customers.get(able.customers.size() - 1).getServiceTimeEnd();
        //probably of the server being busy
        double ableBusyProb = (double) 1 - ableIdleFraction;

        //calculate average service time
        double ableAvgServiceTime = (double) ableTotalServiceTime / able.customers.size();
        //expected Value
        double ableExpectedValue = (2 * 0.3) + (3 * 0.28) + (4 * 0.25) + (5 * 0.17);

        //average time between arrivals
        double ableAvgTimeBetweenArrival = (double) able.customers.get(able.customers.size() - 1).getArrivalTime() / (able.customers.size() - 1);
        //discrete uniform distribution
        double ableDiscreteUniform = (double) (1 + 4) / 2;
        //average waiting time of those who wait
        double ableAvgWaitingTime = 0.0;
        if (ableNumberOfCustomerWait != 0)
            ableAvgWaitingTime = (double) ableTotalTimeCustomerQueue / ableNumberOfCustomerWait;

        //average timer a customer spends in the system
        double ableAvgTimeCustomerSpends = (double) ableTotalSpend / able.customers.size();
        //average time customer spends in the system
        double ableAvgTimeCustomerSpend = ableAvgWaitInQueue + ableAvgServiceTime;
        System.err.println("ABLE CALCULATIONS : ");
        System.out.println("ABLE average waiting time for a customer : " + ableAvgWaitInQueue);
        System.out.println("ABLE probably that a customer has to wait in queue  : " + ableWaitProb);
        System.out.println("ABLE fraction of idle time of the server : " + ableIdleFraction);
        System.out.println("ABLE probably of the server being busy : " + ableBusyProb);
        System.out.println("ABLE average service time : " + ableAvgServiceTime);
        System.out.println("ABLE expected value : " + ableExpectedValue);
        System.out.println("ABLE average time between arrivals : " + ableAvgTimeBetweenArrival);
        System.out.println("ABLE discrete uniform distribution : " + ableDiscreteUniform);
        System.out.println("ABLE average waiting time of those who wait : " + ableAvgWaitingTime);
        System.out.println("ABLE average time a customer spends in the system : " + ableAvgTimeCustomerSpends);
        System.out.println("ABLE average time customer spends in the system : " + ableAvgTimeCustomerSpend);
        System.out.println("The able customer  : " + able.customers.size());
        System.out.println("The backer customer : " + backer.customers.size());

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
        int backerSumOfWait = 0;
        int backerNumberOfCustomerWait = 0;
        int backerTotalIdle = 0;
        int backerTotalServiceTime = 0;
        int backerTotalTimeCustomerQueue = 0;
        int backerTotalSpend = 0;
        for (Customer c : backer.customers) {
            backerSumOfWait += c.getWaitInQueue();
            backerTotalIdle += c.getIdleServerTime();
            backerTotalServiceTime += c.getServiceTime();
            backerTotalSpend += c.getTimeInSystem();
            backerTotalTimeCustomerQueue += c.getWaitInQueue();
            if (c.getWaitInQueue() != 0) backerNumberOfCustomerWait++;

        }
        //calculate average Waiting time for a customer
        double backerAvgWaitInQueue = (double) backerSumOfWait / backer.customers.size();
        //calculate probably that a customer has to wait in queue
        double backerWaitProb = (double) backerNumberOfCustomerWait / backer.customers.size();
        //calculate fraction of idle time of the server
        double backerIdleFraction = (double) backerTotalIdle / backer.customers.get(backer.customers.size() - 1).getServiceTimeEnd();
        //probably of the server being busy
        double backerBusyProb = (double) 1 - backerIdleFraction;

        //calculate average service time
        double backerAvgServiceTime = (double) backerTotalServiceTime / backer.customers.size();
        //expected Value
        double backerExpectedValue = (3 * 0.35) + (4 * 0.25) + (5 * 0.2) + (6 * 0.2);

        //average time between arrivals
        double backerAvgTimeBetweenArrival = (double) backer.customers.get(backer.customers.size() - 1).getArrivalTime() / (backer.customers.size() - 1);
        //discrete uniform distribution
        double backerDiscreteUniform = (double) (1 + 4) / 2;
        //average waiting time of those who wait
        double backerAvgWaitingTime = 0.0;
        if (backerNumberOfCustomerWait != 0)
            backerAvgWaitingTime = (double) backerTotalTimeCustomerQueue / backerNumberOfCustomerWait;

        //average timer a customer spends in the system
        double backerAvgTimeCustomerSpends = (double) backerTotalSpend / backer.customers.size();
        //average time customer spends in the system
        double backerAvgTimeCustomerSpend = backerAvgWaitInQueue + backerAvgServiceTime;
        System.err.println("BACKER CALCULATIONS : ");
        System.out.println("BACKER average waiting time for a customer : " + backerAvgWaitInQueue);
        System.out.println("BACKER probably that a customer has to wait in queue  : " + backerWaitProb);
        System.out.println("BACKER fraction of idle time of the server : " + backerIdleFraction);
        System.out.println("BACKER probably of the server being busy : " + backerBusyProb);
        System.out.println("BACKER average service time : " + backerAvgServiceTime);
        System.out.println("BACKER expected value : " + backerExpectedValue);
        System.out.println("BACKER average time between arrivals : " + backerAvgTimeBetweenArrival);
        System.out.println("BACKER discrete uniform distribution : " + backerDiscreteUniform);
        System.out.println("BACKER average waiting time of those who wait : " + backerAvgWaitingTime);
        System.out.println("BACKER average time a customer spends in the system : " + backerAvgTimeCustomerSpends);
        System.out.println("BACKER average time customer spends in the system : " + backerAvgTimeCustomerSpend);
        System.out.println("The able customer  : " + able.customers.size());
        System.out.println("The backer customer : " + backer.customers.size());



        HSSFWorkbook workbook  = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("20Run4");
        Map<String , Object[]> data = new HashMap<String , Object[]>();
        int counter =0;
//        data.put("0", new Object[] {"Customer", "Time Since Last Arrival", "Arrival Time" , "Service Time" , "Time Service Begin"
//        , "Time Customer Wait in Queue" , "Time Service End" , "Time Customer Spend in System" , "Idle Time of Server"});
//        counter++;
        for (Customer c : customers){
            data.put(counter+"" , new Object[]{c.getId()+"" , c.getTimeBetweenArrival()+"" , c.getArrivalTime()+""  , c.getServiceTime()+"" ,
                    c.getServiceTimeBegin()+"" , c.getWaitInQueue()+"" , c.getServiceTimeEnd()+"" , c.getTimeInSystem()+"" , c.getIdleServerTime()+"" , c.getServerName()});
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

    private static int arrivalTime(int arrive) {
        if (arrive >= 1 && arrive <= 25) return 1;
        if (arrive >= 26 && arrive <= 65) return 2;
        if (arrive >= 66 && arrive <= 85) return 3;
        if (arrive >= 86 && arrive <= 100) return 4;
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
