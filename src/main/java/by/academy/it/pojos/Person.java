package by.academy.it.pojos;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User: yslabko
 * Date: 14.04.14
 * Time: 12:24
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "manytomanyperson")
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name="increment" , strategy="increment")
    @GeneratedValue(generator="increment")
    private Integer id;
    @Column(name = "age")
    private Integer age;
    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private PersonDetail personDetail;

    @ManyToOne
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "f_department_id")
    private Department department;

    @ManyToMany//(cascade = {CascadeType.ALL})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "manytomany_PERSON_MEETING",
    joinColumns = {@JoinColumn(name = "F_PERSON_ID")},
    inverseJoinColumns = {@JoinColumn(name = "F_MEETING_ID")})
    private Set<Meeting> meetings = new HashSet<Meeting>();

    public Person() {
    }

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }

    public Person(Integer age, String name, String surname) {
        this.age = age;
        this.name = name;
        this.surname = surname;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public PersonDetail getPersonDetail() {
        return personDetail;
    }

    public void setPersonDetail(PersonDetail personDetail) {
        this.personDetail = personDetail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (age != null ? !age.equals(person.age) : person.age != null) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (surname != null ? !surname.equals(person.surname) : person.surname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person : id: " + id + " Name: " + name + " Surname: " + surname + " Age: " +age;/*
                + " City: " + personDetail.getCity() + " Street: " +  personDetail.getStreet() +
                " State: " +  personDetail.getState() + " Country: " + personDetail.getCountry()
                + " Department: id: " + getDepartment().getDepartmentId() + ", DepartmentName: "+  getDepartment().getDepartmentName();*/
    }
}




