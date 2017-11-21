package com.cse3111project.bot.spring.category.function.thanksgiving;

import com.cse3111project.bot.spring.category.Category;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.*;
import java.time.*;
import java.util.*;

/**
 * 
 * 
 * This is a divied class of Activity for Thanksgiving party
 */
public abstract class Thanksgiving extends Activity{
	/**
	 * This is the Food list of Thanksgiving party
	 */
	private ArrayList<Food> foodlist;
	
	/**
	 * This is the default constructor
	 */
	public Thanksgiving() {
		super("Thanksgiving", new Timeslot(0, 23));
		foodlist = new ArrayList<Food>();
	}
	/**
	 * This is a function for adding new food to the list
	 * @param newFood this is a string type input parameter  
	 */
	public void addNewFood(String newFood) {
		if(foodlist==null)
			foodlist.add(new Food(newFood));
		else {
			Boolean temp = true;
			for (Food loop : foodlist)
			{
				// same name
				if(loop.compareFoodName(newFood))
				{
					temp = false;
					break;
				}
			}
			if(temp)
			{
				foodlist.add(new Food(newFood));
				replyText(newFood+" has been added");
			}
			else
			{
				replyText("Someone is bringing that already, can you pick another one?");
			}
				
		}
	}
	/**
	 * This is a function for handing the invitation message
	 */
	public void invitation()
	{
    	
    	ArrayList<LocalDate> durationOfThanksGiving = new ArrayList<LocalDate>();
		boolean []releaseOfThanksGivingInvitation = new boolean[5];
		for(int i=0;i<5;i++)
			releaseOfThanksGivingInvitation[i]=false;
		//boolean accepted = false;
		DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(
		        FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
		LocalDate x1 = LocalDate.parse("21.11.2017", germanFormatter);
		LocalDate x2 = LocalDate.parse("21.11.2017", germanFormatter);
		LocalDate x3 = LocalDate.parse("21.11.2017", germanFormatter);
		LocalDate x4 = LocalDate.parse("21.11.2017", germanFormatter);
		LocalDate x5 = LocalDate.parse("21.11.2017", germanFormatter);
		durationOfThanksGiving.add(x1);
		durationOfThanksGiving.add(x2);
		durationOfThanksGiving.add(x3);
		durationOfThanksGiving.add(x4);
		durationOfThanksGiving.add(x5);
		LocalDate todaysDate = LocalDate.now();
		int tempForThanksgiving = 0;
		for (LocalDate d : durationOfThanksGiving)
		{
			// if today's day are in durationOfThanksGiving
			if(todaysDate.equals(d))
			{
				//checking the existance of ture in releaseOfThanksGivingInvitation
				boolean tempOfExistanceOfReleaseOfThanksGivingInvitation = true;
				for(boolean a:releaseOfThanksGivingInvitation)
				{
					if(a==true)
					{
						tempOfExistanceOfReleaseOfThanksGivingInvitation = false;
						break;
					}
				}
				if(releaseOfThanksGivingInvitation[tempForThanksgiving]==false&&tempOfExistanceOfReleaseOfThanksGivingInvitation)
				{
					// send invitation !!! incomplete 
					// ...
					//if return accept
					boolean resultOfInvitation = true;
					
					if(resultOfInvitation)
					{
						releaseOfThanksGivingInvitation[tempForThanksgiving] = true;
						ThanksgivingParty tgp = new ThanksgivingParty();
						//get user input for food name !!! incomplete 
						replyText("what party snack will you bring?");
						String foodname = "";
						tgp.addNewFood(foodname);
					}
					
				}
				break;
			}
			
			tempForThanksgiving++;
		}
    }
	
}