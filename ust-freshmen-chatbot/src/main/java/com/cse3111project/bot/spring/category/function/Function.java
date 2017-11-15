package com.cse3111project.bot.spring.category.function;

import com.cse3111project.bot.spring.category.Category;
import com.cse3111project.bot.spring.category.function.timetable.TimeTable;

import com.cse3111project.bot.spring.KitchenSinkController;

// import org.springframework.beans.factory.annotation.Autowired;

// import com.linecorp.bot.client.LineMessagingService;
// import com.linecorp.bot.model.event.MessageEvent;
// import com.linecorp.bot.model.event.message.TextMessageContent;
// import com.linecorp.bot.model.message.Message;
// import com.linecorp.bot.model.message.TextMessage;
// import com.linecorp.bot.model.ReplyMessage;
// import com.linecorp.bot.model.response.BotApiResponse;
// import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
// import com.linecorp.bot.spring.boot.annotation.EventMapping;

// import java.util.concurrent.ExecutionException;

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

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import lombok.NonNull;

// @LineMessageHandler
public abstract class Function extends Category {
    // @Autowired
    // private LineMessagingService client;

    public static final String QUERY_KEYWORD[] = TimeTable.FUNCTION_KEYWORD;

    protected Path saveRootDir = FileSystems.getDefault().getPath("function");

    // switch the controller to handle this function event
    private KitchenSinkController controller = new KitchenSinkController(this);

    // private MessageEvent<TextMessageContent> currentEvent = null;
    private String userMessage = null;
    private String replyToken = null;
    // private String prevReplyToken = null;

    public static Category analyze(final ArrayList<String> extractedResults){
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

    // user text message would be handled here
    // @EventMapping
    // public void handleTextMessageEvent(MessageEvent<TextMessageContent> event){
    //     currentEvent = event;
    //     userMessage = event.getMessage().getText();  // obtain user message
    //     // a reply token would be issued every time when user sends a message (triggering this event)
    //     prevReplyToken = event.getReplyToken();
    // }

    // protected boolean userHasReplied(){
    //     return currentEvent.getReplyToken() != prevReplyToken;
    // }

    protected boolean userHasReplied(){
        return replyToken != null;
    }

    // protected void replyText(@NonNull String reply){
    //     if (reply.length() > 1000)  // truncate the reply if it is too long
    //         reply = reply.substring(0, 1000 - 2) + "..";

    //     ReplyMessage replyMessage = new ReplyMessage(currentEvent.getReplyToken(), 
    //                                                  Collections.singletonList(new TextMessage(reply)));

    //     try {
    //         BotApiResponse apiResponse = client.replyMessage(replyMessage).execute().body();
    //     }
    //     catch (IOException e) {
    //         Utilities.errorLog("I/O error occurred while trying to reply", e);
    //     }
    // }

    protected void replyText(@NonNull String reply){
        controller.replyText(replyToken, reply);
        // after reply, reset userMessage and replyToken
        userMessage = null; replyToken = null;
    }

    // redirect user message from KitchenSinkController
    public void retrieveUserMessage(final String userMessage) { this.userMessage = userMessage; }

    public void retrieveReplyToken(final @NonNull String replyToken) { this.replyToken = replyToken; }

    protected String getUserMessage() { return userMessage; }

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
