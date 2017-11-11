package com.cse3111project.bot.spring.society;

public class Society 
{
	private String name;
	private String websiteLink;
	
	public Society()
	{
		this.name = "";
		this.websiteLink = "";
	}
	
	public Society(String name, String websiteLink)
	{
		this.name = name;
		this.websiteLink = websiteLink;
	}
	
	public String getName() { return name; }
	public String getWebsiteLink() { return websiteLink; }
}
