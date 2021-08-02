package com.intern_project.museum_of_interesting_things.controller;

import com.intern_project.museum_of_interesting_things.entity.*;
import com.intern_project.museum_of_interesting_things.repository.GenericDao;
import com.intern_project.museum_of_interesting_things.utils.EntityUtility;
import com.intern_project.museum_of_interesting_things.utils.PropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller where
 * all application's endpoints are provided
 *
 * @author mturchanov
 */
@Controller
public class MyController implements PropertiesLoader {

    private final GenericDao genericDao;

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final Properties properties;
    private final String UPLOAD_LOCATION;

    @Autowired
    public MyController(GenericDao genericDao) {
        this.genericDao = genericDao;
        properties = loadProperties("/paths.properties");
        UPLOAD_LOCATION = properties.getProperty("upload.location");
    }



    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(Model model) {
        List<Item> items = genericDao.getAll(Item.class);
        items = items.stream()
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("items", items);
        model.addAttribute("uploadLocation", UPLOAD_LOCATION);

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
                             @RequestParam(value = "newImage", required = false) MultipartFile newImage,
                             @RequestParam int id
    ) {
        System.out.println("updatedItem" + updatedItem);
        String referer = request.getHeader("Referer");
        Item original = genericDao.get(Item.class, id);
        if (newImage != null && !newImage.isEmpty()) {
            String img = saveImage(newImage);
            deleteItemImage(original.getImage());
            updatedItem.setImage(img);
        }

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
                          @RequestParam(value = "itemImage", required = false) MultipartFile itemImage,
                          HttpServletRequest request,
                          final Model model) {

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

        if (!itemImage.isEmpty()) {
            String imageName = saveImage(itemImage);
            newItem.setImage(imageName);
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
        Item item = genericDao.get(Item.class, id);
        deleteItemImage(item.getImage());
        genericDao.deleteObject(item);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
//        return "items";
    }

    private boolean deleteItemImage(String image) {
        if (!image.equals("noItemImage.png")){
            File fileToDelete = new File(UPLOAD_LOCATION + image);
            System.out.println("deletePath:" + UPLOAD_LOCATION + image);
            return fileToDelete.delete();
        }
        return true;
    }


    @RequestMapping(value = "/addEmployee", method = RequestMethod.GET)
    public String addEmployee(Model model) {
        model.addAttribute("newEmployee", new Employee());
        model.addAttribute("phoneNumber", new PhoneNumber());

        return "newEmployee";
    }

    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST)
    public String addEmployee(Model model,
                              @ModelAttribute("newEmployee") Employee newEmployee,
                              @RequestParam(value = "phoneNumber", required = false) int phoneNumber,
                              @RequestParam(value = "employeeImage", required = false) MultipartFile employeeImage,
                              HttpServletRequest request
    ) {
        System.out.println(phoneNumber);
        User user = genericDao.getFirstEntryBasedOnAnotherTableColumnProperty("username", getCurrentUsername(), User.class);
        //TODO: decide what to do with input number optionality(if not entered then err bcause num cannot be null)
        //      or leave like this (0 instead null)
        if (!employeeImage.isEmpty()) {
            String imageName = saveImage(employeeImage);
            newEmployee.setImage(imageName);
        }
        addPhoneNum(newEmployee, phoneNumber);
        newEmployee.setUser(user);
        user.setEmployee(newEmployee);
        genericDao.save(newEmployee);
        HttpSession session = request.getSession();
        session.setAttribute("title", "New item was successfully saved");
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    private void addPhoneNum(Employee newEmployee, int phoneNumber) {
        if (phoneNumber != 0) {
            PhoneNumber number = new PhoneNumber(phoneNumber);
            number.setEmployee(newEmployee);
            newEmployee.addPhoneNumberToEmployee(number);
        }
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
        String username = getCurrentUsername();
        System.out.println(username);
        if (username != null) {
            User currentUser = genericDao.getFirstEntryBasedOnAnotherTableColumnProperty("username", username, User.class);
            List<Authority> currentUserAuthorityRights = currentUser.getAuthorityList();
            System.out.println(currentUserAuthorityRights);
            for (Authority authority : currentUserAuthorityRights) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    employee.setHasAdminRights(true);
                    break;
                }
            }
            model.addAttribute("currentUsername", currentUser.getUsername());
        }

        model.addAttribute("updatedEmp", new Employee());
        model.addAttribute("newPhone", new PhoneNumber());
        model.addAttribute("employee", employee);

        return "employee";
    }

    @RequestMapping(value = "/updatedEmp", method = RequestMethod.POST)
    public String updatedEmp(@ModelAttribute("updatedEmp") Employee updatedEmp,
                             HttpServletRequest request,
                             Model model,
                             @RequestParam(value = "newImage", required = false) MultipartFile newImage,
                             @RequestParam int id
    ) {

        Employee original = genericDao.get(Employee.class, id);


        if (newImage != null) {
            String img = saveImage(newImage);
            System.out.println("isDeleted:" + deleteItemImage(original.getImage()));
            updatedEmp.setImage(img);
        }
        EntityUtility.mergeEmployees(original, updatedEmp);
        System.out.println("originalImgAfterMerge:" + original.getImage());
        System.out.println("updatedImgAfterMerge:" + updatedEmp.getImage());
        System.out.println("updated ImgName:" + original.getImage());
        genericDao.saveOrUpdate(original);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public String deleteEmployee(@RequestParam("empId") int id,
                             HttpServletRequest request
    ) {
        Employee emp = genericDao.get(Employee.class, id);
        deleteItemImage(emp.getImage());
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


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String registrationProcessing(final @ModelAttribute("employee") Employee employee,
                                         final BindingResult bindingResult,
                                         final Model model,
                                         @RequestParam(value = "phoneNumber", required = false) int phoneNumber,
                                         HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        User user = employee.getUser();
        int isSaved = genericDao.processUser(user);
        genericDao.saveObject(user);
        if (isSaved == 0) { // if no saved then such username already exists
            model.addAttribute("warning", "Such username already in use! Try again");
            return "redirect:" + referer;
        }
        if (!employee.getFirstName().isEmpty() && !employee.getLastName().isEmpty()) {
            addPhoneNum(employee, phoneNumber);
            user.setEmployee(employee);
            employee.setUser(user);
            genericDao.saveObject(employee);
            return "redirect:/login";
        }
        genericDao.saveObject(user);
        return "redirect:/login";
    }



    /**
     * get-redirect for prg pattern
     *
     * @return default spring security login
     */
    @GetMapping("/login")
    public String login() {
        return "/login";
    }


    @GetMapping("/signup")
    public String registrationProcessing(Model model, @ModelAttribute("warning") String warning) {
//        model.addAttribute("user", new User());
        model.addAttribute("employee", new Employee());
        model.addAttribute("warning", warning);
        return "/sign-up";
    }



    @RequestMapping("/home")
    public String home(Model model) {
        return "index";
    }


    @RequestMapping(value = "/test", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody

    public String submit(@RequestParam("file") MultipartFile file, ModelMap modelMap, HttpServletRequest request) {
        //set the saved location and create a directory location
//        saveImage(file);
        return "test";
    }

    private String saveImage(MultipartFile file) {
        String fileName  = file.getOriginalFilename();
//        properties = loadProperties("");
//        String locationToLoad = properties.getProperty("upload.location");
//        String location = "/home/student/IdeaProjects/museum/src/main/webapp/resources/images/";

        //create the actual file
        File pathFile = new File(UPLOAD_LOCATION + fileName);
        System.out.println("saveImage.pathFile.exists():"+pathFile.exists());
        //checks whether file with such name already exist
        if (pathFile.exists() && fileName != null) {
            int counter = 0;
            while (true) {
                fileName = fileName.replaceAll("(\\d+)?\\.", counter + ".");
                pathFile = new File(UPLOAD_LOCATION + fileName);
                System.out.println("pathFile.exists():/" + fileName + pathFile.exists());
                if(!pathFile.exists()) {
                    break;
                }
                System.out.println("saveImage:pathFile:" + pathFile);
                counter++;
            }
        }
        try {
            file.transferTo(pathFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }


    //Testing time
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(Model model) throws IOException, URISyntaxException {
        //Item item = new Item("name2", "desc2", new Date(),0,1);
        //LostItem lostItem = new LostItem(item.getId(), "desc", new Date(), item);
        //item.setLostItem(lostItem);

//        Item item = genericDao.get(Item.class, 3);
//        item.setName("updatedName");
//        genericDao.saveOrUpdate(item);
        //Location location = new Location("room 5A", "left top shelf B7", new Date());
        //item.addLocationToItem(location);
//        Employee employee = new Employee("manager", "Myke", "Turchanov", 333.33, "address", "city", "WI", "1233", true);
//        User user = new User();
//        user.setUsername("123");
//        user.setPassword("1");
//        user.setEnabled(1);
//
//        genericDao.processUser(user);
//        user.setEmployee(employee);
//        genericDao.saveObject(user);

//        genericDao.merge(user);
//        employee.setUser(user);
//        System.out.println(employee);
//
//        genericDao.saveOrUpdate(user);
//        user.setEmployee(employee);
//        employee.setUser(user);
//
//        genericDao.saveOrUpdate(employee);

        //Employee employee = genericDao.get(Employee.class, 2);
        //PhoneNumber phoneNumber = new PhoneNumber(12321312);
        //phoneNumber.setEmployee(employee);
        //employee.addPhoneNumberToEmployee(phoneNumber);
        //EmployeeItem employeeItem = new EmployeeItem(employee,item, 99.12);

        //genericDao.saveOrUpdate(employeeItem);
        //System.out.println(employee);
        //model.addAttribute("employee", employee);
        String[] cols = new String[]{"Name", "Storage Type", "Days"};
        List<List<String>> rowss = genericDao.generatedReportBasedOnSQLQuery(cols, "");
        model.addAttribute("rows", rowss);
        model.addAttribute("cols", cols);

        return "test";
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public String reports() {

        return "reports";
    }
    @RequestMapping(value = "/generateReports", method = RequestMethod.GET)
    public String generateReports(@RequestParam(name = "reports", required = true) List<String> reports, Model model) {
        for (String rep : reports) {
            System.out.println(rep);
        }
        System.out.println(EntityUtility.getSQLReportQuery("sad"));

//        if (reports.size() > 0) {
//            if (reports.contains("avgValues")) {
//                String[] columns = new String[]{};
//
////                genericDao.generatedReportBasedOnSQLQuery();
//            }
//        }
        return "reports";
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        return username;
    }
}
