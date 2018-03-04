package performance;

import orderbook.Order;
import orderbook.OrderBook;
import orderbook.Side;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by keikeicheung on 04/03/2018.
 */
public class PerformanceTest {

    public static void main(String[] args) {
        System.out.println("Preparing loads of orders...");
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            Order order = new Order("order-" + i, Side.BID, 99.0 + (i % 1000 * 0.001), 1000000L);
            orders.add(order);
        }
        System.out.println("Start Performance Test");
        executePerformanceTest(orders);
    }

    private static void executePerformanceTest(List<Order> orders) {
        System.out.println("Performance Test: Add Order");
        long startTime = System.currentTimeMillis();
        OrderBook orderBook = new OrderBook();
        for (Order order : orders) {
            orderBook.addOrder(order);
        }
        long stopTime = System.currentTimeMillis();
        long addOrderTime = stopTime - startTime;
        System.out.println("total add order time:" + addOrderTime);
        System.out.println("average add order time:" + (new Long(addOrderTime).doubleValue() / new Integer(orders.size()).doubleValue()));

        System.out.println("Performance Test: Retrieve Price At Level");
        startTime = System.currentTimeMillis();
        int level = 1000;
        for (int i = 0; i < level; i++) {
            orderBook.getPriceWithSideAndLevel(Side.BID, i);
        }
        stopTime = System.currentTimeMillis();
        long retrievePriceFromSideAndLevelTime = stopTime - startTime;
        System.out.println("total retrieve price time:" + retrievePriceFromSideAndLevelTime);
        System.out.println("average retrieve price time:" + (new Long(retrievePriceFromSideAndLevelTime).doubleValue() / 99.0));

        System.out.println("Performance Test: Modify Order Quantity");
        startTime = System.currentTimeMillis();
        for (Order order : orders) {
            orderBook.modifyOrderQuantityWithId(order.getId(), 2000000L);
        }
        stopTime = System.currentTimeMillis();
        long modifyQuantityTime = stopTime - startTime;
        System.out.println("total modify quantity time:" + modifyQuantityTime);
        System.out.println("average modify quantity time:" + (new Long(modifyQuantityTime).doubleValue() / new Integer(orders.size()).doubleValue()));


        System.out.println("Performance Test: Retrieve Total Quantity With Side And Level");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < level; i++) {
            orderBook.getTotalQuantityWithSideAndLevel(Side.BID, i);
        }
        stopTime = System.currentTimeMillis();
        long retrieveTotalQuantityTime = stopTime - startTime;
        System.out.println("total retrieve quantity at level time:" + retrieveTotalQuantityTime);
        System.out.println("average retrieve quantity at level time:" + (new Long(retrieveTotalQuantityTime).doubleValue() / 99.0));


        System.out.println("Performance Test: Remove Order By Id");
        List<String> orderIds = orders.stream().map(order -> order.getId()).collect(Collectors.toList());
        startTime = System.currentTimeMillis();
        for (String orderId: orderIds) {
            orderBook.removeOrderByOrderId(orderId);
        }
        stopTime = System.currentTimeMillis();
        long removeOrderTime = stopTime - startTime;
        System.out.println("total remove order time:" + removeOrderTime);
        System.out.println("average remove order time:" + (new Long(removeOrderTime).doubleValue() / new Integer(orders.size()).doubleValue()));
    }
}
