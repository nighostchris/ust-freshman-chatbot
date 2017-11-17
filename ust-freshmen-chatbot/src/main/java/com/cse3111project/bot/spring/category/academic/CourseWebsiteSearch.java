package com.cse3111project.bot.spring.category.academic;

import java.util.ArrayList;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class CourseWebsiteSearch 
{
	private String parentURL[] = { "https://www.google.com/search?q=", "+hkust&num=4" };
	private ArrayList<Website> searchList;
	
	public CourseWebsiteSearch()
	{
		searchList = new ArrayList<Website>();
		try
		{
			webCrawling();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void webCrawling() throws Exception
	{
		URL url = new URL(parentURL[0] + "comp2012" + parentURL[1]);
		Document doc = Jsoup.parse(url, 3000);
		Elements rows = doc.select("h3.r > a");
		for (Element result : rows)
		{
			String title = result.text();
			String websiteLink = result.attr("href");
			searchList.add(new Website(title, websiteLink));
		}
	}
	
	public ArrayList<Website> getSearchList() { return searchList; }
	
	// testing if the crawler works locally
	//public static void main(String[] args)
	//{
	//	new CourseWebsiteSearch();
	//	for (Website website : searchList)
	//	{
	//		System.out.println(website.getTitle());
	//		System.out.println(website.getWebsiteLink());
	//		System.out.println();
	//	}
	//}
}
