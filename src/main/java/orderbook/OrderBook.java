package orderbook;

import java.util.*;

/**
 * Created by keikeicheung on 02/03/2018.
 */
public class OrderBook {
    private final Map<String, Order> orderMap;
    private final SortedMap<Double, Long> bidOrderLadder;
    private final SortedMap<Double, Long> offerOrderLadder;

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

    private void addLadder(Side side, Double price, Long quantity) {
        SortedMap<Double, Long> ladder = getLadder(side);
        if (ladder.containsKey(price)) {
            Long originalQuantity = ladder.get(price);
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
        SortedMap<Double, Long> ladder = getLadder(order.getSide());
        Long originalQuantity = ladder.get(order.getPrice());
        Long newQuantity = originalQuantity - order.getQuantity();
        if (newQuantity > 0) {
            ladder.put(order.getPrice(), newQuantity);
        } else {
            ladder.remove(order.getPrice());
        }

        orderMap.remove(id);
    }

    public void modifyOrderQuantityWithId(String id, Long newQuantity) {
        Order order = getOrderById(id);
        SortedMap<Double, Long> ladder = getLadder(order.getSide());
        Long originalQuantityInLadder = ladder.get(order.getPrice());
        ladder.put(order.getPrice(), originalQuantityInLadder - order.getQuantity() + newQuantity);
        order.setQuantity(newQuantity);
    }

    public Order getOrderById(String id) {
        return orderMap.get(id);
    }

    public Double getPriceWithSideAndLevel(Side side, int level) {
        return getEntry(side, level).getKey();
    }

    public Long getTotalQuantityWithSideAndLevel(Side side, int level) {
        return getEntry(side, level).getValue();
    }

    public SortedMap<Double, Long> getLadder(Side side) {
        if (side == Side.BID) {
            return bidOrderLadder;
        } else {
            return offerOrderLadder;
        }
    }

    private Map.Entry<Double, Long> getEntry(Side side, int level) {
        int index = 0;
        Iterator<Map.Entry<Double, Long>> iterator = getLadder(side).entrySet().iterator();
        while (iterator.hasNext() && index <= level) {
            Map.Entry<Double, Long> entry = iterator.next();
            if (index == level) {
                return entry;
            }
            index++;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
}
