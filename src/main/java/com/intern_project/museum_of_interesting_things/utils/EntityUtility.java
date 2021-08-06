package com.intern_project.museum_of_interesting_things.utils;

import com.intern_project.museum_of_interesting_things.entity.Authority;
import com.intern_project.museum_of_interesting_things.entity.Item;
import com.intern_project.museum_of_interesting_things.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@Component
public class EntityUtility implements PropertiesLoader {
    final Properties properties;
    static String UPLOAD_LOCATION;

    @Autowired
    public EntityUtility() {
        properties = loadProperties("/paths.properties");
        UPLOAD_LOCATION = properties.getProperty("upload.location");
    }

    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        return username;
    }

    public static String saveImage(MultipartFile file) {


        String fileName = file.getOriginalFilename();

        //create the actual file
        File pathFile = new File(UPLOAD_LOCATION + fileName);
        //checks whether file with such name already exist
        if (pathFile.exists() && fileName != null) {
            int counter = 0;
            while (true) {
                fileName = fileName.replaceAll("(\\d+)?\\.", counter + ".");
                pathFile = new File(UPLOAD_LOCATION + fileName);
                System.out.println("pathFile.exists():/" + fileName + pathFile.exists());
                if (!pathFile.exists()) {
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


    public static int processUser(User user, User existedUserWithTheSameUsername) {
        if (existedUserWithTheSameUsername != null) {
            return 0;
        }
        user.setEnabled(1);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Authority authority = new Authority();
        authority.setUsername(user.getUsername());
        authority.setAuthority("ROLE_USER");

        user.addAuthorityToUser(authority);
        return 1;
    }

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


    public static Map<List<String>, String> getSQLReportQuery(String report) {

        List<String> columns;
        switch (report) {
            case "avgValuesReport":
                columns = Arrays.asList("Storage Type", "Average Value");
                break;
            case "currentItemsOnDisplaysReport":
                columns = Arrays.asList("Name", "Storage Type", "Days");
                break;
            case "overallItemDaysReport":
                columns = Arrays.asList("Name", "Storage Type", "Location Description");
                break;

            default:
                throw new IllegalStateException("Unexpected value. Following report sql file doesn't exist: " + report);
        }

        Map<List<String>, String> columnsAndReportQuery = new HashMap<>();
        report = String.format("/%s.sql", report);
        System.out.println("formattedSqlFileName:" + report);
        String sqlReport = getQuery(report);
        columnsAndReportQuery.put(columns, sqlReport);
        return columnsAndReportQuery;
    }

    public static String getQuery(String report) {
        URL reportSQLQuery = EntityUtility.class.getResource("/sqlReports" + report);
        String filePathForSparqlQuery = null;
        String sqlReport = null;

        try {
            filePathForSparqlQuery = Paths.get(reportSQLQuery.toURI()).toFile().getAbsolutePath();
            sqlReport = new String(Files.readAllBytes(Paths.get(filePathForSparqlQuery)));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return sqlReport;
    }


    /**
     * rough testing
     */
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
