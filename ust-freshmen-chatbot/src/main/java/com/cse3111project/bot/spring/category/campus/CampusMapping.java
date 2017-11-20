package com.cse3111project.bot.spring.category.campus;

import java.io.InputStream;
import java.io.FileNotFoundException;

import java.util.Scanner;

/**
 * The CampusMapping Class store all floor and building details of UST Campus and handle time estimation adjustment
 * for the first draft. It is included in CampusETA Class as instance variable.
 * @version 1.0
 */
class CampusMapping 
{
	private String floorName[] = { "LG7", "LG5", "LG4", "LG3", "LG1", "G", "1", "2", "3", "4", "5", "6", "7", 
								   "CYTG", "CYTUG", "CYT1", "CYT2", "CYT3", "CYT4", "CYT5", "CYT6", "CYT7",
								   "NABG", "NAB1", "NAB2", "NAB3", "NAB4", "NAB5", "NAB6", "NAB7",
								   "IASG", "IAS1", "IAS2", "IAS3", "IAS4", "IAS5" };
	
	private int timeMatrix[][];

    private static final String TIME_MATRIX = "/static/campus/timeMatrix.txt";
	
    /**
     * This is constructor for CampusMapping Class. It loads the static database which stores time adjustment for 
     * different floor.
     * @throws FileNotFoundException
     */
	CampusMapping() throws FileNotFoundException
	{
		this.timeMatrix = new int[36][36];

        Scanner sc = null;
		try
		{
            InputStream is = this.getClass().getResourceAsStream(TIME_MATRIX);
            if (is == null)
                throw new FileNotFoundException(TIME_MATRIX + " file not found");

            sc = new Scanner(is);

			for (int i = 0; i < 36; i++)
				for (int j = 0; j < 36; j++) 
					this.timeMatrix[i][j] = sc.nextInt();
		}
        finally {
            if (sc != null)
                sc.close();
        }
	}
	
	/**
	 * This method will calculate the corresponding position of a certain floor in campus on the database.
	 * @param input Only parameter of this method, representing the floor name.
	 * @return int location on database matrix.
	 */
	public int locationOnMatrix(String input)
	{
		int location = 0;
		for (int i = 0; i < floorName.length; i++)
		{
			if (input.equals(floorName[i]))
				return location;
			location++;
		}
		return location;
	}
	
	/**
	 * This method will get the time adjustment base on the database. It will then be used for adjusting the eta
	 * in CampusETA Class. 
	 * @param row First parameter taken by the method, representing the row position in the database.
	 * @param column Second parameter taken by the method, representing the column position in the database.
	 * @return int The time adjustment.
	 */
	public int getTimeFromMatrix(int row, int column) { return timeMatrix[row][column]; }
}
