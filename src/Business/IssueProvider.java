package Business;

import java.util.Vector;

import Data.RepositoryProviderFactory;

/**
 * Encapsulates any business logic to be executed on the app server; 
 * and uses the data layer for data queries/creates/updates/deletes
 * @author matthewsladescu
 *
 */
public class IssueProvider implements IIssueProvider{

	/**
	 * check userName is in the database and return their userId (or 0 if not known)
	 * @param issue : the issue for which to update details
	 */
	@Override
	public int checkUserCredentials(String checkUserCredentials) {
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().checkUserCredentials(checkUserCredentials);
	}
	
	/**
	 * Update the details for a given issue
	 * @param issue : the issue for which to update details
	 */
	@Override
	public void updateIssue(Issue issue) {
		RepositoryProviderFactory.getInstance().getRepositoryProvider().updateIssue(issue);
	}

	/**
	 * Find the issues associated in some way with a user
	 * Issues which have the id parameter below in any one or more of the
	 * creator, resolver, or verifier fields should be included in the result
	 * @param id
	 * @return
	 */
	@Override
	public Vector<Issue> findUserIssues(int id) {		
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().findUserIssues(id);
	}
	
	/**
	 * Add the details for a new issue to the database
	 * @param issue: the new issue to add
	 */
	@Override
	public void addIssue(Issue issue) {
		RepositoryProviderFactory.getInstance().getRepositoryProvider().addIssue(issue);
	}

	/**
	 * Given an expression searchString like 'word' or 'this phrase', this method should return any issues 
	 * that contains this phrase in its title.
	 * @param searchString: the searchString to use for finding issues in the database based on the issue titles
	 * @return
	 */
	@Override
	public Vector<Issue> findIssueBasedOnExpressionSearchOnTitle(String searchString) {
		return RepositoryProviderFactory.getInstance().getRepositoryProvider().findIssueBasedOnExpressionSearchOnTitle(searchString);

	}

}
