package com.cse3111project.bot.spring.category.function.timetable;

import java.io.Serializable;

class Activity implements Comparable<Activity>, Serializable
{
    private static final long serialVersionUID = 1L;

	private String name;
	private Timeslot timeslot;
	
	Activity(String name, Timeslot timeslot)
	{
		this.name = name;
		this.timeslot = timeslot;
	}
	
	String getName() { return name; }
	
	Timeslot getTimeslot() { return timeslot; }
	
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
