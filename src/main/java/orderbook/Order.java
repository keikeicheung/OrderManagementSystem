package orderbook;

import java.util.UUID;

/**
 * Created by keikeicheung on 02/03/2018.
 */
public class Order {
    private final String id;
    private final Side side;
    private final Double price;
    private Long quantity;


    public Order(Side side, Double price, Long quantity) {
        this.id = UUID.randomUUID().toString();
        this.side = side;
        this.price = price;
        this.quantity = quantity;
    }

    public Order(String id, Side side, Double price, Long quantity) {
        this.id = id;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public Side getSide() {
        return side;
    }

    public Double getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return "id=" + id + "," +
                "side=" + side + "," +
                "price=" + price + "," +
                "quantity=" + quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!quantity.equals(order.quantity)) return false;
        if (!id.equals(order.id)) return false;
        if (side != order.side) return false;
        return price.equals(order.price);
    }
}
