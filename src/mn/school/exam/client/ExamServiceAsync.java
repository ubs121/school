package mn.goody.exam.client;

import java.util.List;

import mn.goody.exam.shared.Quiz;
import mn.goody.exam.shared.Test;
import mn.goody.exam.shared.Topic;
import mn.goody.exam.shared.User;
import mn.goody.exam.shared.UserGroup;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client side stub for the RPC service.
 */
public interface ExamServiceAsync {
	// хэрэглэгчтэй холбоотой үйлдлүүд
	void login(String uid, String pwd, AsyncCallback<User> callback);

	void saveUser(User u, AsyncCallback<String> callback);

	void removeUsers(List<String> ids, AsyncCallback<Void> callback);

	void saveUserGroup(UserGroup gr, AsyncCallback<String> callback);

	void queryUsersByGroup(String gid, AsyncCallback<List<User>> callback);

	void queryGroups(String type, AsyncCallback<List<UserGroup>> callback);

	// асуулттай холбоотой үйлдлүүд
	void saveTopic(Topic t, AsyncCallback<String> callback);

	void removeTopics(List<String> ids, AsyncCallback<Void> callback);

	void queryTopics(String parent, AsyncCallback<List<Topic>> callback);

	void saveQuiz(Quiz q, AsyncCallback<Long> callback);

	void removeQuizs(List<Long> ids, AsyncCallback<Void> callback);

	void queryQuizs(String topic, AsyncCallback<List<Quiz>> callback);

	// тесттэй холбогдох үйлдлүүд
	void saveTest(Test t, AsyncCallback<String> callback);

	void queryTests(User u, AsyncCallback<List<Test>> callback);

	void removeTests(List<String> ids, AsyncCallback<Void> callback);

}
