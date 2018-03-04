package integration.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import orderbook.Order;
import orderbook.OrderBook;
import orderbook.Side;

import java.util.*;

import static integration.util.CucumberDataTableUtil.getTitles;
import static integration.util.CucumberDataTableUtil.getValue;
import static integration.util.CustomizedMatcher.containsInAnyOrder;
import static integration.util.CustomizedMatcher.ladderMatch;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Created by keikeicheung on 03/03/2018.
 */
public class OrderBookSteps {

    private OrderBook orderBook = new OrderBook();
    private List<Exception> actualExpections = new ArrayList<>();

    @Given("^There are several orders in (BID|OFFER) ladder of order book$")
    public void thereAreSeveralOrdersInLadderOfOrderBook(String side, DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        List<String> titles = getTitles(table);
        for (int i = 1; i < rows.size(); i++) {
            DataTableRow currentRow = rows.get(i);
            Double price = getPrice(titles, currentRow);
            Long quantity = getQuantity(titles, currentRow);
            String orderId = getId(titles, currentRow);
            if (orderId == null) {
                orderBook.addOrder(new Order(Side.valueOf(side), price, quantity));
            } else {
                orderBook.addOrder(new Order(orderId, Side.valueOf(side), price, quantity));
            }
        }
    }

    @Given("^There are several orders in order book$")
    public void thereAreSeveralOrdersInOrderBook(DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        List<String> titles = getTitles(table);
        for (int i = 1; i < rows.size(); i++) {
            DataTableRow currentRow = rows.get(i);
            Double price = getPrice(titles, currentRow);
            Long quantity = getQuantity(titles, currentRow);
            Side side = getSide(titles, currentRow);
            String orderId = getId(titles, currentRow);
            if (orderId == null) {
                orderBook.addOrder(new Order(side, price, quantity));
            } else {
                orderBook.addOrder(new Order(orderId, side, price, quantity));
            }
        }
    }

    @When("^Adding order$")
    public void addingOrder(DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        List<String> titles = getTitles(table);
        for (int i = 1; i < rows.size(); i++) {
            DataTableRow currentRow = rows.get(i);
            Double price = getPrice(titles, currentRow);
            Long quantity = getQuantity(titles, currentRow);
            Side side = getSide(titles, currentRow);
            String orderId = getId(titles, currentRow);
            orderBook.addOrder(new Order(orderId, side, price, quantity));
        }
    }

    @When("^Removing order (.*)$")
    public void removingOrder(String orderId) {
        orderBook.removeOrderByOrderId(orderId);
    }

    @When("^Modify order (.*) to quantity (\\d+)$")
    public void modifyOrderOrderToQuantity(String orderId, Long newQuantity) {
        orderBook.modifyOrderQuantityWithId(orderId, newQuantity);
    }

    @When("^Adding order with order id exists$")
    public void addingOrderWithOrderIdExists(DataTable table) {
        try {
            addingOrder(table);
        } catch (RuntimeException e) {
            actualExpections.add(e);
        }
    }

    @When("^Getting the price of (BID|OFFER) ladder at non existing level (\\d+)$")
    public void gettingThePriceOfBIDLadderAtLevel(String side, int level) {
        try {
            orderBook.getPriceWithSideAndLevel(Side.valueOf(side), level);
        } catch (ArrayIndexOutOfBoundsException e) {
            actualExpections.add(e);
        }
    }

    @Then("^The (BID|OFFER) ladder will be$")
    public void theLadderWillBe(String side, DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        List<String> titles = getTitles(table);
        Map<Double, Long> expectedLadder = new HashMap<>();
        for (int i = 1; i < rows.size(); i++) {
            DataTableRow currentRow = rows.get(i);
            Double price = getPrice(titles, currentRow);
            Long quantity = getQuantity(titles, currentRow);
            expectedLadder.put(price, quantity);
        }
        assertThat(orderBook.getLadder(Side.valueOf(side)), ladderMatch(expectedLadder));
    }

    @Then("^The total quantity of (BID|OFFER) ladder at level (\\d+) will be (\\d+)$")
    public void theQuantityOfBIDLadderAtLevelWillBe(String side, int level, Long expectedQuantity) {
        assertThat(orderBook.getTotalQuantityWithSideAndLevel(Side.valueOf(side), level), equalTo(expectedQuantity));
    }

    @Then("^The price of (BID|OFFER) ladder at level (\\d+) will be (.*)$")
    public void thePriceOfLadderAtLevelWillBe(String side, int level, String price) {
        Double expectedPrice = Double.parseDouble(price);
        assertThat(orderBook.getPriceWithSideAndLevel(Side.valueOf(side), level), equalTo(expectedPrice));
    }

    @Then("^All orders in order book$")
    public void allOrdersInOrderBook(DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        List<String> titles = getTitles(table);
        List<Order> expectedOrders = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            DataTableRow currentRow = rows.get(i);
            Double price = getPrice(titles, currentRow);
            Long quantity = getQuantity(titles, currentRow);
            String orderId = getId(titles, currentRow);
            Side side = getSide(titles, currentRow);
            expectedOrders.add(new Order(orderId, side, price, quantity));
        }
        assertThat(orderBook.getAllOrders(), containsInAnyOrder(expectedOrders));
    }

    @Then("^It throws runtime exception$")
    public void itFailsToCreateOrder() throws Throwable {
        assertThat(actualExpections.size(), equalTo(1));
        assertThat(actualExpections.get(0).getClass(), equalTo(RuntimeException.class));
    }

    @Then("^It throws array index out of bound exception$")
    public void itThrowsArrayIndexOutOfBoundException() {
        assertThat(actualExpections.size(), equalTo(1));
        assertThat(actualExpections.get(0).getClass(), equalTo(ArrayIndexOutOfBoundsException.class));
    }

    private double getPrice(List<String> titles, DataTableRow currentRow) {
        return Double.parseDouble(getValue(titles, currentRow, "Price"));
    }

    private Long getQuantity(List<String> titles, DataTableRow currentRow) {
        return Long.parseLong(getValue(titles, currentRow, "Quantity"));
    }

    private Side getSide(List<String> titles, DataTableRow currentRow) {
        return Side.valueOf(getValue(titles, currentRow, "Side"));
    }

    private String getId(List<String> titles, DataTableRow currentRow) {
        return getValue(titles, currentRow, "Id");
    }
}
