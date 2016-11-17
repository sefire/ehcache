package by.academy.it.loader;

import by.academy.it.pojos.Person;
import by.academy.it.util.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.Locale;

import static by.academy.it.loader.MenuLoader.getPersonDao;

import static by.academy.it.loader.MenuLoader.createPerson;
import static by.academy.it.loader.MenuLoader.menu;

public class PersonLoader {
    private static Logger log = Logger.getLogger(PersonLoader.class);
    public static HibernateUtil util = null;

    public static void main(String[] args) throws Exception {

        Locale.setDefault(Locale.US);
        util = HibernateUtil.getHibernateUtil();
        System.out.println("Start Menu");

        menu();
    }
}


