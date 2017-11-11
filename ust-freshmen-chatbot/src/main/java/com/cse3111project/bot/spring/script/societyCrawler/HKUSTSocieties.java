package com.cse3111project.bot.spring.society;

import java.util.ArrayList;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class HKUSTSocieties
{
	private ArrayList<Society> societyList;
	
	public HKUSTSocieties() 
	{ 
		societyList = new ArrayList<Society>();
		try
		{
			webCrawling();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void webCrawling() throws Exception
	{
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> websiteLink = new ArrayList<String>();
		
		String exceptHref[] = { "drive.google.com", ".pdf", "wordpress.com", "#", "su.ust.hk", "court", "ASC/Cons/"};
		String exceptName = "Name of the Society";
		
		URL url = new URL("https://hkustsucouncil.wordpress.com/standing-committees/the-affiliated-societies-committee/list-of-affiliated-societies/");
		Document doc = Jsoup.parse(url, 3000);
		
		// Crawl societies' names
		Elements table = doc.select("tr");
		for (int i = 0; i < table.size(); i++)
		{
			Elements sName = table.get(i).select("td");
			String englishName = sName.get(0).text();
			if (!englishName.contains(exceptName))
			{
				englishName = englishName.replaceAll(", HKUSTSU", "");
				name.add(englishName);
			}
		}
		
		// Crawl website link of each society
		Elements link = doc.select("a");
		for (int i = 0; i < link.size(); i++)
		{
			String href = link.get(i).attr("href");
			if (!href.contains(exceptHref[0]) && !href.contains(exceptHref[1]) 
				&& !href.contains(exceptHref[2]) && !href.contains(exceptHref[3]) 
				&& !href.contains(exceptHref[4]) && !href.contains(exceptHref[5])
				&& !href.contains(exceptHref[6]))
				websiteLink.add(href);
		}
		
		for (int i = 0; i < name.size(); i++)
		{
			Society newSociety = new Society(name.get(i), websiteLink.get(i));
			societyList.add(newSociety);
		}
	}
	
	public ArrayList<Society> getSocietyList() { return societyList; }
	
	/** return Society Object upon successful search, Society with empty name returned otherwise */
	public Society searchSociety(String name)
	{
		Society target = new Society();
		for (Society society : societyList)
			if (society.getName().contains(name))
				return society;
		return target;
	}
}
