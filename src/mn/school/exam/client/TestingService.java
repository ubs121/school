package mn.goody.exam.client;

import java.util.List;

import mn.goody.exam.shared.Test;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tester")
public interface TestingService extends RemoteService {
    String greetServer(String name) throws IllegalArgumentException;
    
    List<Test> getTestingsForStudent(String uid);
}
