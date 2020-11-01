package edu.northeastern.cs5500.delivery.model;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {
    @Provides
    public Class<Delivery> provideDeliveryClass() {
        return Delivery.class;
    }

    @Provides
    public Class<Restaurant> provideRestaurantClass() {
        return Restaurant.class;
    }

    @Provides
    public Class<Order> provideOrderClass() {
        return Order.class;
    }

    // TODO: do I just make general User or separate Customer/ DeliveryDriver?
    @Provides
    public Class<User> provideUserClass() {
        return User.class;
    }

    @Provides
    public Class<Customer> provideCustomerClass() {
        return Customer.class;
    }

    @Provides
    public Class<DeliveryDriver> provideDeliveryDriverClass() {
        return DeliveryDriver.class;
    }
}
