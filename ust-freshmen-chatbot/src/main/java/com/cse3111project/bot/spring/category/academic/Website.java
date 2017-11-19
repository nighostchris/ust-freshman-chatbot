package com.cse3111project.bot.spring.category.academic;

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
	
	public String getTitle() { return title; }
	
	public String getWebsiteLink() { return websiteLink; }
	
	@Override
	public String toString()
	{
		return "Title: " + title + "\nWebsite: " + websiteLink;
	}
}
