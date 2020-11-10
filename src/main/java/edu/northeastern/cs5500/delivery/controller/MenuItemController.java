package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.MenuItem;
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
public class MenuItemController {
    private final GenericRepository<MenuItem> menuItems;

    @Inject
    MenuItemController(GenericRepository<MenuItem> menuItemRepository) {
        menuItems = menuItemRepository;

        log.info("MenuItemController > construct");

        if (menuItems.count() > 0) {
            return;
        }

        log.info("MenuItemController > construct > adding default menu items");

        final MenuItem defaultItem1 = new MenuItem();
        defaultItem1.setName("BBQ Pork Bun");
        defaultItem1.setPrice(499);

        final MenuItem defaultItem2 = new MenuItem();
        defaultItem2.setName("Shrimp Dumpling");
        defaultItem2.setPrice(599);

        try {
            addMenuItem(defaultItem1);
            addMenuItem(defaultItem2);
        } catch (Exception e) {
            log.error("MenuItemController > construct > adding default menu items > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public MenuItem getMenuItem(@Nonnull ObjectId uuid) {
        log.debug("MenuItemController > getMenuItem({})", uuid);
        return menuItems.get(uuid);
    }

    @Nonnull
    public Collection<MenuItem> getMenuItems() {
        log.debug("MenuItemController > getMenuItems()");
        return menuItems.getAll();
    }

    public void updateMenuItem(@Nonnull MenuItem item) throws Exception {
        log.debug("MenuItemController > updateMenuItem(...)");
        // TODO: Do we need to check that the order id exists in the repo?
        menuItems.update(item);
    }

    public void deleteMenuItem(@Nonnull ObjectId id) throws Exception {
        log.debug("MenuItemController > deleteMenuItem(...)");
        menuItems.delete(id);
    }

    /**
     * Adds a menu item to the repository
     *
     * @param item - the menu item to add
     * @throws InvalidOrderException - when the menu item given is invalid
     * @throws DuplicateKeyException - when the item id is already contained in the collection of
     *     menu items
     */
    public MenuItem addMenuItem(@Nonnull MenuItem item)
            throws InvalidObjectException, DuplicateKeyException {
        log.debug("OrderController > addOrder(...)");
        if (!item.isValid()) {
            throw new InvalidObjectException("Invalid Menu Item");
        }

        ObjectId id = item.getId();

        if (id != null && menuItems.get(id) != null) {
            throw new DuplicateKeyException("Key already in use");
        }
        return menuItems.add(item);
    }
}
