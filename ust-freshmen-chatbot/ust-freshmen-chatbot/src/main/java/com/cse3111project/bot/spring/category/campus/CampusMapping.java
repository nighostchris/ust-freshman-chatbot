package com.cse3111project.bot.spring.category.campus;

import java.io.InputStream;
import java.io.FileNotFoundException;

import java.util.Scanner;

class CampusMapping 
{
	private String floorName[] = { "LG7", "LG5", "LG4", "LG3", "LG1", "G", "1", "2", "3", "4", "5", "6", "7", 
								   "CYTG", "CYTUG", "CYT1", "CYT2", "CYT3", "CYT4", "CYT5", "CYT6", "CYT7",
								   "NABG", "NAB1", "NAB2", "NAB3", "NAB4", "NAB5", "NAB6", "NAB7",
								   "IASG", "IAS1", "IAS2", "IAS3", "IAS4", "IAS5" };
	
	private int timeMatrix[][];

    private static final String TIME_MATRIX = "/static/campus/timeMatrix.txt";
	
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
	
	public int getTimeFromMatrix(int row, int column) { return timeMatrix[row][column]; }
}
