package org.example;


import jakarta.persistence.*;


// In order to make the class work in Hibernate, we need add it to the configuration: configuration.addAnnotatedClass(Department.class);

// Base on this annotation Hibernate will use Department object as DB entity
// This will allow for operations such as persist to be performed
@Entity
@Table(name = "department") // pozwala Hibernatowi wygenerować tabelę w db na podstawie klasy
public class Department {

    // this field will be a Primary Key (@Id is responsible for it)
    @Id
    // Hibernate will handle key generation for us
    @GeneratedValue
    // explicitly setting column name, if not used column name = field name
    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "department_name", nullable = false)
    //@Transient - used in case if we do not want to map/have column firstName in Worker table, but we want to have such field in Java
    private String departmentName;

    public Department() {} // Hibernate requires the non-parameter constructor

    // When we use @GeneratedValue Id does need to be in the constructor, because it is automatically set by Hibernate
    // However if Id is in the constructor, then when we call the constructor we should put there value 0.
    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Department{");
        sb.append("departmentId=").append(departmentId);
        sb.append(", departmentName='").append(departmentName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
