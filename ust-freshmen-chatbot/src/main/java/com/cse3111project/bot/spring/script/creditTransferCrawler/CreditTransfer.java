package com.cse3111project.bot.spring.script.creditTransferCrawler;

import java.util.ArrayList;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class CreditTransfer 
{
	public static final String website = "http://arr.ust.hk/ust_actoe/credit_local.php?selI=";
	public static final String parameter = "&txtK=&search=y&btn1=+Search+#myform";
	public static final String localNumber[] = { "B0650", "B0058", "B0062", "B0131", "B0686", "B0746", "B0132", "B0640", 
												 "B0144", "B0182", "B0262", "B0989", "B0652", "B0328a", "B0321", "B0326",
												 "B0328", "B0335", "B0344", "X033" };
	
	public static final String specialLink[] = { "http://arr.ust.hk/ust_actoe/credit_local.php?selI=B0652&txtK=&search=y&btn1=+Search+#myform", 
												 "http://arr.ust.hk/ust_actoe/credit_local.php?selI=B0652&selC=&txtK=&search=y&page=2#myresult",
												 "http://arr.ust.hk/ust_actoe/credit_local.php?selI=B0652&selC=&txtK=&search=y&page=3#myresult",
												 "http://arr.ust.hk/ust_actoe/credit_local.php?selI=B0652&selC=&txtK=&search=y&page=4#myresult",
												 "http://arr.ust.hk/ust_actoe/credit_local.php?selI=B0652&selC=&txtK=&search=y&page=5#myresult" };
	
	private ArrayList<LocalSchoolCredit> localSchoolList;
	
	public CreditTransfer()
	{
		localSchoolList = new ArrayList<LocalSchoolCredit>();
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
		// Crawl local school credit details first
		ArrayList<String> courseCode = new ArrayList<String>();
		ArrayList<String> transferCourseCode = new ArrayList<String>();
		ArrayList<String> restriction = new ArrayList<String>();
		
		for (int i = 0; i < localNumber.length; i++)
		{
			if (i != 12)
			{
				URL url = new URL(website + localNumber[i] + parameter);
				Document doc = Jsoup.parse(url, 3000);
				Element table = doc.select("table").get(5);
				Elements rows = table.select("tr");
				
				for (int j = 2; j < rows.size(); j++)
				{
					Elements details = rows.get(j).select("td");
					if (!details.get(0).text().equals("No record(s) found."))
					{
						String code = details.get(1).text();
						String USTCode = details.get(2).text();
						String restrict = details.get(4).text();
						if (restrict.equals("EXP"))
							restrict = "Expired";
						else if (restrict.equals("--"))
							restrict = "No Restriction";
						
						courseCode.add(code);
						transferCourseCode.add(USTCode);
						restriction.add(restrict);
					}
				}
			}
			else
			{
				for (int k = 0; k < specialLink.length; k++)
				{
					URL url = new URL(specialLink[k]);
					Document doc = Jsoup.parse(url, 3000);
					Element table = doc.select("table").get(5);
					Elements rows = table.select("tr");
					
					for (int j = 2; j < rows.size(); j++)
					{
						Elements details = rows.get(j).select("td");
						if (!details.get(0).text().equals("No record(s) found."))
						{
							String code = details.get(1).text();
							String USTCode = details.get(2).text();
							String restrict = details.get(4).text();
							if (restrict.equals("EXP"))
								restrict = "Expired";
							else if (restrict.equals("--"))
								restrict = "No Restriction";
							
							courseCode.add(code);
							transferCourseCode.add(USTCode);
							restriction.add(restrict);
						}
					}
				}
			}
		}
		
		for (int i = 0; i < courseCode.size(); i++)
		{
			localSchoolList.add(new LocalSchoolCredit(courseCode.get(i), transferCourseCode.get(i), restriction.get(i)));
			System.out.println(localSchoolList.get(i));
		}
	}
	
	// for testing web crawler only
	//public static void main(String[] args)
	//{
	//	new CreditTransfer();
	//}
}
