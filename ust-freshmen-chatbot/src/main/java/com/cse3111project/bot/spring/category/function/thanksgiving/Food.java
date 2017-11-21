package com.cse3111project.bot.spring.category.function.thanksgiving;
/**
 * 
 * 
 * This is a class for food that the student bring
 */
public class Food {
	/**
	 * This is the food name.
	 */
	private String foodName;
	
	/**
	 * This is a default constructor
	 * @param n This a the name of the food
	 */
	public Food(String n) {
		foodName = n;
	}
	/**
	 * This is a getter function for food name
	 * @return foodName This is the food name of this object
	 */
	public String getFoodName() {
		return foodName;
	}
	/**
	 * This is a function for comparing the food name whether is same as this object including upper lower case
	 * @param thatFood this is a food name in string type for comparasion
	 * @return true if they are equal
	 */
	public boolean compareFoodName(String thatFood) {
		String foodA = thatFood.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		String foodB = this.foodName.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		return foodA.equals(foodB);
	}
	
}