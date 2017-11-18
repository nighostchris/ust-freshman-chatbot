package com.cse3111project.bot.spring.script.creditTransferCrawler;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class CreditTransfer 
{
	public static final String localWebsite = "http://arr.ust.hk/ust_actoe/credit_local.php?selI=";
	public static final String localParameter = "&txtK=&search=y&btn1=+Search+#myform";
	public static final String localCode[] = { "B0650", "B0058", "B0062", "B0131", "B0686", "B0746", "B0132", "B0640", 
												 "B0144", "B0182", "B0262", "B0989", "B0652", "B0328a", "B0321", "B0326",
												 "B0328", "B0335", "B0344", "X033" };
	
	public static final String examWebsite = "http://arr.ust.hk/ust_actoe/credit_exam.php?selI=";
	public static final String examParameter = "&search=y#myform";
	public static final String examCode[] = { "HKAL", "GCEAL", "IAL", "IBDP", "AP", "AUNSWHSC", "BDHSC", "CAMBHSD", "CABCHSD",
											  "CAPE", "EB", "EC-TBRE", "FMB", "FB", "GA", "IS", "AISSCE", "ISCE", "INMHHSCE",
											  "TSB", "T-ATY", "IDUN", "IMO", "STPM", "UEC", "VWO", "NOR", "SCGCEAL", "NQ", "TB" };
	
	public static final String nonLocalWebsite = "http://arr.ust.hk/ust_actoe/credit_overseas.php?selCty=";
	public static final String nonLocalLocation[] = { "Algeria", "Australia", "Austria", "Belgium", "Brunei", "Canada", "China",
													  "Czech+Republic", "Denmark", "Estonia", "Finland", "France", "Germany",
													  "Hungary", "India", "Indonesia", "Ireland", "Israel", "Italy", "Japan",
													  "Korea%2C+Republic+of", "Malaysia", "Mexico", "Netherlands", "New+Zealand",
													  "Norway", "Norwegian", "Philippines", "Poland", "Portugal", "Russian+Federation",
													  "Singapore", "Spain", "Sweden", "Switzerland", "Taiwan", "Thailand",
													  "Turkey", "United+Kingdom", "United+States" };
	public static final String nonLocalCode[][] = {
		{ "X027" }, { "B0529", "B0316", "B0349", "B0352", "B0360" }, { "B0346", "B0498", "B0498" }, { "B0742" }, { "B0766" },
		{ "B0649", "B0071", "B0119", "B0194", "B0566", "B0244", "X0008", "B0279", "B0340", "B0531", "B0396", "B0405", "B0446",
			"B0481", "B0492", "B0504", "B0516" }, { "B0020", "B0085", "B0103", "B0114", "B0204", "B0206", "B0236", "B0247",
			"B0247a", "B0268", "B0271", "B0273", "B0278", "B0295", "B0370", "B0471", "B0508", "B0511", "B0517" }, { "B0606",
			"B0423" }, { "B0392", "B0070", "B0303", "B0656" }, { "X034" }, { "B0525", "X031" }, { "B0090NA", "B0098", "X0009",
			"B0110", "B0592", "B0563", "B0026", "B0151", "B0151AA", "B0151AB", "B0682", "B0682AA", "B0592NA", "B0528", "B0571",
			"B0386" }, { "B0097", "B0097NA", "X016", "B0657", "B0681", "B0986", "B0564", "B0683", "B0307", "B0308", "B0309",
			"B0383", "B0662", "B0505NA", "B0505" }, { "B0073" }, { "B0595" }, { "B0538", "B0743", "B0479" }, { "B0215", "B0390" },
		{ "B0325" }, { "B0658", "B0379" }, { "B0175", "B0527", "B0233", "B0250", "B0284", "B0567", "B0604", "B0714" }, { "B0802",
			"B0099", "B0645", "B0173", "B0174", "B0241", "B0266", "B0296", "B0544", "B0515" }, { "B0521" }, { "B0310" }, { "B0252",
			"X032", "B0751", "B0187", "B0684", "B0367", "B0397", "B0705", "B0495" }, { "B0193", "B0400" }, { "B0028", "B0402" },
		{ "B0991" }, { "B0009" }, { "B0582", "B0506" }, { "X020", "B0661AA", "X028" }, { "X022", "B0762" }, { "X029", "B0207", 
			"B0216", "B0220", "B0280", "B0281" }, { "B0164", "B0164AA", "B0164AB", "B0095", "B0378", "B0455" }, { "B0053", "B0183",
			"B0611", "B0186", "B0254", "B0294", "B0427", "B0663" }, { "B0089", "B0298", "B0655", "B0477" }, { "B0530", "B0208",
			"B0209", "B0210", "B0990", "B0211", "B0212" }, { "B0059" }, { "B0754", "B0605", "B0522" }, { "B0687", "B0061AA",
			"B0925", "B0152", "B0176", "B0353", "B0224", "B0152NA", "B0332", "B0332a", "B0339", "B0348", "B0358", "B0363", "B0532",
			"B0393", "B0535", "B0415", "B0594", "B0533", "B0586", "B0439", "B0441", "B0461", "B0474", "B0478", "B0590" },
		{ "X015", "B0006", "B0032", "B0033", "B0035", "B0047", "B0067", "B0072", "B0083", "B0092", "B0115", "B0116", "B0155",
			"B0167", "B0179", "B0669", "B0191", "B0524", "B0196", "B0727", "B0198", "B0219", "B0222", "B0225", "B0608", "B0803",
			"B0235", "B0243", "B0248", "B0249", "B0255", "B0282", "B0291", "B0292", "B0299", "B0313", "B0638", "B0105", "B0324",
			"B0334", "B0341", "B0362", "B0570", "B0372", "B0407", "B0408", "B0409", "B0410", "B0628", "B0411", "B0412", "B0413",
			"B0420", "B0425", "B0428", "B0432", "B0660", "B0447", "B0448", "B0350", "B0350AA", "B0452", "B0374", "B0467", "B0475",
			"B0475AA", "B0484", "B0485", "B0488", "B0496", "B0502", "B0748" }
	};
	public static final String nonLocalParameter1 = "&selI=";
	public static final String nonLocalParameter2 = "&txtK=&search=y&btn1=+Search+#myform";
	
	private ArrayList<LocalInstitutionCredit> localInstitutionList;
	private ArrayList<ExaminationCredit> examinationList;
	private ArrayList<NonLocalInstitutionCredit> nonLocalInstitutionList;
	private int option;
	
	public CreditTransfer()
	{
		localInstitutionList = new ArrayList<LocalInstitutionCredit>();
		examinationList = new ArrayList<ExaminationCredit>();
		nonLocalInstitutionList = new ArrayList<NonLocalInstitutionCredit>();
		System.out.println("Which type of credit you want to crawl for? (1. Examinations, 2. Local Institutions, 3. Non-Local Institutions)");
		Scanner sc = new Scanner(System.in);
	    this.option = sc.nextInt();
		try
		{
			webCrawling(option);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void webCrawling(int option) throws Exception
	{
		switch(option) {
		case 1: 
			// Crawl examination credit details
			ArrayList<String> examination = new ArrayList<String>();
			ArrayList<String> subject = new ArrayList<String>();
			ArrayList<String> minGrade = new ArrayList<String>();
			ArrayList<String> transferCourseCode = new ArrayList<String>();
			ArrayList<String> dbReferenceNo = new ArrayList<String>();
			
			for (int i = 0; i < examCode.length; i++) {
				URL url = new URL(examWebsite + examCode[i] + examParameter);
				Document doc = Jsoup.parse(url, 3000);
				Element table = doc.select("table").get(6);
				Elements rows = table.select("tr");
				Elements div = doc.select("div.brown");
				String exam = div.text();
				
				for (int j = 1; j < rows.size(); j++) {
					Elements details = rows.get(j).select("td");
					if (!details.get(0).text().equals("No record(s) found."))
					{
						String sub = details.get(0).text();
						String minScore = details.get(1).text();
						String USTCode = details.get(2).text();
						String refNo = details.get(5).text();
						
						examination.add(exam);
						subject.add(sub);
						minGrade.add(minScore);
						transferCourseCode.add(USTCode);
						dbReferenceNo.add(refNo);
					}
				}
			}
			// display for testing
//			for (int i = 0; i < examination.size(); i++)
//			{
//				examinationList.add(new ExaminationCredit(examination.get(i), subject.get(i), minGrade.get(i), transferCourseCode.get(i), dbReferenceNo.get(i)));
//				System.out.println(examinationList.get(i));
//			}
			break;
		case 2:
			// Crawl local school credit details
			ArrayList<String> institution = new ArrayList<String>();
			ArrayList<String> courseCode = new ArrayList<String>();
			transferCourseCode = new ArrayList<String>();
			ArrayList<String> restriction = new ArrayList<String>();
			dbReferenceNo = new ArrayList<String>();
			
			for (int i = 0; i < localCode.length; i++)
			{
				URL url = new URL(localWebsite + localCode[i] + localParameter);
				Document doc = Jsoup.parse(url, 3000);
				Element table = doc.select("table").get(5);
				Element tableLink = doc.select("table").get(6);
				Elements rows = table.select("tr");
				Elements rowLink = tableLink.select("tr");
				Elements content = rowLink.select("td");
				Elements link = content.get(2).select("a");
				String absHref = link.attr("abs:href");
				Elements div = doc.select("div.brown");
				String school = div.text();
				
				for (int j = 2; j < rows.size(); j++)
				{
					Elements details = rows.get(j).select("td");
					if (!details.get(0).text().equals("No record(s) found."))
					{
						String code = details.get(1).text();
						String USTCode = details.get(2).text();
						String restrict = details.get(4).text();
						String refNo = details.get(6).text();
						if (restrict.equals("EXP"))
							restrict = "Expired";
						else if (restrict.equals("--"))
							restrict = "No Restriction";
						
						institution.add(school);
						courseCode.add(code);
						transferCourseCode.add(USTCode);
						restriction.add(restrict);
						dbReferenceNo.add(refNo);
					}
				}
				
				while(!absHref.isEmpty()){
					url = new URL(absHref);
					doc = Jsoup.parse(url, 3000);
					table = doc.select("table").get(5);
					tableLink = doc.select("table").get(6);
					rows = table.select("tr");
					rowLink = tableLink.select("tr");
					content = rowLink.select("td");
					link = content.get(2).select("a");
					absHref = link.attr("abs:href");
					
					for (int j = 2; j < rows.size(); j++)
					{
						Elements details = rows.get(j).select("td");
						if (!details.get(0).text().equals("No record(s) found."))
						{
							String code = details.get(1).text();
							String USTCode = details.get(2).text();
							String restrict = details.get(4).text();
							String refNo = details.get(6).text();
							if (restrict.equals("EXP"))
								restrict = "Expired";
							else if (restrict.equals("--"))
								restrict = "No Restriction";
							
							institution.add(school);
							courseCode.add(code);
							transferCourseCode.add(USTCode);
							restriction.add(restrict);
							dbReferenceNo.add(refNo);
						}
					}
				}
			}
			
			// display for testing
//			for (int i = 0; i < institution.size(); i++)
//			{
//				localInstitutionList.add(new LocalInstitutionCredit(institution.get(i), courseCode.get(i), transferCourseCode.get(i), restriction.get(i), dbReferenceNo.get(i)));
//				System.out.println(localInstitutionList.get(i));
//			}
			break;
		case 3:
			// Crawl non-local school credit details
			ArrayList<String> country = new ArrayList<String>();
			institution = new ArrayList<String>();
			courseCode = new ArrayList<String>();
			transferCourseCode = new ArrayList<String>();
			restriction = new ArrayList<String>();
			dbReferenceNo = new ArrayList<String>();
			
			for (int i = 0; i < nonLocalLocation.length; i++)
			{
				for (int j = 0; j < nonLocalCode[i].length; j++) {
					URL url = new URL(nonLocalWebsite + nonLocalLocation[i] + nonLocalParameter1 + nonLocalCode[i][j] + nonLocalParameter2);
					Document doc = Jsoup.parse(url, 3000);
					Element table = doc.select("table").get(5);
					Element tableLink = doc.select("table").get(6);
					Elements rows = table.select("tr");
					Elements rowLink = tableLink.select("tr");
					Elements content = rowLink.select("td");
					Elements link = content.get(2).select("a.link1");
					String absHref = link.attr("abs:href");
					Elements div = doc.select("div.brown");
					String school = div.text();
					String location = nonLocalLocation[i];
					if (location.equals("Korea%2C+Republic+of")) {
						location = "Republic of Korea";
					}
					else {
						location = location.replace("+", " ");
					}
					
					for (int k = 2; k < rows.size(); k++)
					{
						Elements details = rows.get(k).select("td");
						if (!details.get(0).text().equals("No record(s) found."))
						{
							String code = details.get(1).text();
							String USTCode = details.get(2).text();
							String restrict = details.get(4).text();
							String refNo = details.get(6).text();
							if (restrict.equals("EXP"))
								restrict = "Expired";
							else if (restrict.equals("--"))
								restrict = "No Restriction";
							
							country.add(location);
							institution.add(school);
							courseCode.add(code);
							transferCourseCode.add(USTCode);
							restriction.add(restrict);
							dbReferenceNo.add(refNo);
						}
					}
					
					while(!absHref.isEmpty()) {
						absHref = absHref.replaceAll(" ", "%20");
						url = new URL(absHref);
						doc = Jsoup.parse(url, 3000);
						table = doc.select("table").get(5);
						tableLink = doc.select("table").get(6);
						rows = table.select("tr");
						rowLink = tableLink.select("tr");
						content = rowLink.select("td");
						link = content.get(2).select("a.link1");
						absHref = link.attr("abs:href");
						
						for (int k = 2; k < rows.size(); k++)
						{
							Elements details = rows.get(k).select("td");
							if (!details.get(0).text().equals("No record(s) found."))
							{
								String code = details.get(1).text();
								String USTCode = details.get(2).text();
								String restrict = details.get(4).text();
								String refNo = details.get(6).text();
								if (restrict.equals("EXP"))
									restrict = "Expired";
								else if (restrict.equals("--"))
									restrict = "No Restriction";
								
								country.add(location);
								institution.add(school);
								courseCode.add(code);
								transferCourseCode.add(USTCode);
								restriction.add(restrict);
								dbReferenceNo.add(refNo);
							}
						}
					}
				}
			}
			
			// display for testing
//			for (int i = 0; i < country.size(); i++)
//			{
//				nonLocalInstitutionList.add(new NonLocalInstitutionCredit(country.get(i), institution.get(i), courseCode.get(i), transferCourseCode.get(i), restriction.get(i), dbReferenceNo.get(i)));
//				System.out.println(nonLocalInstitutionList.get(i));
//			}
			break;
		}
	}
	
	public int getOption() { return option; }
	
	public ArrayList<LocalInstitutionCredit> getLocalInstitutionList() { return localInstitutionList; }
	
	public ArrayList<ExaminationCredit> getExaminationList() { return examinationList; }
	
	public ArrayList<NonLocalInstitutionCredit> getNonLocalInstitutionList() { return nonLocalInstitutionList; }
	
	// for testing
//	public static void main(String[] args)
//	{
//		new CreditTransfer();
//	}
}
