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
	
    // minibus keyword case 1: at the near end + merge with other character '?'
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testMinibus1() throws Exception {
        log.info("--- testMinibus1() ---");

        Object answer = this.searchEngine.search("What is the arrival time of minibus 11?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Estimated Arrival Time")).isEqualTo(true);

        log.info("--- End of testMinibus1() ---");
    }

    // minibus keyword case 2: different case + middle
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testMinibus2() throws Exception {
        log.info("--- testMinibus2() ---");

        Object answer = null;

        answer = this.searchEngine.search("Get Minibus arrival time");

        assertThat(answer).isNull();

        log.info("--- End of testMinibus2() ---");
    }

    // minibus keyword case 3: at the front + different case
    // local SQL database     -- pass
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testMinibus3() throws Exception {
        log.info("--- testMinibus3() ---");

        Object answer = null;

        answer = this.searchEngine.search("11 Minibus arrival time please?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Estimated Arrival Time")).isEqualTo(true);

        log.info("--- End of testMinibus3() ---");
    }

    // staff keyword case 1: at the middle + exact staff name <lastName> <firstName>
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testStaff1() throws Exception {
        log.info("--- testStaff1() ---");

        Object answer = null;

        answer = this.searchEngine.search("Could you tell where the office of Prof. Li Bo is?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("LI Bo") && 
                   !((String) answer).contains("LI Xin")).isEqualTo(true);

        log.info("--- End of testStaff1() ---");
    }

    // staff keyword case 2: multiple matches + staff last name
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testStaff2() throws Exception {
        log.info("--- testStaff2() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where is the office of Professor Li?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("LI Bo") && 
                   ((String) answer).contains("LI Xin")).isEqualTo(true);

        log.info("--- End of testStaff2() ---");
    }

    // staff keyword case 3: multiple matches + exact staff name <firstName> <lastName>
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testStaff3() throws Exception {
        log.info("--- testStaff3() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where is the office of Prof. Sunghun Kim?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("KIM Sunghun")).isEqualTo(true);

        log.info("--- End of testStaff3() ---");
    }

    // bus keyword case 1: middle + 2 matches (route + location) + different case
    // KMB database           -- pass
    @Test
    public void testBus1() throws Exception {
        log.info("--- testBus1() ---");

        Object answer = null;

        answer = this.searchEngine.search("Arrival time of 91M please. I am currently at UST North gate.");

        assertThat(answer).isNotNull();
        log.info("reply: {}", (String) answer);
        assertThat(answer instanceof String).isEqualTo(true);
        assertThat((((String) answer).contains("91M") && ((String) answer).contains("arrival time")) ||
                    ((String) answer).contains("no available") ||
                    ((String) answer).contains("missed")).isEqualTo(true);
        // assertThat(answer.equals("here")).isEqualTo(true);

        log.info("--- End of testBus1() ---");
    }

    // bus keyword case 1.1: middle + 2 matches (route + location) + different case +
    //                       merged with other char
    // KMB database           -- pass
    @Test
    public void testBus1_1() throws Exception {
        log.info("--- testBus1_1() ---");

        Object answer = null;

        answer = this.searchEngine.search("What is the arrival time of 91 route? I am at south gate");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat((((String) answer).contains("91") && ((String) answer).contains("arrival time")) ||
                   ((String) answer).contains("no available") ||
                   ((String) answer).contains("missed")).isEqualTo(true);
        // assertThat(answer.equals("here")).isEqualTo(true);

        log.info("--- End of testBus1_1() ---");
    }

    // bus keyword case 2: end + merged with other char + no location provided
    // KMB database           -- pass
    @Test
    public void testBus2() throws Exception {
        log.info("--- testBus2() ---");

        Object answer = null;

        answer = this.searchEngine.search("What is the arrival time of 91?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Sorry") && ((String) answer).contains("query again")).isEqualTo(true);

        log.info("--- End of testBus2() ---");
    }

    // societies keyword case 1: end + merged with other char + different case
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testSocieties1() throws Exception {
        log.info("--- testSocieties1() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where is the webpage of film society?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:")).isEqualTo(true);

        log.info("--- End of testSocieties1() ---");
    }

    // societies keyword case 2: front + merged with other char + multiple matches
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testSocieties2() throws Exception {
        log.info("--- testSocieties2() ---");

        Object answer = null;

        answer = this.searchEngine.search("Nature Club, Cricket Club where could I get info on them?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("Nature Club") && 
                   ((String) answer).contains("Cricket Club")).isEqualTo(true);

        log.info("--- End of testSocieties2() ---");
    }

    // societies keyword case 3: end + merged with other char + different case +
    //                           no Societies.SOCIETIES_KEYWORD
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testSocieties3() throws Exception {
        log.info("--- testSocieties3() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where could I get info on UST Soc?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("There are a variety of UST societies")).isEqualTo(true);

        log.info("--- End of testSocieties3() ---");
    }

    // recreation keyword case 1: middle + different case
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testRecreation1() throws Exception {
        log.info("--- testRecreation1() ---");

        Object answer = null;

        answer = this.searchEngine.search("Where could I book music room in UST?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("Music Room")).isEqualTo(true);

        log.info("--- End of testRecreation1() ---");
    }

    // recreation keyword case 2: multiple matches + different case + merged with other char
    //                            + keyword transformation
    // heroku SQL database    -- pass
    // local static database  -- pass
    @Test
    public void testRecreation2() throws Exception {
        log.info("--- testRecreation2() ---");

        Object answer = null;

        // Lecture Room doesn't exist in SQL / static database
        // but it would be transformed to the keyword that they appear: Classroom
        answer = this.searchEngine.search("Where to book Lecture Room, and also study room?");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", (String) answer);
        assertThat(((String) answer).contains("Results:") && ((String) answer).contains("Classroom") && 
                   ((String) answer).contains("Study Room")).isEqualTo(true);

        log.info("--- End of testRecreation2() ---");
    }

    // timetable keyword case 1: end + merged with other char + different case
    @Test
    public void testTimeTable1() throws Exception {
        log.info("--- testTimeTable1() ---");

        Object answer = null;

        // would match timetable and TA ....
        // ==> resolved in ~~Category.analyze()~~
        // ====> resolved using SearchEngine.editDistance()
        answer = this.searchEngine.search("I would like to use the Timetable function.");

        assertThat(answer).isNotNull();
        assertThat(answer instanceof TimeTable).isEqualTo(true);
        // assertThat(((TimeTable) answer).getController().getFunctionEvent() instanceof TimeTable);

        log.info("--- End of testTimeTable1() ---");
    }

    // campus keyword case 1: end + merged with other char
    @Test
    public void testCampus1() throws Exception {
        log.info("--- testCampus1() ---");
        Object answer = this.searchEngine.search("Can I know eta from 4619 to 2407?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("It takes")).isEqualTo(true);
        log.info("--- End of testCampus1() ---");
    }

    // campus keyword case 2: end + merged with other char + 
    //                        starting point string consisting of multiple words 
    //                        (to test HTML encoding) + different case
    @Test
    public void testCampus2() throws Exception {
        log.info("--- testCampus2() ---");
        Object answer = this.searchEngine.search("Can I know eta from LTA back entrance to 2504?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("It takes")).isEqualTo(true);
        log.info("--- End of testCampus2() ---");
    }

    // campus keyword case 3: end + merged with other char + 
    //                                      only providing starting point without ending point
    @Test
    public void testCampus3() throws Exception {
        log.info("--- testCampus3() ---");
        Object answer = this.searchEngine.search("Can I know eta from 4619?");
        assertThat(answer).isNull();
        log.info("--- End of testCampus3() ---");
    }
    
    @Test
    public void userHelpTest1() throws Exception {
        log.info("--- userHelpTest1() ---");
        Object answer = this.searchEngine.search("/help");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("Commands")).isEqualTo(true);
        log.info("--- End of userHelpTest1() ---");
    }
    
    @Test
    public void userHelpTest2() throws Exception {
        log.info("--- userHelpTest2() ---");
        Object answer = this.searchEngine.search("/dir");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("Kim")).isEqualTo(true);
        log.info("--- End of userHelpTest2() ---");
    }
    
    @Test
    public void userHelpTest3() throws Exception {
        log.info("--- userHelpTest3() ---");
        Object answer = this.searchEngine.search("/kmb");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("ETA of 91m")).isEqualTo(true);
        log.info("--- End of userHelpTest3() ---");
    }
    
    @Test
    public void userHelpTest4() throws Exception {
        log.info("--- userHelpTest4() ---");
        Object answer = this.searchEngine.search("/minibus");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("by our bot!")).isEqualTo(true);
        log.info("--- End of userHelpTest4() ---");
    }
    
    @Test
    public void userHelpTest5() throws Exception {
        log.info("--- userHelpTest5() ---");
        Object answer = this.searchEngine.search("/society");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("search all")).isEqualTo(true);
        log.info("--- End of userHelpTest5() ---");
    }
    
    @Test
    public void userHelpTest6() throws Exception {
        log.info("--- userHelpTest6() ---");
        Object answer = this.searchEngine.search("/campus");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("takes from one")).isEqualTo(true);
        log.info("--- End of userHelpTest6() ---");
    }
    
    @Test
    public void userHelpTest7() throws Exception {
        log.info("--- userHelpTest7() ---");
        Object answer = this.searchEngine.search("/facb");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("I want to")).isEqualTo(true);
        log.info("--- End of userHelpTest7() ---");
    }

    @Test
    public void testExamCreditTransfer1() throws Exception {
        log.info("--- testExamCreditTransfer1 ---");
        Object answer = this.searchEngine.search("Can I transfer HKALE Chemistry (A-level) to UST?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("CHEM 1010")).isEqualTo(true);
        log.info("--- End of testExamCreditTransfer1 ---");
    }

    @Test
    public void testExamCreditTransfer2() throws Exception {
        log.info("--- testExamCreditTransfer2 ---");
        Object answer = this.searchEngine.search("Can I transfer HKALE grades to UST?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("What subjects")).isEqualTo(true);
        log.info("--- End of testExamCreditTransfer2 ---");
    }

    @Test
    public void testExamCreditTransfer3() throws Exception {
        log.info("--- testExamCreditTransfer3 ---");
        Object answer = this.searchEngine.search("Can I transfer Advanced Mathematics II to UST?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("Advanced Mathematics II")).isEqualTo(true);
        log.info("--- End of testExamCreditTransfer3 ---");
    }

    @Test
    public void testExamCreditTransfer4() throws Exception {
        log.info("--- testExamCreditTransfer4 ---");
        Object answer = this.searchEngine.search("Can I transfer Chinese History from HKALE to UST?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("Chinese History")).isEqualTo(true);
        log.info("--- End of testExamCreditTransfer4 ---");
    }

    @Test
    public void testLocalInstitutionCreditTransfer1() throws Exception {
        log.info("--- testLocalInstitutionCreditTransfer1 ---");
        Object answer = this.searchEngine.search("Can I transfer HUM4001?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("HUMA 1720")).isEqualTo(true);
        log.info("--- End of testLocalInstitutionCreditTransfer1 ---");
    }
    
    @Test
    public void testLocalInstitutionCreditTransfer2() throws Exception {
        log.info("--- testLocalInstitutionCreditTransfer2 ---");
        Object answer = this.searchEngine.search("Can I transfer PHI3105 ?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("Lingnan")).isEqualTo(true);
        log.info("--- End of testLocalInstitutionCreditTransfer2 ---");
    }
    
    @Test
    public void testNonLocalInstitutionCreditTransfer1() throws Exception {
        log.info("--- testNonLocalInstitutionCreditTransfer1 ---");
        Object answer = this.searchEngine.search("credit transfer Aalto University");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("Which")).isEqualTo(true);
        log.info("--- End of testLocalInstitutionCreditTransfer1 ---");
    }
        
    @Test
    public void partialMatchCourseWebsiteSearch1() throws Exception {
        log.info("--- partialMatchCourseWebsiteSearch1 ---");
        Object answer = this.searchEngine.search("Can you find the course website of COMP 3111 for me?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("Courses")).isEqualTo(true);
        log.info("--- End of partialMatchCourseWebsiteSearch1 ---");
    }
    
    @Test
    public void partialMatchCourseWebsiteSearch2() throws Exception {
        log.info("--- partialMatchCourseWebsiteSearch2 ---");
        Object answer = this.searchEngine.search("Can you search details of course COMP4641 for me?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("James Kwok")).isEqualTo(true);
        log.info("--- End of partialMatchCourseWebsiteSearch2 ---");
    }
    
    @Test
    public void partialMatchCourseWebsiteSearch3() throws Exception {
        log.info("--- partialMatchCourseWebsiteSearch3 ---");
        Object answer = this.searchEngine.search("Can you find 2012?");
        assertThat(answer).isNull();
        log.info("--- End of partialMatchCourseWebsiteSearch3 ---");
    }
    
    @Test
    public void StudyPathSearchTest1() throws Exception {
        log.info("--- StudyPathSearchTest1 ---");
        Object answer = this.searchEngine.search("Can you suggest some course after I have taken COMP 2012?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("COMP 2611")).isEqualTo(true);
        log.info("--- End of StudyPathSearchTest1 ---");
    }
    
    @Test
    public void StudyPathSearchTest2() throws Exception {
        log.info("--- StudyPathSearchTest2 ---");
        Object answer = this.searchEngine.search("What can I study if I have finished COMP 3111?");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("COMP 4111")).isEqualTo(true);
        log.info("--- End of StudyPathSearchTest2 ---");
    }
    
    @Test
    public void StudyPathSearchTest3() throws Exception {
        log.info("--- StudyPathSearchTest3 ---");
        Object answer = this.searchEngine.search("Suggest some path for me after COMP3511");
        assertThat(answer).isNotNull();
        assertThat(answer instanceof String).isEqualTo(true);
        log.info("reply: {}", answer);
        assertThat(((String) answer).contains("COMP 4511")).isEqualTo(true);
        log.info("--- End of StudyPathSearchTest3 ---");
    }
}
