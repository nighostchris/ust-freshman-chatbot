package com.cse3111project.bot.spring.category.timetable;

import java.util.ArrayList;

public class Date implements Comparable<Date>
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
		for (Activity check : this.activity)
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
	
	public boolean removeActivity(String name)
	{
		for (Activity check : this.activity)
		{
			if (check.getName().equals(name))
			{
				this.activity.remove(check);
				noOfActivity--;
				return true;
			}
		}
		return false;
	}
	
	/** return the Activity object when found, otherwise a null Activity object*/
	public Activity searchActivity(String name)
	{
		for (Activity check : this.activity)
			if (check.getName().equals(name))
				return check;
		return null;
	}
	
	public int getMonth() { return month; }
	
	public int getDay() { return day; }
	
	public int getNoOfActivity() { return noOfActivity; }
	
	public ArrayList<Activity> getActivity() { return activity; }
	
	@Override
	public int compareTo(Date d)
	{
		if (this.month > d.month)
			return 1;
		else if (this.month == d.month)
		{
			if (this.day > d.day)
				return 1;
			else if (this.day < d.day)
				return -1;
			else
				return 0;
		}
		else
			return -1;
	}
	
	@Override
	public String toString()
	{
		String output = "[" + month + "-" + day + "]";
		for (Activity loop : this.activity)
			output = output + '\n' + loop.toString();
		output += '\n';
		return output;
	}
}
