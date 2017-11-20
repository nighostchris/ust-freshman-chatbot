package com.cse3111project.bot.spring.category.instruction;

import java.util.ArrayList;
import com.cse3111project.bot.spring.category.Category;

/**
 * Instruction class inherits from Category class and acts as the main controller for providing various hints to user
 * about how to use the chatbot.
 * @version 1.0
 */
public class Instruction extends Category
{
	private String reply;
	
	public static final String QUERY_KEYWORD[] = { "/help", "/dir", "/kmb", "/minibus", "/society", "/campus", "/facb" };
	
	private static final String HELP_QUERY = "Commands available for checking guidelines on\n"
										   + "different features of this chatbot:\n"
										   + "/dir  Directory Enquiry\n"
										   + "/kmb  KMB ETA Enquiry\n"
										   + "/minibus  Minibus ETA Enquiry\n"
										   + "/society  Societies Enquiry\n"
										   + "/campus  Campus ETA Enquiry\n"
										   + "/facb  Facilities Booking Website Enquiry";
	
	private static final String DIRECTORY_QUERY = "Tips on HKUST Staff Directory Enquiry\n"
												+ "You can search details of any valid staff available in UST!\n"
												+ "e.g.1 Can I know where is the office of Professor Li?\n"
												+ "e.g.2 Professor Kim Sunghun";
	
	private static final String KMB_QUERY = "Tips on KMB ETA Enquiry\n"
										  + "You can get ETA of 91 and 91m in UST by our bot!\n"
										  + "e.g.1 Can I know ETA of 91m at south gate?\n"
										  + "e.g.2 When will 91 arrive at north gate?";
	
	private static final String MINIBUS_QUERY = "Tips on Minibus ETA Enquiry\n"
											  + "You can get ETA of minibus in UST by our bot!\n"
											  + "e.g.1 What is the arrival time of minibus 11?\n"
											  + "e.g.2 11 Minibus arrival time please?";
	
	private static final String SOCIETY_QUERY = "Tips on HKUST Societies Enquiry\n"
											  + "You can search all the available societies in UST!\n"
											  + "e.g.1 Where is the webpage of film society?\n"
											  + "e.g.2 Where could I get info on UST Soc?";
	
	private static final String CAMPUS_QUERY = "Tips on UST Campus ETA Enquiry\n"
											 + "You can search how long it takes from one place to\n"
											 + "another within UST campus by our bot!\n"
											 + "e.g.1 Can I know eta from 4619 to 2407?\n"
											 + "e.g.2 Can I know eta from LTA back entrance to 2504?";
	
	private static final String BOOKING_QUERY = "Tips on Booking facilities in UST\n"
											  + "You can get links to all facilities booking instructions\n"
											  + "available online from our bot!\n"
											  + "e.g.1 Can I know how to book LC?\n"
											  + "e.g.2 I want to book Hall 6 common room";
	
	public Instruction()
	{
		reply = "";
	}
	
	/**
	 * Constructor of Innstruction class, which set the reply to LINE client as the input parameter.
	 * @param reply the hints for helping users to use this chatbot as a String.
	 */
	public Instruction(String reply)
	{
		this.reply = reply;
	}
	
	/**
	 * Getter method of the instance variable reply in Instruction class.
	 * @return String 
	 */
	public String getReply() { return reply; }
	
	/**
	 * This method will analyze the user-query and determine what hints the user is asking for, and return
	 * the corresponding reply.
	 * @param extractedResults Only parameter of the method, which is the processed user-query keywords.
	 * @return Category
	 */
	public static Category analyze(final ArrayList<String> extractedResults)
    {
		String reply = "";
        for (String result : extractedResults)
        {
            if (result.equals(QUERY_KEYWORD[0]))
                reply = HELP_QUERY;
            else if (result.equals(QUERY_KEYWORD[1]))
            	reply = DIRECTORY_QUERY;
            else if (result.equals(QUERY_KEYWORD[2]))
            	reply = KMB_QUERY;
            else if (result.equals(QUERY_KEYWORD[3]))
                reply = MINIBUS_QUERY;
            else if (result.equals(QUERY_KEYWORD[4]))
            	reply = SOCIETY_QUERY;
            else if (result.equals(QUERY_KEYWORD[5]))
            	reply = CAMPUS_QUERY;
            else
            	reply = BOOKING_QUERY;
        }
        return new Instruction(reply);
    }
}