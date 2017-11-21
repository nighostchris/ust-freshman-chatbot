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
 * The NonLocalInstitutionCreditTransfer class acts as the main controller for coordinating all the user-query
 * on non local institution credit transfer related issues.
 * @version 1.0
 */
public class NonLocalInstitutionCreditTransfer extends CreditTransfer implements SQLAccessible, StaticAccessible {
    public static final String INSTITUTION_KEYWORD[] = { "Aalto University",
                                                         "Aarhus University",
                                                         "American University in Bulgaria",
                                                         "Arizona State University",
                                                         "BI Norwegian Business School",
                                                         "Bandung Institute of Technology",
                                                         "Beijing Institute of Technology",
                                                         "Boston College",
                                                         "Boston University",
                                                         "Bo�zi癟i University",
                                                         "Briercrest College and Seminary",
                                                         "Brigham Young University",
                                                         "Cardiff University",
                                                         "Carnegie Mellon University",
                                                         "Chalmers University of Technology",
                                                         "City University London, Cass Business School",
                                                         "Columbia University In the City of New York",
                                                         "Commercial College of Iceland",
                                                         "Copenhagen Business School",
                                                         "Coquitlam College",
                                                         "Cornell University",
                                                         "Corvinus University of Budapest",
                                                         "Czech Technical University in Prague",
                                                         "Dankook University",
                                                         "Drexel University",
                                                         "Durham University",
                                                         "EBS Universit瓣t f羹r Wirtschaft und Recht",
                                                         "ESSEC Business School",
                                                         "East China Normal University",
                                                         "Ecole Polytechnique Federale de Lausanne",
                                                         "Emory University",
                                                         "Erasmus University",
                                                         "European Business School - Germany",
                                                         "European Business School - Paris",
                                                         "European Innovation Academy",
                                                         "Ewha Womans Univeristy",
                                                         "Freie Universit瓣t Berlin",
                                                         "Fudan University",
                                                         "Grenoble Institute of Technology",
                                                         "HEC Montreal",
                                                         "Hamburg University of Applied Sciences",
                                                         "Handong Global University",
                                                         "Harbin Institute of Technology",
                                                         "Harvard College",
                                                         "Harvard University",
                                                         "Helsinki Metropolia University of Applied Science",
                                                         "Humboldt-Universit瓣t zu Berlin",
                                                         "ISCTE Business School",
                                                         "Imperial College London",
                                                         "Indiana University",
                                                         "Institut D'矇tudes Politiques de Paris",
                                                         "Institut Sup矇rieur d'�ectronique de Paris",
                                                         "Instituto De Empresa",
                                                         "Iowa State University",
                                                         "Jacobs University Bremen",
                                                         "KEDGE Business School",
                                                         "Katholieke Universiteit Leuven",
                                                         "Korea Advanced Institute of Science and Technology",
                                                         "Korea University",
                                                         "Kozminski University",
                                                         "Ko癟 University",
                                                         "Kyoto University",
                                                         "Lancaster University",
                                                         "Lehigh University",
                                                         "Leiden University",
                                                         "Lille Catholic University",
                                                         "Lille Catholic University, EDHEC Business School",
                                                         "Lille Catholic University, IESEG School of Management",
                                                         "Linkopings University",
                                                         "Linnaeus University",
                                                         "Louisiana State University",
                                                         "Lund University",
                                                         "Maastricht University",
                                                         "Massachusetts Institute of Technology",
                                                         "Massey University",
                                                         "McGill University",
                                                         "Memorial University of Newfoundland",
                                                         "Miami University",
                                                         "Michigan State University",
                                                         "Mills College",
                                                         "Missouri University of Science and Technology",
                                                         "Monash University",
                                                         "Munich University of Applied Sciences",
                                                         "NEOMA Business School",
                                                         "NEOMA Business School, Institut de Formation Internationale",
                                                         "Nagoya University",
                                                         "Nanjing University",
                                                         "Nankai University",
                                                         "Nanyang Polytehcnic",
                                                         "Nanyang Technological University",
                                                         "National Central University",
                                                         "National Cheng Kung University",
                                                         "National Chengchi University",
                                                         "National Chiao Tung University",
                                                         "National Research University Higher School of Economics",
                                                         "National Taichung University of Education",
                                                         "National Taiwan University",
                                                         "National Tsing Hua University",
                                                         "National University of Ireland, Galway",
                                                         "National University of Singapore",
                                                         "New York University",
                                                         "Newcastle University",
                                                         "Ngee Ann Polytechnic",
                                                         "Northeastern University",
                                                         "Northumbria University",
                                                         "Northwestern University",
                                                         "Occidental College",
                                                         "Olympic College",
                                                         "Osaka University",
                                                         "Pasadena City College",
                                                         "Peking University",
                                                         "Pohang University of Science and Technology",
                                                         "Politecnico di Milano",
                                                         "Purdue University",
                                                         "Queen's University",
                                                         "RWTH Aachen University",
                                                         "Ramon Llull University",
                                                         "Renmin University of China",
                                                         "Rensselaer Polytechnic Institute",
                                                         "Rice University",
                                                         "Ritsumeikan Asia Pacific University",
                                                         "Royal Institute of Technology",
                                                         "Rutgers, The State University of New Jersey",
                                                         "Ryerson University",
                                                         "SKEMA Business School",
                                                         "Sabanci University",
                                                         "Saint Petersburg State University",
                                                         "Sciences Po",
                                                         "Seoul National University",
                                                         "Shandong University",
                                                         "Shanghai Jiao Tong University",
                                                         "Shanghai University of Finance and Economics",
                                                         "Simon Fraser University",
                                                         "Singapore Management University",
                                                         "Singapore Polytechnic",
                                                         "Smith College",
                                                         "Sophia University",
                                                         "Stanford University",
                                                         "State University of New York at Stony Brook",
                                                         "Stockholm School of Economics",
                                                         "Sun Yat-Sen University",
                                                         "Sungkyunkwan University",
                                                         "Swiss Federal Institute of Technology Zurich",
                                                         "Syracuse University",
                                                         "Tallinn University",
                                                         "Technical University of Denmark",
                                                         "Technische Universiteit Eindhoven",
                                                         "Technische Universit瓣t Darmstadt",
                                                         "Technische Universit瓣t Dortmund",
                                                         "Technische Universit瓣t M羹nchen",
                                                         "Tecnol籀gico de Monterrey",
                                                         "Texas A&M University",
                                                         "The Australian National University",
                                                         "The Catholic University of America",
                                                         "The George Washington University",
                                                         "The Georgia Institute of Technology",
                                                         "The Hebrew University of Jerusalem",
                                                         "The Imperial College of Science, Technology and Medicine",
                                                         "The London School of Economics and Political Science",
                                                         "The Ohio State University",
                                                         "The University Center in Svalbard",
                                                         "The University of Auckland",
                                                         "The University of Birmingham",
                                                         "The University of British Columbia",
                                                         "The University of Cergy-Pontoise",
                                                         "The University of Chicago",
                                                         "The University of Innsbruck",
                                                         "The University of Manchester",
                                                         "The University of Melbourne",
                                                         "The University of New South Wales",
                                                         "The University of Sheffield",
                                                         "The University of Sydney",
                                                         "The University of Texas at Austin",
                                                         "The University of Tokyo",
                                                         "The University of Utah",
                                                         "The University of Warwick",
                                                         "The University of Western Ontario",
                                                         "Tilburg University",
                                                         "Tohoku University",
                                                         "Tsinghua University",
                                                         "Tulane University",
                                                         "Ulsan National Institute of Science and Technology",
                                                         "Universidade Nova de Lisboa, NOVA School of Business and Economics",
                                                         "Universitas Indonesia",
                                                         "Universitat Polit癡cnica de Val癡ncia",
                                                         "Universite Joseph Fourier",
                                                         "Universiti Brunei Darussalam",
                                                         "Universiti Putra Malaysia",
                                                         "University College Dublin",
                                                         "University College London",
                                                         "University Institute of Lisbon",
                                                         "University of Aberdeen",
                                                         "University of Alberta",
                                                         "University of Amsterdam",
                                                         "University of Bergen",
                                                         "University of Bern",
                                                         "University of Bristol",
                                                         "University of Calgary",
                                                         "University of California, Berkeley",
                                                         "University of California, Davis",
                                                         "University of California, Irvine",
                                                         "University of California, Los Angeles",
                                                         "University of California, Merced",
                                                         "University of California, San Diego",
                                                         "University of California, Santa Barbara",
                                                         "University of Cambridge",
                                                         "University of Connecticut",
                                                         "University of Copenhagen",
                                                         "University of Delhi",
                                                         "University of East Anglia",
                                                         "University of Economics, Prague",
                                                         "University of Exeter",
                                                         "University of Florida",
                                                         "University of Glasgow",
                                                         "University of Gothenburg",
                                                         "University of Groningen",
                                                         "University of Hawaii At Manoa",
                                                         "University of Illinois at Urbana - Champaign",
                                                         "University of Iowa",
                                                         "University of Leeds",
                                                         "University of London",
                                                         "University of Manitoba",
                                                         "University of Mannheim",
                                                         "University of Maryland",
                                                         "University of Michigan",
                                                         "University of Michigan, Stephen M Ross School of Business",
                                                         "University of Minnesota",
                                                         "University of Navarra",
                                                         "University of Oxford",
                                                         "University of Pennsylvania",
                                                         "University of Rochester",
                                                         "University of Southampton",
                                                         "University of Southern California",
                                                         "University of Southern California, Marshall School of Business",
                                                         "University of St. Gallen",
                                                         "University of Strathclyde",
                                                         "University of Sussex",
                                                         "University of Tarumanagara",
                                                         "University of Toronto",
                                                         "University of Virginia",
                                                         "University of Waterloo",
                                                         "University of Wisconsin - Madison",
                                                         "Universit�� Commerciale Luigi Bocconi",
                                                         "Uppsala University",
                                                         "Utrecht University",
                                                         "Vanderbilt University",
                                                         "Vienna University of Economics and Business",
                                                         "WHU - Otto Beisheim School of Management",
                                                         "Waseda University",
                                                         "Washington University in St. Louis",
                                                         "Wilfrid Laurier University",
                                                         "Wroclaw University of Technology",
                                                         "Wuhan University",
                                                         "Xi'an Jiaotong University",
                                                         "Yale University",
                                                         "Yonsei University",
                                                         "York University",
                                                         "Zhejiang University"
                                                       };

    // id country institution course_code transfer_course_code restriction db_reference_no
    private static final String SQL_TABLE = "non_local_institutions_credits";
    private static final String STATIC_TABLE = "/static/academic/credit_transfer/nonLocalInstitutionCredits.txt";

    private List<String> nonLocalInstitutionQuery;
    private List<CreditTransferInfo> results;

    NonLocalInstitutionCreditTransfer(final List<String> nonLocalInstitutionQuery) {
        super();
        this.nonLocalInstitutionQuery = nonLocalInstitutionQuery;
    }

    /**
     * This method is used to detect non-local institution subjects (course code) 
     * @param userQuery Original sentence of the user-query
     * @param matchedResults Keywords detected from the user-query
     * @throws NotStaticAccessibleError
     * @throws StaticDatabaseFileNotFoundException
     */
    public static synchronized void detectSubject(final String userQuery, List<String> matchedResults)
        throws NotStaticAccessibleError, StaticDatabaseFileNotFoundException {
        try (StaticDatabaseEngine database = new StaticDatabaseEngine(NonLocalInstitutionCreditTransfer.class, STATIC_TABLE)) {
            Scanner reader = database.executeQuery();

            while (reader.hasNextLine()){
                String line = reader.nextLine();
                if (line.startsWith("#") || line.length() == 0)
                    continue;

                List<String> row = reconstruct(line);

                if (userQuery.contains(row.get(3)))
                    matchedResults.add(row.get(3));
            }
        }
    }

    /**
     * This method will connect to the SQL database and retrieve data on details of non local institution credit transfer.
     * @return String
     * @throws NotSQLAccessibleError
     * @throws URISyntaxException
     * @throws SQLException
     */
    @Override
    public synchronized String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException {
        try (SQLDatabaseEngine database = new SQLDatabaseEngine(this.getClass(), SQL_TABLE, System.getenv("NON_LOCAL_INSTITUTION_DATABASE_URL"))) {
            results = Collections.synchronizedList(new ArrayList<>());

            StringBuilder SQLStatement = new StringBuilder("SELECT country, institution, course_code, transfer_course_code, restriction FROM ")
                                             .append(SQL_TABLE)
                                             .append(" WHERE course_code = ?");
            for (int i = 1; i < nonLocalInstitutionQuery.size(); i++)
                SQLStatement.append(" OR course_code = ?");

            PreparedStatement SQLQuery = database.prepare(SQLStatement.toString());
            for (int i = 0; i < nonLocalInstitutionQuery.size(); i++)
                SQLQuery.setString(i + 1, nonLocalInstitutionQuery.get(i));

            ResultSet reader = database.executeQuery();
            while (reader.next()){
                results.add(new InstitutionCreditTransferInfo(reader.getString(1), reader.getString(2),
                                                              reader.getString(3), reader.getString(4),
                                                              reader.getString(5)));
            }

            return super.replyResults(results);
        }
    }

    /**
     * This method will connect to the static database and retrieve data on details of credit transfer.
     * It is used when the SQL database has run into some trouble of connection.
     * @return String
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
                if (nonLocalInstitutionQuery.contains(row.get(3)))
                    results.add(new InstitutionCreditTransferInfo(row.get(1), row.get(2), row.get(3),
                                                                  row.get(4), row.get(5)));
            }

            return super.replyResults(results);
        }
    }
}
