package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.CreditCardController;
import edu.northeastern.cs5500.delivery.model.CreditCard;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CreditCardView implements View {

    @Inject
    CreditCardView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject CreditCardController creditCardController;

    @Override
    public void register() {
        log.info("CreditCardView > register");

        get(
                "/creditcard",
                (request, response) -> {
                    log.debug("/creditcard");
                    response.type("application/json");
                    return creditCardController.getCreditCards();
                },
                jsonTransformer);

        get(
                "/creditcard/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/creditcard/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    CreditCard creditCard = creditCardController.getCreditCard(id);
                    if (creditCard == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return creditCard;
                },
                jsonTransformer);

        post(
                "/creditcard",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    CreditCard creditCard = mapper.readValue(request.body(), CreditCard.class);
                    if (!creditCard.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    creditCard.setId(null);
                    creditCard = creditCardController.addCreditCard(creditCard);

                    response.redirect(
                            String.format("/creditcard/{}", creditCard.getId().toHexString()), 301);
                    return creditCard;
                });

        put(
                "/creditcard",
                (request, response) -> {
                    final String creditCardParam = request.queryParams("creditCardId");
                    final String creditCardNum = request.queryParams("number");
                    log.debug("/creditcard/:creditcardid<{}>", creditCardParam);
                    final ObjectId creditCardId = new ObjectId(creditCardParam);
                    CreditCard creditCard = creditCardController.getCreditCard(creditCardId);

                    if (!creditCard.isValid()) {
                        response.status(400);
                        return "";
                    }
                    long updatedNumber = Long.parseLong(creditCardNum);
                    creditCard.setCardNumber(updatedNumber);

                    creditCardController.updateCreditCard(creditCard);
                    return creditCard;
                },
                jsonTransformer);

        delete(
                "/creditcard",
                (request, response) -> {
                    final String creditCardParam = request.queryParams("creditCardId");
                    final ObjectId creditCardId = new ObjectId(creditCardParam);
                    creditCardController.deleteCreditCard(creditCardId);

                    response.type("application/json");
                    log.debug("Successfully Deleted Credit Card id <{}>", creditCardId);
                    return null;
                });
    }
}
