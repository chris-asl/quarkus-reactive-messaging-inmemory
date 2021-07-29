package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import java.util.List;

import static org.awaitility.Awaitility.await;

@QuarkusTest
@QuarkusTestResource(KafkaTestResourceLifecycleManager.class)
class BaristaTest {

    @Inject @Any
    InMemoryConnector connector;

    @Incoming("quarkus-order")
    @Outgoing("quarkus-queue")
    Beverage consume(Order order) {
        System.out.println("Order received " + order.getFood());
        final var beverage = new Beverage();
        beverage.setBeverage(order.getFood());
        return beverage;
    }

    @Test
    void testProcessOrder() {
        InMemorySource<Order> orders = connector.source("quarkus-order");
        InMemorySink<Beverage> queue = connector.sink("quarkus-queue");

        Order order = new Order();
        order.setFood("coffee");

        orders.send(order);

        await().<List<? extends Message<Beverage>>>until(queue::received, t -> t.size() == 1);

        Beverage queuedBeverage = queue.received().get(0).getPayload();
        Assertions.assertEquals("coffee", queuedBeverage.getBeverage());
    }

}