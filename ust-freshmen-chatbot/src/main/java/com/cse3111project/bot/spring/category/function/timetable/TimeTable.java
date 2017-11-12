package com.cse3111project.bot.spring.category.function.timetable;

import com.cse3111project.bot.spring.category.function.Function;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.NotSerializableException;
import java.io.IOException;

import java.util.Collections;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.InvalidDateException;
import com.cse3111project.bot.spring.exception.InvalidTimeslotException;

public class TimeTable extends Function {
    public static final String FUNCTION_KEYWORD[] = { "timetable", "time table", "time manager",
                                                      "time schedule", "schedule" };

    private People user = null;

    private Path saveDir = saveRootDir.resolve("timetable");

    // save filename
    // ** currently only one save slot is provided **
    private static final String SAVEFILE = "schedule";

    // create save directory for TimeTable function
    @Override
    protected void createSaveDir() throws FileAlreadyExistsException, IOException {
        super.createSaveDir();

        try {
            if (!Files.exists(saveDir))
                Files.createDirectory(saveDir);
        }
        catch (FileAlreadyExistsException e) {
            Utilities.errorLog(saveDir.toString() + " directory already exists", e);
            throw e;
        }
        catch (IOException e) {
            Utilities.errorLog("I/O error occurred while creating " + saveDir.toString(), e);
            throw e;
        }
    }

    // read the saved timetable
    @Override
    protected void read(){
        Path saveFilePath = saveDir.resolve(SAVEFILE);
        if (!Files.exists(saveFilePath))
            return;

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File saveFile = saveFilePath.toFile();
            fis = new FileInputStream(saveFile);
            ois = new ObjectInputStream(fis);

            this.user = (People) ois.readObject();
        }
        catch (ClassNotFoundException e) {  // should not happen
            Utilities.errorLog("Deserialized class not found", e);
            replyText("System error occurred. Abort reading.");
        }
        catch (IOException e) {
            Utilities.errorLog("I/O error occurred while reading save file", e);
            replyText("Error occurred while reading the save. Abort reading.");
        }
        finally {
            try {
                if (fis != null)
                    fis.close();
                if (ois != null)
                    ois.close();
            }
            catch (IOException e) {
                Utilities.errorLog("Error occurred while closing stream", e);
            }
        }
    }

    // save current timetable
    // ** currently only one save slot is provided **
    @Override
    protected void save(){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            // check if saveDir exists. If not, create one
            this.createSaveDir();

            // overwrite the old schedule if exists **
            File saveFile = saveDir.resolve(SAVEFILE).toFile();

            fos = new FileOutputStream(saveFile);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(user);

            replyText("Saving Complete");
        }
        catch (FileAlreadyExistsException e) {
            replyText("Error occurred while creating cache. Saving operation abort.");
        }
        catch (NotSerializableException e) {  // should not occur in Release
            Utilities.errorLog("Unable to serialize a non-serializable class", e);
        }
        catch (IOException e) {
            Utilities.errorLog("Error occurred while writing save", e);
            replyText("Error occurred while writing save. Saving operation abort.");
        }
        finally {
            try {
                if (fos != null)
                    fos.close();
                if (oos != null)
                    oos.close();
            }
            catch (IOException e) {
                Utilities.errorLog("Error occurred while closing stream", e);
            }
        }
    }

    // before exit, ask whether to save the current time table
    private void askSave(){
        loop: while (true) {
            replyText("Save the current timetable?[Y/N]\n\n" + user.toString());
            while (!userHasReplied());
            String userResponse = super.getUserMessage();
            switch (userResponse) {
                case "Y": case "y": case "Yes": case "yes":
                    this.save();
                    break loop;

                case "N": case "n": case "No": case "no":
                    break loop;

                default:
                    replyText("Unknown option");
                    break;
            }
        }
    }

    // option 1
    private void addEvent(){
        int month = 0; int day = 0;
        int startTime = 0; int endTime = 0;
        String activityName = null;
        try {
            replyText("Enter month and day for event (Separated by Space):");
            while (!userHasReplied());
            String dateParts[] = super.getUserMessage().split(" ");
            if (dateParts.length != 2)
                throw new IllegalArgumentException("2 arguments should be given:\n<month> <day>");

            // try to convert month, day string into Integer
            // throw NumberFormatException if not convertible
            month = new Integer(dateParts[0]).intValue();
            day = new Integer(dateParts[1]).intValue();
            Date.checkValidity(month, day);  // check whether the user inputed date is valid

            replyText("Enter event start and end time in hour (0-23, Separated by Space): ");
            while (!userHasReplied());
            String timeslotParts[] = super.getUserMessage().split(" ");
            if (timeslotParts.length != 2)
                throw new IllegalArgumentException("2 arguments should be given:\n<starting time> <ending time>");

            // try to convert startTime, endTime string into Integer
            // throw NumberFormatException if not convertible
            startTime = new Integer(timeslotParts[0]).intValue();
            endTime = new Integer(timeslotParts[1]).intValue();
            Timeslot.checkValidity(startTime, endTime);  // check whether the timeslot is valid

            replyText("Enter activity name:");
            while (!userHasReplied());
            activityName = super.getUserMessage();

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
            String[] dateParts = super.getUserMessage().split(" ");
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
                    activityName = super.getUserMessage();
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
        replyText("testing reply");
		// TimeManager tm = new TimeManager();
        this.read();  // attempt to read the saved time schedule if exists
        if (user == null){  // no save / error occurred while reading save
            replyText("Enter New Username (for later retrieving the timetable): ");
            // wait for user reply
            while (!userHasReplied()); 
            user = new People(super.getUserMessage());
        }

		// user = tm.searchPeople(username);
		// if (user == null)
		// {
		// 	user = new People(username);
		// 	tm.getPeopleList().add(user);
		// }

        String input = "";
        while (input != "q")
		{
            StringBuilder replyBuilder = new StringBuilder().append("\n1) Add event")
                                                            .append("2) Remove event")
                                                            .append("3) Display event for particular date")
                                                            .append("4) Display all event")
                                                            .append("Enter q to save/leave");
            replyText(replyBuilder.toString());
            while (!userHasReplied()); input = super.getUserMessage();
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
