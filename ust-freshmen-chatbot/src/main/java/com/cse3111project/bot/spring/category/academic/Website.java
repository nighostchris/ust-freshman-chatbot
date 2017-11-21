package com.cse3111project.bot.spring.category.academic;

/**
 * Website class is wrapped in the CourseWebsiteSearch class, which is used to store details of the course websites crawled
 * from the Internet.
 * @version 1.0
 */
public class Website 
{
	private String title;
	private String websiteLink;
	
	public Website()
	{
		title = "";
		websiteLink = "";
	}
	
	public Website(String title, String websiteLink)
	{
		this.title = title;
		this.websiteLink = websiteLink;
	}
	
	/**
	 * Getter method of the website title.
	 * @return String
	 */
	public String getTitle() { return title; }
	
	/**
	 * Getter method of the URL of the website.
	 * @return String
	 */
	public String getWebsiteLink() { return websiteLink; }
	
	@Override
	public String toString()
	{
		return "Title: " + title + "\nWebsite: " + websiteLink + "\n";
	}
}
