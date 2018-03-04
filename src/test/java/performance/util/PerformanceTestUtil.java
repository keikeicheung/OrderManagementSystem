package performance.util;

import orderbook.Order;
import orderbook.OrderBook;
import orderbook.Side;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by keikeicheung on 04/03/2018.
 */
public class PerformanceTestUtil {

    public static void executePerformanceTest(int runIndex, List<Order> orders, int level, PrintWriter printWriter) {
        System.out.println("Performance Test: Add Order");
        double ordersSizeInDouble = new Integer(orders.size()).doubleValue();
        double levelInDouble = new Integer(level).doubleValue();
        OrderBook orderBook = new OrderBook();

        long addOrderTime = timeTakenForAddingOrders(orders, orderBook);
        double averageAddOrderTime = new Long(addOrderTime).doubleValue() / ordersSizeInDouble;

        long retrievePriceFromSideAndLevelTime = timeTakenForRetrievingPriceFromLevels(level, orderBook);
        double averageRetrievePriceFromSideAndLevelTime = new Long(retrievePriceFromSideAndLevelTime).doubleValue() / levelInDouble;

        long modifyQuantityTime = timeTakenForModifyingOrderQuantity(orders, orderBook);
        double averageModifyQuantityTime = new Long(modifyQuantityTime).doubleValue() / ordersSizeInDouble;

        long retrieveTotalQuantityTime = timeTakenRetrievingTotalQuantityAtLevel(level, orderBook);
        double averageRetrieveTotalQuantityTime = new Long(retrieveTotalQuantityTime).doubleValue() / levelInDouble;

        long removeOrderTime = timeTakenRemovingOrders(orders, orderBook);
        double averageRemoveOrderTime = new Long(removeOrderTime).doubleValue() / ordersSizeInDouble;

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

    private static long timeTakenRemovingOrders(List<Order> orders, OrderBook orderBook) {
        System.out.println("Performance Test: Remove Order By Id");
        List<String> orderIds = orders.stream().map(order -> order.getId()).collect(Collectors.toList());
        long startTime = System.currentTimeMillis();
        for (String orderId : orderIds) {
            orderBook.removeOrderByOrderId(orderId);
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }

    private static long timeTakenRetrievingTotalQuantityAtLevel(int level, OrderBook orderBook) {
        System.out.println("Performance Test: Retrieve Total Quantity With Side And Level");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < level; i++) {
            orderBook.getTotalQuantityWithSideAndLevel(Side.BID, i);
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }

    private static long timeTakenForModifyingOrderQuantity(List<Order> orders, OrderBook orderBook) {
        System.out.println("Performance Test: Modify Order Quantity");
        long startTime = System.currentTimeMillis();
        for (Order order : orders) {
            orderBook.modifyOrderQuantityWithId(order.getId(), 2000000L);
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }

    private static long timeTakenForRetrievingPriceFromLevels(int level, OrderBook orderBook) {
        System.out.println("Performance Test: Retrieve Price At Level");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < level; i++) {
            orderBook.getPriceWithSideAndLevel(Side.BID, i);
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }

    private static long timeTakenForAddingOrders(List<Order> orders, OrderBook orderBook) {
        System.out.println("Performance Test: Add Order");
        long startTime = System.currentTimeMillis();
        for (Order order : orders) {
            orderBook.addOrder(order);
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }
}
