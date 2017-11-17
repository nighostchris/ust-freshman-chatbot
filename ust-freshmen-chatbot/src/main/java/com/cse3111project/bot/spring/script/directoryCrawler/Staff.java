package com.cse3111project.bot.spring.directoryCrawler;

public class Staff
{
    private String name;
    private String position;
    private String officeLocation;
    private String email;

    public Staff()
    {
        this.name = "";
        this.position = "";
        this.officeLocation = "";
        this.email = "";
    }

    public Staff(String name, String position, String officeLocation, String email)
    {
        this.name = name;
        this.position = position;
        this.officeLocation = officeLocation;
        this.email = email;
    }

    public String getName() { return name; }
    public String getPosition() { return position; }
    public String getOfficeLocation() { return officeLocation; }
    public String getEmail() { return email; }
}