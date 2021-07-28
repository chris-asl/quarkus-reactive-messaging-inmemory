package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.assertj.core.api.Assertions;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.List;

import static org.awaitility.Awaitility.await;

@QuarkusTestResource(KafkaTestResourceLifecycleManager.class)
@QuarkusTest
class InMemoryConnectorTest {

    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    void test() {
        InMemorySource<Order> orders = connector.source("order");
        InMemorySink<Order> results = connector.sink("order-sink");

        final var order = new Order();
        order.setFood("Mousaka");
        orders.send(order);

        await().<List<? extends Message<Order>>>until(results::received, t -> t.size() == 1);

        final var receivedOrder = results.received().get(0).getPayload();
        Assertions.assertThat(receivedOrder.getFood()).isEqualTo("Mousaka");
    }
}
