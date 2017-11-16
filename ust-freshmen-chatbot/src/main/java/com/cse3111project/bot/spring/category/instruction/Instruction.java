package com.cse3111project.bot.spring.category.instruction;

import com.cse3111project.bot.spring.category.Category;

public class Instruction
{
	private String reply;
	
	private static final String DIRECTORY_QUERY = "Tips on HKUST Staff Directory Enquiry\n"
												+ "You can search details of any valid staff available in UST!\n"
												+ "e.g.1 Can I know where is the office of Professor Li?\n"
												+ "e.g.2 Professor Kim Sunghun\n";
	
	private static final String KMB_QUERY = "Tips on KMB ETA Enquiry\n"
										  + "You can get ETA of 91 and 91m in UST by our bot!\n"
										  + "e.g.1 Can I know ETA of 91m at south gate?\n"
										  + "e.g.2 When will 91 arrive at north gate?\n";
}