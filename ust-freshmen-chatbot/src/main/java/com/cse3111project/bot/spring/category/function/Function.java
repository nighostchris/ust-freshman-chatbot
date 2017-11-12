package com.cse3111project.bot.spring.category.function;

import com.cse3111project.bot.spring.category.Category;
import com.cse3111project.bot.spring.category.function.timetable.TimeTable;

import com.cse3111project.bot.spring.KitchenSinkController;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.bot.spring.boot.annotation.EventMapping;

// import java.io.ObjectInputStream;
// import java.io.FileInputStream;
// import java.io.ObjectOutputStream;
// import java.io.FileOutputStream;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.nio.file.FileAlreadyExistsException;
import java.io.IOException;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

@LineMessageHandler
public abstract class Function extends Category {
    public static final String QUERY_KEYWORD[] = TimeTable.FUNCTION_KEYWORD;

    protected Path saveRootDir = FileSystems.getDefault().getPath("function");

    // just grab the current controller every time before launching sub-applications
    private static KitchenSinkController controller = null;

    private MessageEvent<TextMessageContent> currentEvent = null;
    private String userMessage = null;
    private String prevReplyToken = null;

    public static Category query(final ArrayList<String> extractedResults){
        // ArrayList<String> timetableKeywords = new ArrayList<>();

        // there is only one subcategory in Function
        // => no need to categorize temporarily
        // for (String result : extractedResults){
        //     for (String timetableKeyword : TimeTable.FUNCTION_KEYWORD)
        //         if (result.equals(timetableKeyword))
        //             timetableKeywords.add(result);
        // }

        return new TimeTable();
    }

    public static void equipController(KitchenSinkController controller){
        Function.controller = controller;
    }

    // user text message would be handled here
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        currentEvent = event;
        userMessage = event.getMessage().getText();  // obtain user message
        // a reply token would be issued every time when user sends a message (triggering this event)
        prevReplyToken = event.getReplyToken();
    }

    protected boolean userHasReplied(){
        return currentEvent.getReplyToken() != prevReplyToken;
    }

    protected void replyText(String reply){
        if (reply == null)  // should NOT be null
            reply = "";
        controller.replyText(currentEvent.getReplyToken(), reply);
    }

    protected String getUserMessage() { return this.userMessage; }

    // restore the previous state of user settings from a local file
    protected abstract void read();

    // save current state of user settings / configurations on applications, e.g. timetable
    protected abstract void save();

    // create save root directory if not exists
    protected void createSaveDir() throws FileAlreadyExistsException, IOException {
        try {
            if (!Files.exists(saveRootDir))
                Files.createDirectory(saveRootDir);
        }
        catch (FileAlreadyExistsException e) {
            Utilities.errorLog(saveRootDir.toString() + " directory already exists", e);
            throw e;
        }
        catch (IOException e) {
            Utilities.errorLog("I/O error occurred while creating " + saveRootDir.toString(), e);
            throw e;
        }
    }

    // entry point for every function
    public abstract void run();
}
