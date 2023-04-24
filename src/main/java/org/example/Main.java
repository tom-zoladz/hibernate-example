package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;

public class Main {

    // We need to declare static field type Logger form Simple Logging Facade for Java
    // in order to logg content. We are using static method from logger factory.
    // Main.class parameter is required to provide log information about origin class
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Hello hibernate!");

        // This is needed to configure Hibernate based on hibernate.properties
        // Hibernate will use jdbc as a DB connector
        Configuration configuration = new Configuration();
        // We need to add all annotated classes otherwise Hibernate will not be able to create tables or perform CRUD operations
        configuration.addAnnotatedClass(Department.class);
        configuration.addAnnotatedClass(Worker.class);

        ServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder().build();
        // Base on configuration we can create a SessionFactory
        SessionFactory sessionFactory =  configuration.buildSessionFactory(serviceRegistry);
        /* SessionFactory creates session with allow us to perform operations on Entities such as
        - persist
        - get
        - remove
        - merge
        Results of these operations will not be automatically saved to DB.
        To save batch of operations to DB we need to perform commit on transaction
         */
        Session session = sessionFactory.openSession();

        // Transaction allows us to group several operations
        Transaction transaction = session.beginTransaction();

        /*
        ********* DEPARTMENT TABLE *********
         */
        // Gdy używamy @GeneratedValue, Id nie musi być w konstruktorze, bo Hibernate ustawia za nas
        // Jeśli jednak Id jest w konstruktorze, to przy wywoływaniu konstruktora należy wpisać tam 0
        Department hr = new Department("HR");
        logger.info("HR before persist {}", hr);
        // persist method used to store Entity, persist operation will also modify hr object (sets id)
        session.persist(hr);
        logger.info("HR after persist {}", hr);

        Department hrFromSession = session.get(Department.class, hr.getDepartmentId());
        logger.info("Hr from session {}", hrFromSession);

        session.persist(new Department("Staffing"));


        Department department = session.get(Department.class, 2);
        logger.info("Department in session {}", department);

        /*
         ********* WORKER TABLE *********
         */

        Worker bob = new Worker();
        bob.setFirstName("Bob");
        bob.setLastName("Doe");
        bob.setHireDate(LocalDate.of(2018, Month.APRIL, 4));

        Department it = new Department("IT");
        // We do not have to save it (persist) separately due to usage of CascadeType.ALL (all operations) or CascadeType.PERSIST option on OneToOne annotation in Worker class
        //session.persist(it);
        bob.setDepartment(it);

        //Persisting worker to session including the department
        session.persist(bob);

        /*
         ********* WORKER DAO *********
         */

        WorkerDao workerDao = new WorkerDao(session);

        Worker ted = new Worker();

        ted.setFirstName("Ted");
        ted.setLastName("Doe");
        ted.setHireDate(LocalDate.of(2018, Month.APRIL, 1));
        ted.setDepartment(it);

        workerDao.create(ted);
        
        // This will initiate real (non resource-local) DB operations
        transaction.commit();
        // Closing session as a cleanup
        session.close();


        // For the demo purposes second session and transactions are created
        Session secondSession = sessionFactory.openSession();
        Transaction secondTransaction = secondSession.beginTransaction();

        Worker bobFromDb = secondSession.get(Worker.class, 1);
        logger.info("Worker bob from database: {}", bobFromDb);

        secondTransaction.commit();
        secondSession.close();
    }
}