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

/**
 * CourseWebsiteSearch class inherits from Academic class, which handles all user query on helping them to find
 * relevant course websites according to the given course code.
 * @version 1.0
 */
public class CourseWebsiteSearch extends Academic
{
	private ArrayList<String> userQuery;
	private String parentURL[] = { "https://www.google.com/search?q=", "+hkust&num=4" };
	private ArrayList<Website> searchList;
	
	public static final String WEBSITE_SEARCH_KEYWORD[] = Utilities.concatArrays(new String[] { "course website", "website", "search" },
																				 Course.COURSE_CODE_KEYWORD);

	public CourseWebsiteSearch(final ArrayList<String> userQuery)
	{
		this.userQuery = userQuery;
		searchList = new ArrayList<Website>();
	}
	
	/**
	 * This method will perform web crawling on the search engine and try to get the results of searching relevant course
	 * websites according to the given course code.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void webCrawling() throws MalformedURLException, IOException
	{
		try
		{
			String finalURL = parentURL[0];
			for (int i = 0; i < userQuery.size(); i++)
			{
				if (userQuery.get(i) != "course website")
					if (userQuery.get(i) != "website")
						if (userQuery.get(i) != "search")
						{
							finalURL += URLEncoder.encode(userQuery.get(i), "UTF-8");
							finalURL += "+";
						}
			}
			URL url = new URL(finalURL + parentURL[1]);
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
	
	/**
	 * Getter method of the instance variable searchList, which is the list containing the search engine result.
	 * @return ArrayList
	 */
	public ArrayList<Website> getSearchList() { return searchList; }
	
	/**
	 * This method will help the SearchEngine class to get the search results on this class and write them in a nicer way
	 * in a single string.
	 * @return String
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getCourseWebsite() throws MalformedURLException, IOException
	{ 
		webCrawling();
		return super.replyResults(searchList); 
	}
}
