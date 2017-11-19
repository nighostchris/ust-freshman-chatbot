package com.cse3111project.bot.spring.category.academic;
import com.cse3111project.bot.spring.utility.Utilities;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.net.MalformedURLException;

public class CourseWebsiteSearch extends Academic
{
	private ArrayList<String> userQuery;
	private String parentURL[] = { "https://www.google.com/search?q=", "+hkust&num=4" };
	private ArrayList<Website> searchList;
	
	public static final String WEBSITE_SEARCH_KEYWORD[] = Utilities.concatArrays(new String[] { "course", "website", "search" },
																				 Course.COURSE_CODE_KEYWORD);

	public CourseWebsiteSearch(final ArrayList<String> userQuery)
	{
		this.userQuery = userQuery;
		searchList = new ArrayList<Website>();
	}
	
	public void webCrawling() throws MalformedURLException, IOException
	{
		try
		{
			URL url = new URL(parentURL[0] + URLEncoder.encode(userQuery.get(0), "UTF-8") + parentURL[1]);
			Document doc = Jsoup.parse(url, 3000);
			Elements rows = doc.select("h3.r > a");
			for (Element result : rows)
			{
				String title = result.text();
				String websiteLink = result.attr("href");
				searchList.add(new Website(title, websiteLink));
			}
		}
        catch (MalformedURLException e) 
		{
            Utilities.errorLog("Bad URL", e);
            throw new MalformedURLException("Unexpected Error occurred. Sorry");
        }
        catch (IOException e)
        {
            Utilities.errorLog("I/O error occurred while querying course website", e);
            throw new IOException("Unexpected Error occurred while searching the course website. Sorry");
        }
	}
	
	public ArrayList<Website> getSearchList() { return searchList; }
	
	public String getCourseWebsite() throws MalformedURLException, IOException
	{ 
		webCrawling();
		return super.replyResults(searchList); 
	}
}
