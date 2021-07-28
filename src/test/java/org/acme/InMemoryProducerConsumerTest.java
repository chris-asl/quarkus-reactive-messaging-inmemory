package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTestResource(KafkaTestResourceLifecycleManager.class)
@QuarkusTest
class InMemoryProducerConsumerTest {

    @Inject @Any
    InMemoryConnector connector;

    @Inject
    PriceConsumer priceConsumer;

    @Inject
    PriceProducer priceProducer;

    @Test
    void testProducer() {
        InMemorySink<Price> receivedPrices = connector.sink("price-sink");
        final var price = new Price();
        price.setPrice(42d);

        priceProducer.publishPrice(price);

        await().<List<? extends Message<Price>>>until(receivedPrices::received, t -> t.size() == 1);

        final var receivedPrice = receivedPrices.received().get(0).getPayload();
        assertThat(receivedPrice.getPrice()).isEqualTo(42);
    }

    @Test
    void testConsumer() {
        InMemorySource<Price> priceSource = connector.source("price");
        final var price = new Price();
        price.setPrice(42d);

        priceSource.send(price);

        await().until(() -> priceConsumer.getPrice() != null);

        final var receivedPrice = priceConsumer.getPrice();
        assertThat(receivedPrice.getPrice()).isEqualTo(43);
    }
}
