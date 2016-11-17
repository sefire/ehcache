package by.academy.it.pojos;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gsv on 09.11.2016.
 */

@Entity
@Table(name = "manytomanymeeting")
public class Meeting implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "F_MEETING_ID")
    @GeneratedValue
    private Long meetingId;

    @Column
    private String subject;

    @ManyToMany(mappedBy = "meetings")
    private Set<Person> persons = new HashSet<Person>();

    public Meeting() {
    }


    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        String result = "Meeting: id: " + meetingId + ", subject: " + subject;
        return result;
    }
}
