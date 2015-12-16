package mn.goody.exam.client;

import java.util.List;

import mn.goody.exam.shared.Quiz;
import mn.goody.exam.shared.Test;
import mn.goody.exam.shared.Topic;
import mn.goody.exam.shared.User;
import mn.goody.exam.shared.UserGroup;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("examservice")
public interface ExamService extends RemoteService {
	// хэрэглэгчтэй холбоотой үйлдлүүд
	User login(String uid, String pwd);

	String saveUser(User u);

	void removeUsers(List<String> ids);

	String saveUserGroup(UserGroup gr);

	List<User> queryUsersByGroup(String gid);

	List<UserGroup> queryGroups(String type);

	// асуулттай холбоотой үйлдлүүд
	String saveTopic(Topic t);

	void removeTopics(List<String> ids);

	List<Topic> queryTopics(String parent);

	Long saveQuiz(Quiz q);

	void removeQuizs(List<Long> ids);

	List<Quiz> queryQuizs(String topic);

	// тесттэй холбогдох үйлдлүүд
	String saveTest(Test t);

	List<Test> queryTests(User u);

	void removeTests(List<String> ids);

}
