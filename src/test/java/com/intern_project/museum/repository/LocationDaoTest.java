package com.intern_project.museum.repository;

import com.intern_project.museum.AbstractTest;
import com.intern_project.museum.testUtils.Database;
import com.intern_project.museum_of_interesting_things.configuration.MyConfig;
import com.intern_project.museum_of_interesting_things.configuration.MyWebInitializer;
import com.intern_project.museum_of_interesting_things.entity.Item;
import com.intern_project.museum_of_interesting_things.entity.Location;
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
public class LocationDaoTest extends AbstractTest {
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
        List<Location> locations = dao.getAll(Location.class);
        assertEquals(2, locations.size());
    }

    @Test
    void deleteSuccess() {
        dao.delete(Location.class, 1);
        Location deleted = dao.get(Location.class, 1);
        assertNull(deleted);
    }

    @Test
    void getByIdSuccess() {
        Location retrievedLoc = dao.get(Location.class, 1);
        String name = "room 4A";
        assertEquals(name, retrievedLoc.getStorageType());
    }

    @Test
    void saveSuccess() {
        Location location = new Location("room 6A", "left top shelf B7", new Date());
        dao.save(location);
        Location inserted = dao.get(Location.class, 3);
        assertNotNull(inserted);
        assertEquals(inserted.getStorageType(), location.getStorageType());
    }

    @Test
    void saveOrUpdateTest() {
        Location getLoc = dao.get(Location.class, 1);
        getLoc.setDescription("updated");
        dao.saveOrUpdate(getLoc);
        Location updatedLoc = dao.get(Location.class, 1);
        assertEquals(getLoc.getDescription(), updatedLoc.getDescription());
    }
}
