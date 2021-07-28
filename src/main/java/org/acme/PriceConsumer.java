package org.acme;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PriceConsumer {

    private static final Logger LOGGER = Logger.getLogger(PriceConsumer.class);

    private Price price;

    @Incoming("price")
    public void increasePrice(Price price) {
        LOGGER.infof("Received price [%f]", price.getPrice());

        price.setPrice(price.getPrice() + 1);
        this.price = price;

        LOGGER.infof("Increased price [%f] and stored", price.getPrice());
    }

    public Price getPrice() {
        return price;
    }
}
