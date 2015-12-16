package mn.goody.exam.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HomePage extends Composite {

	private static HomePageUiBinder uiBinder = GWT
			.create(HomePageUiBinder.class);

	interface HomePageUiBinder extends UiBinder<Widget, HomePage> {
	}

	private Exampro root;

	@UiField
	TextBox txtUid;
	@UiField
	PasswordTextBox txtPwd;

	public HomePage(Exampro root) {
		initWidget(uiBinder.createAndBindUi(this));
		this.root = root;
	}

	@UiHandler("loginButton")
	public void doLogin(ClickEvent e) {
		authenticate();
	}

	void authenticate() {

		if (txtUid.getText().equals("admin")
				&& txtPwd.getText().equals("admin")) {
			root.showAdminUI();
		} else {
			// нэвтрэх хүсэлт илгээх
			this.root.getEditServiceProxy().login(txtUid.getText(),
					txtPwd.getText(), new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});
		}

	}
}
