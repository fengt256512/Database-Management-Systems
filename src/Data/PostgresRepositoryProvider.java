package Data;

import java.sql.Connection;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.postgresql.ds.PGSimpleDataSource;
import Business.Issue;
import Presentation.IRepositoryProvider;

/**
 * Encapsulates create/read/update/delete operations to PostgreSQL database
 * @author PaulG
 */
public class PostgresRepositoryProvider implements IRepositoryProvider {
	   // connection parameters - ENTER YOUR LOGIN AND PASSWORD HERE
    private final String userid = "y20s1c9120_feli5949";
    private final String passwd = "597lifengteng695";
    private final String myHost = "soit-db-pro-2.ucc.usyd.edu.au";

    
	private Connection openConnection() throws SQLException
	{
			PGSimpleDataSource source = new PGSimpleDataSource();
			source.setServerName(myHost);
			source.setDatabaseName(userid);
			source.setUser(userid);
			source.setPassword(passwd);
			Connection conn = source.getConnection();
		    
		    return conn;
	} 
	
	
	public String takeName(int id) {
		Connection conn = null;
		String name = null;

		try {
			conn = openConnection();
			PreparedStatement flag = conn.prepareStatement("SELECT USER_ID,USERNAME FROM A3_USER");
			ResultSet nameSet = flag.executeQuery();
			while(nameSet.next()) {
				if(nameSet.getInt(1) == id) {
					name = nameSet.getString(2);
				}
			}
		}catch (SQLException e) {
			//handle exception
			System.out.println("Exception:" + e.getMessage());
		}
		if (conn != null) {
			try{
				conn.close();
			} catch(SQLException e) {
				//handle exception
				System.out.println("Exception: "+ e.getMessage());	
			}
		}
		return name;
	}
	
	/**
	 * validate a user login request
	 * @param userName: the user trying to login
	 * @return userId for user (or 0 if username not found)
	 */
	@Override
	public int checkUserCredentials(String userName) {
	    //TODO - validate a user login request
		Connection conn = null;
		int index = 0;

		try {
			conn = openConnection();
			PreparedStatement flag = conn.prepareStatement("SELECT USERNAME FROM A3_USER");
			ResultSet flagSet = flag.executeQuery();
			while(flagSet.next()) {
				if(flagSet.getString(1).equalsIgnoreCase(userName)) {
					index = flagSet.getRow();
				}
			}
		}catch (SQLException e) {
			System.out.println("Exception:" + e.getMessage());
		}
		if (conn != null) {
			try{  
				conn.close();
			} catch(SQLException e) {
				//handle exception
				System.out.println("Exception: "+ e.getMessage());	
			}
		}
		if(index == 0) {
			System.out.println("Rejected.");
		}
		if(index != 0) {
			System.out.println(userName + "'s userId is: " + index);
		}
		return index;
	}
	
	/**
	 * Find the user associated issues as per the assignment description
	 * @param userId the user id
	 * @return
	 */
	@Override
	public Vector<Issue> findUserIssues(int userId) {		
		Vector<Issue> results = new Vector<Issue>();
		Connection conn = null;
		// TODO - list all user associated issues from db using sql
		try {
			conn = openConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT ISSUE_ID,TITLE,DESCRIPTION,CREATOR,RESOLVER,VERIFIER FROM A3_ISSUE WHERE CREATOR=? OR VERIFIER=? OR RESOLVER=?" 
					                                + "ORDER BY TITLE");
			stmt.setInt(1, userId);
			stmt.setInt(2, userId);
			stmt.setInt(3, userId);
			ResultSet resSet = stmt.executeQuery();
			
			int index = 0;
			while(resSet.next()) {
				Issue issue = new Issue();
				// the following code illustrates the structure of Issue
				issue.setId(resSet.getInt("ISSUE_ID"));
				issue.setTitle(resSet.getString("TITLE"));
				issue.setDescription(resSet.getString("DESCRIPTION"));
				int creat = resSet.getInt("CREATOR");
				String creator = takeName(creat);
				issue.setCreator(creator);	
				int resolve = resSet.getInt("RESOLVER");
				String resolver = takeName(resolve);
				issue.setResolver(resolver);
				int verify = resSet.getInt("VERIFIER");
				String verifier = takeName(verify);
				issue.setVerifier(verifier);
				results.add(issue);	
				index++;
			}
			if(index == 0) {
				System.out.println("Cannot find any question");
			}
			stmt.close();
		}catch (SQLException e) {
			//handle exception
			System.out.println("Exception:" + e.getMessage());
		}
		if (conn != null) {
			try{
				conn.close();
			} catch(SQLException e) {
				//handle exception
				System.out.println("Exception:" + e.getMessage());	
			}
		}
		return results;
	}

	/**
	 * Find the issues based on the searchString provided as the parameter
	 * @param searchString: see assignment description search specification
	 * @return
	 */
	@Override
	public Vector<Issue> findIssueBasedOnExpressionSearchOnTitle(String searchString) {
		
		Vector<Issue> results = new Vector<Issue>();
		// TODO - find necessary issues using sql database based on search input
		Connection conn = null;
		try {
			conn = openConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
		
			CallableStatement call = conn.prepareCall("{call Search1(?, ?)}");
			call.setString(1, searchString);
			//call.setInt(2, userId);
			call.registerOutParameter(2, java.sql.Types.REF_CURSOR);
			call.executeUpdate();
			ResultSet resSet = (ResultSet) call.getObject(2);

			int index = 0;
			while(resSet.next()) {
				Issue issue = new Issue();
				issue.setId(resSet.getInt("ISSUE_ID"));
				issue.setTitle(resSet.getString("TITLE"));
				issue.setDescription(resSet.getString("DESCRIPTION"));
				int create = resSet.getInt("CREATOR");
				String creator = takeName(create);
				issue.setCreator(creator);
				int resolve = resSet.getInt("RESOLVER");
				String resolver = takeName(resolve);
				issue.setResolver(resolver);
				int verify = resSet.getInt("VERIFIER");
				String verifier = takeName(verify);
				issue.setVerifier(verifier);
				results.add(issue);
				index++;
			} 
			if(index == 0) {
				System.out.println("Cannot find issue");
			}
			stmt.close();
		}catch (SQLException e) {
			//handle exception
			System.out.println("Exception:" + e.getMessage());
		}
		if (conn != null) {
			try{
				conn.close();
			} catch(SQLException e) {
				//handle exception
				System.out.println("Exception:" + e.getMessage());	
			}
		}
		return results;
	}

	/**
	 * Add the details for a new issue to the database
	 * @param issue: the new issue to add
	 */
	@Override
	public void addIssue(Issue issue) {
	    //TODO - add an issue
	    //Insert a new issue to database
		Connection conn = null;
		try {
			conn = openConnection();
			PreparedStatement stmt = conn.prepareStatement("Insert into A3_ISSUE (CREATOR, VERIFIER, RESOLVER, TITLE, DESCRIPTION) Values(?,?,?,?,?)");
			
			int creat = checkUserCredentials(issue.getCreator());
			int verify =  checkUserCredentials(issue.getVerifier());
			int reSolve = checkUserCredentials(issue.getResolver());
			stmt.setInt(1, creat);
			stmt.setInt(2, verify);
			stmt.setInt(3, reSolve);
			stmt.setString(4, issue.getTitle());
			stmt.setString(5, issue.getDescription());
			stmt.executeUpdate();
		}catch (SQLException e) {
			//handle exception
			System.out.println("Unknown users: Rejected");
		}
		if (conn != null) {
			try{
				conn.close();
			} catch(SQLException e) {
				//handle exception
				System.out.println("Exception:" + e.getMessage());	
			}
		}
	}
	
	/**
	 * Update the details for a given issue
	 * @param issue : the issue for which to update details
	 */
	@Override
	public void updateIssue(Issue issue) {
		//TODO - update the issue using db
		Connection conn = null;
		try {
			conn = openConnection();
			PreparedStatement stmt = conn.prepareStatement("UPDATE A3_ISSUE SET CREATOR = ?, VERIFIER = ?, RESOLVER = ?, TITLE = ?, DESCRIPTION = ? WHERE ISSUE_ID = ?");
			int creat = checkUserCredentials(issue.getCreator());
			int verify =  checkUserCredentials(issue.getVerifier());
			int reSolve = checkUserCredentials(issue.getResolver());
			stmt.setInt(1, creat);
			stmt.setInt(2, verify);
			stmt.setInt(3, reSolve);
			stmt.setString(4, issue.getTitle());
			stmt.setString(5, issue.getDescription());
			stmt.setInt(6, issue.getId());
			stmt.executeUpdate();
			}catch (SQLException e) {
				//handle exception
				System.out.println("Exception:" + e.getMessage());
			}
		if (conn != null) {
			try{
				conn.close();
			} catch(SQLException e) {
				//handle exception
				System.out.println("Exception:" + e.getMessage());	
			}
		}
	}
}
