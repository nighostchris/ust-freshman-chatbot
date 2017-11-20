package com.cse3111project.bot.spring.script.courseDetailsCrawl;

import java.util.ArrayList;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class HKUSTCourse 
{
	private ArrayList<Course> courseList;
	
	private static final String parentUrl = "http://prog-crs.ust.hk/ugcourse/2017-18/";
	
	private static final String courseCategory[] = { "ACCT", "BIBU", "BIEN", "BIPH", "BMED", "CENG", "CHEM", "CIVL", "COMP",
													 "CPEG", "ECON", "ELEC", "ENGG", "ENTR", "ENVR", "ENVS", "FINA", "FYTG",
													 "GBUS", "GNED", "HART", "HLTH", "HUMA", "IDPO", "IELM", "IIMP", "IROP",
													 "ISOM", "LABU", "LANG", "LIFS", "MARK", "MATH", "MECH", "MGMT", "PHYS",
													 "RMBI", "SBMT", "SCIE", "SHSS", "SISP", "SOSC", "SUST", "TEMG", "UROP",
													 "WBBA"};
	
	public HKUSTCourse()
	{
		courseList = new ArrayList<Course>();
		try
		{
			webCrawling();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void webCrawling() throws Exception
	{
		for (int i = 0; i < courseCategory.length; i++)
		//for (int i = 0; i < 1; i++)
		{
			URL url = new URL(parentUrl + courseCategory[i]);
			Document doc = Jsoup.parse(url, 3000);
			Elements courseTable = doc.select("li.crse.accordion-item");
			Elements course = courseTable.select("div.accordion-item-header");
			Elements courseCodeList = course.select("div.crse-code");
			Elements courseTitleList = course.select("div.crse-title");
			Elements courseCreditList = course.select("div.crse-unit");
			
			for (int j = 0; j < courseTable.size(); j++)
			{
				String courseCode = courseCodeList.get(j).text();
				String courseTitle = courseTitleList.get(j).text();
				String courseCredit = courseCreditList.get(j).text();
				String prerequisite = "";
				String exclusion = "";
				String corequisite = "";
				
				Elements courseCriteria = courseTable.get(j).select("div.data-row.data-row-default");
				if (!courseCriteria.isEmpty())
				{
					Elements header = courseCriteria.select("div.header");
					Elements data = courseCriteria.select("div.data");
					
					for (int k = 0; k < header.size(); k++)
					{
						if (header.get(k).text().contains("Prerequisite(s)"))
							prerequisite = data.get(k).text();
						else if (header.get(k).text().contains("Exclusion(s)"))
							exclusion = data.get(k).text();
						else if (header.get(k).text().contains("Corequisite(s)"))
							corequisite = data.get(k).text();
					}
				}
				Course newCourse = new Course(courseCode, courseTitle, courseCredit, prerequisite, exclusion, corequisite);
				courseList.add(newCourse);
			}
		}
	}
	
	public ArrayList<Course> getCourseList() { return courseList; }
	
	// local testing for web crawling function only
	//public static void main(String[] args)
	//{
		//HKUSTCourse test = new HKUSTCourse();
		//for (Course print : test.courseList)
			//System.out.println(print);
	//}
}