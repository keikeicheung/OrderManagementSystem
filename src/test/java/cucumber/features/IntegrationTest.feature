# Created by keikeicheung at 03/03/2018
Feature: Order Book Integration Tests

  Scenario: Adding BID order to order book
    Given There are several orders in BID ladder of order book
      | Price | Quantity |
      | 100.0 | 3000000  |
      | 99.8  | 7000000  |
      | 99.7  | 1000000  |
    When Adding order
      | Side | Price | Quantity |
      | BID  | 99.9  | 2000000  |
    Then The BID ladder will be
      | Price | Quantity |
      | 100.0 | 3000000  |
      | 99.9  | 2000000  |
      | 99.8  | 7000000  |
      | 99.7  | 1000000  |

  Scenario: Adding OFFER order to order book
    Given There are several orders in OFFER ladder of order book
      | Price | Quantity |
      | 100.2 | 2000000  |
      | 100.3 | 6000000  |
      | 100.6 | 3000000  |
    When Adding order
      | Side  | Price | Quantity |
      | OFFER | 100.5 | 2000000  |
    Then The OFFER ladder will be
      | Price | Quantity |
      | 100.2 | 2000000  |
      | 100.3 | 6000000  |
      | 100.5 | 2000000  |
      | 100.6 | 3000000  |

  Scenario: Removing BID order to order book
    Given There are several orders in BID ladder of order book
      | Id      | Price | Quantity |
      | order-1 | 100.0 | 3000000  |
      | order-2 | 99.8  | 7000000  |
      | order-3 | 99.7  | 1000000  |
    When Removing order order-1
    Then The BID ladder will be
      | Price | Quantity |
      | 99.8  | 7000000  |
      | 99.7  | 1000000  |

  Scenario: Removing OFFER order to order book
    Given There are several orders in OFFER ladder of order book
      | Id      | Price | Quantity |
      | order-1 | 101.1 | 3000000  |
      | order-2 | 101.2 | 7000000  |
      | order-3 | 101.3 | 1000000  |
      | order-4 | 101.7 | 1000000  |
    When Removing order order-3
    Then The OFFER ladder will be
      | Price | Quantity |
      | 101.1 | 3000000  |
      | 101.2 | 7000000  |
      | 101.7 | 1000000  |

  Scenario: Adding BID order to order book when order with same price exists
    Given There are several orders in BID ladder of order book
      | Price | Quantity |
      | 100.0 | 3000000  |
      | 99.8  | 7000000  |
      | 99.7  | 1000000  |
    When Adding order
      | Side | Price | Quantity |
      | BID  | 100.0 | 2000000  |
    Then The total quantity of BID ladder at level 0 will be 5000000

  Scenario: Adding OFFER order to order book when order with same price exists
    Given There are several orders in OFFER ladder of order book
      | Price | Quantity |
      | 100.1 | 2000000  |
      | 100.4 | 6000000  |
      | 100.5 | 4000000  |
    When Adding order
      | Side  | Price | Quantity |
      | OFFER | 100.5 | 2000000  |
    Then The total quantity of OFFER ladder at level 2 will be 6000000

  Scenario: Removing BID order from order book with multiple orders at same price on ladder
    Given There are several orders in BID ladder of order book
      | Id      | Price | Quantity |
      | order-1 | 100.0 | 3000000  |
      | order-2 | 99.8  | 7000000  |
      | order-3 | 99.8  | 9000000  |
      | order-4 | 99.7  | 1000000  |
    When Removing order order-2
    Then The BID ladder will be
      | Price | Quantity |
      | 100.0 | 3000000  |
      | 99.8  | 9000000  |
      | 99.7  | 1000000  |

  Scenario: Removing OFFER order from order book with multiple orders at same price on ladder
    Given There are several orders in OFFER ladder of order book
      | Id      | Price | Quantity |
      | order-1 | 101.0 | 3000000  |
      | order-2 | 101.2 | 7000000  |
      | order-3 | 101.3 | 9000000  |
      | order-4 | 101.2 | 2000000  |
      | order-5 | 101.5 | 1000000  |
      | order-6 | 101.2 | 1000000  |
    When Removing order order-4
    Then The OFFER ladder will be
      | Price | Quantity |
      | 101.0 | 3000000  |
      | 101.2 | 8000000  |
      | 101.3 | 9000000  |
      | 101.5 | 1000000  |

  Scenario: Retrieve BID price from side and level
    Given There are several orders in BID ladder of order book
      | Price | Quantity |
      | 100.0 | 3000000  |
      | 99.8  | 7000000  |
      | 99.7  | 1000000  |
      | 99.6  | 8000000  |
    Then The price of BID ladder at level 2 will be 99.7

  Scenario: Retrieve BID price from side with non existing level
    Given There are several orders in BID ladder of order book
      | Price | Quantity |
      | 100.0 | 3000000  |
      | 99.8  | 7000000  |
      | 99.7  | 1000000  |
      | 99.6  | 8000000  |
    When Getting the price of BID ladder at non existing level 4
    Then It throws array index out of bound exception

  Scenario: Retrieve OFFER price from side and level
    Given There are several orders in OFFER ladder of order book
      | Price | Quantity |
      | 101.0 | 3000000  |
      | 101.2 | 7000000  |
      | 101.3 | 1000000  |
      | 101.6 | 8000000  |
    Then The price of OFFER ladder at level 3 will be 101.6

  Scenario: Retrieve OFFER price from side with non existing level
    Given There are several orders in OFFER ladder of order book
      | Price | Quantity |
      | 100.0 | 3000000  |
      | 100.2  | 7000000  |
      | 100.3  | 1000000  |
      | 100.4  | 8000000  |
    When Getting the price of OFFER ladder at non existing level 4
    Then It throws array index out of bound exception

  Scenario: Retrieve all orders
    Given There are several orders in order book
      | Id      | Side  | Price | Quantity |
      | order-1 | BID   | 100.0 | 3000000  |
      | order-2 | BID   | 99.8  | 7000000  |
      | order-3 | BID   | 99.7  | 1000000  |
      | order-4 | BID   | 99.6  | 8000000  |
      | order-5 | OFFER | 100.3 | 7000000  |
      | order-6 | OFFER | 100.4 | 1000000  |
    Then All orders in order book
      | Id      | Side  | Price | Quantity |
      | order-1 | BID   | 100.0 | 3000000  |
      | order-2 | BID   | 99.8  | 7000000  |
      | order-3 | BID   | 99.7  | 1000000  |
      | order-4 | BID   | 99.6  | 8000000  |
      | order-5 | OFFER | 100.3 | 7000000  |
      | order-6 | OFFER | 100.4 | 1000000  |

  Scenario: Modify order quantity with order id and new quantity
    Given There are several orders in order book
      | Id      | Side  | Price | Quantity |
      | order-1 | BID   | 100.0 | 3000000  |
      | order-2 | BID   | 99.8  | 7000000  |
      | order-3 | OFFER | 100.3 | 7000000  |
      | order-4 | OFFER | 100.4 | 1000000  |
    When Modify order order-3 to quantity 5000000
    Then All orders in order book
      | Id      | Side  | Price | Quantity |
      | order-1 | BID   | 100.0 | 3000000  |
      | order-2 | BID   | 99.8  | 7000000  |
      | order-3 | OFFER | 100.3 | 5000000  |
      | order-4 | OFFER | 100.4 | 1000000  |
    Then The OFFER ladder will be
      | Price | Quantity |
      | 100.3 | 5000000  |
      | 100.4 | 1000000  |

  Scenario: Creating order with the same order id
    Given There are several orders in order book
      | Id      | Side  | Price | Quantity |
      | order-1 | BID   | 100.0 | 3000000  |
      | order-2 | BID   | 99.8  | 7000000  |
      | order-3 | OFFER | 100.3 | 7000000  |
      | order-4 | OFFER | 100.4 | 1000000  |
    When Adding order with order id exists
      | Id      | Side  | Price | Quantity |
      | order-3 | OFFER | 100.5 | 2000000  |
    Then It throws runtime exception
    Then All orders in order book
      | Id      | Side  | Price | Quantity |
      | order-1 | BID   | 100.0 | 3000000  |
      | order-2 | BID   | 99.8  | 7000000  |
      | order-3 | OFFER | 100.3 | 7000000  |
      | order-4 | OFFER | 100.4 | 1000000  |