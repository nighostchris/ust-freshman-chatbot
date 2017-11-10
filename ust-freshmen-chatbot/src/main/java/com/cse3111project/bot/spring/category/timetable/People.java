package com.cse3111project.bot.spring.category.timetable;

import java.util.ArrayList;

public class People
{
	private String username;
	private ArrayList<Date> dateList;
	
	public People(String username)
	{
		this.username = username;
		this.dateList = new ArrayList<Date>();
	}
	
	public void addEventDate(int month, int day)
	{
		Date target = searchDate(month, day);
		if (target == null)
			dateList.add(new Date(month, day));
	}
	
	public boolean removeDate(int month, int day)
	{
		Date target = searchDate(month, day);
		if (target == null)
			return false;
		dateList.remove(target);
		return true;
	}
	
	/** return the Date object when found, otherwise a null Date object*/
	public Date searchDate(int month, int day)
	{
		for (Date check : dateList)
			if (check.getMonth() == month && check.getDay() == day)
				return check;
		return null;
	}
	
	public String getUsername() { return username; }
	
	public ArrayList<Date> getDateList() { return dateList; }
	
	@Override
	public String toString()
	{
		String output = "Schedule of " + username;
		for (Date loop : dateList)
			output = output + '\n' + loop.toString();
		return output;
	}
}
