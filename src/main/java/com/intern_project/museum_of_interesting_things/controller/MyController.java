package com.intern_project.museum_of_interesting_things.controller;

import com.intern_project.museum_of_interesting_things.entity.*;
import com.intern_project.museum_of_interesting_things.repository.GenericDao;
import com.intern_project.museum_of_interesting_things.service.GeneralService;
import com.intern_project.museum_of_interesting_things.utils.EntityUtility;
import com.intern_project.museum_of_interesting_things.utils.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Controller where
 * all application's endpoints are provided
 *
 * @author mturchanov
 */
@Controller
public class MyController implements PropertiesLoader {

    private final GenericDao genericDao;
    private final GeneralService generalService;

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final Properties properties;
    private final String UPLOAD_LOCATION;

    @Autowired
    public MyController(GenericDao genericDao, GeneralService generalService) {
        this.genericDao = genericDao;
        this.generalService = generalService;
        properties = loadProperties("/paths.properties");
        UPLOAD_LOCATION = properties.getProperty("upload.location");
    }


    @RequestMapping("/home")
    public String home(Model model) {
        return "index";
    }


    //Testing time
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(Model model) throws IOException, URISyntaxException {
        String test = "test";
        model.addAttribute(test, 12333);

        return "test";
    }


    /***********************************
     * REGISTRATION/LOGIN BLOCK
     */

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String registrationProcessing(final @ModelAttribute("employee") Employee employee,
                                         final BindingResult bindingResult,
                                         final Model model,
                                         @RequestParam(value = "phoneNumber", required = false) int phoneNumber,
                                         HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        HttpSession session = request.getSession();
        User user = employee.getUser();
        User existedUserWithTheSameUsername = genericDao.getFirstEntryBasedOnAnotherTableColumnProperty(
                "username", user.getUsername(), User.class);

        int isSaved = EntityUtility.processUser(user, existedUserWithTheSameUsername);
        genericDao.saveObject(user);
        if (isSaved == 0) { // if no saved then such username already exists
            session.setAttribute("warning", "Such username already in use! Try again");
            return "redirect:" + referer;
        }
        session.setAttribute("warning", "Such username already in use! Try again");

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
        model.addAttribute("employee", new Employee());
        model.addAttribute("warning", warning);
        return "/sign-up";
    }


    /************************************
     * ITEMS BLOCK
     */
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(Model model) {
        List<Item> items = generalService.getAllItems();
        model.addAttribute("items", items);
        model.addAttribute("uploadLocation", UPLOAD_LOCATION);

        return "items";
    }


    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public String item(Model model, @RequestParam int id) throws IOException {
        List<Employee> allEmps = genericDao.getAll(Employee.class); //TODO: check are there any duplicates in view?
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
        Item original = generalService.updateItem(updatedItem, newImage, id);
        HttpSession session = request.getSession();
        session.setAttribute("successWarning", "Item's data was successfully updated");
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
        HttpSession session = request.getSession();
        String referer = request.getHeader("Referer");

        Item item = generalService.addNewLocationToItem(storageType, locDescription, dateWhenPut, id);
        session.setAttribute("successWarning", String.format("New location was added to the %s item", item.getName()));

        model.addAttribute("item", item);
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/addNewAppraisal", method = RequestMethod.POST)
    public String addNewAppraisal(@ModelAttribute("employeeItem") EmployeeItem employeeItem,
                                  HttpServletRequest request,
                                  Model model
    ) {
        HttpSession session = request.getSession();

        System.out.println("addNewAppraisal" + employeeItem);

        generalService.addNewAppraisalToItem(employeeItem);
        session.setAttribute("successWarning", String.format("New %s %s's appraise value was added to the %s",
                employeeItem.getEmployee().getFirstName(), employeeItem.getEmployee().getLastName(), employeeItem.getItem().getName()));

        String referer = request.getHeader("Referer");
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
        String referer = request.getHeader("Referer");

        int isSaved = generalService.saveNewItem(itemName, itemDescription, dateAcquired, isMuseumItem, itemLostDesc, dateLost, storageType, locDescription, dateWhenPut, itemImage);
        HttpSession session = request.getSession();
        if (isSaved == 0) {
            session.setAttribute("warning", "Item wasn't added");
        } else {
            session.setAttribute("successWarning", "Item was successfully added");
        }

        return "redirect:" + referer;
    }


    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public String addItemToDB(Model model) {
        return "newItem";
    }


    @RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
    public String deleteItem(@RequestParam("itemId") int id, HttpServletRequest request) {
        generalService.deleteItem(id);
        String referer = request.getHeader("Referer");
        HttpSession session = request.getSession();
        session.setAttribute("successWarning", "Item was successfully deleted");
        return "redirect:" + referer;
    }


    /************************************
     * EMPLOYEES BLOCK
     */

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
        int isSaved = generalService.saveNewEmployee(newEmployee, phoneNumber, employeeImage);
        HttpSession session = request.getSession();

        if (isSaved == 0) {
            session.setAttribute("warning", "Employee wasn't saved");
        } else {
            session.setAttribute("successWarning", "Employee was successfully saved");
        }

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
        List<Employee> employees = generalService.getAllEmployees();
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
//        return "redirect:" + referer;

        return "employee";
    }

    @RequestMapping(value = "/updatedEmp", method = RequestMethod.POST)
    public String updatedEmp(@ModelAttribute("updatedEmp") Employee updatedEmp,
                             HttpServletRequest request,
                             Model model,
                             @RequestParam(value = "newImage", required = false) MultipartFile newImage,
                             @RequestParam int id
    ) {
        HttpSession session = request.getSession();

        generalService.updateEmployee(updatedEmp, newImage, id);
        session.setAttribute("successWarning", "Employee's data was successfully updated");

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public String deleteEmployee(@RequestParam("empId") int id,
                                 HttpServletRequest request
    ) {

        HttpSession session = request.getSession();
        Employee emp = generalService.deleteEmployee(id);

        session.setAttribute("successWarning", String.format("Employee %s %s was successfully deleted", emp.getFirstName(), emp.getLastName()));
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    @RequestMapping(value = "/addPhone", method = RequestMethod.POST)
    public String addPhone(Model model, @ModelAttribute("newPhone") PhoneNumber phoneNumber, HttpServletRequest request) {
        System.out.println(phoneNumber);
        System.out.println(phoneNumber.getEmployee());
        String referer = request.getHeader("Referer");
        HttpSession session = request.getSession();
        session.setAttribute("successWarning", "new phone number was successfully added to the employee");
        Employee emp = genericDao.get(Employee.class, phoneNumber.getEmployee().getId());
        emp.addPhoneNumberToEmployee(phoneNumber);
        phoneNumber.setEmployee(emp);
        genericDao.saveOrUpdate(emp);
        return "redirect:" + referer;

    }


    /************************************
     * REPORTS BLOCK
     */

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public String reports() {
        return "reports";
    }


    @RequestMapping(value = "/generateReports", method = RequestMethod.GET)
    public String generateReports(@RequestParam(name = "reports", required = false) List<String> reports, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String referer = request.getHeader("Referer");

        if (reports != null && reports.size() > 0) {
            for (String reportCheckbox : reports) {
                Map<List<String>, String> columnsAndReportQuery = EntityUtility.getSQLReportQuery(reportCheckbox);
                Map.Entry<List<String>, String> entry = columnsAndReportQuery.entrySet().iterator().next();
                List<String> columns = entry.getKey();
                String reportQuery = entry.getValue();
                List<List<String>> rowsResult = genericDao.generatedReportBasedOnSQLQuery(reportQuery);
                System.out.println("reportCheckbox:" + reportCheckbox);
                model.addAttribute(reportCheckbox + "Rows", rowsResult);
                model.addAttribute(reportCheckbox + "Columns", columns);
            }
        } else {
            session.setAttribute("warning", "At least 1 report must be chosen!");
        }
        return "reports";
//        return "redirect:" + referer;
    }


    @RequestMapping(value = "/updateDamagedItems", method = RequestMethod.POST)
    public String updateDamagedItems(@RequestParam(name = "inputDateWhenDamaged") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date inputDateWhenDamaged,
                                     @RequestParam(name = "inputRoomName") String inputRoomName,
                                     Model model,
                                     HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        String referer = request.getHeader("Referer");

        int isUpdated = genericDao.updateDateDamagedForItems(inputRoomName, inputDateWhenDamaged);
        if (isUpdated == 0) {
            session.setAttribute("warning", "Entry(ies) was not updated");
        } else {
            session.setAttribute("successWarning", "Entry(ies) was updated");
        }
        return "redirect:" + referer;
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
