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

    @Nullable
    public Customer getCustomer(@Nonnull ObjectId uuid) {
        log.debug("CustomerController > getCustomer({})", uuid);
        return customers.get(uuid);
    }

    @Nonnull
    public Collection<Customer> getCustomers() {
        log.debug("CustomerController > getCustomers()");
        return customers.getAll();
    }

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

    public void updateCustomer(@Nonnull Customer customer) throws Exception {
        log.debug("CustomerController > updateCustomer(...)");
        customers.update(customer);
    }

    public void deleteCustomer(@Nonnull ObjectId id) throws Exception {
        log.debug("CustomerController > deleteCustomer(...)");
        customers.delete(id);
    }

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
