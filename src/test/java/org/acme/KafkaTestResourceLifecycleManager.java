package org.acme;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;

import java.util.HashMap;
import java.util.Map;

public class KafkaTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();

        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("price"));
        env.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("price-sink"));

        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("order"));
        env.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("order-sink"));

        return env;
    }

    @Override
    public void stop() {
        InMemoryConnector.clear();
    }
}