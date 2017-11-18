package com.cse3111project.bot.spring.script.creditTransferCrawler;

public class LocalSchoolCredit 
{
	private String courseCode;
	private String transferCourseCode;
	private String restriction;
	
	public LocalSchoolCredit()
	{
		this.courseCode = "";
		this.transferCourseCode = "";
		this.restriction = "";
	}
	
	public LocalSchoolCredit(String courseCode, String transferCourseCode, String restriction)
	{
		this.courseCode = courseCode;
		this.transferCourseCode = transferCourseCode;
		this.restriction = restriction;
	}
	
	public String getCourseCode() { return courseCode; }
	
	public String getTransferCourseCode() { return transferCourseCode; }
	
	public String getRestriction() { return restriction; }
	
	@Override
	public String toString()
	{
		return courseCode + " " + transferCourseCode + " " + restriction;
	}
}
