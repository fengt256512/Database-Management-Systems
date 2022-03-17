package Business;

import java.util.Vector;

/**
 * Encapsulates any business logic to be executed on the app server; 
 * and uses the data layer for data queries/creates/updates/deletes
 * @author matthewsladescu
 *
 */
public interface IIssueProvider {

	/**
	 * check userName is in the database and return their userId (or 0 if not known)
	 * @param issue : the issue for which to update details
	 */
	public int checkUserCredentials(String userName);
	
	/**
	 * Update the details for a given issue
	 * @param issue : the issue for which to update details
	 */
	public void updateIssue(Issue issue);
	
	/**
	 * Find the issues associated in some way with a user
	 * Issues which have the id parameter below in any one or more of the
	 * creator, resolver, or verifier fields should be included in the result
	 * @param id
	 * @return
	 */
	public Vector<Issue>  findUserIssues(int id);
	
	/**
	 * Given an expression searchString like 'word' or 'this phrase', this method should return any issues 
	 * that contains this phrase in its title. 
	 * @param searchString: the searchString to use for finding issues in the database based on the issue titles
	 * @return
	 */
	public Vector<Issue> findIssueBasedOnExpressionSearchOnTitle(String searchString);	
	
	/**
	 * Add the details for a new issue to the database
	 * @param issue: the new issue to add
	 */
	public void addIssue(Issue issue);
}
