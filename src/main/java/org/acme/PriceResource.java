package org.acme;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/price")
public class PriceResource {

    private final PriceProducer priceProducer;

    @Inject
    public PriceResource(PriceProducer priceProducer) {
        this.priceProducer = priceProducer;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitPrice() {
        final var price = new Price();
        price.setPrice(40d);

        priceProducer.publishPrice(price);

        return Response.status(200).build();
    }
}
