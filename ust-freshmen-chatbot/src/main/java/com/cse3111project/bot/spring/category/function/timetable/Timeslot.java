package com.cse3111project.bot.spring.category.function.timetable;

import java.io.Serializable;

import com.cse3111project.bot.spring.exception.InvalidTimeslotException;

class Timeslot implements Serializable
{
    private static final long serialVersionUID = 1L;

	private int start;
	private int end;
	
	Timeslot(int start, int end)
	{
		this.start = start;
		this.end = end;
	}

    static void checkValidity(int start, int end) throws InvalidTimeslotException {
        if (start < 0 || start >= 24)  // limit input range [0, 24)
            throw new InvalidTimeslotException("Entered invalid starting time. Please try again");
        if (end < 0 || end >= 24)  // limit input range [0, 24)
            throw new InvalidTimeslotException("Entered invalid ending time. Please try again");
    }
	
	boolean hasConflict(Timeslot timeslot)
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
