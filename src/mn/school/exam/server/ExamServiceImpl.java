package mn.goody.exam.server;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import mn.goody.exam.client.ExamService;
import mn.goody.exam.shared.Quiz;
import mn.goody.exam.shared.Test;
import mn.goody.exam.shared.Topic;
import mn.goody.exam.shared.User;
import mn.goody.exam.shared.UserGroup;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ExamServiceImpl extends RemoteServiceServlet implements
		ExamService {

	@Override
	public void removeQuizs(List<Long> ids) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Quiz.class, ":p.contains(id)");
		try {
			query.deletePersistentAll(ids);
		} finally {
			pm.close();
		}
	}

	@Override
	public Long saveQuiz(Quiz q) {
		Long new_id = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Quiz ret = pm.makePersistent(q);
			new_id = ret.getId();
		} finally {
			pm.close();
		}

		return new_id;
	}

	@Override
	public void removeTests(List<String> ids) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Test.class, ":p.contains(id)");
		try {
			query.deletePersistentAll(ids);
		} finally {
			pm.close();
		}
	}

	@Override
	public void removeTopics(List<String> ids) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Topic.class, ":p.contains(id)");
		try {
			query.deletePersistentAll(ids);
		} finally {
			pm.close();
		}
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
	public User login(String uid, String pwd) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(User.class);
		query.setFilter("uid == userid && pwd == pass");
		query.declareParameters("String userid, String pass");

		User u = null;
		List<User> persisted = (List<User>) pm.newQuery(query)
				.execute(uid, pwd);

		if (persisted.size() > 0) {
			u = pm.detachCopy(persisted.get(0));

			// нэвтэрсэн огноо, хаягыг бүртгэх
			u.setLastLogin(new Date());
			final String ip = getThreadLocalRequest().getRemoteAddr();
			u.setLastIp(ip);

			saveUser(u);
		}
		pm.close();

		return u;
	}

	@Override
	public String saveUser(User u) {
		String new_id = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			User au = pm.makePersistent(u);
			new_id = au.getId();
		} finally {
			pm.close();
		}
		return new_id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> queryUsersByGroup(String gid) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(User.class);
		query.setFilter("group_id == gid");
		query.setOrdering("firstName");
		query.declareParameters("String gid");

		List<User> detached = null;
		try {
			List<User> persisted = (List<User>) pm.newQuery(query).execute(gid);
			detached = (List<User>) pm.detachCopyAll(persisted);
		} finally {
			pm.close();
		}
		return detached;
	}

	@Override
	public void removeUsers(List<String> ids) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(User.class, ":p.contains(id)");
		try {
			query.deletePersistentAll(ids);
		} finally {
			pm.close();
		}
	}

	@Override
	public String saveUserGroup(UserGroup gr) {
		String new_id = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserGroup ug = pm.makePersistent(gr);
			new_id = ug.getId();
		} finally {
			pm.close();
		}
		return new_id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserGroup> queryGroups(String type) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(UserGroup.class);
		query.setFilter("type == t");
		query.declareParameters("String t");

		List<UserGroup> detached = null;
		try {
			List<UserGroup> persisted = (List<UserGroup>) pm.newQuery(query)
					.execute(type);
			detached = (List<UserGroup>) pm.detachCopyAll(persisted);
		} finally {
			pm.close();
		}
		return detached;
	}

	@Override
	public List<Test> queryTests(User u) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Test.class);
		// query.setFilter("type == t");
		// query.declareParameters("String t");

		List<Test> detached = null;
		try {
			List<Test> persisted = (List<Test>) pm.newQuery(query).execute();
			detached = (List<Test>) pm.detachCopyAll(persisted);
		} finally {
			pm.close();
		}
		return detached;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> queryTopics(String parent) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Topic.class, "parent_id == parent");
		q.declareParameters("String parent");

		List<Topic> detachedTopics = null;
		try {
			List<Topic> persistedTopics = (List<Topic>) pm.newQuery(q).execute(
					parent);
			detachedTopics = (List<Topic>) pm.detachCopyAll(persistedTopics);
			// System.out.println(parent + ": " + detachedTopics.size());
		} finally {
			pm.close();
		}
		return detachedTopics;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Quiz> queryQuizs(String topic) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Quiz.class, "topic_id == topic");
		q.declareParameters("String topic");
		List<Quiz> detached = null;
		try {
			List<Quiz> persisted = (List<Quiz>) pm.newQuery(q).execute(topic);
			detached = (List<Quiz>) pm.detachCopyAll(persisted);
		} finally {
			pm.close();
		}
		return detached;
	}
}
