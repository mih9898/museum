package com.intern_project.museum_of_interesting_things.controller;

import com.intern_project.museum_of_interesting_things.entity.*;
import com.intern_project.museum_of_interesting_things.repository.GenericDao;
import com.intern_project.museum_of_interesting_things.utils.EntityUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

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



    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(Model model) {
        List<Item> items = genericDao.getAll(Item.class);
        model.addAttribute("items", items);
        return "items";
    }

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public String item(Model model, @RequestParam int id) throws IOException {
        Item item = genericDao.get(Item.class, id);
        model.addAttribute("updatedItem", new Item());
        model.addAttribute("item", item);
        return "item";
    }

    @RequestMapping(value = "/updateItem", method = RequestMethod.POST)
    public String updateItem(@ModelAttribute("updatedItem") Item updatedItem,
                             HttpServletRequest request,
                             Model model,
                             @RequestParam int id
    ) {
        System.out.println("updatedItem" + updatedItem);
        String referer = request.getHeader("Referer");
        Item original = genericDao.get(Item.class, id);
        EntityUtility.merge(original, updatedItem);
        genericDao.saveOrUpdate(original);
        model.addAttribute("item", original);
        return "redirect:" + referer;
    }



    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public String addItem(@RequestParam(name = "itemName", required = false) String itemName,
                          @RequestParam(name = "itemDescription", required = false) String itemDescription,
                          @RequestParam(name = "dateAcquired", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateAcquired,
                          @RequestParam(value = "isMuseumItem", required = false) String isMuseumItem,
                          @RequestParam(name = "itemLostDesc", required = false) String itemLostDesc,
                          @RequestParam(name = "dateLost", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateLost,
                          @RequestParam(name = "storageType", required = false) String storageType,
                          @RequestParam(name = "locDescription", required = false) String locDescription,
                          @RequestParam(name = "dateWhenPut", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateWhenPut,

                          final Model model) {

        int isMuseum = isMuseumItem.isEmpty() ? 0 : 1;
        int isLost = itemLostDesc.isEmpty() ? 0 : 1;
        Item newItem = new Item(itemName, itemDescription, dateAcquired, isMuseum);
        newItem.setIsLost(isLost);

        if (!itemLostDesc.isEmpty()) {
            LostItem lostItem = new LostItem(locDescription, dateLost, newItem);
            newItem.setLostItem(lostItem);
        }

        if (!storageType.isEmpty()) {
            Location location = new Location(storageType, locDescription, dateWhenPut);
            newItem.addLocationToItem(location);
        }

        genericDao.save(newItem);

        model.addAttribute("title", "New item was successfully saved");

        return "newItem";
    }



    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public String addItemToDB(Model model) {
        return "newItem";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        return "index";
    }

    //Testing time
    @RequestMapping("/test")
    public String test(Model model) throws IOException, URISyntaxException {
        //Item item = new Item("name2", "desc2", new Date(),0,1);
        //LostItem lostItem = new LostItem(item.getId(), "desc", new Date(), item);
        //item.setLostItem(lostItem);

        Item item = genericDao.get(Item.class, 3);
        item.setName("updatedName");
        genericDao.saveOrUpdate(item);
        //Location location = new Location("room 5A", "left top shelf B7", new Date());
        //item.addLocationToItem(location);
        //Employee employee = new Employee("manager", "Myke", "Turchanov", 333.33, "address", "city", "WI", "1233", 1);
        //Employee employee = genericDao.get(Employee.class, 2);
        //PhoneNumber phoneNumber = new PhoneNumber(12321312);
        //phoneNumber.setEmployee(employee);
        //employee.addPhoneNumberToEmployee(phoneNumber);
        //EmployeeItem employeeItem = new EmployeeItem(employee,item, 99.12);

        //genericDao.saveOrUpdate(employeeItem);
        //System.out.println(employee);
        //model.addAttribute("employee", employee);
        return "test";
    }

}
