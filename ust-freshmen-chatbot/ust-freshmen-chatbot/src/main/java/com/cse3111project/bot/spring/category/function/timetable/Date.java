package com.cse3111project.bot.spring.category.function.timetable;

import java.io.Serializable;

import java.util.ArrayList;

import com.cse3111project.bot.spring.exception.InvalidDateException;

class Date implements Comparable<Date>, Serializable
{
    private static final long serialVersionUID = 1L;

	private int month;
	private int day;
	// private int noOfActivity = 0;  // will be tracked by activity.size()
	private ArrayList<Activity> activity;
	
	Date(int month, int day)
    {
		this.month = month;
		this.day = day;
		this.activity = new ArrayList<Activity>();
	}

    static void checkValidity(int month, int day) throws InvalidDateException {
        if (month < 0 || month > 12)
            throw new InvalidDateException("Entered invalid month. Please try again");
        if (day < 0 || day > 31)
            throw new InvalidDateException("Entered invalid day. Please try again");
        if (month == 4 || month == 6 || month == 9 || month == 11)
            if (day > 30)
                throw new InvalidDateException("Entered invalid day. Please try again");
        if (month == 2 && day > 29)  // 
            throw new InvalidDateException("Entered invalid day. Please try again");
    }
	
	boolean addActivity(Activity activity)
	{
		for (Activity check : this.activity)
			if (check.getTimeslot().hasConflict(activity.getTimeslot()))
                return false;

        this.activity.add(activity);
        // noOfActivity++;

		return true;
	}
	
	boolean removeActivity(String name)
	{
		for (Activity check : this.activity)
		{
			if (check.getName().equals(name))
			{
				this.activity.remove(check);
				// noOfActivity--;
				return true;
			}
		}
		return false;
	}
	
	/** return the Activity object when found, otherwise a null Activity object*/
	Activity searchActivity(String name)
	{
		for (Activity check : this.activity)
			if (check.getName().equals(name))
				return check;
		return null;
	}
	
	int getMonth() { return month; }
	
	int getDay() { return day; }
	
	// public int getNoOfActivity() { return noOfActivity; }

    boolean isEmpty() { return activity.isEmpty(); }
	
	ArrayList<Activity> getActivity() { return activity; }
	
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
		String output = "[" + month + "/" + day + "]";
		for (Activity loop : this.activity)
			output = output + '\n' + loop.toString();
		output += '\n';
		return output;
	}
}
