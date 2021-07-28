package com.intern_project.museum_of_interesting_things.repository;

import com.intern_project.museum_of_interesting_things.entity.Authority;
import com.intern_project.museum_of_interesting_things.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

/**
 * The type Generic dao.
 */
@Repository
@Transactional
public class GenericDao {

//    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    public GenericDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final Logger logger = LogManager.getLogger(this.getClass());


    /**
     * Save int.
     *
     * @param <T> the type parameter
     * @param o   the o
     * @return the int
     */
    public <T> int save(final T o) {

        return (Integer) sessionFactory.getCurrentSession().save(o);
    }

    /**
     * Save object.
     *
     * @param <T> the type parameter
     * @param o   the o
     */
    public <T> void saveObject(final T o) {
        sessionFactory.getCurrentSession().save(o);
    }

    /**
     * Delete.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @param id   the id
     */
    public <T> void  delete(final Class<T> type, Integer id){
        T object = get(type, id);
        sessionFactory.getCurrentSession().delete(object);
    }

    /**
     * Delete object.
     *
     * @param <T> the type parameter
     * @param o   the o
     */
    public <T> void  deleteObject(final T o){
        sessionFactory.getCurrentSession().delete(o);
    }

    /**
     * Get t.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @param id   the id
     * @return the t
     */
    public <T> T get(final Class<T> type, final Integer id) {
        return (T) sessionFactory.getCurrentSession().get(type, id);
    }

    /**
     * Save or update.
     *
     * @param <T> the type parameter
     * @param o   the o
     */
    public <T> void saveOrUpdate(final T o) {
        sessionFactory.getCurrentSession().saveOrUpdate(o);
    }

    /**
     * Gets all.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the all
     */
    public <T> List<T> getAll(final Class<T> type) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(type);
        return crit.list();
    }




    /**
     * Gets first entry based on another table column property.
     *
     * @param <T>          the type parameter
     * @param propertyName the property name
     * @param searchVal    the search val
     * @param type         the type
     * @return the first entry based on another table column property
     */
    public <T> T getFirstEntryBasedOnAnotherTableColumnProperty(String propertyName, String searchVal, Class<T> type) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> root = query.from(type);
        query.select(root).where(builder.equal(root.get(propertyName), searchVal));
        T tableEntity = session.createQuery(query)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
        //session.close();
        return tableEntity;
    }


    /**
     * Merge t.
     *
     * @param <T> the type parameter
     * @param o   the o
     * @return the t
     */
    public <T> T merge(final T o)   {
        return (T) sessionFactory.getCurrentSession().merge(o);
    }




    //service
    public int processUser(User user) {
        User existedUserWithTheSameUsername = getFirstEntryBasedOnAnotherTableColumnProperty(
                "username", user.getUsername(), User.class);
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
//        saveObject(user);
//        saveOrUpdate(user);
        return 1;
    }

    public String generatedReportBasedOnSQLQuery(String[] cols, String sql) {
        final Session session = sessionFactory.getCurrentSession();
        String s = "SELECT storage_type,Avg(worth_value) average_Value FROM employee_item " +
                "INNER JOIN items ON employee_item.item_id = items.id " +
                "INNER JOIN item_location ON items.id = item_location.item_id " +
                "LEFT JOIN locations ON item_location.location_id = locations.id " +
                "WHERE  storage_type LIKE '%Room%' " +
                "GROUP  BY storage_type " +
                "ORDER  BY storage_type;";


        List<Object[]> rows = session.createNativeQuery
                (s).list();
        StringBuilder sb = new StringBuilder();
        for (String col : cols) {
            sb.append(col).append("\t\t");
        }
        sb.append(System.lineSeparator());

        for (Object[] row: rows) {
            for (Object col : row) {
                sb.append(col).append("\t\t\t");
            }
            sb.append(System.lineSeparator());
        }
        System.out.println(sb);
        return sb.toString();
    }



}