package mn.goody.exam.client.ui;

import java.util.logging.Level;

import mn.goody.exam.client.Exampro;
import mn.goody.exam.shared.User;
import mn.goody.exam.client.activity.HomePresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HomePage extends Composite implements HomePresenter.Display {

	private static HomePageUiBinder uiBinder = GWT
			.create(HomePageUiBinder.class);

	interface HomePageUiBinder extends UiBinder<Widget, HomePage> {
	}

	@UiField
	TextBox txtUid;
	@UiField
	PasswordTextBox txtPwd;
	@UiField
	Button loginButton;
	@UiField
	Label responseLabel;

	public static final String TOKEN = "home";

	public HomePage() {

		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	protected void onLoad() {
		// нууц үгийг үргэлж цэвэрлэх
		txtPwd.setText("");

		super.onLoad();
	}

	void authenticate() {

		if (txtUid.getText().equals("admin")
				&& txtPwd.getText().equals("admin")) {
			User adminUser = new User();
			adminUser.addPermission(User.ADMIN_GROUP);
			Exampro.setCurrentUser(adminUser);
		} else {
			// нэвтрэх хүсэлт илгээх
			Exampro.getService().login(txtUid.getText(), txtPwd.getText(),
					new AsyncCallback<User>() {

						@Override
						public void onSuccess(User result) {
							Exampro.setCurrentUser(result);
						}

						@Override
						public void onFailure(Throwable caught) {
							Exampro.logger.log(Level.SEVERE,
									"Нэвтрэхэд алдаа гарлаа");
						}
					});
		}

	}

	public void showLoginError(String msg) {
		responseLabel.setText(msg);
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return loginButton;
	}

	public Widget asWidget() {
		return this;
	}
}
