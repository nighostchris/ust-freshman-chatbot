package com.cse3111project.bot.spring;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.io.InputStream;
// import java.util.List;
// import java.util.concurrent.TimeUnit;

import com.cse3111project.bot.spring.category.instruction.Instruction;
import java.util.List;
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

// import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

// import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;  // logging

import com.cse3111project.bot.spring.model.engine.SearchEngine;
import com.cse3111project.bot.spring.category.Category;

import com.cse3111project.bot.spring.category.transport.Minibus;
import com.cse3111project.bot.spring.category.function.timetable.TimeTable;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { KitchenSinkTester.class, SearchEngine.class })
public class KitchenSinkTester {
	@Autowired  // autowired to SearchEngine
	private SearchEngine searchEngine;
	private Category category;
	private List<String> test;
	
    // test if key is not found in database
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
	@Test
	public void testNotFound() throws Exception {
        log.info("--- testNotFound() ---");
        Object answer = this.searchEngine.search("How are you?");  // if not found, return null

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

        Object answer = this.searchEngine.search("What is the arrival time of minibus 11?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Estimated Arrival Time")).isEqualTo(true);

        log.info("--- End of partialMatchMinibus1() ---");
    }

    // partial match minibus keyword case 2: different case + middle
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchMinibus2() throws Exception {
        log.info("--- partialMatchMinibus2() ---");

        Object answer = null;

        answer = this.searchEngine.search("Get Minibus arrival time");

        assertThat(answer).isNull();

        log.info("--- End of partialMatchMinibus2() ---");
    }

    // partial match minibus keyword case 3: at the front + different case
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchMinibus3() throws Exception {
        log.info("--- partialMatchMinibus3() ---");

        Object answer = null;

        answer = this.searchEngine.search("11 Minibus arrival time please?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Estimated Arrival Time")).isEqualTo(true);

        log.info("--- End of partialMatchMinibus3() ---");
    }

    // partial match staff keyword case 1: at the middle + exact staff name <lastName> <firstName>
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchStaff1() throws Exception {
        log.info("--- partialMatchStaff1() ---");

        Object answer = null;

        answer = this.searchEngine.search("Could you tell where the office of Prof. Li Bo is?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("LI Bo") && 
                   !((String) answer).contains("LI Xin")).isEqualTo(true);

        log.info("--- End of partialMatchStaff1() ---");
    }

    // partial match staff keyword case 2: multiple matches + staff last name
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchStaff2() throws Exception {
        log.info("--- partialMatchStaff2() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where is the office of Professor Li?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("LI Bo") && 
                   ((String) answer).contains("LI Xin")).isEqualTo(true);

        log.info("--- End of partialMatchStaff2() ---");
    }

    // partial match staff keyword case 3: multiple matches + exact staff name <firstName> <lastName>
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchStaff3() throws Exception {
        log.info("--- partialMatchStaff3() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where is the office of Prof. Sunghun Kim?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("KIM Sunghun")).isEqualTo(true);

        log.info("--- End of partialMatchStaff3() ---");
    }

    // partial match bus keyword case 1: middle + 2 matches (route + location) + different case
    // KMB database           -- pass
    @Test
    public void partialMatchBus1() throws Exception {
        log.info("--- partialMatchBus1() ---");

        Object answer = null;

        answer = this.searchEngine.search("Arrival time of 91M please. I am currently at UST North gate.");

        assertThat(answer).isNotNull();
        log.info("reply: {}", (String) answer);
        assertThat(answer instanceof String).isEqualTo(true);
        assertThat((((String) answer).contains("91M") && ((String) answer).contains("arrival time")) ||
                    ((String) answer).contains("no available") ||
                    ((String) answer).contains("missed")).isEqualTo(true);
        // assertThat(answer.equals("here")).isEqualTo(true);

        log.info("--- End of partialMatchBus1() ---");
    }

    // partial match bus keyword case 1.1: middle + 2 matches (route + location) + different case +
    //                                     merged with other char
    // KMB database           -- pass
    @Test
    public void partialMatchBus1_1() throws Exception {
        log.info("--- partialMatchBus1_1() ---");

        Object answer = null;

        answer = this.searchEngine.search("What is the arrival time of 91 route? I am at south gate");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat((((String) answer).contains("91") && ((String) answer).contains("arrival time")) ||
                   ((String) answer).contains("no available") ||
                   ((String) answer).contains("missed")).isEqualTo(true);
        // assertThat(answer.equals("here")).isEqualTo(true);

        log.info("--- End of partialMatchBus1_1() ---");
    }

    // partial match bus keyword case 2: end + merged with other char + no location provided
    // KMB database           -- pass
    @Test
    public void partialMatchBus2() throws Exception {
        log.info("--- partialMatchBus2() ---");

        Object answer = null;

        answer = this.searchEngine.search("What is the arrival time of 91?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Sorry") && ((String) answer).contains("query again")).isEqualTo(true);

        log.info("--- End of partialMatchBus2() ---");
    }

    // partial match societies keyword case 1: end + merged with other char + different case
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchSocieties1() throws Exception {
        log.info("--- partialMatchSocieties1() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where is the webpage of film society?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:")).isEqualTo(true);

        log.info("--- End of partialMatchSocieties1() ---");
    }

    // partial match societies keyword case 2: front + merged with other char + multiple matches
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchSocieties2() throws Exception {
        log.info("--- partialMatchSocieties2() ---");

        Object answer = null;

        answer = this.searchEngine.search("Nature Club, Cricket Club where could I get info on them?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("Nature Club") && 
                   ((String) answer).contains("Cricket Club")).isEqualTo(true);

        log.info("--- End of partialMatchSocieties2() ---");
    }

    // partial match societies keyword case 3: end + merged with other char + different case +
    //                                         no Societies.SOCIETIES_KEYWORD
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchSocieties3() throws Exception {
        log.info("--- partialMatchSocieties3() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where could I get info on UST Soc?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("There are a variety of UST societies")).isEqualTo(true);

        log.info("--- End of partialMatchSocieties3() ---");
    }

    // partial match recreation keyword case 1: middle + different case
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchRecreation1() throws Exception {
        log.info("--- partialMatchRecreation1() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where could I book music room in UST?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("Music Room")).isEqualTo(true);

        log.info("--- End of partialMatchRecreation1() ---");
    }

    // partial match recreation keyword case 2: multiple matches + different case + merged with other char
    //                                          + keyword transformation
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void partialMatchRecreation2() throws Exception {
        log.info("--- partialMatchRecreation2() ---");

        Object answer = null;

        // Lecture Room doesn't exist in SQL / static database
        // but it would be transformed to the keyword that they appear: Classroom
        answer = this.searchEngine.search("Where to book Lecture Room, and also study room?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("Classroom") && 
                   ((String) answer).contains("Study Room")).isEqualTo(true);

        log.info("--- End of partialMatchRecreation2() ---");
    }

    // partial match timetable keyword case 1: end + merged with other char + different case
    @Test
    public void partialMatchTimeTable1() throws Exception {
        log.info("--- partialMatchTimeTable1() ---");

        Object answer = null;

        // would match timetable and TA ....
        // ==> resolved in ~~Category.analyze()~~
        // ====> resolved using SearchEngine.editDistance()
        answer = this.searchEngine.search("I would like to use the Timetable function.");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof TimeTable).isEqualTo(true);
        // assertThat(((TimeTable) answer).getController().getFunctionEvent() instanceof TimeTable);

        log.info("--- End of partialMatchTimeTable1() ---");
    }

    // partial match campus keyword case 1: end + merged with other char
    @Test
    public void partialMatchCampus1() throws Exception {
        log.info("--- partialMatchCampus1() ---");
        Object answer = this.searchEngine.search("Can I know eta from 4619 to 2407?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("It takes")).isEqualTo(true);
        log.info("--- End of partialMatchCampus1() ---");
    }

    // partial match campus keyword case 2: end + merged with other char + 
    //                                      starting point string consisting of multiple words 
    //                                      (to test HTML encoding) + different case
    @Test
    public void partialMatchCampus2() throws Exception {
        log.info("--- partialMatchCampus2() ---");
        Object answer = this.searchEngine.search("Can I know eta from LTA back entrance to 2504?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("It takes")).isEqualTo(true);
        log.info("--- End of partialMatchCampus2() ---");
    }

    // partial match campus keyword case 3: end + merged with other char + 
    //                                      only providing starting point without ending point
    @Test
    public void partialMatchCampus3() throws Exception {
        log.info("--- partialMatchCampus3() ---");
        Object answer = this.searchEngine.search("Can I know eta from 4619?");
        assertThat(answer).isNull();
        log.info("--- End of partialMatchCampus3() ---");
    }
    
    @Test
    public void userHelpTest() throws Exception {
        log.info("--- userHelpTest() ---");
    	test.add("help");
        Object answer = this.category.analyze(test);
        assertThat(answer).isNotNull();
        assertThat(answer instanceof Instruction).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("features"));
        log.info("--- End of userHelpTest() ---");
    }
}
