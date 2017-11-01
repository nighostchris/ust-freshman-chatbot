package com.cse3111project.bot.spring.timemanager;

public class Activity 
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
	public String toString()
	{
		return timeslot.toString() + " " + name;
	}
}
