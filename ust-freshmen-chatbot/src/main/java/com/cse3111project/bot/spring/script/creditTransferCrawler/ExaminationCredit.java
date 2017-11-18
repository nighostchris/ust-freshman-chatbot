package com.cse3111project.bot.spring.script.creditTransferCrawler;

public class ExaminationCredit {
	private String examination;
	private String subject;
	private String minGrade;
	private String transferCourseCode;
	private String dbReferenceNo;
	
	public ExaminationCredit()
	{
		this.examination = "";
		this.subject = "";
		this.minGrade = "";
		this.transferCourseCode = "";
		this.dbReferenceNo = "";
	}
	
	public ExaminationCredit(String examination, String subject, String minGrade, String transferCourseCode, String dbReferenceNo)
	{
		this.examination = examination;
		this.subject = subject;
		this.minGrade = minGrade;
		this.transferCourseCode = transferCourseCode;
		this.dbReferenceNo = dbReferenceNo;
	}
	
	public String getExamination() { return examination; }
	
	public String getSubject() { return subject; }
	
	public String getMinGrade() { return minGrade; }
	
	public String getTransferCourseCode() { return transferCourseCode; }
	
	public String getDBReferenceNo() { return dbReferenceNo; }
	
	@Override
	public String toString()
	{
		return examination + " " + subject + " " + minGrade + " " + transferCourseCode + " " + dbReferenceNo;
	}
}
