package orderbook;

import java.util.*;

/**
 * Created by keikeicheung on 02/03/2018.
 */
public class OrderBook {
    private final Map<String, Order> orderMap;
    private final SortedMap<Double, Integer> bidOrderLadder;
    private final SortedMap<Double, Integer> offerOrderLadder;

    public OrderBook() {
        this.orderMap = new HashMap<>();
        this.bidOrderLadder = new TreeMap<>((p1, p2) -> p1 > p2 ? -1 : p1.equals(p2) ? 0 : 1);
        this.offerOrderLadder = new TreeMap<>((p1, p2) -> p1 < p2 ? -1 : p1.equals(p2) ? 0 : 1);
    }

    public void addOrder(Order order) {
        if (orderMap.containsKey(order.getId())) {
            throw new RuntimeException();
        }
        orderMap.put(order.getId(), order);
        addLadder(order.getSide(), order.getPrice(), order.getQuantity());
    }

    private void addLadder(Side side, Double price, Integer quantity) {
        SortedMap<Double, Integer> ladder = getLadder(side);
        if (ladder.containsKey(price)) {
            Integer originalQuantity = ladder.get(price);
            ladder.put(price, originalQuantity + quantity);
        } else {
            ladder.put(price, quantity);
        }
    }

    public Collection<Order> getAllOrders() {
        return orderMap.values();
    }

    public void removeOrderByOrderId(String id) {
        Order order = orderMap.get(id);
        SortedMap<Double, Integer> ladder = getLadder(order.getSide());
        Integer originalQuantity = ladder.get(order.getPrice());
        int newQuantity = originalQuantity - order.getQuantity();
        if (newQuantity > 0) {
            ladder.put(order.getPrice(), newQuantity);
        } else {
            ladder.remove(order.getPrice());
        }

        orderMap.remove(id);
    }

    public void modifyOrderQuantityWithId(String id, int newQuantity) {
        Order order = getOrderById(id);
        SortedMap<Double, Integer> ladder = getLadder(order.getSide());
        Integer originalQuantityInLadder = ladder.get(order.getPrice());
        ladder.put(order.getPrice(), originalQuantityInLadder - order.getQuantity() + newQuantity);
        order.setQuantity(newQuantity);
    }

    public Order getOrderById(String id) {
        return orderMap.get(id);
    }

    public Double getPriceWithSideAndLevel(Side side, int level) {
        return getEntry(side, level).getKey();
    }

    public int getTotalQuantityWithSideAndLevel(Side side, int level) {
        return getEntry(side, level).getValue();
    }

    public SortedMap<Double, Integer> getLadder(Side side) {
        if (side == Side.BID) {
            return bidOrderLadder;
        } else {
            return offerOrderLadder;
        }
    }

    private Map.Entry<Double, Integer> getEntry(Side side, int level) {
        int index = 0;
        Iterator<Map.Entry<Double, Integer>> iterator = getLadder(side).entrySet().iterator();
        while (iterator.hasNext() && index <= level) {
            Map.Entry<Double, Integer> entry = iterator.next();
            if (index == level) {
                return entry;
            }
            index++;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
}
