package com.cse3111project.bot.spring.category.timetable;

public class Timeslot 
{
	private int start;
	private int end;
	
	public Timeslot(int start, int end)
	{
		this.start = start;
		this.end = end;
	}
	
	public boolean hasConflict(Timeslot timeslot)
	{
		if (this.start >= timeslot.start && this.start < timeslot.end)
			return true;
		else if (timeslot.start >= this.start && timeslot.start < this.end)
			return true;
		else
			return false;
	}
	
	int getStart() { return start; }
	int getEnd() { return end; }
	
	@Override
	public String toString()
	{
		return getHourFormat(start) + "-" + getHourFormat(end);
	}
	
	private String getHourFormat(int hour)
	{
		if (hour <= 9)
			return "0" + hour + ":00";
		else
			return hour + ":00";
	}
}
