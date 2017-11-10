package com.cse3111project.bot.spring.category.timetable;

public class Activity implements Comparable<Activity>
{
	private String name;
	private Timeslot timeslot;
	
	public Activity(String name, Timeslot timeslot)
	{
		this.name = name;
		this.timeslot = timeslot;
	}
	
	public String getName() { return name; }
	
	public Timeslot getTimeslot() { return timeslot; }
	
	@Override
	public int compareTo(Activity a)
	{
		Timeslot another = a.getTimeslot();
		if (timeslot.getStart() >= another.getEnd())
			return 1;
		else if (timeslot.getEnd() <= another.getStart())
			return -1;
		else
			return 0;
	}
	
	@Override
	public String toString()
	{
		return timeslot.toString() + " " + name;
	}
}
