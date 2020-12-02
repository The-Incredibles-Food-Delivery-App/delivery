package edu.northeastern.cs5500.delivery.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.delivery.model.CreditCard;
import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DeliveryDriver;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.User;
import edu.northeastern.cs5500.delivery.service.MongoDBService;

@Module
public class RepositoryModule {
    @Provides
    public GenericRepository<Delivery> provideDeliveryRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<>(Delivery.class, mongoDBService);
    }

    @Provides
    public GenericRepository<Order> provideOrderRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<>(Order.class, mongoDBService);
    }

    @Provides
    public GenericRepository<Customer> provideCustomerRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<>(Customer.class, mongoDBService);
    }

    @Provides
    public GenericRepository<DeliveryDriver> provideDeliveryDriverRepository(
            MongoDBService mongoDBService) {
        return new MongoDBRepository<>(DeliveryDriver.class, mongoDBService);
    }

    @Provides
    public GenericRestaurantRepository provideRestaurantRepository(MongoDBService mongoDBService) {
        return new MongoDBRestaurantRepository(mongoDBService);
    }

    @Provides
    public GenericRepository<CreditCard> provideCreditCardRepository(
            MongoDBService mongoDBService) {
        return new MongoDBRepository<>(CreditCard.class, mongoDBService);
    }

    @Provides
    public GenericRepository<User> provideReviewRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<>(User.class, mongoDBService);
    }
}

/*
package edu.northeastern.cs5500.delivery.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.delivery.model.*;

@Module
public class RepositoryModule {
    @Provides
    public GenericRepository<Delivery> provideDeliveryRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<Order> provideOrderRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<MenuItem> provideMenuItemRepository() {
        return new InMemoryRepository<>();
    }

    // TODO: do I just make general User or separate Customer/ DeliveryDriver?
    @Provides
    public GenericRepository<Customer> provideCustomerRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<DeliveryDriver> provideDeliveryDriverRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRestaurantRepository provideRestaurantRepository() {
        return new InMemoryRestaurantRepository();
    }

    @Provides
    public GenericRepository<CreditCard> provideCreditCardRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<Review> provideReviewRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<Recommendation> provideRecommendationRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<User> provideUserRepository() {
        return new InMemoryRepository<>();
    }
}
*/
