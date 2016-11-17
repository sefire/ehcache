package by.academy.it.loader;

import by.academy.it.db.DepartmentDao;
import by.academy.it.db.MeetingDao;
import by.academy.it.db.PersonDao;
import by.academy.it.db.PersonDetailDao;
import by.academy.it.db.dto.PersonDTO;
import by.academy.it.db.exceptions.DaoException;
import by.academy.it.pojos.Department;
import by.academy.it.pojos.Meeting;
import by.academy.it.pojos.Person;
import by.academy.it.pojos.PersonDetail;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * User: yslabko
 * Date: 14.04.14
 * Time: 12:28
 * Modified by Glotov S. 2016
 */
public class MenuLoader {
    private static Logger log = Logger.getLogger(MenuLoader.class);
    public static Boolean needMenu = true;
    private static PersonDao personDao = null;
    private static PersonDetailDao personDetailDao = null;
    private static DepartmentDao departmentDao = null;
    private static MeetingDao meetingDao = null;


    public static void menu() throws DaoException {
        Person person = null;
        while (needMenu) {
            printMenu();
            System.out.println("Choose an option: ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    createPerson(person);
                    break;
                case 2:
                    person = getFindPerson();
                    break;
                case 3:
                    deletePerson();
                    break;
                case 4:
                    createPersonDetail();
                    break;
                case 5:
                    getFindPersonDetail();
                    break;
                case 6:
                    deletePersonDetail();
                    break;
                case 7:
                    createDepartment();
                    break;
                case 8:
                    getFindDepartment();
                    break;
                case 9:
                    deleteDepartment();
                    break;
                case 10:
                    createMeeting();
                    break;
                case 11:
                    getFindMeeting();
                    break;
                case 12:
                    deleteMeeting();
                    break;
                case 13:
                    getAllPersonsOrderByAge();
                    break;
                case 14:
                    getAllPersonsWithNotNullNamesAndAgeInRange();
                    break;
                case 15:
                    getAnyPersonsFromAnyPosition();
                    break;
                case 16:
                    getAveragePersonAge();
                    break;
                case 17:
                    getBean();
                    break;

                case 18:
                    tryCache();
                    break;

                default:
                    needMenu = true;
                    break;
            }
            needMenu = true;
        }
    }

    private static void printMenu() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n Options:");
        System.out.println("        0. Exit");
        System.out.println("           ---Person---");
        System.out.println("        1. Save or Update Person");
        System.out.println("        2. Get(Find) Person");
        System.out.println("        3. Delete Person");
        System.out.println("           ---PersonDetail---");
        System.out.println("        4. Save or Update PersonDetail");
        System.out.println("        5. Get(Find) PersonDetail");
        System.out.println("        6. Delete PersonDetail");
        System.out.println("           ---Department---");
        System.out.println("        7. Save or Update Department");
        System.out.println("        8. Get(Find) Department");
        System.out.println("        9. Delete Department");
        System.out.println("           ---Meeting---");
        System.out.println("        10. Save or Update Meeting");
        System.out.println("        11. Get(Find) Meeting");
        System.out.println("        12. Delete Meeting");
        System.out.println("           ---Criteria---");
        System.out.println("        13. Get all persons order by age");
        System.out.println("        14. Get all persons with Names and age in range");
        System.out.println("        15. Get any persons from any position");
        System.out.println("        16. Get average age of persons");
        System.out.println("        17. Get PersonDTO");
        System.out.println("           ---Cache---");
        System.out.println("        18. Try Cache!");
    }

    public static Person createPerson(Person person) throws DaoException {
        System.out.println("Please enter person description:");
        System.out.print("Name - ");

        if (person == null) {
            person = new Person();
        }
        Scanner scanner = new Scanner(System.in);
        String parameter = scanner.nextLine();
        person.setName(parameter);
        System.out.print("Surname - ");
        parameter = scanner.nextLine();
        person.setSurname(parameter);
        System.out.print("Age - ");
        person.setAge(scanner.nextInt());

        PersonDetail personDetail = new PersonDetail();
        System.out.print("Street(personDetail) - ");
        parameter = scanner.nextLine();
        parameter = scanner.nextLine();
        personDetail.setStreet(parameter);

        System.out.print("City(personDetail) - ");
        parameter = scanner.nextLine();
        personDetail.setCity(parameter);

        System.out.print("State(personDetail) - ");
        parameter = scanner.nextLine();
        personDetail.setState(parameter);

        System.out.print("Country(personDetail) - ");
        parameter = scanner.nextLine();
        personDetail.setCountry(parameter);

        person.setPersonDetail(personDetail);


        Department department = new Department();
        System.out.print("DepartmentName(Department) - ");
        parameter = scanner.nextLine();
        department.setDepartmentName(parameter);

        getDepartmentDao().saveOrUpdate(department);

        person.setDepartment(department);

        Set<Person> persons = new HashSet<Person>();
        persons.add(person);
        department.setPersons(persons);

        Set<Meeting> meetings = new HashSet<Meeting>();
        boolean isMeetingsFilled = false;
        while (!isMeetingsFilled) {
            Meeting meeting = new Meeting();
            System.out.print("Subject(Meeting) - ");
            parameter = scanner.nextLine();
            meeting.setSubject(parameter);
            Set<Person> personsForMeeting = new HashSet<Person>();
            personsForMeeting.add(person);
            meeting.setPersons(personsForMeeting);
            meetings.add(meeting);
            getMeetingDao().saveOrUpdate(meeting);

            boolean isAnswerCorrect = false;
            while (!isAnswerCorrect) {
                System.out.print("Do you want to add another one meeting to this person(y/n)?");
                parameter = scanner.nextLine();
                if (parameter.toLowerCase().equals("y")) {
                    isAnswerCorrect = true;
                } else {
                    if (parameter.toLowerCase().equals("n")) {
                        isAnswerCorrect = true;
                        isMeetingsFilled = true;
                    } else {
                        System.out.print("You have only two options here : (y/n).");
                    }
                }
            }
        }
        person.setMeetings(meetings);

        personDetail.setPerson(person);

        getPersonDao().saveOrUpdate(person);

        //getPersonDetailDao().saveOrUpdate(personDetail);
        System.out.println(person);
        return person;
    }

    public static Person getFindPerson() {
        System.out.println("Get by Id. Please enter person id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        Person person = null;
        Integer id = scanner.nextInt();
        try {
            person = getPersonDao().get(id);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable get(find) person:", e);
        }
        System.out.println(person);
        System.out.println(person.getPersonDetail());
        System.out.println(person.getDepartment());
        System.out.println(person.getMeetings());
        return person;
    }

    public static boolean deletePerson() {
        System.out.println("Delete by Id. Please enter person id:");
        System.out.print("Id - ");
        Scanner scanner = new Scanner(System.in);
        Person person = null;
        PersonDetail personDetail = null;
        Integer id = scanner.nextInt();
        try {

            person = getPersonDao().get(id);
            Set<Person> personsBeforeDeletion = person.getDepartment().getPersons();
            Set<Person> personsAfterDeletion = new HashSet<Person>();
            for (Person person1 : personsBeforeDeletion)
                if (person1.getId() != person.getId())
                    personsAfterDeletion.add(person1);


            Department department = getDepartmentDao().get(person.getDepartment().getDepartmentId());
            department.setPersons(personsAfterDeletion);

            Set<Meeting> meetings = person.getMeetings();
            for (Meeting meeting : meetings) {
                Set<Person> personsInMeetingAfterDeletion = new HashSet<Person>();
                for (Person person1 : meeting.getPersons())
                    if (person1.getId() != person.getId())
                        personsInMeetingAfterDeletion.add(person1);
                meeting.setPersons(personsAfterDeletion);
                getMeetingDao().saveOrUpdate(meeting);
            }


            getDepartmentDao().saveOrUpdate(department);
            getPersonDao().delete(person);

        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable find person:", e);
        }
        return true;
    }

    public static PersonDao getPersonDao() {
        if (personDao == null) {
            personDao = new PersonDao();
        }

        return personDao;
    }


    public static void createPersonDetail() throws DaoException {
        createPerson(new Person());
    }

    public static void getFindPersonDetail() {

        System.out.println("Get by Id. Please enter personDetail id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        PersonDetail personDetail = null;
        Integer id = scanner.nextInt();
        try {
            personDetail = getPersonDetailDao().get(id);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable get(find) personDetail:", e);
        }
        System.out.println(personDetail);
        System.out.println(personDetail.getPerson());
    }

    public static boolean deletePersonDetail() {

        System.out.println("Delete by Id. Please enter personDetail id:");
        System.out.print("Id - ");
        Scanner scanner = new Scanner(System.in);
        Person person = null;
        PersonDetail personDetail = null;
        Integer id = scanner.nextInt();
        try {
            personDetail = getPersonDetailDao().get(id);
            getPersonDao().get(id).setPersonDetail(null);
            getPersonDetailDao().delete(personDetail);

        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable find personDetail:", e);
        }
        return true;
    }

    public static PersonDetailDao getPersonDetailDao() {
        if (personDetailDao == null) {
            personDetailDao = new PersonDetailDao();
        }

        return personDetailDao;
    }


    public static void createDepartment() throws DaoException {
        System.out.println("Please enter department description:");
        System.out.print("DepartmentName - ");

        Department department = new Department();
        Scanner scanner = new Scanner(System.in);
        String parameter = scanner.nextLine();
        department.setDepartmentName(parameter);

        getDepartmentDao().saveOrUpdate(department);

        Set<Person> persons = new HashSet<Person>();
        boolean isPersonsFilled = false;
        while (!isPersonsFilled) {
            Person person = new Person();
            System.out.println("Please enter person description:");
            System.out.print("Name - ");
            scanner = new Scanner(System.in);
            parameter = scanner.nextLine();
            person.setName(parameter);
            System.out.print("Surname - ");
            parameter = scanner.nextLine();
            person.setSurname(parameter);
            System.out.print("Age - ");
            person.setAge(scanner.nextInt());

            PersonDetail personDetail = new PersonDetail();
            System.out.print("Street(personDetail) - ");
            parameter = scanner.nextLine();
            parameter = scanner.nextLine();
            personDetail.setStreet(parameter);

            System.out.print("City(personDetail) - ");
            parameter = scanner.nextLine();
            personDetail.setCity(parameter);

            System.out.print("State(personDetail) - ");
            parameter = scanner.nextLine();
            personDetail.setState(parameter);

            System.out.print("Country(personDetail) - ");
            parameter = scanner.nextLine();
            personDetail.setCountry(parameter);

            person.setPersonDetail(personDetail);
            person.setDepartment(department);

            personDetail.setPerson(person);


            getPersonDao().saveOrUpdate(person);
            // getPersonDetailDao().saveOrUpdate(personDetail);


            persons.add(person);

            boolean isAnswerCorrect = false;
            while (!isAnswerCorrect) {
                System.out.print("Do you want to add another one person to this department(y/n)?");
                parameter = scanner.nextLine();
                if (parameter.toLowerCase().equals("y")) {
                    isAnswerCorrect = true;
                } else {
                    if (parameter.toLowerCase().equals("n")) {
                        isAnswerCorrect = true;
                        isPersonsFilled = true;
                    } else {
                        System.out.print("You have only two options here : (y/n).");
                    }
                }
            }
        }
        department.setPersons(persons);

    }

    public static Department getFindDepartment() {

        System.out.println("Get by Id. Please enter department id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        Department department = null;
        Integer id = scanner.nextInt();
        try {
            department = getDepartmentDao().get(id);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable get(find) department:", e);
        }
        System.out.println(department);

        for (Person person : department.getPersons())
            System.out.println(person);
        return department;
    }

    public static boolean deleteDepartment() throws DaoException {
        //deletePerson();
        System.out.println("Delete by Id. Please enter department id:");
        System.out.print("Id - ");
        Scanner scanner = new Scanner(System.in);
        Person person = null;
        PersonDetail personDetail = null;
        Integer id = scanner.nextInt();

        Set<Person> persons = getDepartmentDao().get(id).getPersons();

        for (Person personDep : persons) {
            //getPersonDetailDao().delete(getPersonDetailDao().get(personDep.getId()));

            //тут возможно еще придется удалять person из каждого meeting на который он идет

            getPersonDao().delete(personDep);
        }

        getDepartmentDao().delete(getDepartmentDao().get(id));
        return true;
    }

    public static DepartmentDao getDepartmentDao() {
        if (departmentDao == null) {
            departmentDao = new DepartmentDao();
        }

        return departmentDao;
    }


    public static void createMeeting() throws DaoException {
        System.out.println("Please enter meeting description:");
        System.out.print("Subject - ");

        Meeting meeting = new Meeting();
        Scanner scanner = new Scanner(System.in);
        String parameter = scanner.nextLine();
        meeting.setSubject(parameter);

        Set<Person> persons = new HashSet<Person>();
        Set<Meeting> meetings = new HashSet<Meeting>();
        boolean isPersonsFilled = false;
        while (!isPersonsFilled) {
            Person person = new Person();
            System.out.println("Please enter person description:");
            System.out.print("Name - ");

            parameter = scanner.nextLine();
            person.setName(parameter);
            System.out.print("Surname - ");
            parameter = scanner.nextLine();
            person.setSurname(parameter);
            System.out.print("Age - ");
            person.setAge(scanner.nextInt());

            PersonDetail personDetail = new PersonDetail();
            System.out.print("Street(personDetail) - ");
            parameter = scanner.nextLine();
            parameter = scanner.nextLine();
            personDetail.setStreet(parameter);

            System.out.print("City(personDetail) - ");
            parameter = scanner.nextLine();
            personDetail.setCity(parameter);

            System.out.print("State(personDetail) - ");
            parameter = scanner.nextLine();
            personDetail.setState(parameter);

            System.out.print("Country(personDetail) - ");
            parameter = scanner.nextLine();
            personDetail.setCountry(parameter);

            person.setPersonDetail(personDetail);


            Department department = new Department();
            System.out.print("DepartmentName(Department) - ");
            parameter = scanner.nextLine();
            department.setDepartmentName(parameter);
            persons.add(person);

            meetings.add(meeting);

            person.setMeetings(meetings);


            ///
            getDepartmentDao().saveOrUpdate(department);
            person.setDepartment(department);

            department.setPersons(persons);
            personDetail.setPerson(person);

            getMeetingDao().saveOrUpdate(meeting);

            getPersonDao().saveOrUpdate(person);

            getPersonDetailDao().saveOrUpdate(personDetail);

            ///

            boolean isAnswerCorrect = false;
            while (!isAnswerCorrect) {
                System.out.print("Do you want to add another one person to this meeting(y/n)?");
                parameter = scanner.nextLine();
                if (parameter.toLowerCase().equals("y")) {
                    isAnswerCorrect = true;
                } else {
                    if (parameter.toLowerCase().equals("n")) {
                        isAnswerCorrect = true;
                        isPersonsFilled = true;
                    } else {
                        System.out.print("You have only two options here : (y/n).");
                    }
                }
            }
        }
        meeting.setPersons(persons);
        getMeetingDao().saveOrUpdate(meeting);
    }

    public static Meeting getFindMeeting() {

        System.out.println("Get by Id. Please enter meeting id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        Meeting meeting = null;
        Long id = scanner.nextLong();
        try {
            meeting = getMeetingDao().get(id);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable get(find) meeting:", e);
        }
        System.out.println(meeting);

        for (Person person : meeting.getPersons())
            System.out.println(person);
        return meeting;
    }

    public static boolean deleteMeeting() throws DaoException {

        System.out.println("Delete by Id. Please enter meeting id:");
        System.out.print("Id - ");
        Scanner scanner = new Scanner(System.in);

        Long id = scanner.nextLong();
        Meeting meeting = getMeetingDao().get(id);

        Set<Person> persons = meeting.getPersons();
        for (Person person : persons) {
            Set<Meeting> meetingsInPersonAfterDeletion = new HashSet<Meeting>();
            for (Meeting meeting1 : person.getMeetings())
                if (meeting1.getMeetingId() != meeting.getMeetingId())
                    meetingsInPersonAfterDeletion.add(meeting1);
            person.setMeetings(meetingsInPersonAfterDeletion);
            getPersonDao().saveOrUpdate(person);
        }

        getMeetingDao().delete(getMeetingDao().get(id));
        return true;
    }

    public static MeetingDao getMeetingDao() {
        if (meetingDao == null) {
            meetingDao = new MeetingDao();
        }

        return meetingDao;
    }

    public static List<Person> getAllPersonsOrderByAge()    {
        List<Person> persons = null;

        try {
            persons = getPersonDao().getAllPersonsOrderByAge();
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable get persons:", e);
        }

        for (Person person : persons)
            System.out.println(person);

        return persons;
    }

    public static List<Person> getAllPersonsWithNotNullNamesAndAgeInRange()    {
        List<Person> persons = null;

        System.out.println("Please enter start age of persons: ");
        System.out.print("StartAge - ");

        Scanner scanner = new Scanner(System.in);
        Integer startAge = scanner.nextInt();

        System.out.print("EndAge - ");

        Integer endAge = scanner.nextInt();

        try {
            persons = getPersonDao().getAllPersonsWithNotNullNamesAndAgeInRange(startAge,endAge);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable get persons:", e);
        }

        for (Person person : persons)
            System.out.println(person);

        return persons;
    }

    public static void getAveragePersonAge()
    {
        Object result = 0;
        try {
            result = getPersonDao().getAveragePersonAge();
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable get average age of persons:", e);
        }
        System.out.println(result);
    }

    public static void getAnyPersonsFromAnyPosition()
    {
        System.out.println("getAnyPersonsFromAnyPosition. Please enter count of persons: ");
        System.out.print("Count - ");

        Scanner scanner = new Scanner(System.in);
        Integer count = scanner.nextInt();

        System.out.print("StartPosition - ");

        Integer startPosition = scanner.nextInt();

        List<Person> persons = null;

        try {
            persons = getPersonDao().getAnyPersonsFromAnyPosition(count, startPosition);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable delete person:", e);
        }

        for(Person person: persons)
            System.out.println(person);

    }

    public static void getBean()
    {
        List<PersonDTO> list =null;
        try {
            list = getPersonDao().getBean();
            for(PersonDTO personDTO: list)
                System.out.println(personDTO);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable getBean:", e);
        }

    }
    public static void tryCache()
    {
        try {
            getPersonDao().tryCache();
        } catch (NullPointerException e) {
            log.error("tryCache: ", e);
        }
    }


}