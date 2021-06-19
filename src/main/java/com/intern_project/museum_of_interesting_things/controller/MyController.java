package com.intern_project.museum_of_interesting_things.controller;

import com.intern_project.museum_of_interesting_things.entity.*;
import com.intern_project.museum_of_interesting_things.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller where
 * all application's endpoints are provided
 *
 * @author mturchanov
 */
@Controller
public class MyController {

    private GenericDao genericDao;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public MyController(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    //Testing time
    @RequestMapping("/test")
    public String test(Model model) throws IOException, URISyntaxException {
        Item item = new Item("name2", "desc2", new Date(),0,1);
        //LostItem lostItem = new LostItem(item.getId(), "desc", new Date(), item);
        //item.setLostItem(lostItem);

        //Item item = genericDao.get(Item.class, 2);
        //Location location = new Location("room 5A", "left top shelf B7", new Date());
        //item.addLocationToItem(location);
        //Employee employee = new Employee("manager", "Myke", "Turchanov", 333.33, "address", "city", "WI", "1233", 1);
        Employee employee = genericDao.get(Employee.class, 2);
        //PhoneNumber phoneNumber = new PhoneNumber(12321312);
        //phoneNumber.setEmployee(employee);
        //employee.addPhoneNumberToEmployee(phoneNumber);
        EmployeeItem employeeItem = new EmployeeItem(employee,item, 99.12);

        genericDao.saveOrUpdate(employeeItem);
        //System.out.println(employee);
        //model.addAttribute("employee", employee);
        return "test";
    }

}
