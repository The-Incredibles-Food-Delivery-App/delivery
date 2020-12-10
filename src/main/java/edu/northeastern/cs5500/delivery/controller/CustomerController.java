package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Customer;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CustomerController {
    private final GenericRepository<Customer> customers;

    @Inject
    CustomerController(GenericRepository<Customer> customerRepository) {
        customers = customerRepository;
        log.info("CustomerController > construct");

        initializeCustomers();
        if (customers.count() > 0) {
            return;
        }
    }
 /**
     * Returns the customer corresponding to the given id
     *
     * @param uuid - the id of the customer
     * @return the customer corresponding to the given id
     */
    @Nullable
    public Customer getCustomer(@Nonnull ObjectId uuid) {
        log.debug("CustomerController > getCustomer({})", uuid);
        return customers.get(uuid);
    }

    /**
     * Returns all customers in the repo
     *
     * @return all the customers
     */
    @Nonnull
    public Collection<Customer> getCustomers() {
        log.debug("CustomerController > getCustomers()");
        return customers.getAll();
    }

    /**
     * Adds a customer to the repository after it has been validated to be a valid customer
     *
     * @param customer - the customer to be validated
     * @throws InvalidUserException if the customer is not valid
     * @return the newly added customer to the repository
     */
    @Nonnull
    public Customer addCustomer(@Nonnull Customer customer)
            throws DuplicateKeyException, InvalidUserException {
        log.debug("CutomerController > addCustomer(...)");
        if (!customer.isValid()) {
            throw new InvalidUserException("Invalid Customer");
        }

        ObjectId id = customer.getId();

        if (id != null && customers.get(id) != null) {
            throw new DuplicateKeyException("This customer already exists");
        }

        return customers.add(customer);
    }

    /**
     * Updates the customer with the newly inputted customer
     *
     * @param customer - the customer to be validated
     * @throws Exception if the customer is invalid
     */
    public void updateCustomer(@Nonnull Customer customer) throws Exception {
        log.debug("CustomerController > updateCustomer(...)");
        customers.update(customer);
    }

    /**
     * Deletes the customer from the repository based off the customer id passed in
     *
     * @param id - the customer's id to vbe deleted
     * @throws Exception if the customer is invalid
     */
    public void deleteCustomer(@Nonnull ObjectId id) throws Exception {
        log.debug("CustomerController > deleteCustomer(...)");
        customers.delete(id);
    }

    /** Initializes the customer repository with default data */
    private void initializeCustomers() {
        log.info("CustomerController > construct > adding default customers");

        Customer defaultcustomer1 = new Customer();
        defaultcustomer1.setFirstName("Ellie");
        defaultcustomer1.setLastName("Gatto");
        defaultcustomer1.setUsername("ellie7");
        defaultcustomer1.setPassword("elle777");
        defaultcustomer1.setEmail("ellie7@gmail.com");
        defaultcustomer1.setPhoneNumber("1111111111");
        defaultcustomer1.setAddress("123 Kimchi Road");

        Customer defaultcustomer2 = new Customer();
        defaultcustomer2.setFirstName("Paul");
        defaultcustomer2.setLastName("Hollywood");
        defaultcustomer2.setUsername("phollywood");
        defaultcustomer2.setPassword("phollywoods123");
        defaultcustomer2.setEmail("phollywood@gbbo.com");
        defaultcustomer2.setPhoneNumber("1234567890");
        defaultcustomer2.setAddress("555 Hollywood Blvd");

        try {
            addCustomer(defaultcustomer1);
            addCustomer(defaultcustomer2);
        } catch (Exception e) {
            log.error("CustomerController > construct > adding default customers > failure?");
            e.printStackTrace();
        }
    }
}
