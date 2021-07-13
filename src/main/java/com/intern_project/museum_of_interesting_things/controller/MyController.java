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
import java.util.stream.Collectors;

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

    private final GenericDao genericDao;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public MyController(GenericDao genericDao) {
        this.genericDao = genericDao;
    }



    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(Model model) {
        List<Item> items = genericDao.getAll(Item.class);
        items = items.stream()
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("items", items);

        return "items";
    }

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public String item(Model model, @RequestParam int id) throws IOException {
        List<Employee> allEmps = genericDao.getAll(Employee.class);
        Item item = genericDao.get(Item.class, id);
        model.addAttribute("allEmps", allEmps);
        model.addAttribute("employeeItem", new EmployeeItem());
        model.addAttribute("updatedItem", new Item());
        model.addAttribute("newLocation", new Location());
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

    @RequestMapping(value = "/addNewLocation", method = RequestMethod.POST)
    public String addNewLocation(@RequestParam(name = "storageType", required = false) String storageType,
                                 @RequestParam(name = "locDescription", required = false) String locDescription,
                                 @RequestParam(name = "dateWhenPut", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateWhenPut,
                             HttpServletRequest request,
                             Model model,
                             @RequestParam int id
    ) {
        Item item = genericDao.get(Item.class, id);
        if (!storageType.isEmpty()) {
            Location location = new Location(storageType, locDescription, dateWhenPut);
            item.addLocationToItem(location);
        }
        String referer = request.getHeader("Referer");
        genericDao.saveOrUpdate(item);
        model.addAttribute("item", item);
        return "redirect:" + referer;
    }


    @RequestMapping(value = "/addNewAppraisal", method = RequestMethod.POST)
    public String addNewAppraisal(@ModelAttribute("employeeItem") EmployeeItem employeeItem,
                             HttpServletRequest request,
                             Model model
    ) {
        System.out.println("addNewAppraisal" + employeeItem);
        Employee emp = genericDao.get(Employee.class, employeeItem.getEmployee().getId());
        Item item = genericDao.get(Item.class, employeeItem.getItem().getId());
        employeeItem.setEmployee(emp);
        employeeItem.setItem(item);
        item.addEmployeeAprToItem(employeeItem);
        genericDao.saveOrUpdate(item);

        String referer = request.getHeader("Referer");
//        Item original = genericDao.get(Item.class, id);
//        EntityUtility.merge(original, updatedItem);
//        genericDao.saveOrUpdate(original);
//        model.addAttribute("item", original);
        return "redirect:" + referer;
    }




    //TODO: replace requestParams with modelAttribute -> change on view as well
    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public String addItem(@RequestParam(name = "itemName", required = false) String itemName,
                          @RequestParam(name = "itemDescription", required = false) String itemDescription,
                          @RequestParam(name = "dateAcquired", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateAcquired,
                          @RequestParam(value = "isMuseumItem", required = false) boolean isMuseumItem,
                          @RequestParam(name = "itemLostDesc", required = false) String itemLostDesc,
                          @RequestParam(name = "dateLost", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateLost,
                          @RequestParam(name = "storageType", required = false) String storageType,
                          @RequestParam(name = "locDescription", required = false) String locDescription,
                          @RequestParam(name = "dateWhenPut", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateWhenPut,

                          final Model model) {

//        int isMuseum = isMuseumItem.isEmpty() ? 0 : 1;

        int isLost = itemLostDesc.isEmpty() ? 0 : 1;
        Item newItem = new Item(itemName, itemDescription, dateAcquired, isMuseumItem);
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


    @RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
    public String deleteItem(@RequestParam("itemId") int id, HttpServletRequest request) {
        genericDao.delete(Item.class, id);
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
//        return "items";
    }


    @RequestMapping(value = "/addEmployee", method = RequestMethod.GET)
    public String addEmployee(Model model) {
//        List<PhoneNumber> phones = new ArrayList<>();
        model.addAttribute("newEmployee", new Employee());
        model.addAttribute("phoneNumber", new PhoneNumber());

        return "newEmployee";
    }

    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST)
    public String addEmployee(Model model,
                              @ModelAttribute("newEmployee") Employee newEmployee,
                              @RequestParam(value = "phoneNumber", required = false) int phoneNumber,
                              HttpServletRequest request
    ) {
        System.out.println(phoneNumber);

        //TODO: decide what to do with input number optionality(if not entered then err bcause num cannot be null)
        //      or leave like this (0 instead null)
        if (phoneNumber != 0) {
            PhoneNumber number = new PhoneNumber(phoneNumber);
            number.setEmployee(newEmployee);
            newEmployee.addPhoneNumberToEmployee(number);
        }

        genericDao.save(newEmployee);
        HttpSession session = request.getSession();
        session.setAttribute("title", "New item was successfully saved");
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public String employees(Model model) {
        List<Employee> employees = genericDao.getAll(Employee.class);
        employees = employees.stream()
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("employees", employees);

        return "employees";
    }

    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String employee(Model model, @RequestParam int id) throws IOException {
        Employee employee = genericDao.get(Employee.class, id);
        model.addAttribute("updatedEmp", new Employee());
        model.addAttribute("newPhone", new PhoneNumber());
        model.addAttribute("employee", employee);
        return "employee";
    }

    @RequestMapping(value = "/updatedEmp", method = RequestMethod.POST)
    public String updatedEmp(@ModelAttribute("updatedEmp") Employee updatedEmp,
                             HttpServletRequest request,
                             Model model,
                             @RequestParam int id
    ) {

        Employee orig = genericDao.get(Employee.class, id);
        genericDao.saveOrUpdate(orig);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public String deleteEmployee(@RequestParam("empId") int id,
                             HttpServletRequest request
    ) {
        Employee emp = genericDao.get(Employee.class, id);
        genericDao.deleteObject(emp);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }



    @RequestMapping(value = "/addPhone", method = RequestMethod.POST)
    public String addPhone(Model model, @ModelAttribute("newPhone") PhoneNumber phoneNumber, HttpServletRequest request) {
        System.out.println(phoneNumber);
        System.out.println(phoneNumber.getEmployee());
        String referer = request.getHeader("Referer");

        Employee emp = genericDao.get(Employee.class, phoneNumber.getEmployee().getId());
        emp.addPhoneNumberToEmployee(phoneNumber);
        phoneNumber.setEmployee(emp);
        genericDao.saveOrUpdate(emp);
        return "redirect:" + referer;

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
