package com.cse3111project.bot.spring.category.function.thanksgiving;

import com.cse3111project.bot.spring.category.Category;

import java.util.ArrayList;
import java.util.Date;

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
	
}