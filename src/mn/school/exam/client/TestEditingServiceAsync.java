package mn.goody.exam.client;

import java.util.List;

import mn.goody.exam.shared.Quiz;
import mn.goody.exam.shared.Test;
import mn.goody.exam.shared.Topic;
import mn.goody.exam.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client side stub for the RPC service.
 */
public interface TestEditingServiceAsync {
	void greetServer(String name, AsyncCallback<String> callback);

	// User operations
	void login(String uid, String pwd, AsyncCallback<Boolean> callback);

	void addUser(User u, AsyncCallback<String> callback);

	void removeUser(String uid, AsyncCallback<Void> callback);

	// Quiz operations
	void saveTopic(Topic t, AsyncCallback<String> callback);

	void removeTopics(List<String> ids, AsyncCallback<Void> callback);

	void getRootTopics(AsyncCallback<List<Topic>> callback);

	void getChildTopics(String parent, AsyncCallback<List<Topic>> callback);

	void saveQuiz(Quiz q, AsyncCallback<String> callback);

	void removeQuizs(List<String> ids, AsyncCallback<Void> callback);

	void getQuizs(Topic cat, AsyncCallback<List<Quiz>> callback);

	// Test operations
	void saveTest(Test t, AsyncCallback<String> callback);

	void getTest(AsyncCallback<List<Test>> callback);

	void getTestsForTeacher(String uid, AsyncCallback<List<Test>> callback);

	void removeTests(List<String> ids, AsyncCallback<Void> callback);

}
