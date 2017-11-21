package com.cse3111project.bot.spring.category.function.timetable;

import com.cse3111project.bot.spring.utility.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.cse3111project.bot.spring.exception.InvalidDateException;
import com.cse3111project.bot.spring.exception.InvalidTimeslotException;

// database structure
// username month day activityName startTime endTime
public class Timetable 
{
    public static final String FUNCTION_KEYWORD[] = { "timetable", "time table", "time manager",
                                                      "time schedule", "schedule" };

    public static Category analyze(final ArrayList<String> extractedResults)
    {
    	/*
    	 * Chris wants to eat dinner from 6 to 9 on November 22.
			Input parameter to analyze
			Chris <- username
			eat dinner <- event nme
			from 6 to 9 <- get the time
			November 22 <- Date
    	 */
    	String originalText = extractedResults.get(0);
    	// get the username for checking
    	String username = originalText.substring(0, originalText.indexOf(' '));
    	// get the activity name
    	String eventName = originalText.substring(originalText.indexOf("to") + 3, originalText.indexOf("from") - 1);
    }
    
    
    // option 1
    private void addEvent()
    {
        int month = 0; int day = 0;
        int startTime = 0; int endTime = 0;
        String activityName = null;
        try {
            replyText("Enter month and day for event (Separated by Space):");
            while (!userHasReplied());
            String dateParts[] = getUserMessage().split(" ");
            if (dateParts.length != 2)
                throw new IllegalArgumentException("2 arguments should be given:\n<month> <day>");

            // try to convert month, day string into Integer
            // throw NumberFormatException if not convertible
            month = new Integer(dateParts[0]).intValue();
            day = new Integer(dateParts[1]).intValue();
            Date.checkValidity(month, day);  // check whether the user inputed date is valid

            replyText("Enter event start and end time in hour (0-23, Separated by Space): ");
            while (!userHasReplied());
            String timeslotParts[] = getUserMessage().split(" ");
            if (timeslotParts.length != 2)
                throw new IllegalArgumentException("2 arguments should be given:\n<starting time> <ending time>");

            // try to convert startTime, endTime string into Integer
            // throw NumberFormatException if not convertible
            startTime = new Integer(timeslotParts[0]).intValue();
            endTime = new Integer(timeslotParts[1]).intValue();
            Timeslot.checkValidity(startTime, endTime);  // check whether the timeslot is valid

            replyText("Enter activity name:");
            while (!userHasReplied());
            activityName = getUserMessage();

            user.addEventDate(month, day);
            Date date = user.searchDate(month, day);
            Timeslot timeslot = new Timeslot(startTime, endTime);
            Activity newActivity = new Activity(activityName, timeslot);
            if (date.addActivity(newActivity))  // if successfully add activity
            {
                replyText("Event Added.");
                Collections.sort(user.getDateList());
                Collections.sort(date.getActivity());
            }
            else
                replyText("Time Conflict occurs");
        }
        catch (NumberFormatException e) {
            replyText("Entered invalid date/timeslot format. Please try again");
        }
        catch (IllegalArgumentException | InvalidDateException | InvalidTimeslotException e) {
            replyText(e.getMessage());
        }
    }

    // option 2
    private void removeEvent(){
        int month = 0; int day = 0;
        String activityName = null;
        try {
            if (user.hasEmptySchedule()){
                replyText("The current time schedule is empty. Nothing can be deleted");
                return;
            }

            replyText("Enter month and day for removal:");
            while (!userHasReplied());
            String[] dateParts = getUserMessage().split(" ");
            if (dateParts.length != 2)
                throw new IllegalArgumentException("2 arguments should be given:\n<month> <day>");

            // try to convert month, day string into Integer
            // throw NumberFormatException if not convertible
            month = new Integer(dateParts[0]).intValue();
            day = new Integer(dateParts[1]).intValue();
            Date.checkValidity(month, day);  // check whether the user inputed date is valid

            Date date = user.searchDate(month, day);
            if (date == null)
                replyText("Invalid Removal. No event on that day");
            else
            {
                if (date.getActivity().size() == 1){  // there is only one activity to remove
                    activityName = date.getActivity().get(0).getName();
                    date.removeActivity(activityName);
                    replyText("Event deleted.");
                    // remove the empty date as well
                    user.removeDate(date.getMonth(), date.getDay());
                }
                else {
                    replyText("Enter activity name:");
                    while (!userHasReplied());
                    activityName = getUserMessage();
                    if (date.removeActivity(activityName))
                        replyText("Event deleted.");
                    else
                        replyText("No event with name " + activityName);
                }
            }
        }
        catch (NumberFormatException e) {
            replyText("Entered invalid date/timeslot format. Please try again");
        }
        catch (IllegalArgumentException | InvalidDateException e) {
            replyText(e.getMessage());
        }
    }

    // option 3
    private void displayEventsAtParticularDate(){
        int month = 0; int day = 0;
        try {
            replyText("Enter month and day for display (Separated by Space):");
            while (!userHasReplied());
            String dateParts[] = this.getUserMessage().split(" ");
            if (dateParts.length != 2)
                throw new IllegalArgumentException("2 arguments should be given:\n<month> <day>");

            // try to convert month, day string into Integer
            // throw NumberFormatException if not convertible
            month = new Integer(dateParts[0]).intValue();
            day = new Integer(dateParts[1]).intValue();
            Date.checkValidity(month, day);

            Date date = user.searchDate(month, day);
            if (date == null)
                replyText("No event on " + month + "/" + day);
            else
                replyText(date.toString());
        }
        catch (NumberFormatException e) {
            replyText("Entered invalid date format. Please try again");
        }
        catch (IllegalArgumentException | InvalidDateException e) {
            replyText(e.getMessage());
        }
    }

    // option 4
    private void displayAllEvents(){
        replyText(user.toString());
    }

    // entry point for TimeTable function
    @Override
	public void run(){
		// TimeManager tm = new TimeManager();
        this.read();  // attempt to read the saved time schedule if exists
        if (user == null){  // no save / error occurred while reading save
            replyText("Enter New Username (for later retrieving the timetable): ");
            // wait for user reply
            while (!userHasReplied()); 
            user = new People(getUserMessage());
        }

		// user = tm.searchPeople(username);
		// if (user == null)
		// {
		// 	user = new People(username);
		// 	tm.getPeopleList().add(user);
		// }

        String input = "";
        StringBuilder menuBuilder = new StringBuilder().append("\n1) Add event")
                                                       .append("2) Remove event")
                                                       .append("3) Display event for particular date")
                                                       .append("4) Display all event")
                                                       .append("Enter q to save/leave");
        while (input != "q")
		{
            replyText(menuBuilder.toString());
            while (!userHasReplied()); input = getUserMessage();
			switch (input)
			{
				case "1":
                    this.addEvent();
					break;
				
				case "2":
                    this.removeEvent();
					break;
					
				case "3":
                    this.displayEventsAtParticularDate();
					break;
					
				case "4":
					this.displayAllEvents();
					break;

                case "Q": case "q":
                    input = "q";
                    if (!user.hasEmptySchedule())
                        this.askSave();
                    break;

				default:
                    replyText("Unknown option");
					break;
			}
		}
	}
}
