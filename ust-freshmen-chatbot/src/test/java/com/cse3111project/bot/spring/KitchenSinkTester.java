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
import lombok.extern.slf4j.Slf4j;  // logging

import com.cse3111project.bot.spring.SearchEngine;

import com.cse3111project.bot.spring.category.transport.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { KitchenSinkTester.class, SearchEngine.class })  // test SQL Database
public class KitchenSinkTester {
	@Autowired  // autowired to SearchEngine
	private SearchEngine searchEngine;
	
    // test if key is not found in database
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
	@Test
	public void testNotFound() throws Exception {
        log.info("--- testNotFound() ---");
        String answer = this.searchEngine.search("How are you?");  // if not found, return null

		assertThat(answer).isNull();

        log.info("--- End of testNotFound() ---");
	}
	
    // partial match minibus keyword case 1: at the near end + merge with other character '?'
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchMinibus1() throws Exception {
        log.info("--- partialMatchMinibus1() ---");

        // { "minibus", "minibus 11", "11 minibus" }
        for (String minibusKeyword : Minibus.QUERY_KEYWORD){
            StringBuilder questionBuilder = new StringBuilder("What is the arrival time of ")
                                                .append(minibusKeyword).append('?');
            String answer = this.searchEngine.search(questionBuilder.toString());

            assertThat(answer).isNotNull();
            log.info("reply: {}", answer);
            assertThat(answer.contains("Estimated Arrival Time")).isEqualTo(true);
        }

        log.info("--- End of partialMatchMinibus1() ---");
    }

    // partial match minibus keyword case 2: different case + middle
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchMinibus2() throws Exception {
        log.info("--- partialMatchMinibus2() ---");

        String answer = null;

        answer = this.searchEngine.search("Get Minibus arrival time");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Estimated Arrival Time")).isEqualTo(true);

        log.info("--- End of partialMatchMinibus2() ---");
    }

    // partial match minibus keyword case 3: at the front + different case
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchMinibus3() throws Exception {
        log.info("--- partialMatchMinibus3() ---");

        String answer = null;

        answer = this.searchEngine.search("11 Minibus arrival time please?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Estimated Arrival Time")).isEqualTo(true);

        log.info("--- End of partialMatchMinibus3() ---");
    }

    // partial match staff keyword case 1: at the middle + exact staff name
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchStaff1() throws Exception {
        log.info("--- partialMatchStaff1() ---");

        String answer = null;

        answer = this.searchEngine.search("Could you tell where the office of Prof. Li Bo is?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Results:") && answer.contains("Li Bo") && 
                   !answer.contains("Li Xin")).isEqualTo(true);

        log.info("--- End of partialMatchStaff1() ---");
    }

    // partial match staff keyword case 2: multiple matches + staff last name
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchStaff2() throws Exception {
        log.info("--- partialMatchStaff2() ---");

        String answer = null;

        answer = this.searchEngine.search("Where is the office of Professor Li?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Results:") && answer.contains("Li Bo") && 
                   answer.contains("Li Xin")).isEqualTo(true);

        log.info("--- End of partialMatchStaff2() ---");
    }

    // partial match bus keyword case 1: middle + 2 matches (route + location) + different case
    // KMB database           -- pass
    @Test
    public void partialMatchBus1() throws Exception {
        log.info("--- partialMatchBus1() ---");

        String answer = null;

        answer = this.searchEngine.search("Arrival time of 91M please. I am currently at UST North gate.");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("91M") && answer.contains("arrival time")).isEqualTo(true);

        log.info("--- End of partialMatchBus1() ---");
    }

    // partial match bus keyword case 2: end + merged with other char + no location provided
    // KMB database           -- pass
    @Test
    public void partialMatchBus2() throws Exception {
        log.info("--- partialMatchBus2() ---");

        String answer = null;

        answer = this.searchEngine.search("What is the arrival time of 91?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Sorry") && answer.contains("query again")).isEqualTo(true);

        log.info("--- End of partialMatchBus2() ---");
    }

    // partial match societies keyword case 1: end + merged with other char + different case
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchSocieties1() throws Exception {
        log.info("--- partialMatchSocieties1() ---");

        String answer = null;

        answer = this.searchEngine.search("Where is the webpage of film society?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Results:")).isEqualTo(true);

        log.info("--- End of partialMatchSocieties1() ---");
    }

    // partial match societies keyword case 2: front + merged with other char + multiple matches
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchSocieties2() throws Exception {
        log.info("--- partialMatchSocieties2() ---");

        String answer = null;

        answer = this.searchEngine.search("Nature Club, Cricket Club where could I get info on them?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Results:") && answer.contains("Nature Club") && 
                   answer.contains("Cricket Club")).isEqualTo(true);

        log.info("--- End of partialMatchSocieties2() ---");
    }

    // partial match societies keyword case 3: end + merged with other char + different case +
    //                                         no Societies.SOCIETIES_KEYWORD
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchSocieties3() throws Exception {
        log.info("--- partialMatchSocieties3() ---");

        String answer = null;

        answer = this.searchEngine.search("Where could I get info on UST Soc?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("There are a variety of UST societies")).isEqualTo(true);

        log.info("--- End of partialMatchSocieties3() ---");
    }

    // partial match recreation keyword case 1: middle + different case
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchRecreation1() throws Exception {
        log.info("--- partialMatchRecreation1() ---");

        String answer = null;

        answer = this.searchEngine.search("Where could I book music room in UST?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Results:") && answer.contains("Music Room")).isEqualTo(true);

        log.info("--- End of partialMatchRecreation1() ---");
    }

    // partial match recreation keyword case 2: multiple matches + different case + merged with other char
    //                                          + keyword transformation
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchRecreation2() throws Exception {
        log.info("--- partialMatchRecreation2() ---");

        String answer = null;

        // Lecture Room doesn't exist in SQL / static database
        // but it would be transformed to the keyword that they appear: Classroom
        answer = this.searchEngine.search("Where to book Lecture Room, and also study room?");

        assertThat(answer).isNotNull();
        log.info("reply: {}", answer);
        assertThat(answer.contains("Results:") && answer.contains("Classroom") && 
                   answer.contains("Study Room")).isEqualTo(true);

        log.info("--- End of partialMatchRecreation2() ---");
    }
}
