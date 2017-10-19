package com.cse3111project.bot.spring;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.cse3111project.bot.spring.DatabaseEngine;
import com.cse3111project.bot.spring.SQLDatabaseEngine;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
// @SpringBootTest(classes = { KitchenSinkTester.class, SQLDatabaseEngine.class })  // test SQL Database
public class KitchenSinkTester {
	@Autowired  // autowired to DatabaseEngine / SDLDatabaseEngine
	private DatabaseEngine databaseEngine;
	
    // test if key is not found in database
	@Test
	public void testNotFound() throws Exception {
		boolean thrown = false;
		try {
			this.databaseEngine.search("no");  // not found
		} 
        catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(true);  // indeed thrown=true -> true assertion
	}
	
    // test if key is found in database (by exact match)
	@Test
	public void testFound() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("abc");
		} 
        catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown).isEqualTo(true);
		assertThat(result.equals("def")).isEqualTo(true);
	}

    // partial match case 1: at the front
    @Test
    public void partialMatch1() throws Exception {
        boolean thrown = false;
        String answer = null;
        try {
            answer = this.databaseEngine.search("Hi, I am Sam");
        }
        catch (Exception e){
            thrown = true;
        }
        assertThat(!thrown).isEqualTo(true);
        assertThat(answer.equals("Hey, how things going?")).isEqualTo(true);
    }

    // partial match case 2: at the end + different case
    @Test
    public void partialMatch2() throws Exception {
        boolean thrown = false;
        String answer = null;
        try {
            answer = this.databaseEngine.search("I wonder who is Prof Kim");
        }
        catch (Exception e){
            thrown = true;
        }
        assertThat(!thrown).isEqualTo(true);
        assertThat(answer.equals("Well, this is your instructor.")).isEqualTo(true);
    }

    // partial match case 3: at the middle + different case + merged with other char ','
    @Test
    public void partialMatch3() throws Exception {
        boolean thrown = false;
        String answer = null;
        try {
            answer = this.databaseEngine.search("My name is aBc, how are you?");
        }
        catch (Exception e){
            thrown = true;
        }

        assertThat(!thrown).isEqualTo(true);
        assertThat(answer.equals("def")).isEqualTo(true);
    }

    // partial match case 4: multiple matches
    //                       ==> return first matched result found in database (can be amended)
    @Test
    public void partialMatch4() throws Exception {
        boolean thrown = false;
        String answer = null;
        try {
            answer = this.databaseEngine.search("Hi guys, how are you? I am fine");
        }
        catch (Exception e){
            thrown = true;
        }

        assertThat(!thrown).isEqualTo(true);
        assertThat(answer.equals("Hey, how things going?")).isEqualTo(true);  // matched with 'Hi' key
    }
}
