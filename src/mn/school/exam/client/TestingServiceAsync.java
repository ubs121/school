package mn.goody.exam.client;

import java.util.List;

import mn.goody.exam.shared.Test;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TestingServiceAsync {
    void greetServer(String name, AsyncCallback<String> callback);
    
    void getTestingsForStudent(String uid, AsyncCallback<List<Test>> callback);
}
