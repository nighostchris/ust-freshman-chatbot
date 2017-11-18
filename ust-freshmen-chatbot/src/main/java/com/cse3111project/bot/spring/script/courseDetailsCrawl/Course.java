package com.cse3111project.bot.spring.script.courseDetailsCrawl;

public class Course 
{
	private String courseCode;
	private String courseName;
	private String credit;
	// note that the prerequisite, exclusion and corequisite are all the related courses combined into a string
	// to check if a course is inside either of the 3 criteria
	// one need to use example: prerequisite.contains(courseCode)
	private String prerequisite;
	private String exclusion;
	private String corequisite;
	
	public Course()
	{
		this.courseCode = "";
		this.courseName = "";
		this.credit = "";
		this.prerequisite = "";
		this.exclusion = "";
		this.corequisite = "";
	}
	
	public Course(String courseCode, String courseName, String credit, String prerequisite, String exclusion, String corequisite)
	{
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.credit = credit;
		this.prerequisite = prerequisite;
		this.exclusion = exclusion;
		this.corequisite = corequisite;
	}
	
	public String getCourseCode() { return courseCode; }
	
	public String getCourseName() { return courseName; }
	
	public String getCredit() { return credit; }
	
	public String getPrerequisite() { return prerequisite; }
	
	public String getExclusion() { return exclusion; }
	
	public String getCoRequisite() { return corequisite; }
	
	@Override
	public String toString()
	{
		return courseCode + " " + courseName + " " + credit + "\nPrerequsite: " + prerequisite + "\nExclusion: " + exclusion + "\nCorequisite: " + corequisite;
	}
}
