package orderbook;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Created by keikeicheung on 02/03/2018.
 */
public class OrderBookTest {

    @Test
    public void shouldAddToOrderBookWithAGivenOrder() {
        OrderBook orderBook = new OrderBook();
        Order order = new Order(Side.BID, 77.2, 1000000L);
        orderBook.addOrder(order);
      /*
       * inserting to orderMap (HashMap) = O(1) usually unless hashcode collision then worst case can be O(n)
       * inserting to ladder (TreeMap) = O(log(n))
       * --> O(log(n))
       */
        assertThat(orderBook.getAllOrders(), hasItem(order));
    }


    @Test
    public void shouldRemoveOrderFromBookByOrderId() {
        OrderBook orderBook = new OrderBook();
        Order order = new Order(Side.BID, 77.2, 1000000L);
        orderBook.addOrder(order);
        orderBook.removeOrderByOrderId(order.getId());
      /*
       * removing from orderMap (HashMap) = O(1) usually unless hashcode collision then worst case can be O(n)
       * removing from ladder (TreeMap) = O(log(n))
       * --> O(log(n))
       */
        assertThat(orderBook.getAllOrders().isEmpty(), equalTo(true));
    }

    @Test
    public void shouldModifyOrderQuantityWithOrderIdAndNewQuantity() {
        OrderBook orderBook = new OrderBook();
        Order order = new Order(Side.BID, 77.2, 1000000L);
        orderBook.addOrder(order);
        Long newQuantity = 2000000L;
        orderBook.modifyOrderQuantityWithId(order.getId(), newQuantity);
      /*
       * retrieving order from orderMap (HashMap) = O(1) usually unless hashcode collision then worst case can be O(n)
       * retrieving and updating from ladder (TreeMap) = O(log(n))
       * --> O(log(n))
       */
        assertThat(orderBook.getOrderById(order.getId()).getQuantity(), equalTo(newQuantity));
    }

    @Test
    public void shouldReturnPriceOfThePositionOfLadderWithSideAndLevel() {
        OrderBook orderBook = new OrderBook();
        Order order = new Order(Side.BID, 77.1, 1000000L);
        orderBook.addOrder(order);
        orderBook.addOrder(new Order(Side.BID, 77.0, 2000000L));
        orderBook.addOrder(new Order(Side.OFFER, 77.5, 2000000L));
        orderBook.addOrder(new Order(Side.BID, 77.2, 3000000L));
        orderBook.addOrder(new Order(Side.BID, 76.9, 3000000L));
        assertThat(orderBook.getPriceWithSideAndLevel(Side.BID, 1), equalTo(order.getPrice()));
      /*
       * getting entrySet from ladder
       * iterate to level = 0(m) and get price
       * --> O(m)
       */
    }

    @Test
    public void shouldReturnTotalQuantityWithSideAndLevel() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(new Order(Side.BID, 77.2, 3000000L));
        Order order1 = new Order(Side.BID, 77.1, 1000000L);
        Order order2 = new Order(Side.BID, 77.1, 3000000L);
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(new Order(Side.BID, 77.0, 2000000L));
        orderBook.addOrder(new Order(Side.OFFER, 77.5, 2000000L));
        assertThat(orderBook.getTotalQuantityWithSideAndLevel(Side.BID, 1),
                equalTo(order1.getQuantity() + order2.getQuantity()));
       /*
       * getting entrySet from ladder
       * iterate to level = 0(m) and get total quantity
       * --> O(m)
       */
    }
}
