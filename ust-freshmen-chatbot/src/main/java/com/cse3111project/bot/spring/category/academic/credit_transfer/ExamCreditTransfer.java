package com.cse3111project.bot.spring.category.academic.credit_transfer;

import com.cse3111project.bot.spring.model.engine.marker.SQLAccessible;
import com.cse3111project.bot.spring.model.engine.SQLDatabaseEngine;
import com.cse3111project.bot.spring.model.engine.marker.StaticAccessible;
import com.cse3111project.bot.spring.model.engine.StaticDatabaseEngine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.net.URISyntaxException;

import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;
import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

/**
 * ExamCreditTransfer class acts as the main controller for handling all user-query about exam
 * credit transfer.
 * @version 1.0
 */
public class ExamCreditTransfer extends CreditTransfer implements SQLAccessible, StaticAccessible {
    public static final String EXAM_KEYWORD[] = { "Australia, Higher School Certificate Examination (NSW)",
                                                  "Bangladesh Higher Secondary Certificate (BDHSC)",
                                                  "Canada, High School Diploma (British Columbia)",
                                                  "Canada, High School Diploma (Manitoba)",
                                                  "College Board Advanced Placement (AP)",
                                                  "Ecuador, Titulo de Bachiller de la Republica del Ecuador",
                                                  "European Baccalaureate",
                                                  "Finnish Matriculation Examination",
                                                  "French Baccalaureate",
                                                  "General Certificate of Education Advanced Level (GCEAL)",
                                                  "German Abitur",
                                                  "Hong Kong Advanced Level Examination (HKALE)",
                                                  "Iceland Matriculation Examination",
                                                  "India, All India Senior School Certificate Examination (AISSCE)",
                                                  "India, Indian School Certificate Examination (ISCE)",
                                                  "India, Maharashtra State Higher Secondary Certificate Examination (INMHHSCE)",
                                                  "India, Telangana State Board of Intermediate Education: Hyderabad",
                                                  "Indonesia, Ujian Nasional (IDUN)",
                                                  "International Advanced Level (IAL)",
                                                  "International Baccalaureate (IB) Diploma",
                                                  "International Mathematical Olympiad",
                                                  "Jamaica, Caribbean Advanced Proficiency Examination (CAPE)",
                                                  "Malaysia, Higher School Certificate (Sijil Tinggi Persekolahan MY)",
                                                  "Malaysia, Unified Examination Certificate (Senior)",
                                                  "Netherlands, Pre-University Education VWO Examination",
                                                  "Norway National Examination",
                                                  "Scottish National Qualification (SNQ)",
                                                  "Singapore-Cambridge GCE Advanced Level",
                                                  "Tajikistan Certificate of General Secondary School Education",
                                                  "Tunisia Baccalaureate",
                                                  "AISSCE",
                                                  "AP",
                                                  "BDHSC",
                                                  "CAPE",
                                                  "GCEAL",
                                                  "H2",
                                                  "HKALE",
                                                  "IAL",
                                                  "IB",
                                                  "IDUN",
                                                  "INMHHSCE",
                                                  "ISCE",
                                                  "NSW",
                                                  "SNQ"
                                                 };

    public static final String SUBJECT_KEYWORD[] = { "84 SAF1001 Social Studies",
                                                     "Accounting + Cash Flow + Tax Statements (BOK 113, 201, 213, 313)",
                                                     "Advanced Higher Chemistry",
                                                     "Advanced Higher Mathematics",
                                                     "Advanced Higher Physics",
                                                     "Advanced Mathematics II",
                                                     "Anthropology (A-level)",
                                                     "Applied Information and Communications Technology (A-level)",
                                                     "Biologie-Ecologie (Coef. 5)",
                                                     "Biology",
                                                     "Biology (A-level)",
                                                     "Biology (H2)",
                                                     "Biology (Higher level)",
                                                     "Biology (Standard level)",
                                                     "Biology 3",
                                                     "Biology Unit 1 + Unit 2",
                                                     "Business Management STJ103",
                                                     "Calculus (AB)",
                                                     "Calculus (BC)",
                                                     "Calculus 12",
                                                     "Calculus 12 AP (AB)",
                                                     "Calculus 12 AP (BC)",
                                                     "Calculus 42S",
                                                     "Chemistry",
                                                     "Chemistry (A-level)",
                                                     "Chemistry (H2)",
                                                     "Chemistry (Higher level)",
                                                     "Chemistry (Kimia)",
                                                     "Chemistry 40S",
                                                     "Chemistry A (A-level)",
                                                     "Chemistry B (A Level) (Salters)",
                                                     "Chemistry Unit 1",
                                                     "Chinese History (A-level)",
                                                     "Chinese History (AS-level)",
                                                     "Computer Science (Higher level)",
                                                     "Computer Science (Standard level)",
                                                     "Computer Science A",
                                                     "Computer Science Unit 1 + Unit 2",
                                                     "Computer Studies (A-level)",
                                                     "Computing (A-level)",
                                                     "Critical Thinking (A-level)",
                                                     "Earth and Life Sciences / Sc. vie Terre (Coef. 6)",
                                                     "Economics (A-level)",
                                                     "Economics (AS-level)",
                                                     "Economics (Higher level)",
                                                     "Economics (Standard level)",
                                                     "Electronics (A Level)",
                                                     "Environmental Science",
                                                     "Further Mathematics",
                                                     "Further Mathematics (A-level)",
                                                     "Further Mathematics (Higher level)",
                                                     "Government and Politics (A-level)",
                                                     "Government and Public Affairs (A-level)",
                                                     "Government and Public Affairs (AS-level)",
                                                     "History (SAG103, 203, 303)",
                                                     "Human Biology (A-level)",
                                                     "Humanities (A-level)",
                                                     "Information Technology in a Global Society (Higher level)",
                                                     "Information Technology in a Global Society (Standard level)",
                                                     "Information and Communications Technology (A-level)",
                                                     "Macroeconomics",
                                                     "Marketing (MAR103, 203)",
                                                     "Mathematics",
                                                     "Mathematics (A-level)",
                                                     "Mathematics (Advanced) Matematiikassa, Pitkä Oppimäärä",
                                                     "Mathematics (H2)",
                                                     "Mathematics (Higher level)",
                                                     "Mathematics (Mathematics Section / Coeff. 4)",
                                                     "Mathematics (Science Stream, Coef. 7 or 9)",
                                                     "Mathematics Extension 2",
                                                     "Mathematics T (Technology)",
                                                     "Mathematics and Statistics",
                                                     "Microeconomics",
                                                     "Microeconomics (REK103, 203, 323)",
                                                     "Natural Science (Biology, Geology, Physics and Chemistry NAT103, 113, 123)",
                                                     "Natural science",
                                                     "Physics",
                                                     "Physics & Chemistry (Coef. 6 or 8)",
                                                     "Physics (A-level)",
                                                     "Physics (H2)",
                                                     "Physics (Higher level)",
                                                     "Physics 1 + Physics 2",
                                                     "Physics B",
                                                     "Physics C (Electricity and Magnetism)",
                                                     "Physics C (Mechanics)",
                                                     "Physics Unit 1 + Unit 2",
                                                     "Psychology",
                                                     "Psychology (A-level)",
                                                     "Psychology (AS-level)",
                                                     "Psychology (Higher level)",
                                                     "Psychology (SAL103, 203, 303, 403)",
                                                     "Psychology (Standard level)",
                                                     "Pure Mathematics",
                                                     "Pure Mathematics (A-level)",
                                                     "Social and Cultural Anthropology (Higher level)",
                                                     "Social and Cultural Anthropology (Standard level)",
                                                     "Sociology (A-level)",
                                                     "Statistics",
                                                     "Wiskunde (Mathematics) B 1, 2"
                                                   };

    // id examination subject min_grade transfer_course_code db_reference_no
    private static final String SQL_TABLE = "examinations_credits";
    private static final String STATIC_TABLE = "/static/academic/credit_transfer/examinationCredits.txt";

    private List<String> examQuery;
    private List<CreditTransferInfo> results;

    /**
     * This is the constructor of ExamCreditTransfer class.
     * @param examQuery
     */
    ExamCreditTransfer(final List<String> examQuery) 
    {
        super();
        this.examQuery = examQuery;
    }

    /**
     * This method will connect to the SQL database and retrieve data on details of exam credit transfer.
     * @return String Return the data retrieved from SQL database
     * @throws NotSQLAccessibleError
     * @throws URISyntaxException
     * @throws SQLException
     */
    @Override
    public synchronized String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException {
        try (SQLDatabaseEngine database = new SQLDatabaseEngine(this.getClass(), SQL_TABLE)) {
            results = Collections.synchronizedList(new ArrayList<>());

            StringBuilder SQLStatement = new StringBuilder("SELECT examination, subject, transfer_course_code, min_grade FROM ")
                                             .append(SQL_TABLE)
                                             .append(" WHERE subject = ?");
            for (int i = 1; i < examQuery.size(); i++)
                SQLStatement.append(" OR subject = ?");

            PreparedStatement SQLQuery = database.prepare(SQLStatement.toString());
            for (int i = 0; i < examQuery.size(); i++)
                SQLQuery.setString(i + 1, examQuery.get(i));

            ResultSet reader = database.executeQuery();
            while (reader.next()){
                results.add(new ExamCreditTransferInfo(reader.getString(1), reader.getString(2), 
                                                       reader.getString(3), reader.getString(4)));
            }

            return super.replyResults(results);
        }
    }

    /**
     * This method will connect to the static database and retrieve data on details of exam credit transfer.
     * It is used when the SQL database has run into some trouble of connection.
     * @return String Return the data retrieved from database
     * @throws NotStaticAccessibleError
     * @throws StaticDatabaseFileNotFoundException
     */
    @Override
    public synchronized String getDataFromStatic() throws NotStaticAccessibleError, StaticDatabaseFileNotFoundException {
        try (StaticDatabaseEngine database = new StaticDatabaseEngine(this.getClass(), STATIC_TABLE)) {
            results = Collections.synchronizedList(new ArrayList<>());

            Scanner reader = database.executeQuery();
            while (reader.hasNextLine()){
                String line = reader.nextLine();
                // starting with # is considered as comment
                if (line.startsWith("#") || line.length() == 0)
                    continue;

                List<String> row = reconstruct(line);
                if (examQuery.contains(row.get(2)))
                    results.add(new ExamCreditTransferInfo(row.get(1), row.get(2), row.get(4), row.get(3)));
            }

            return super.replyResults(results);
        }
    }
}
