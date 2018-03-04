package performance;

import orderbook.Order;
import orderbook.OrderBook;
import orderbook.Side;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                executePerformanceTest(i, orders, numberOfLevel, printWriter);
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void executePerformanceTest(int runIndex, List<Order> orders, int level, PrintWriter printWriter) {
        System.out.println("Performance Test: Add Order");
        long startTime = System.currentTimeMillis();
        OrderBook orderBook = new OrderBook();
        for (Order order : orders) {
            orderBook.addOrder(order);
        }
        long stopTime = System.currentTimeMillis();
        long addOrderTime = stopTime - startTime;
        double averageAddOrderTime = new Long(addOrderTime).doubleValue() / new Integer(orders.size()).doubleValue();

        System.out.println("Performance Test: Retrieve Price At Level");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < level; i++) {
            orderBook.getPriceWithSideAndLevel(Side.BID, i);
        }
        stopTime = System.currentTimeMillis();
        long retrievePriceFromSideAndLevelTime = stopTime - startTime;
        double averageRetrievePriceFromSideAndLevelTime = new Long(retrievePriceFromSideAndLevelTime).doubleValue() / new Integer(level).doubleValue();

        System.out.println("Performance Test: Modify Order Quantity");
        startTime = System.currentTimeMillis();
        for (Order order : orders) {
            orderBook.modifyOrderQuantityWithId(order.getId(), 2000000L);
        }
        stopTime = System.currentTimeMillis();
        long modifyQuantityTime = stopTime - startTime;
        double averageModifyQuantityTime = new Long(modifyQuantityTime).doubleValue() / new Integer(orders.size()).doubleValue();

        System.out.println("Performance Test: Retrieve Total Quantity With Side And Level");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < level; i++) {
            orderBook.getTotalQuantityWithSideAndLevel(Side.BID, i);
        }
        stopTime = System.currentTimeMillis();
        long retrieveTotalQuantityTime = stopTime - startTime;
        double averageRetrieveTotalQuantityTime = new Long(retrieveTotalQuantityTime).doubleValue() / new Integer(level).doubleValue();

        System.out.println("Performance Test: Remove Order By Id");
        List<String> orderIds = orders.stream().map(order -> order.getId()).collect(Collectors.toList());
        startTime = System.currentTimeMillis();
        for (String orderId : orderIds) {
            orderBook.removeOrderByOrderId(orderId);
        }
        stopTime = System.currentTimeMillis();
        long removeOrderTime = stopTime - startTime;
        double averageRemoveOrderTime = new Long(removeOrderTime).doubleValue() / new Integer(orders.size()).doubleValue();

        StringBuilder sb = new StringBuilder();
        sb.append((runIndex + 1) + ",");
        sb.append(addOrderTime + ",");
        sb.append(averageAddOrderTime + ",");
        sb.append(retrievePriceFromSideAndLevelTime + ",");
        sb.append(averageRetrievePriceFromSideAndLevelTime + ",");
        sb.append(modifyQuantityTime + ",");
        sb.append(averageModifyQuantityTime + ",");
        sb.append(retrieveTotalQuantityTime + ",");
        sb.append(averageRetrieveTotalQuantityTime + ",");
        sb.append(removeOrderTime + ",");
        sb.append(averageRemoveOrderTime);
        sb.append('\n');
        printWriter.write(sb.toString());
    }
}
