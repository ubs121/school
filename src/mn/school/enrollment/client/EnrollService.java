package mn.school.enrollment.client;

import java.util.List;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("enroll")
public interface EnrollService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	// нэвтрэх
	String дугаарАвах(String name, String email);
	EnrollRequestClient нэвтрэх(String email, String key);
	
	// элсэх хүсэлтийн үйлдлүүд
	String бүртгэх(EnrollRequestClient e);
	String батлах(String key);
	String цуцлах(String key);
	
	// элсэлтийн менежерийн үйлдлүүд
	String зөвшөөрөх(String key);
	String татгалзах(String key, String comment);
	
	// элсэх хүсэлт унших, устгах
	EnrollRequestClient элсэхХүсэлт(String k);
	List<EnrollRequestClient> элсэхХүсэлтүүд();
	List<EnrollRequestClient> элсэхХүсэлтүүд(String where, String order, String range);
	void хүсэлтУстгах(List<String> keys);
}
