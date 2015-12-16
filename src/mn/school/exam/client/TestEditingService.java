package mn.goody.exam.client;

import java.util.List;

import mn.goody.exam.shared.Quiz;
import mn.goody.exam.shared.Test;
import mn.goody.exam.shared.Topic;
import mn.goody.exam.shared.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("designer")
public interface TestEditingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	// User operations
	boolean login(String uid, String pwd);

	String addUser(User u);

	void removeUser(String uid);

	// Quiz operations
	String saveTopic(Topic t);

	void removeTopics(List<String> ids);

	List<Topic> getRootTopics();

	List<Topic> getChildTopics(String parent);

	String saveQuiz(Quiz q);

	void removeQuizs(List<String> ids);

	List<Quiz> getQuizs(Topic cat);

	// Test operations
	String saveTest(Test t);

	List<Test> getTest();

	List<Test> getTestsForTeacher(String uid);

	void removeTests(List<String> ids);

}
