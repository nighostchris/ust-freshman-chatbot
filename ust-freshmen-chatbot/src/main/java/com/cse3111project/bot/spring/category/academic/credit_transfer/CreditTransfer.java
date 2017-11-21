package com.cse3111project.bot.spring.category.academic.credit_transfer;

import com.cse3111project.bot.spring.category.academic.Academic;

import com.cse3111project.bot.spring.model.engine.marker.SQLAccessible;
import com.cse3111project.bot.spring.model.engine.SQLDatabaseEngine;
import com.cse3111project.bot.spring.model.engine.marker.StaticAccessible;
import com.cse3111project.bot.spring.model.engine.StaticDatabaseEngine;

import java.sql.SQLException;

import java.net.URISyntaxException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;
import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * The CreditTransfer class acts as the main controller for coordinating all the user-query
 * on credit transfer related issues like exam credit transfer and local institution credit
 * transfer.
 * @version 1.0
 */
@Slf4j
public class CreditTransfer extends Academic implements SQLAccessible, StaticAccessible 
{
    public static final String QUERY_KEYWORD[] = { "credit transfer" , "transfer" };

    private List<CreditTransfer> subcategoryResults = new ArrayList<>();
    private List<String> generalResults;

    /**
     * This is the constructor of CreditTransfer which initialize the List of String of
     * corresponding sub-class and further process the data in the List.
     * @param examQuery This is the List that stores all query about exam credit transfer
     * @param localInstitutionQuery This is the List that stores all query about local 
     * 								institution credit transfer
     * @param nonLocalInstitutionQuery This is the List that stores all query about non local 
     * 								   institution credit transfer
     */
    public CreditTransfer(final List<String> examQuery,
                          final List<String> localInstitutionQuery, 
                          final List<String> nonLocalInstitutionQuery) {
        if (!examQuery.isEmpty()) 
            subcategoryResults.add(new ExamCreditTransfer(examQuery));
        if (!localInstitutionQuery.isEmpty()) 
            subcategoryResults.add(new LocalInstitutionCreditTransfer(localInstitutionQuery));
        if (!nonLocalInstitutionQuery.isEmpty()) 
            subcategoryResults.add(new NonLocalInstitutionCreditTransfer(nonLocalInstitutionQuery));
    }

    /**
     * Default constructor of CreditTransfer Class.
     */
    CreditTransfer() { }

    /**
     * This method will reconstruct each line (row) from static database
     * @param line The original data retrieved from database
     * @return List Return a List of String that contains the processed data from database
     */
    protected static synchronized List<String> reconstruct(String line){
        // reconstruct the row
        List<String> row = Collections.synchronizedList(new ArrayList<>());
        // split comma by myself since quoted columns may consist of comma
        int i = 0;
        while (i < line.length()){
            if (line.charAt(i) == '\"'){
                int j = line.indexOf('\"', i + 1);  // find its pair quote
                if (j == -1) {
                    log.info("Missing a paired quote in {}", line);
                    throw new RuntimeException("Unexpected error occurred while reading database. Sorry");
                }
                row.add(line.substring(i + 1, j));
                // skip the last quote and comma
                i = j + 2;
            }
            else {
                int j = line.indexOf(',', i);
                if (j != -1){
                    row.add(line.substring(i, j));
                    // skip the comma
                    i = j + 1;
                }
                else {
                    row.add(line.substring(i));
                    break;
                }
            }
        }

        return row;
    }

    /**
     * This method will connect to the SQL database and retrieve data on details of credit transfer.
     * @return String Return the data retrieved from SQL database
     * @throws NotSQLAccessibleError
     * @throws URISyntaxException
     * @throws SQLException
     */
    @Override
    public synchronized String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException {
        generalResults = Collections.synchronizedList(new ArrayList<>());

        for (CreditTransfer subcategoryResult : subcategoryResults)
            generalResults.add(subcategoryResult.getDataFromSQL());

        return super.replyResults();
    }

    /**
     * This method will connect to the static database and retrieve data on details of credit transfer.
     * It is used when the SQL database has run into some trouble of connection.
     * @return String Return the data retrieved from database
     * @throws NotStaticAccessibleError
     * @throws StaticDatabaseFileNotFoundException
     */
    @Override
    public synchronized String getDataFromStatic() throws NotStaticAccessibleError, StaticDatabaseFileNotFoundException {
        generalResults = Collections.synchronizedList(new ArrayList<>());

        for (CreditTransfer subcategoryResult : subcategoryResults)
            generalResults.add(subcategoryResult.getDataFromStatic());

        return super.replyResults();
    }

    /**
     * Overriden the original toString() method given by Java.
     * @return
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (String result : generalResults)
            sb.append(result);

        return sb.toString();
    }
}
