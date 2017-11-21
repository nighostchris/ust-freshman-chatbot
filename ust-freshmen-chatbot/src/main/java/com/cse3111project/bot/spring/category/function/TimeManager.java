package com.cse3111project.bot.spring.category.function;

import java.util.ArrayList;
import java.util.Collections;

class TimeManager
{
	private ArrayList<People> peopleList;
	
	TimeManager() { peopleList = new ArrayList<People>(); }
	
	boolean addNewPeople(String name) 
	{
		People target = searchPeople(name);
		if (target == null)
		{
			peopleList.add(new People(name));
			return true;
		}
		return false;
	}
	
	boolean removePeople(String name) 
	{
		People target = searchPeople(name);
		if (target != null)
		{
			peopleList.remove(target);
			return true;
		}
		return false;
	}
	
	People searchPeople(String name) 
	{
		for (People add : peopleList)
			if (add.getUsername().equals(name))
				return add;
		return null;
	}
	
	ArrayList<People> getPeopleList() { return peopleList; }
}