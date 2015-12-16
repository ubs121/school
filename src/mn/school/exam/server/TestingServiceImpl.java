package mn.goody.exam.server;

import java.util.List;

import mn.goody.exam.client.TestingService;
import mn.goody.exam.shared.FieldVerifier;
import mn.goody.exam.shared.Test;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TestingServiceImpl extends RemoteServiceServlet implements
		TestingService {

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

	@Override
	public List<Test> getTestingsForStudent(String uid) {
		// TODO Auto-generated method stub
		return null;
	}
}
