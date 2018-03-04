package integration.util;

import orderbook.Order;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by keikeicheung on 03/03/2018.
 */
public class CustomizedMatcher {

    public static Matcher<Map<Double, Long>> ladderMatch(Map<Double, Long> expectedLadder) {
        return new TypeSafeDiagnosingMatcher<Map<Double, Long>>() {
            @Override
            protected boolean matchesSafely(Map<Double, Long> ladder, Description mismatchDescription) {
                mismatchDescription.appendText("\n")
                        .appendText(String.join("\n",
                        ladder.entrySet().stream().map(entry -> entry.getKey()+":"+entry.getValue()).collect(Collectors.toList())));
                if (ladder.size() != expectedLadder.size()) {
                    return false;
                }
                return ladder.equals(expectedLadder);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("\n")
                        .appendText(String.join("\n",
                        expectedLadder.entrySet().stream().map(entry -> entry.getKey()+":"+entry.getValue()).collect(Collectors.toList())));
            }
        };
    }

    public static Matcher<Collection<Order>> containsInAnyOrder(Collection<Order> expectedItems) {
        return new TypeSafeDiagnosingMatcher<Collection<Order>>() {
            @Override
            protected boolean matchesSafely(Collection<Order> items, Description mismatchDescription) {
                mismatchDescription.appendText("\n")
                        .appendText(String.join("\n",
                                items.stream().map(item -> item.toString()).collect(Collectors.toList())));
                if (items.size() != expectedItems.size()) {
                    return false;
                }
                return items.containsAll(expectedItems);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("\n")
                        .appendText(String.join("\n",
                                expectedItems.stream().map(item -> item.toString()).collect(Collectors.toList())));

            }
        };
    }
}
