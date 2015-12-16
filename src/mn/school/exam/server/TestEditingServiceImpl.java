package mn.goody.exam.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import mn.goody.exam.client.TestEditingService;
import mn.goody.exam.shared.FieldVerifier;
import mn.goody.exam.shared.Quiz;
import mn.goody.exam.shared.Test;
import mn.goody.exam.shared.Topic;
import mn.goody.exam.shared.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TestEditingServiceImpl extends RemoteServiceServlet implements
		TestEditingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * ids дугаартай асуултуудыг хасах
	 */
	@Override
	public void removeQuizs(List<String> ids) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Quiz.class, ":p.contains(id)");
		query.deletePersistentAll(ids);
		pm.close();
	}

	/**
	 * q асуултыг хадгалах
	 */
	@Override
	public String saveQuiz(Quiz q) {
		String new_id = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Quiz ret = pm.makePersistent(q);
			new_id = ret.getId();
		} finally {
			pm.close();
		}

		return new_id;
	}

	/**
	 * topic сэдэвт хамаарах асуултуудыг унших
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Quiz> getQuizs(Topic topic) {
		// TODO: where topic='topic'
		String query = "select from " + Quiz.class.getName();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Quiz> detachedQuizs = null;
		List<Quiz> persistedQuizs = (List<Quiz>) pm.newQuery(query).execute();
		detachedQuizs = (List<Quiz>) pm.detachCopyAll(persistedQuizs);
		pm.close();
		return detachedQuizs;
	}

	/**
	 * Тестүүд унших
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Test> getTest() {
		String query = "select from " + Test.class.getName();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Test> detachedTests = null;
		List<Test> persistedTests = (List<Test>) pm.newQuery(query).execute();
		detachedTests = (List<Test>) pm.detachCopyAll(persistedTests);
		pm.close();
		return detachedTests;
	}

	@Override
	public List<Topic> getRootTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTests(List<String> ids) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Test.class, ":p.contains(id)");
		query.deletePersistentAll(ids);
		pm.close();
	}

	@Override
	public void removeTopics(List<String> ids) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Topic.class, ":p.contains(id)");
		query.deletePersistentAll(ids);
		pm.close();
	}

	/**
	 * Тестийг хадгалах
	 */
	@Override
	public String saveTest(Test t) {
		String new_id = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Test pt = pm.makePersistent(t);
			new_id = pt.getId();
		} finally {
			pm.close();
		}

		return new_id;
	}

	@Override
	public String saveTopic(Topic t) {
		String new_id = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Topic to = pm.makePersistent(t);
			new_id = to.getId();
		} finally {
			pm.close();
		}
		return new_id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> getChildTopics(String parent_id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Topic.class, "parent_id == parent");
		q.declareParameters("java.lang.String parent");

		List<Topic> detachedTopics = null;
		List<Topic> persistedTopics = (List<Topic>) q.execute(parent_id);
		detachedTopics = (List<Topic>) pm.detachCopyAll(persistedTopics);
		pm.close();
		return detachedTopics;
	}

	@Override
	public List<Test> getTestsForTeacher(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean login(String uid, String pwd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String addUser(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeUser(String uid) {
		// TODO Auto-generated method stub

	}

}
