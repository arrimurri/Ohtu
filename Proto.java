package Ohtu;

import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Collections;

/*
 * Kalenteriohjelman k�li-luokka
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
			System.out.println("Kurssi on jo kalenterissasi, lis�t��n sille viikottaisen tapahtuman");
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
                "   Rekister�inti:                                \n"+
                "   Anna sen kurssin numero jolle haluat          \n"+
                "   osallistua. Jos haluat rekister�ity� monelle  \n"+
                "   kurssille samalla kertaa, niin annan kaikkien \n"+
                "   kurssien numerot v�lily�nnille erotettuina.   \n"+
                "                                                 \n"+
                "*************************************************");
        
		String cmd;
		if(!sc.hasNextLine())
        {
            System.out.println("Ei komentoa, palaat takaisin p��valikkoon");
            return;
        }
		cmd = sc.nextLine();
        String[] ids = cmd.split("[ ]");
        for(int i = 0; i < ids.length; i++) 
        {
            int id = Integer.parseInt(ids[i]);
            Course c = this.calendar.allCourses.get(id - 1);
            System.out.println("Anna lis�tietoja kurssille " + c.coursename);
            System.out.println("Anna opintopisteiden m��r� (pelkk� luku):");
            int cp = 0;
            cmd = sc.nextLine();
            try
            {
                cp = Integer.parseInt(cmd);
            }
            catch(Exception e)
            {
                System.out.println("V��r�nlainen sy�te, 0 opintopistett� rekister�ity");
            }
            c.setCoursepoints(cp);
            System.out.println("Anna tenttip�iv�m��r� muodossa dd.mm.yyyy:");
            cmd = sc.nextLine();
            GregorianCalendar date = new GregorianCalendar();
            try
            {
                String[] datestring = cmd.split("[.]");
                int year = Integer.parseInt(datestring[2]);
                int month = Integer.parseInt(datestring[1]);
                int day = Integer.parseInt(datestring[0]);
                date.set(year, month, day);

            }
            catch (Exception e)
            {
                System.out.println("V��r�nlainen p�iv�m��r�. Kurssikoe on t�m�n p�iv�n p�iv�m��r�ll�");
            }
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
			System.out.println(we.description);
			System.out.println(we.toString());
		}
	}

	private void pressEnter()
	{
		System.out.println("Paina entteri� jatkaaksesi");
		String dummy = sc.nextLine();
	}

	public void printMenu()
	{
		System.out.println(
		"*************************************************\n"+		
		"   Kalenteri                                     \n"+
		"                                                 \n"+
		"   L   Lis�� kurssi tai tapahtuma kurssille      \n"+
		"   N   N�yt� kalenteri                           \n"+
		"   V   Vaihda n�kym��                            \n"+
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
		//Kurssi kurssi1 = new Kurssi(123, "xml");
		//kurssi1.setStartDate(2011, 3, 2);
		//kurssi1.setEndDate(2012, 6, 15);
		//System.out.println(kurssi1.toString());
	}
}