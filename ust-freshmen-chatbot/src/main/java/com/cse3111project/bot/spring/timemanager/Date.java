package com.cse3111project.bot.spring.timemanager;

import java.util.ArrayList;

public class Date
{
	private int month;
	private int day;
	private int noOfActivity = 0;
	private ArrayList<Activity> activity;
	
	public Date(int month, int day)
	{
		this.month = month;
		this.day = day;
		this.activity = new ArrayList<Activity>();
	}
	
	public boolean addActivity(Activity activity)
	{
		boolean canAdd = true;
		for (Activity check : activity)
		{
			if (check.getTimeslot().hasConflict(activity.getTimeslot()))
				canAdd = false;
		}
		if (canAdd)
		{
			this.activity.add(activity);
			noOfActivity++;
			return canAdd;
		}
		return canAdd;
	}
	
	public boolean removeActivity(Activity activity)
	{
		for (Activity check : activity)
		{
			if (check.getName().equals(activity.getName()))
			{
				this.activity.remove(check);
				noOfActivity--;
				return true;
			}
		}
		return false;
	}
	
	public int getMonth() { return month; }
	
	public int getDay() { return day; }
	
	public int getNoOfActivity() { return noOfActivity; }
	
	public ArrayList<Activity> getActivity() { return activity; }
	
	@Override
	public String toString()
	{
		
	}
}
