package com.cse3111project.bot.spring.category.function.timetable;

import java.util.ArrayList;

public class People
{
	private String username;
	private ArrayList<Date> dateList;
	
	People(String username)
	{
		this.username = username;
		this.dateList = new ArrayList<Date>();
	}
	
	void addEventDate(int month, int day)
    {
		Date target = searchDate(month, day);
		if (target == null)
			dateList.add(new Date(month, day));
	}
	
	boolean removeDate(int month, int day)
    {
		Date target = searchDate(month, day);
		if (target == null)
			return false;
		dateList.remove(target);
		return true;
	}
	
	/** return the Date object when found, otherwise a null Date object*/
	Date searchDate(int month, int day)
	{
		for (Date check : dateList)
			if (check.getMonth() == month && check.getDay() == day)
				return check;
		return null;
	}
	
	String getUsername() { return username; }
	
	ArrayList<Date> getDateList() { return dateList; }

    boolean hasEmptySchedule() { return dateList.isEmpty(); }
	
	@Override
	public String toString()
	{
		String output = "Schedule of " + username;
		for (Date loop : dateList)
			output = output + '\n' + loop.toString();
		return output;
	}
}
