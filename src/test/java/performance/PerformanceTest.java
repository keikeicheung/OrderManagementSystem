package performance;

import orderbook.Order;
import orderbook.Side;
import performance.util.PerformanceTestUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keikeicheung on 04/03/2018.
 */
public class PerformanceTest {

    public static void main(String[] args) {
        int numberOfPerformanceTestRun = 10;
        int numberOfOrders = 10000000;
        int numberOfLevel = 10000;

        System.out.println("Preparing loads of orders...");
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < numberOfOrders; i++) {
            Order order = new Order("order-" + i, Side.BID, 99.0 + (i % numberOfLevel * (1.0 / numberOfLevel)), (i % 10 + 1) * 1000000L);
            orders.add(order);
        }
        System.out.println("Start Performance Test...");
        try {
            PrintWriter printWriter = new PrintWriter(new File("performance_test_result.csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("Run Id,");
            sb.append("Add Order (" + orders.size() + " orders) (ms),");
            sb.append("Add Order (ms),");
            sb.append("Retrieve Price At Level (1000 levels) (ms),");
            sb.append("Retrieve Price (ms),");
            sb.append("Modify Order Quantity (" + orders.size() + " orders) (ms),");
            sb.append("Modify Order Quantity (ms),");
            sb.append("Retrieve Total Quantity (1000 levels) (ms),");
            sb.append("Retrieve Total Quantity (ms),");
            sb.append("Remove Order (" + orders.size() + " orders) (ms),");
            sb.append("Remove Order (ms)");
            sb.append('\n');
            printWriter.write(sb.toString());
            for (int i = 0; i < numberOfPerformanceTestRun; i++) {
                System.out.println("Starting Performance Test " + i);
                PerformanceTestUtil.executePerformanceTest(i, orders, numberOfLevel, printWriter);
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
