package com.intern_project.museum.repository;

import com.intern_project.museum.AbstractTest;
import com.intern_project.museum.testUtils.Database;
import com.intern_project.museum_of_interesting_things.configuration.MyConfig;
import com.intern_project.museum_of_interesting_things.configuration.MyWebInitializer;
import com.intern_project.museum_of_interesting_things.entity.Employee;
import com.intern_project.museum_of_interesting_things.entity.EmployeeItem;
import com.intern_project.museum_of_interesting_things.repository.GenericDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


/**
 * The type Generic dao test.
 *
 * @author mturchanov
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { MyConfig.class, MyWebInitializer.class})
public class EmployeeDaoTest extends AbstractTest {
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
        List<Employee> employees = dao.getAll(Employee.class);
        assertEquals(1, employees.size());
    }

    //@Test
    //void deleteSuccess() {
    //    Employee test = dao.get(Employee.class, 1);
    //    test.setEmployeeItems(null);
    //    dao.saveOrUpdate(test);
    //    dao.delete(Employee.class, 1);
    //    Employee deleted = dao.get(Employee.class, 1);
    //    assertNull(deleted);
    //}

    @Test
    void getByIdSuccess() {
        Employee retrievedEmp = dao.get(Employee.class, 1);
        String name = "Myke";
        assertEquals(name, retrievedEmp.getFirstName());
    }

    @Test
    void saveSuccess() {
        Employee employee = new Employee("programmer", "Rojer", "Smith", 111.22, "address", "city", "WI", "1233", 1);
        dao.save(employee);
        Employee inserted = dao.get(Employee.class, 2);
        assertNotNull(inserted);
        assertEquals(inserted.getFirstName(), employee.getFirstName());
    }

    @Test
    void saveOrUpdateTest() {
        Employee getEmployee = dao.get(Employee.class, 1);
        getEmployee.setFirstName("Mike");
        dao.saveOrUpdate(getEmployee);
        Employee updatedEmp = dao.get(Employee.class, 1);
        assertEquals(getEmployee.getFirstName(), updatedEmp.getFirstName());
    }
}
