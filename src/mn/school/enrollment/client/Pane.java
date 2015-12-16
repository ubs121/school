package mn.school.enrollment.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

public class Pane extends Composite {
	protected static EnrollServiceAsync proxy = null;
	protected static EnrollRequestClient enrollObj;

	// програмын дэлгэцүүд
	protected RegisterPage enrollPage;
	protected LoginPage loginPage;
	protected AdminPage adminPage;

	public EnrollServiceAsync getServiceProxy() {
		if (proxy == null) {
			proxy = (EnrollServiceAsync) GWT.create(EnrollService.class);
		}
		return proxy;
	}

	public void setEnrollObj(EnrollRequestClient o) {
		enrollObj = o;
	}

	public EnrollRequestClient getEnrollObj() {
		return enrollObj;
	}

	public void бүртгэхХуудас() {
		RootPanel.get("content").clear();
		if (enrollPage == null)
			enrollPage = new RegisterPage();
		RootPanel.get("content").add(enrollPage);
	}

	public void нэвтрэхХуудас() {
		RootPanel.get("content").clear();
		if (loginPage == null)
			loginPage = new LoginPage();
		RootPanel.get("content").add(loginPage);
	}

	public void удирдахХуудас() {
		RootPanel.get("content").clear();
		if (adminPage == null)
			adminPage = new AdminPage();
		RootPanel.get("content").add(adminPage);
	}
}
