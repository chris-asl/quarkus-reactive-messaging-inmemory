package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OrderDeserializer  extends ObjectMapperDeserializer<Order> {
    public OrderDeserializer() {
        super(Order.class);
    }
}
