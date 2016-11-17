/*
 * Copyright (c) 2012 by VeriFone, Inc.
 * All Rights Reserved.
 *
 * THIS FILE CONTAINS PROPRIETARY AND CONFIDENTIAL INFORMATION
 * AND REMAINS THE UNPUBLISHED PROPERTY OF VERIFONE, INC.
 *
 * Use, disclosure, or reproduction is prohibited
 * without prior written approval from VeriFone, Inc.
 */
package by.academy.it.db;

import by.academy.it.db.dto.PersonDTO;
import by.academy.it.db.exceptions.DaoException;
import by.academy.it.loader.PersonLoader;
import by.academy.it.pojos.Person;
import by.academy.it.util.HibernateUtilSession;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.List;

import static by.academy.it.loader.PersonLoader.util;

/**
 * User: yslabko
 * Date: 14.04.14
 * Time: 13:05
 * Modified by Glotov S. 2016
 */
public class PersonDao extends BaseDao<Person> {

    private static Logger log = Logger.getLogger(PersonDao.class);
    private Transaction transaction = null;

    public void flush(Integer id, String newName) throws DaoException {
        try {
            Session session = util.getSession();
            transaction = session.beginTransaction();
            Person p = (Person) session.get(Person.class, id);
            log.info("After getting person from DB: " + p);
            p.setName(newName);
            log.info("Now person has changed with new name: " + newName + "\nwhole person information: " + p);
            session.isDirty();
            log.info("session.isDirty(): " + session.isDirty());
            session.flush();
            transaction.commit();
            log.info("after flush, you should see in DB new name of person:\n" + p);
        } catch (HibernateException e) {
            transaction.rollback();
            log.error("Error Flush person" + e);
            throw new DaoException(e);
        }
    }

    public void refresh(Integer id, String newName) throws DaoException {
        try {
            Session session = util.getSession();
            transaction = session.beginTransaction();
            Person p = (Person) session.get(Person.class, id);
            log.info("After getting person from DB: " + p);

            p.setName(newName);
            log.info("Now person has changed with new name: " + newName + "\nwhole person information: " + p);
            session.isDirty();
            log.info("session.isDirty(): " + session.isDirty());
            session.refresh(p);
            log.info("after refresh, you should see OLD name of person!\nand your persistent person: " + p);
            session.isDirty();
            log.info("session.isDirty(): " + session.isDirty());
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            log.error("Error Fresh person" + e);
            throw new DaoException(e);
        }

    }

    public List<Person> getAllPersonsOrderByAge() throws DaoException {
        List<Person> persons = null;

        try {
            Session session = util.getSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(Person.class);
            criteria.addOrder(Order.desc("age"));
            persons = criteria.list();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            log.error("Error Fresh person" + e);
            throw new DaoException(e);
        }
        return persons;
    }

    public List<Person> getAllPersonsWithNotNullNamesAndAgeInRange(int startAge, int endAge) throws DaoException {
        List<Person> persons = null;

        try {
            Session session = util.getSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(Person.class);
            Criterion nameNotNull = Restrictions.isNotNull("name");
            Criterion age = Restrictions.between("age", startAge, endAge);
            LogicalExpression andExp = Restrictions.and(nameNotNull, age);
            criteria.add(andExp);
            persons = criteria.list();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            log.error("Error in getAllPersonsWithNotNullNamesAndAgeInRange" + e);
            throw new DaoException(e);
        }
        return persons;
    }

    public List<Person> getAnyPersonsFromAnyPosition(Integer count, Integer startPosition) throws DaoException {
        List<Person> persons = null;
        try {
            Session session = PersonLoader.util.getSession();
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Person.class);
            cr.setFirstResult(startPosition);
            cr.setMaxResults(count);
            persons = cr.list();
            transaction.commit();

        } catch (HibernateException e) {
            transaction.rollback();
            log.error("Error get " + " in Dao" + e);
            throw new DaoException(e);
        }
        return persons;
    }

    public Object getAveragePersonAge() throws DaoException {
        Object avgAge = 0;
        try {
            Session session = PersonLoader.util.getSession();
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Person.class);
            cr.setProjection(Projections.avg("age"));

            avgAge = cr.uniqueResult();
            transaction.commit();

        } catch (HibernateException e) {
            transaction.rollback();
            log.error("Error get " + " in Dao" + e);
            throw new DaoException(e);
        }
        return avgAge;
    }

    public List<PersonDTO> getBean() throws DaoException {
        List<PersonDTO> list = null;
        try {
            Session session = PersonLoader.util.getSession();

            list = session.createSQLQuery("select e.F_id as id, e.name as name, e.surname as surname from" +
                    " manytomanyperson e")
                    .addScalar("id", StandardBasicTypes.INTEGER)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .addScalar("surname", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(PersonDTO.class))
                    .list();
        } catch (HibernateException e) {
            transaction.rollback();
            log.error("Error get " + " in Dao" + e);
            throw new DaoException(e);
        }
        return list;
    }

    public void tryCache2() {
/*        String hql = "SELECT E FROM Person AS E WHERE E.id = ?";*/
        Session session1 = PersonLoader.util.getSession();
        transaction = session1.beginTransaction();
        Person person = (Person) session1.get(Person.class, 1);
        session1 = null;
        //session1.close();
        System.out.println(person);

        transaction.commit();


        Session session2 = PersonLoader.util.getSession();
        transaction = session2.beginTransaction();
        Person person2 = (Person) session2.get(Person.class, 1);
        transaction.commit();
        session2.close();
        System.out.println(person2);
        System.out.println("FIN");

    }

    public void tryCache() {
        SessionFactory sessionFactory = HibernateUtilSession.getSessionFactory();

        System.out.println("---session0");
        String hql = "SELECT E FROM Person AS E WHERE E.id = ?";
        Session session0 = sessionFactory.openSession();
        transaction = session0.beginTransaction();
        Query query = session0.createQuery(hql);
        query.setParameter(0, 1);
        query.setCacheable(true);
        Person person0 = (Person) query.uniqueResult();
        session0.close();
        System.out.println(person0);
        transaction.commit();

        System.out.println("---session0.5");
        Session session05 = sessionFactory.openSession();
        transaction = session05.beginTransaction();
        Person person05 = (Person) session05.get(Person.class, 1);
        session05.close();
        System.out.println(person05);
        transaction.commit();

        System.out.println("---session1");
        Session session1 = sessionFactory.openSession();
        transaction = session1.beginTransaction();
        Person person = (Person) session1.get(Person.class, 1);
        transaction.commit();
        session1.close();
        System.out.println(person);


        System.out.println("---session2");
        Session session2 = sessionFactory.openSession();
        transaction = session2.beginTransaction();
        Person person2 = (Person) session2.get(Person.class, 1);
        transaction.commit();
        session2.close();
        System.out.println(person2);

        System.out.println("---session3");
        Session session3 = sessionFactory.openSession();
        transaction = session3.beginTransaction();
        Person person3 = (Person) session3.get(Person.class, 1);
        transaction.commit();
        session3.close();
        System.out.println(person3);

        System.out.println("---session4");
        Session session4 = sessionFactory.openSession();
        transaction = session4.beginTransaction();
        Person person4 = (Person) session4.get(Person.class, 1);
        transaction.commit();
        session4.close();
        System.out.println(person4);
    }
}
