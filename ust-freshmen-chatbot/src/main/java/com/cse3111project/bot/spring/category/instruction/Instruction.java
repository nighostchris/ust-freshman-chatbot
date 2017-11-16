package com.cse3111project.bot.spring.category.instruction;

import java.util.ArrayList;
import com.cse3111project.bot.spring.category.Category;

public abstract class Instruction extends Category
{
	private static String reply;
	
	public static final String QUERY_KEYWORD[] = { "/help", "/dir", "/kmb" };
	
	private static final String HELP_QUERY = "Commands available for checking different\n"
										   + "features of this chatbot:\n"
										   + "/dir Directory Enquiry\n"
										   + "/kmb KMB ETA Enquiry";
	
	private static final String DIRECTORY_QUERY = "Tips on HKUST Staff Directory Enquiry\n"
												+ "You can search details of any valid staff available in UST!\n"
												+ "e.g.1 Can I know where is the office of Professor Li?\n"
												+ "e.g.2 Professor Kim Sunghun";
	
	private static final String KMB_QUERY = "Tips on KMB ETA Enquiry\n"
										  + "You can get ETA of 91 and 91m in UST by our bot!\n"
										  + "e.g.1 Can I know ETA of 91m at south gate?\n"
										  + "e.g.2 When will 91 arrive at north gate?";
	
	public static Category analyze(final ArrayList<String> extractedResults)
    {
        for (String result : extractedResults)
        {
            if (result.contains(QUERY_KEYWORD[0]))
                reply = HELP_QUERY;
            else if (result.contains(QUERY_KEYWORD[1]))
            	reply = DIRECTORY_QUERY;
            else
            	reply = KMB_QUERY;
        }
        return reply;
    }
}