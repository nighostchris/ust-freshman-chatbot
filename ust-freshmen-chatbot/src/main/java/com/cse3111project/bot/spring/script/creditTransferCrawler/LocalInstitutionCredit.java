package com.cse3111project.bot.spring.script.creditTransferCrawler;
public class LocalInstitutionCredit 
{
	private String institution;
	private String courseCode;
	private String transferCourseCode;
	private String restriction;
	private String dbReferenceNo;
	
	public LocalInstitutionCredit()
	{
		this.institution = "";
		this.courseCode = "";
		this.transferCourseCode = "";
		this.restriction = "";
		this.dbReferenceNo = "";
	}
	
	public LocalInstitutionCredit(String institution, String courseCode, String transferCourseCode, String restriction, String dbReferenceNo)
	{
		this.institution = institution;
		this.courseCode = courseCode;
		this.transferCourseCode = transferCourseCode;
		this.restriction = restriction;
		this.dbReferenceNo = dbReferenceNo;
	}
	
	public String getInstitution() { return institution; }
	
	public String getCourseCode() { return courseCode; }
	
	public String getTransferCourseCode() { return transferCourseCode; }
	
	public String getRestriction() { return restriction; }
	
	public String getDBReferenceNo() { return dbReferenceNo; }
	
	@Override
	public String toString()
	{
		return institution + " " + courseCode + " " + transferCourseCode + " " + restriction + " " + dbReferenceNo;
	}
}
