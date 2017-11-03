package com.cse3111project.bot.spring.timemanager;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class TimeManager 
{
	private ArrayList<People> peopleList;
	
	public TimeManager() { peopleList = new ArrayList<People>(); }
	
	public boolean addNewPeople(String name)
	{
		People target = searchPeople(name);
		if (target == null)
		{
			peopleList.add(new People(name));
			return true;
		}
		return false;
	}
	
	public boolean removePeople(String name)
	{
		People target = searchPeople(name);
		if (target != null)
		{
			peopleList.remove(target);
			return true;
		}
		return false;
	}
	
	public People searchPeople(String name)
	{
		for (People add : peopleList)
			if (add.getUsername().equals(name))
				return add;
		return null;
	}
	
	public ArrayList<People> getPeopleList() { return peopleList; }
	
	public static void main(String[] args)
	{
		TimeManager tm = new TimeManager();
		System.out.println("Enter Username(For retrieving timetable / create new user): ");
		Scanner sc = new Scanner(System.in);
		String username = sc.next();
		People user = tm.searchPeople(username);
		if (user == null)
		{
			user = new People(username);
			tm.getPeopleList().add(user);
		}
		String input = "";
		while (input != "q")
		{
			System.out.println("\n1) Add event");
			System.out.println("2) Remove event");
			System.out.println("3) Display event for particular date");
			System.out.println("4) Display all event");
			System.out.println("Press q to leave");
			input = sc.next();
			switch (input)
			{
				case "1":
					System.out.println("Enter month and day for event:");
					int month = sc.nextInt();
					int day = sc.nextInt();
					user.addEventDate(month, day);
					Date date = user.searchDate(month, day);
					System.out.println("Enter event start and end time in hour (0-23, Seperated by Space): ");
					int start = sc.nextInt();
					int end = sc.nextInt();
					System.out.println("Enter activity name:");
					String activityName = sc.next();
					Timeslot timeslot = new Timeslot(start, end);
					Activity newActivity = new Activity(activityName, timeslot);
					if (date.addActivity(newActivity))
					{
						System.out.println("Event Added.");
						Collections.sort(user.getDateList());
						Collections.sort(date.getActivity());
					}
					else
						System.out.println("Time Conflict Occurs");
					break;
				
				case "2":
					System.out.println("Enter month and day for removal:");
					int month2 = sc.nextInt();
					int day2 = sc.nextInt();
					Date date2 = user.searchDate(month2, day2);
					if (date2 == null)
						System.out.println("Invalid Removal. No event on that day");
					else
					{
						System.out.println("Enter activity name:");
						String activityName2 = sc.next();
						if (date2.removeActivity(activityName2))
							System.out.println("Event deleted.");
						else
							System.out.println("No event with name " + activityName2);
					}
					break;
					
				case "3":
					System.out.println("Enter month and day for display:");
					int month3 = sc.nextInt();
					int day3 = sc.nextInt();
					Date date3 = user.searchDate(month3, day3);
					if (date3 == null)
						System.out.println("No event on " + month3 + "-" + day3);
					else
						System.out.println(date3);
					break;
					
				case "4":
					System.out.println(user);
					break;
				default:
					input = "q";
					break;
			}
		}
		sc.close();
	}
}
