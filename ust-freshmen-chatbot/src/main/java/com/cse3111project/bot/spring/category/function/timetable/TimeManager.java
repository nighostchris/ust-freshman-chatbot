package com.cse3111project.bot.spring.category.function.timetable;

import java.util.ArrayList;
import java.util.Collections;

@Deprecated
/**
  @deprecated
  user data would be saved in their own local disk (Phones)
  If use SQL database, it would be hard to identify which is which
  since username may <b>NOT</b> be unique
  Even if giving them a unique ID, it would be still hard to identify
  <i>(might be changed if there exists a better solution)</i>
  */
class TimeManager extends TimeTable
{
	private ArrayList<People> peopleList;
	
	TimeManager() { peopleList = new ArrayList<People>(); }
	
	boolean addNewPeople(String name) {
		People target = searchPeople(name);
		if (target == null)
		{
			peopleList.add(new People(name));
			return true;
		}
		return false;
	}
	
	boolean removePeople(String name) {
		People target = searchPeople(name);
		if (target != null)
		{
			peopleList.remove(target);
			return true;
		}
		return false;
	}
	
	People searchPeople(String name) {
		for (People add : peopleList)
			if (add.getUsername().equals(name))
				return add;
		return null;
	}
	
	ArrayList<People> getPeopleList() { return peopleList; }
}
