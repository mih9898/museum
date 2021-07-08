package com.intern_project.museum_of_interesting_things.utils;

import com.intern_project.museum_of_interesting_things.entity.*;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EntityUtility {

    //working fine if fields are primitive. if collections or objects then can be lost (e.g dates)
    public static void mergeObjectsSimple(Object obj, Object update) {
        if (!obj.getClass().isAssignableFrom(update.getClass())) {
            return;
        }
        Method[] methods = obj.getClass().getMethods();
        for (Method fromMethod : methods) {
            if (fromMethod.getDeclaringClass().equals(obj.getClass())
                    && fromMethod.getName().startsWith("get")) {
                String fromName = fromMethod.getName();
                String toName = fromName.replace("get", "set");
                try {
                    Method toMetod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
                    Object value = fromMethod.invoke(update, (Object[]) null);
                    if (value != null) {
                        toMetod.invoke(obj, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //TODO: displace employeeItems, lostItem by adding form:hidden on general form
    //TODO: put all logic from here to servicer package + add needed dao stuff
    public static void merge(Item orig, Item updated) {
        List<Location> copy = new ArrayList<>(orig.getLocations());
        LostItem lostCopy = orig.getLostItem();
        Set<EmployeeItem> employeeItemsCopy = new HashSet<>(orig.getEmployeeItems());

        mergeObjectsSimple(orig, updated);

        // bruteforce bug fix(dates are lost when item is updated) + employeeItem set is lost as well
        for (int i = 0; i < orig.getLocations().size(); i++) {
            Location uLoc = copy.get(i);
            Location oLoc = orig.getLocations().get(i);
            if (oLoc.getDateWhenPut() == null) {
                oLoc.setDateWhenPut(uLoc.getDateWhenPut());
            }
        }
        if (lostCopy.getDateLost() != null && updated.getLostItem().getDateLost() == null) {
            orig.getLostItem().setDateLost(lostCopy.getDateLost());
        }

        if (!employeeItemsCopy.isEmpty() && orig.getEmployeeItems().isEmpty()) {
            orig.setEmployeeItems(employeeItemsCopy);
        }

    }


    public static void mergeEmployees(Employee orig, Employee updated) {
        Set<PhoneNumber> phoneNumbersCopy = orig.getPhoneNumbers();
        Set<EmployeeItem>employeeItemsCopy = orig.getEmployeeItems();
        mergeObjectsSimple(orig, updated);

        if (!phoneNumbersCopy.isEmpty() && orig.getPhoneNumbers().isEmpty()) {
            orig.setPhoneNumbers(phoneNumbersCopy);
        }

        if (!employeeItemsCopy.isEmpty() && orig.getEmployeeItems().isEmpty()) {
            orig.setEmployeeItems(employeeItemsCopy);
        }
    }


    public static void main(String[] args) {
        Item original = new Item();
        Item updated = new Item();
        original.setName("original Name");
        original.setDescription("original desc");
        original.setId(123);


        updated.setName("updated name");
        //updated.setDescription("updated desc");
        updated.setDateAcquired(new Date());

        System.out.println("Before merge:");
        System.out.printf("original id: %s / updated id: %s%n", original.getId(), updated.getId());
        System.out.printf("original name: %s / updated name: %s%n", original.getName(), updated.getName());
        System.out.printf("original desc: %s / updated desc: %s%n", original.getDescription(), updated.getDescription());
        System.out.printf("original date: %s / updated date: %s%n", original.getDateAcquired(), updated.getDateAcquired());
        System.out.println();
        System.out.println("After merge:");


        EntityUtility.mergeObjectsSimple(original, updated);
        System.out.printf("original id: %s / updated id: %s%n", original.getId(), updated.getId());
        System.out.printf("original name: %s / updated name: %s%n", original.getName(), updated.getName());
        System.out.printf("original desc: %s / updated desc: %s%n", original.getDescription(), updated.getDescription());
        System.out.printf("original date: %s / updated date: %s%n", original.getDateAcquired(), updated.getDateAcquired());

        //original took values from updated if they are not null
    }
}
