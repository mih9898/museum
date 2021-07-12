package com.intern_project.museum.repository;

import com.intern_project.museum.AbstractTest;
import com.intern_project.museum.testUtils.Database;
import com.intern_project.museum_of_interesting_things.configuration.MyConfig;
import com.intern_project.museum_of_interesting_things.configuration.MyWebInitializer;
import com.intern_project.museum_of_interesting_things.entity.Item;
import com.intern_project.museum_of_interesting_things.repository.GenericDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


/**
 * The type Generic dao test.
 *
 * @author mturchanov
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { MyConfig.class, MyWebInitializer.class})
public class ItemDaoTest extends AbstractTest {
    /**
     * The Dao.
     */
    @Autowired
    GenericDao dao;

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Run set up tasks before each test:
     * 1. execute sql which deletes everything from the table and inserts records)
     * 2. Create any objects needed in the tests
     */
    @BeforeEach
    void setUp() throws SQLException {
        Database database = Database.getInstance();

        database.runSQL("cleandb.sql");
    }

    @Test
    void getAllSuccess() {
       List<Item> items = dao.getAll(Item.class);
        assertEquals(3, items.size());
    }

    @Test
    void deleteSuccess() {
        dao.delete(Item.class, 1);
        Item deleted = dao.get(Item.class, 1);
        assertNull(deleted);
    }

    @Test
    void getByIdSuccess() {
        Item retrievedItem = dao.get(Item.class, 1);
        System.out.println(retrievedItem);
        String name = "TestName";
        assertEquals(name, retrievedItem.getName());
    }

    @Test
    void saveSuccess() {
        Item item = new Item("Ancient Dioniss Jug", "some new item de4sc", new Date(),0, true);
        dao.save(item);
        Item inserted = dao.get(Item.class, 3);
        assertNotNull(inserted);
        assertEquals(inserted.getName(), item.getName());
    }

    @Test
    void saveOrUpdateTest() {
        Item getItem = dao.get(Item.class, 1);
        getItem.setName("Ancient Backus Jug");
        dao.saveOrUpdate(getItem);
        Item updatedItem = dao.get(Item.class, 1);
        assertEquals(getItem.getName(), updatedItem.getName());
    }
}
