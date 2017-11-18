package com.cse3111project.bot.spring.directoryCrawler;

import java.util.ArrayList;

public class Department
{
    private String name;
    private ArrayList<Staff> staffList;

    public Department()
    {
        this.name = "";
        staffList = new ArrayList<Staff>();
    }

    public Department(String name, ArrayList<Staff> staffList)
    {
        this.name = name;
        this.staffList = staffList;
    }

    /** return Name of this department */
    public String getName() { return name; }

    /** return Arraylist of Staffs */
    public ArrayList<Staff> getStaffList() { return staffList; }

    /** return a Staff object if the staff is found in department, return empty object if not found */
    public Staff getStaff(String staffName)
    {
    	Staff newStaff = new Staff();
        for (Staff staff : staffList)
            if (staff.getName().contains(staffName))
                return staff;
        return newStaff;
    }
    
    public void addStaff(String name, String position, String officeLocation, String email)
    {
    	staffList.add(new Staff(name, position, officeLocation, email));
    }
}