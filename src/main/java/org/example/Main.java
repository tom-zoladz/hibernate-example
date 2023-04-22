package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello hibernate!");

        Configuration configuration = new Configuration();

        ServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder().build();
        SessionFactory sessionFactory =  configuration.buildSessionFactory(serviceRegistry);

        Session session = sessionFactory.openSession();
    }
}