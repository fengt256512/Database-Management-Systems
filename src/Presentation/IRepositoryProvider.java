package Presentation;

import java.util.Vector;

import Business.Issue;

/**
 * Encapsulates create/read/update/delete operations to database
 * @author matthewsladescu
 *
 */
public interface IRepositoryProvider {
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
	 * Find the user associated issues as per the assignment description
	 * @param id the user id
	 * @return
	 */
	public Vector<Issue>  findUserIssues(int id);
	
	/**
	 * Find the associated issues for the user with the given userId based 
	 * on the searchString provided as the parameter
	 * @param searchString: see assignment description search specification
	 * @return
	 */
	public Vector<Issue> findIssueBasedOnExpressionSearchOnTitle(String searchString);	
	
	/**
	 * Add the details for a new issue to the database
	 * @param issue: the new issue to add
	 */
	public void addIssue(Issue issue);
}
