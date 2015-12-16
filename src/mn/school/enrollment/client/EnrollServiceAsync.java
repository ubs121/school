package mn.school.enrollment.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>EnrollService</code>.
 */
public interface EnrollServiceAsync {
    void greetServer(String input, AsyncCallback<String> callback)
	    throws IllegalArgumentException;

    void бүртгэх(EnrollRequestClient e, AsyncCallback<String> callback);

    void батлах(String k, AsyncCallback<String> callback);

    void цуцлах(String k, AsyncCallback<String> callback);

    void элсэхХүсэлтүүд(AsyncCallback<List<EnrollRequestClient>> callback);

    void элсэхХүсэлт(String k, AsyncCallback<EnrollRequestClient> callback);

    void дугаарАвах(String name, String email, AsyncCallback<String> callback);

    void нэвтрэх(String email, String key,
	    AsyncCallback<EnrollRequestClient> callback);

    void зөвшөөрөх(String key, AsyncCallback<String> callback);

    void татгалзах(String key, String comment, AsyncCallback<String> callback);

    void хүсэлтУстгах(List<String> keys, AsyncCallback<Void> callback);

    void элсэхХүсэлтүүд(String where, String order, String range,
	    AsyncCallback<List<EnrollRequestClient>> callback);
}
