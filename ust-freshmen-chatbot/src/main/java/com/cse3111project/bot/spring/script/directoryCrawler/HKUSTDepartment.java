package com.cse3111project.bot.spring.directoryEnquiry;

import java.util.ArrayList;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class HKUSTDepartment
{
	public ArrayList<Department> departmentList;
	private String[] departmentNameList = { "ACCT" , "CBE", "CHEM", "CIVL", "CSE", "ECE", "ECON", "ENVR",
											"FINA", "HUMA", "IELM", "ISOM", "LANG", "LIFS", "MAE", "MARK",
											"MATH", "MGMT", "PHYS", "SOSC"};
	
	public HKUSTDepartment() 
	{
		departmentList = new ArrayList<Department>();
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
		// this URL is for connecting to the ITSC website from outside of campus
		String parentURL = "https://iis.ust.hk/phonebk-extra/default.asp?dept=";
		
		// loop through all department to generate 20 Department staff list
		for (int i = 0; i < 20; i++)
		{
			ArrayList<Staff> staffList = new ArrayList<Staff>();
			ArrayList<String> profName = new ArrayList<String>();
			ArrayList<String> officeLocation = new ArrayList<String>();
			ArrayList<String> email = new ArrayList<String>();
			ArrayList<String> position = new ArrayList<String>();
			departmentList.add(new Department(departmentNameList[i], staffList));
			
			String url = parentURL + departmentNameList[i];
			// in order to access the ITSC website, authorization header will be needed
			Document doc = Jsoup.connect(url).header("Authorization", "Basic" + " eXNzY2hhbmFhOmtlbGVmZTMxNA==").get();
			
			Element table = doc.select("table").get(1);
			Elements rows = table.select("tr");
			
			for (int j = 0; j < rows.size(); j++)
			{
				Elements name = rows.get(j).select("td");
				if (!name.hasAttr("colspan"))
				{
					// get profName
					String englishName = name.get(2).text().replace("\u00a0", "");
					if (!englishName.equals(""))
						profName.add(englishName);
					
					// get officeLocation
					String office = name.get(5).text();
					if (!office.equals("") && !office.equals("Room") && !englishName.equals(""))
						officeLocation.add(office);
					if (office.equals("") && !englishName.equals(""))
						officeLocation.add("");
					
					// get email
					String mail = name.get(4).text();
					if (!mail.equals("") && !mail.equals("E-Mail") && !englishName.equals(""))
						email.add(mail.toLowerCase() + "@ust.hk");
					if (mail.equals("") && !englishName.equals(""))
						email.add("");
					
					// get position
					String pPosition = name.get(0).text();
					if (!pPosition.equals("Functional Title") && !englishName.equals(""))
						position.add(pPosition);
				}
			}
			for (int k = 0; k < profName.size(); k++)
				staffList.add(new Staff(profName.get(k), position.get(k), officeLocation.get(k), email.get(k)));
		}
	}
	
	public ArrayList<Department> getDepartmentList() { return departmentList; }
	
// For testing the web scraper
//	public static void main(String[] args)
//	{
//		new HKUSTDepartment();
//	}
}