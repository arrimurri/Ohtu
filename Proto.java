package Ohtu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Kalenteriohjelman käli-luokka
 */
public class Proto
{
	OhtuCalendar calendar;
	public static Scanner sc;
	private boolean quit;

	public Proto()
	{
		this.sc = new Scanner(System.in);
		this.quit = false;
		this.calendar = OhtuCalendar.getInstance();
		//calendar.add(new Course(0, "Rinnakkaisohjelmointi"));
		//calendar.add(new Course(0, "Tietorakenteet"));
		//calendar.add(new Course(0, "Ohjelmistotuotanto"));
	}

	public void loop()
	{
		while (!quit)
		{
			printMenu();
			readCommand();
		}
	}

	private void readCommand()
	{
		String cmd;
		if(!sc.hasNextLine())
			return;
		cmd = sc.nextLine();
		System.out.println("Annoit komennon "+cmd);
		if (cmd.equalsIgnoreCase("q"))
			this.quit = true;
		else if (cmd.equalsIgnoreCase("L"))
			addCourse();
		else if (cmd.equalsIgnoreCase("N"))
			printCourses();
		else if (cmd.equalsIgnoreCase("T"))
			importAndPrintCourses();
		else if (cmd.equalsIgnoreCase("R"))
			createCourseReport();
		else if (cmd.equalsIgnoreCase("V"))
			printWeek();
        else
            System.out.println("Virheellinen komento");
	}

    private void createCourseReport()
    {
        this.calendar.toCSVFile("raportti.csv");
    }

	private void addCourse()
	{
		System.out.println("Anna kurssin nimi");
		String coursename = sc.nextLine();
		Course course = new Course(0, coursename);

		if(calendar.contains(course))
		{
			System.out.println("Kurssi on jo kalenterissasi, lisätään sille viikottaisen tapahtuman");
			calendar.addEvent(course);
		}
		
		else
		{
			System.out.println("Anna alkupvm muotoa dd.mm.yyyy");
			String str; 
	
			str = sc.nextLine();
			course.setStartDate(str);
	
			System.out.println("Anna loppupvm muotoa dd.mm.yyyy");
			str = sc.nextLine();
			course.setEndDate(str);
	
			calendar.add(course);
		}
	}

	private void printCourses()
	{
		System.out.println(calendar.toString());
		pressEnter();
	}

	private void importAndPrintCourses()
    {
        this.calendar.printAllCoursesWithIndex();
        System.out.println(
                "*************************************************\n"+		
                "                                                 \n"+
                "   Rekisteräinti:                                \n"+
                "   Anna sen kurssin numero jolle haluat          \n"+
                "   osallistua. Jos haluat rekisteräityä monelle  \n"+
                "   kurssille samalla kertaa, niin annan kaikkien \n"+
                "   kurssien numerot välilyännille erotettuina.   \n"+
                "                                                 \n"+
                "*************************************************");
        
		String cmd;
		if(!sc.hasNextLine())
        {
            System.out.println("Ei komentoa, palaat takaisin päävalikkoon");
            return;
        }
		cmd = sc.nextLine();
        String[] ids = cmd.split("[ ]");
        for(int i = 0; i < ids.length; i++) 
        {
            int id = Integer.parseInt(ids[i]);
            Course c = this.calendar.allCourses.get(id - 1);
            System.out.println("Anna lisätietoja kurssille " + c.coursename);
            int cp = InputUtils.askNumber("Anna opintopisteiden määrä (pelkkä luku)", sc);
            c.setCoursepoints(cp);

            GregorianCalendar date = InputUtils.askDate("Anna tenttipäivämäärä muodossa dd.mm.yyyy", sc);
            c.setExamDate(date);
            
            this.calendar.add(c);
        }
    }
	
	public void printWeek()
	{
		System.out.println("Tulostetaan viikko");
		ArrayList<Event> week = new ArrayList<Event>();
		for(Course c : calendar.courses)
        {
			System.out.println(c.coursename);
			week.addAll(c.getEvents());
        }
		
		//Collections.sort(week, null);
		
		for(Event we : week)
		{
			System.out.println(we.getDescription());
			System.out.println(we.toString());
		}
	}

	private void pressEnter()
	{
		System.out.println("Paina entteriä jatkaaksesi");
		String dummy = sc.nextLine();
	}

	public void printMenu()
	{
		System.out.println(
		"*************************************************\n"+		
		"   Kalenteri                                     \n"+
		"                                                 \n"+
		"   L   Lisää kurssi tai tapahtuma kurssille      \n"+
		"   N   Näytä kalenteri                           \n"+
		"   V   Vaihda näkymää                            \n"+
		"   T   Tuo kurssit                               \n"+
		"   R   Luo raportti                              \n"+
		"   Q   Lopeta                                    \n"+
		"                                                 \n"+
		"*************************************************");
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Proto proto = new Proto();
		proto.loop();
		try
		{
			Serialization.saveCalendar(proto.calendar, OhtuCalendar.DATAFILE);
		} catch (Exception ex)
		{
			System.out.println("Virhe tallennuksessa: " + ex.toString());
		}
	}
}