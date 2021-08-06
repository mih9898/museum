package com.intern_project.museum_of_interesting_things.service;


import com.intern_project.museum_of_interesting_things.entity.*;
import com.intern_project.museum_of_interesting_things.repository.GenericDao;
import com.intern_project.museum_of_interesting_things.utils.EntityUtility;
import com.intern_project.museum_of_interesting_things.utils.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeneralService implements PropertiesLoader {

    private GenericDao genericDao;
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final Properties properties;
    private final String UPLOAD_LOCATION;

    @Autowired
    public GeneralService(GenericDao genericDao) {
        this.genericDao = genericDao;
        properties = loadProperties("/paths.properties");
        UPLOAD_LOCATION = properties.getProperty("upload.location");
    }


    /************************************
     * ITEMS SERVICE BLOCK
     */
    public void mergeItems(Item orig, Item updated) {

        List<Location> copy = new ArrayList<>(orig.getLocations());
        LostItem lostCopy = orig.getLostItem();
        System.out.println(lostCopy == null);
        List<EmployeeItem> employeeItemsCopy = new ArrayList<>(orig.getEmployeeItems());
        EntityUtility.mergeObjectsSimple(orig, updated);
        // bruteforce bug fix(dates are lost when item is updated) + employeeItem set is lost as well
        for (int i = 0; i < orig.getLocations().size(); i++) {
            Location uLoc = copy.get(i);
            Location oLoc = orig.getLocations().get(i);
            if (oLoc.getDateWhenPut() == null) {
                oLoc.setDateWhenPut(uLoc.getDateWhenPut());
            }
        }
        if (lostCopy != null && lostCopy.getDateLost() != null
                && updated.getLostItem().getDateLost() == null) {
            orig.getLostItem().setDateLost(lostCopy.getDateLost());
        }

        if (!employeeItemsCopy.isEmpty() && orig.getEmployeeItems().isEmpty()) {
            orig.setEmployeeItems(employeeItemsCopy);
        }
    }


    public int saveNewItem(String itemName, String itemDescription, Date dateAcquired, boolean isMuseumItem, String itemLostDesc, Date dateLost, String storageType, String locDescription, Date dateWhenPut, MultipartFile itemImage) {
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
            String imageName = EntityUtility.saveImage(itemImage);
            newItem.setImage(imageName);
        }

        int isSaved = genericDao.save(newItem);
        return isSaved;
    }


    public Item updateItem(Item updatedItem, MultipartFile newImage, int id) {
        Item original = genericDao.get(Item.class, id);
        updateItemImage(updatedItem, newImage, original);
        mergeItems(original, updatedItem);
        genericDao.saveOrUpdate(original);
        return original;
    }

    public Item addNewLocationToItem(String storageType, String locDescription, Date dateWhenPut, int id) {
        Item item = genericDao.get(Item.class, id);
        if (!storageType.isEmpty()) {
            Location location = new Location(storageType, locDescription, dateWhenPut);
            item.addLocationToItem(location);
        }
        genericDao.saveOrUpdate(item);
        return item;
    }

    public void addNewAppraisalToItem(EmployeeItem employeeItem) {
        Employee emp = genericDao.get(Employee.class, employeeItem.getEmployee().getId());
        Item item = genericDao.get(Item.class, employeeItem.getItem().getId());
        employeeItem.setEmployee(emp);
        employeeItem.setItem(item);
        item.addEmployeeAprToItem(employeeItem);
        genericDao.saveOrUpdate(item);
    }

    public List<Item> getAllItems() {
        List<Item> items = genericDao.getAll(Item.class);
        items = items.stream()
                .distinct()
                .collect(Collectors.toList());
        return items;
    }

    private void updateItemImage(Item updatedItem, MultipartFile newImage, Item original) {
        if (newImage != null && !newImage.isEmpty()) {
            String img = EntityUtility.saveImage(newImage);
            deleteItemImage(original.getImage());
            updatedItem.setImage(img);
        } else {
            updatedItem.setImage(original.getImage());
        }
    }

    public void deleteItem(int id) {
        Item item = genericDao.get(Item.class, id);
        deleteItemImage(item.getImage());
        genericDao.deleteObject(item);
    }

    private boolean deleteItemImage(String image) {
        if (!image.equals("noItemImage.png")) {
            File fileToDelete = new File(UPLOAD_LOCATION + image);
            System.out.println("deletePath:" + UPLOAD_LOCATION + image);
            return fileToDelete.delete();
        }
        return true;
    }


    /************************************
     * EMPLOYEES BLOCK
     */

    public int saveNewEmployee(Employee newEmployee, int phoneNumber, MultipartFile employeeImage) {
        User user = genericDao.getFirstEntryBasedOnAnotherTableColumnProperty(
                "username", EntityUtility.getCurrentUsername(), User.class);
        //TODO: decide what to do with input number optionality(if not entered then err bcause num cannot be null)
        //      or leave like this (0 instead null)
        if (!employeeImage.isEmpty()) {
            String imageName = EntityUtility.saveImage(employeeImage);
            newEmployee.setImage(imageName);
        }
        addPhoneNum(newEmployee, phoneNumber);
        newEmployee.setUser(user);
        user.setEmployee(newEmployee);
        int isSaved = genericDao.save(newEmployee);
        return isSaved;
    }

    public Employee deleteEmployee(int id) {
        Employee emp = genericDao.get(Employee.class, id);
        deleteItemImage(emp.getImage());
        genericDao.deleteObject(emp);
        return emp;
    }

    private void addPhoneNum(Employee newEmployee, int phoneNumber) {
        if (phoneNumber != 0) {
            PhoneNumber number = new PhoneNumber(phoneNumber);
            number.setEmployee(newEmployee);
            newEmployee.addPhoneNumberToEmployee(number);
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = genericDao.getAll(Employee.class);
        employees = employees.stream()
                .distinct()
                .collect(Collectors.toList());
        return employees;
    }

    private void updateEmployeeImage(Employee updatedEmp, MultipartFile newImage, Employee original) {
        if (newImage != null && !newImage.isEmpty()) {
            String img = EntityUtility.saveImage(newImage);
            System.out.println("isDeleted:" + deleteItemImage(original.getImage()));
            updatedEmp.setImage(img);
        } else {
            updatedEmp.setImage(original.getImage());
        }
    }

    public void updateEmployee(Employee updatedEmp, MultipartFile newImage, int id) {
        Employee original = genericDao.get(Employee.class, id);

        updateEmployeeImage(updatedEmp, newImage, original);
        mergeEmployees(original, updatedEmp);
        System.out.println("originalImgAfterMerge:" + original.getImage());
        System.out.println("updatedImgAfterMerge:" + updatedEmp.getImage());
        System.out.println("updated ImgName:" + original.getImage());
        genericDao.saveOrUpdate(original);
    }


    public static void mergeEmployees(Employee orig, Employee updated) {
        List<PhoneNumber> phoneNumbersCopy = new ArrayList<>(orig.getPhoneNumbers());
        Set<EmployeeItem> employeeItemsCopy = orig.getEmployeeItems();

        EntityUtility.mergeObjectsSimple(orig, updated);
        if (!phoneNumbersCopy.isEmpty() && orig.getPhoneNumbers().isEmpty()) {
            orig.setPhoneNumbers(phoneNumbersCopy);
        }
        for (PhoneNumber p : orig.getPhoneNumbers()) {
            p.setEmployee(orig);
        }
        if (!employeeItemsCopy.isEmpty() && orig.getEmployeeItems().isEmpty()) {
            orig.setEmployeeItems(employeeItemsCopy);
        }
    }
}
