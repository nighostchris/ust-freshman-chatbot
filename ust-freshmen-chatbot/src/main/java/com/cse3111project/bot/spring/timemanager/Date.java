package com.cse3111project.bot.spring.timemanager;

import java.util.ArrayList;

public class Date
{
	private int month;
	private int day;
	private int noOfActivity = 0;
	private ArrayList<Activity> activity;
	
	public Date(int month, int day, Activity activity)
	{
		this.month = month;
		this.day = day;
		this.activity = new ArrayList<Activity>();
		this.activity.add(activity);
		noOfActivity++;
	}
	
	public addActivity(Activity activity)
	{
		
	}
	
	@Override
	public String toString()
	{
		return timeslot.toString() + " " + name;
	}
}
