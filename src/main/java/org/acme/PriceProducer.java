package org.acme;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PriceProducer {

    private static final Logger LOGGER = Logger.getLogger(PriceProducer.class);

    @Inject @Channel("price-sink")
    Emitter<Price> priceEmitter;

    public void publishPrice(Price price) {
        LOGGER.infof("Sending price [%f]", price.getPrice());
        priceEmitter.send(price);
    }
}
