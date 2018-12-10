package se.kth.iv1351.grupp23.museum;

/* This program is an simple program using the project database.
 ** It uses the JDBC driver UCanAccess for Microsoft Access.
 **
 ** This program is based on a program written by nikos dimitrakas
 ** for use in the basic database courses.
 **
 ** There is no error management in this program.
 ** Instead an exception is thrown. Ideally all exceptions
 ** should be caught and managed appropriately. But this 
 ** program's goal is only to illustrate the basic JDBC classes.
 **
 ** Last modified by Anders Gawell on 2015-10-07
 */

import java.sql.*;

public class Museum
{

    // DB connection variable
    static protected Connection conn;
    // DB access variables
    private String URL = "jdbc:ucanaccess://c:/path/to/access/database/file.accdb";
    private String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
    private String userID = "";
    private String password = "";

private static final String ALL_GUIDES = "SELECT personnr, förnamn, efternamn FROM Guide";
	private static final String KNOWN_LANGUAGES = "SELECT språk, kunskapnivå from SpråkKunskap WHERE guide=?";
	private static final String NEW_LANGUAGE_SKILL = "INSERT INTO SpråkKunskap (guide, språk, kunskapnivå) VALUES (?, ?, ?)";
	
    // method for establishing a DB connection
    public void connect()
    {
        try
        {
            // register the driver with DriverManager
            Class.forName(driver);
            //create a connection to the database
            conn = DriverManager.getConnection(URL, userID, password);
            // Set the auto commit of the connection to false.
            // An explicit commit will be required in order to accept
            // any changes done to the DB through this connection.
            conn.setAutoCommit(false);
				//Some logging
				System.out.println("Connected to " + URL + " using "+ driver);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	public void showAllGuides() throws Exception
	{
		String query = ALL_GUIDES;
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		System.out.println("Alla guider:");
		System.out.println("Personnr\tFörnamn\tEfternamn");
		while(rs.next())
		{
			String persnr = rs.getString("personnr");
			String enamn = rs.getString("efternamn");
			String fnamn = rs.getString("förnamn");
			System.out.println(String.format("%s\t%s\t%s", persnr, fnamn, enamn));
		}
		statement.close();
	}
	public void getGuideLanguages() throws Exception
    {
       
        // Create a Scanner in order to allow the user to provide input.
        java.util.Scanner in = new java.util.Scanner(System.in);

        // This is the old way (Java 1.4 or earlier) for reading user input:
        // Create a BufferedReader in order to allow the user to provide input.
        // java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        // Ask the user to specify city.
        System.out.print("Ange guidens personnr: ");
        // Retrieve the value and place it in the variable param.
        String param = in.nextLine();

        // Set the SQL statement into the query variable
        String query = KNOWN_LANGUAGES;

        // Create a statement associated to the connection and the query.
        // The new statement is placed in the variable statement.
        PreparedStatement statement = conn.prepareStatement(query);

        // Provide the value for the ? in the SQL statement.
        // The value of the variable param will be sent to the database manager
        // through the variables statement and conn.
        statement.setString(1, param);

        // Execute the SQL statement that is prepared in the variable statement
        // and store the result in the variable rs.
        ResultSet rs = statement.executeQuery();

        System.out.println("Guide " + param + " talar :");

        // Loop through the result set and print the results.
        // The method next() returns false when there are no more rows.
        System.out.println("Språk\t\tKunskapsnivå");
		while (rs.next())
        {
            System.out.println(rs.getString("språk") + "\t\t" + rs.getString("kunskapnivå"));
        }

        // Close the variable statement and release all resources bound to it
        // Any ResultSet associated to the Statement will be automatically closed too.
        statement.close();
    }

	public void insertNewLanguageSkill() throws Exception
    {
        // Local variables
        String query;
        PreparedStatement statement;
        String sprakparam;
		String guideparam;
		String nivaparam;

        // Create a Scanner in order to allow the user to provide input.
        java.util.Scanner in = new java.util.Scanner(System.in);

        // Ask the user to specify a value for personnr.
        System.out.print("Ange guidens personnr: ");
        // Retrieve the value and place it in the variable guideparam.
        guideparam = in.nextLine();
		
		System.out.println("Ange språk: ");
		sprakparam = in.nextLine();
		
		System.out.println("Ange kunskapsnivå: ");
		nivaparam = in.nextLine();

        // Set the SQL statement into the query variable
        query = NEW_LANGUAGE_SKILL;

        // Create a statement associated to the connection and the query.
        // The new statement is placed in the variable statement.
        statement = conn.prepareStatement(query);

        // Provide the values for the ?'s in the SQL statement.
        // The value of the variable guideparam is the first,
        // sprakparam is second and nivaparam is third.
        statement.setString(1, guideparam);
        statement.setString(2, sprakparam);
        statement.setString(3, nivaparam);

        // Execute the SQL statement that is prepared in the variable statement
        statement.executeUpdate();

        // Close the variable statement and release all resources bound to it
        statement.close();
    }

    public static void main(String[] argv) throws Exception
    {
        // Create a new object of this class.
        Museum m = new Museum();

        // Call methods on the object m.
		  System.out.println("-------- connect() ---------");
        m.connect();
		  System.out.println("-------- showAllGuides() ---------");
        m.showAllGuides();
		  System.out.println("-------- getGuideLanguages() ---------");
        m.getGuideLanguages();
		  System.out.println("-------- insertNewLanguageSkill() ---------");
        m.insertNewLanguageSkill();

        // Commit the changes made to the database through this connection.
        conn.commit();
        // Close the connection.
        conn.close();
    }
}
