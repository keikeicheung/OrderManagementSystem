# Order Book Tech Assessment

Implement a single symbol order book

## Setup

Import project as gradle project
1. New > New from existing source > use gradle wrapper > choose build.gradle in the project
2. After install and build gradle, copy the runConfigurations folder from root directory to .idea (so you do not need to create runConfigurations again)
3. You may need to change build.gradle repository mavenCentral() to any maven url you use in company; then refresh gradle
4. Then you can run All Unit Tests, Integration Tests and Performance Tests

## Part A 

Run "All Unit Tests" if you are using Intellij 

(Otherwise run all junit tests in src/test/java/orderbook)

1. Given an orderbook.Order, add a new order to the book.
    * shouldAddToOrderBookWithAGivenOrder()

2. Given an id, remove the order with that ID from the book.
    * shouldRemoveOrderFromBookByOrderId()

3. Given an id and quantity value, modify the order with that ID to have the new quantity.
    * shouldModifyOrderQuantityWithOrderIdAndNewQuantity()

4. Given a side and integer level, return the price for that side and level. (For example, with side B and level 0, the method would return
the best bid price.)
    * shouldReturnPriceOfThePositionOfLadderWithSideAndLevel()

5. Given a side and integer level, return the total quantity of all orders for that side and level.
    * shouldReturnTotalQuantityWithSideAndLevel()

## Part B

1. addOrder(order)
    * inserting to orderMap (HashMap) = O(1) usually unless hashcode collision then worst case can be O(n)
    * inserting to ladder (TreeMap) = O(log(n))
    * --> O(log(n))

2. removeOrderByOrderId(orderId)
    * removing from orderMap (HashMap) = O(1) usually unless hashcode collision then worst case can be O(n)
    * removing from ladder (TreeMap) = O(log(n))
    * --> O(log(n))

3. modifyOrderQuantityWithId(orderId, newQuantity)
    * retrieving order from orderMap (HashMap) = O(1) usually unless hashcode collision then worst case can be O(n)
    * retrieving and updating from ladder (TreeMap) = O(log(n))
    * --> O(log(n))

4. getPriceWithSideAndLevel(side, level)
    * getting entrySet from ladder
    * iterate to level = 0(m) and get price
    * --> O(m)

5. getTotalQuantityWithSideAndLevel(side, level)
    * getting entrySet from ladder
    * iterate to level = 0(m) and get total quantity
    * --> O(m)
       
## Part C

Integration Tests

Run "All Integration Feature Tests" if you are using Intellij

(Otherwise run all cucumber tests in src/test/java/integration/features integration.TestRunner)

Added Performance Tests Also

Run "Performance Test"

see performance_test_result.csv