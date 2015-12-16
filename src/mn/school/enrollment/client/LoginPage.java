package mn.school.enrollment.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginPage extends Pane {
	private int invalidAccessCounter = 0;
	private static final String emailPattern = ".+@.+\\.\\w+";

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

	interface LoginUiBinder extends UiBinder<Widget, LoginPage> {
	}

	@UiField
	HTMLPanel loginPanel;
	@UiField
	TextBox emailField;
	@UiField
	Label emailError;
	@UiField
	TextBox keyField;
	@UiField
	Hyperlink regLink;
	@UiField
	Hyperlink adminLink;
	@UiField
	Button loginButton;
	@UiField
	Label responseLabel;

	public LoginPage() {
		initWidget(uiBinder.createAndBindUi(this));
		emailError.addStyleName("errorLabel");
	}

	@UiHandler("loginButton")
	void handleLogin(ClickEvent e) {
		// clearMessage();
		String email = emailField.getText().trim();
		String key = keyField.getText().trim();

		authenticate(email, key);
	}

	@UiHandler("regLink")
	protected void handleRegister(ClickEvent e) {
		RegisterDialog dialog = new RegisterDialog();
		dialog.center();
		dialog.show();
	}

	class RegisterDialog extends DialogBox implements ClickHandler {
		TextBox name;
		TextBox email;
		Button sendButton;
		Label error;

		public RegisterDialog() {
			setText("Код авах");

			sendButton = new Button("Илгээх", this);
			name = new TextBox();
			email = new TextBox();
			email.setText(emailField.getText());
			error = new Label();
			error.setStyleName("errorLabel");

			Grid grid = new Grid(4, 2);
			grid.setWidget(0, 0, new Label("Таны нэр:"));
			grid.setWidget(0, 1, name);
			grid.setWidget(1, 0, new Label("И-мэйл хаяг:"));
			grid.setWidget(1, 1, email);
			grid.setWidget(2, 0, new Button("Болих", this));
			grid.setWidget(2, 1, sendButton);
			grid.setWidget(3, 1, error);
			setWidget(grid);
		}

		@Override
		public void onClick(ClickEvent e) {
			if (e.getSource().equals(sendButton)) {
				boolean validInput = true;
				String err = "";

				if (name.getText().length() <= 0) {
					name.setStyleName("errorInput");
					err = err + "Нэр оруулаагүй!\r\n";
					validInput = false;
				} else
					name.removeStyleName("errorInput");

				if (email.getText().length() <= 0) {
					email.setStyleName("errorInput");
					err = err + "И-мэйл оруулаагүй!\r\n";
					validInput = false;
				} else if (email.getText().matches(emailPattern) == false) {
					email.setStyleName("errorInput");
					err = err + "И-мэйл буруу!";
					validInput = false;
				} else
					email.removeStyleName("errorInput");

				error.setText(err);

				if (validInput) {
					// дугаар авах хүсэлт илгээх
					getServiceProxy().дугаарАвах(name.getText(),
							email.getText(), new AsyncCallback<String>() {
								@Override
								public void onFailure(Throwable caught) {
									responseLabel
											.setText("Код авахад алдаа гарлаа");
								}

								@Override
								public void onSuccess(String result) {
									responseLabel.setText(result);
								}
							});

					this.hide();
				}
			} else {
				this.hide();
			}
		}
	}

	void authenticate(String email, String k) {
		// нэвтрэх хүсэлт илгээх
		getServiceProxy().нэвтрэх(email, k,
				new AsyncCallback<EnrollRequestClient>() {
					@Override
					public void onSuccess(EnrollRequestClient obj) {
						if (obj != null) {
							setEnrollObj(obj);
							бүртгэхХуудас();
						} else {
							onFailure(null);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						invalidAccessCounter++;
						if (invalidAccessCounter < 3) {
							responseLabel.setText("Хандалт зөвшөөрөгдсөнгүй! "
									+ invalidAccessCounter);
						} else {
							responseLabel
									.setText("Та олон удаа буруу хандалт хийлээ!");
							loginPanel.setVisible(false);
						}
						keyField.setText("");
					}

				});
	}

	@UiHandler("adminLink")
	protected void handleAdmin(ClickEvent e) {
		AdminLoginDialog dialog = new AdminLoginDialog();
		dialog.center();
		dialog.show();
	}

	class AdminLoginDialog extends DialogBox implements ClickHandler {
		TextBox admId;
		PasswordTextBox admPwd;
		Button admLogin;
		Label error;

		public AdminLoginDialog() {
			setText("Админ хэсэгт нэвтрэх");

			admLogin = new Button("Нэвтрэх", this);
			admId = new TextBox();
			admPwd = new PasswordTextBox();
			error = new Label();
			error.setStyleName("errorLabel");

			Grid grid = new Grid(4, 2);
			grid.setWidget(0, 0, new Label("Нэвтрэх нэр:"));
			grid.setWidget(0, 1, admId);
			grid.setWidget(1, 0, new Label("Нууц үг:"));
			grid.setWidget(1, 1, admPwd);
			grid.setWidget(2, 0, new Button("Болих", this));
			grid.setWidget(2, 1, admLogin);
			grid.setWidget(3, 1, error);
			setWidget(grid);
		}

		@Override
		public void onClick(ClickEvent e) {
			if (e.getSource().equals(admLogin)) {
				if (admId.getText().equals("admin")
						&& admPwd.getText().equals("admin")) {
					удирдахХуудас();
					this.hide();
				} else
					error.setText("Хандалт буруу!");
			} else {
				this.hide();
			}
		}
	}
}
