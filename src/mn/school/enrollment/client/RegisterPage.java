package mn.school.enrollment.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RegisterPage extends Pane {
	private static final String regnoPattern = "[А-Яа-я]{2}\\d{8}";
	private static final String realPattern = "\\d+\\.?\\d*";

	private static RegisterPageUiBinder uiBinder = GWT
			.create(RegisterPageUiBinder.class);

	interface RegisterPageUiBinder extends UiBinder<Widget, RegisterPage> {
	}

	public RegisterPage() {
		initWidget(uiBinder.createAndBindUi(this));
		country.addItem("Монгол");
		country.addItem("Хятад");
		country.addItem("Орос");
		country.addItem("Солонгос");
		country.addItem("Япон");
		country.addItem("Америк");

		state.addItem("Улаанбаатар");
		state.addItem("Архангай");
		state.addItem("Баян-Өлгий");
		state.addItem("Баянхонгор");
		state.addItem("Булган");
		state.addItem("Говь-Алтай");
		state.addItem("Говьсүмбэр");
		state.addItem("Дархан-Уул");
		state.addItem("Дорноговь");
		state.addItem("Дорнод");
		state.addItem("Дундговь");
		state.addItem("Завхан");
		state.addItem("Орхон");
		state.addItem("Сэлэнгэ");
		state.addItem("Сүхбаатар");
		state.addItem("Төв");
		state.addItem("Увс");
		state.addItem("Ховд");
		state.addItem("Хэнтий");
		state.addItem("Хөвсгөл");
		state.addItem("Өвөрхангай");
		state.addItem("Өмнөговь");

		prof.addItem("Програм хангамж");
		prof.addItem("Компьютер график");
		prof.addItem("Техник хангамж");

		gender.addItem("эр");
		gender.addItem("эм");

		esubject.addItem("Математик");
		esubject.addItem("Физик");

		GWT.log("Бүртгэх хуудас: Key=" + getEnrollObj().getKey());

		tabPanel.selectTab(0);

		updatePanel();
	}

	private void updatePanel() {
		EnrollRequestClient e = getEnrollObj();
		boolean isDraft = getEnrollObj().getТөлөв().equals(
				EnrollRequestClient.DRAFT);

		firstName.setText(e.getНэр());
		lastName.setText(e.getЭцгийнНэр());
		regno.setText(e.getРегистр());
		pasno.setText(e.getПасспорт());
		email.setText(e.getИмэйл());
		cert.setText(e.getГэрчилгээ());
		payment_id.setText(e.getТөлбөрId());
		school.setText(e.getСургууль());
		group.setText(e.getБүлэг());
		score.setText(String.valueOf(e.getҮнэлгээ()));
		escore.setText(String.valueOf(e.getЕшОноо()));
		status.setText(e.getТөлөв());
		regDate.setText(DateTimeFormat.getShortDateFormat()
				.format(e.getОгноо()));

		saveButton.setVisible(isDraft);
		validateButton.setVisible(!isDraft);
		cancelButton.setVisible(!isDraft);

		firstName.setReadOnly(!isDraft);
		lastName.setReadOnly(!isDraft);
		regno.setReadOnly(!isDraft);
		pasno.setReadOnly(!isDraft);
		email.setReadOnly(!isDraft);
		cert.setReadOnly(!isDraft);
		group.setReadOnly(!isDraft);
		score.setReadOnly(!isDraft);
		escore.setReadOnly(!isDraft);
		payment_id.setReadOnly(!isDraft);

		// бусад контролууд
		school.setVisible(isDraft);
		prof.setVisible(isDraft);
		country.setVisible(isDraft);
		state.setVisible(isDraft);
		gender.setVisible(isDraft);

		schoolText.setVisible(!isDraft);
		profText.setVisible(!isDraft);
		countryText.setVisible(!isDraft);
		stateText.setVisible(!isDraft);
		genderText.setVisible(!isDraft);

		if (isDraft) {
			if (gender.getItemText(0).equals(e.getХүйс()))
				gender.setSelectedIndex(0);
			else if (gender.getItemText(1).equals(e.getХүйс()))
				gender.setSelectedIndex(1);
			else
				gender.setSelectedIndex(-1);

			for (int i = 0; i < prof.getItemCount(); i++) {
				if (prof.getItemText(i).equals(e.getЭлсэхМэргэжил())) {
					prof.setSelectedIndex(i);
					break;
				}
			}

			for (int i = 0; i < country.getItemCount(); i++) {
				if (country.getItemText(i).equals(e.getХаръяалал())) {
					country.setSelectedIndex(i);
					break;
				}
			}

			for (int i = 0; i < state.getItemCount(); i++) {
				if (state.getItemText(i).equals(e.getАймаг())) {
					state.setSelectedIndex(i);
					break;
				}
			}

		} else {
			schoolText.setText(e.getСургууль());
			profText.setText(e.getЭлсэхМэргэжил());
			countryText.setText(e.getХаръяалал());
			stateText.setText(e.getАймаг());
			genderText.setText(e.getХүйс());
		}
	}

	@Override
	public void onBrowserEvent(Event event) {
		if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
			handleClick(null);
		}
		super.onBrowserEvent(event);
	}

	@UiHandler("saveButton")
	void handleClick(ClickEvent e) {
		if (validateFormInput()) {
			EnrollRequestClient en = getEnrollObj();
			// en.setKey(getEnrollObj().getKey());
			en.setНэр(firstName.getText());
			en.setЭцгийнНэр(lastName.getText());
			en.setРегистр(regno.getText());
			en.setПасспорт(pasno.getText());
			en.setГэрчилгээ(cert.getText());
			en.setЭлсэхМэргэжил(prof.getSelectedIndex() > 0 ? prof
					.getItemText(prof.getSelectedIndex()) : prof.getItemText(0));
			en.setТөлбөрId(payment_id.getText());
			en.setҮнэлгээ(Float.parseFloat(score.getText()));
			en.setЕшОноо(Float.parseFloat(escore.getText()));
			en.setСургууль(school.getText());

			en.setХаръяалал(country.getSelectedIndex() > 0 ? country
					.getItemText(state.getSelectedIndex()) : country
					.getItemText(0));

			en.setАймаг(state.getSelectedIndex() > 0 ? state.getItemText(state
					.getSelectedIndex()) : state.getItemText(0));

			getServiceProxy().бүртгэх(en, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					responseLabel.setText("Амжилттай хадгалагдлаа:" + result);
				}

				@Override
				public void onFailure(Throwable caught) {
					responseLabel
							.setText("Хүсэлтийг хадгалах үед алдаа гарлаа");

				}
			});
		}
	}

	boolean validateFormInput() {
		boolean valid = true;
		ArrayList<String> withErrorFields = new ArrayList<String>();
		if (firstName.getText().length() == 0) {
			firstNameError.setText("Нэрээ оруулна уу");
			withErrorFields.add("Нэр");
			valid = false;
		} else
			firstNameError.setText("");

		if (regno.getText().length() == 0) {
			regnoError.setText("Регистрийн дугаараа оруулна уу");
			withErrorFields.add("Регистр");
			valid = false;
		} else if (regno.getText().matches(regnoPattern) == false) {
			regnoError
					.setText("Регистрийн дугаар буруу байна! Зөв жишээ: ХЗ88221133");
			withErrorFields.add("Регистр");
			valid = false;
		} else {
			regnoError.setText("");
		}

		if (pasno.getText().length() > 0
				&& pasno.getText().matches(regnoPattern) == false) {
			pasnoError.setText("Паспортын дугаар буруу бичигдсэн байна!");
			withErrorFields.add("Паспорт");
			valid = false;
		} else {
			pasnoError.setText("");
		}

		if (cert.getText().length() == 0) {
			certError.setText("Гэрчилгээний дугаараа оруулна уу");
			withErrorFields.add("Гэрчилгээ");
			valid = false;
		} else {
			certError.setText("");
		}

		if (score.getText().length() > 0
				&& score.getText().matches(realPattern) == false) {
			scoreError.setText("Зөв тоон утга оруулна уу");
			withErrorFields.add("Төгсөлтийн үнэлгээ");
			valid = false;
		} else {
			scoreError.setText("");
		}

		if (escore.getText().length() == 0) {
			escoreError.setText("Ерөнхий шалгалтын оноог оруулна уу");
			withErrorFields.add("ЕШ оноо");
			valid = false;
		} else if (escore.getText().matches(realPattern) == false) {
			escoreError.setText("Зөв тоон утга оруулна уу");
			withErrorFields.add("ЕШ оноо");
			valid = false;
		} else {
			escoreError.setText("");
		}

		if (withErrorFields.size() > 0) {
			String err = "";
			for (int i = 0; i < withErrorFields.size(); i++) {
				if (i == 0)
					err = withErrorFields.get(0);
				else
					err += ", " + withErrorFields.get(i);
			}
			responseLabel.setText(err + " алдаатай!");
		}
		return valid;
	}

	@UiHandler("logout")
	void handleLogout(ClickEvent e) {
		нэвтрэхХуудас();
	}

	@UiField
	TabLayoutPanel tabPanel;
	@UiField
	TextBox firstName;
	@UiField
	Label firstNameError;
	@UiField
	TextBox lastName;
	@UiField
	ListBox gender;
	@UiField
	TextBox genderText;
	@UiField
	Label lastNameError;
	@UiField
	TextBox email;
	@UiField
	Label emailError;
	@UiField
	TextBox regno;
	@UiField
	Label regnoError;
	@UiField
	TextBox pasno;
	@UiField
	Label pasnoError;
	@UiField
	ListBox prof;
	@UiField
	TextBox profText;
	@UiField
	TextBox payment_id;
	@UiField
	SuggestBox school;
	@UiField
	TextBox schoolText;
	@UiField
	TextBox group;
	@UiField
	TextBox score;
	@UiField
	Label scoreError;
	@UiField
	ListBox esubject;
	@UiField
	TextBox esubjectText;
	@UiField
	TextBox escore;
	@UiField
	Label escoreError;
	@UiField
	TextBox cert;
	@UiField
	Label certError;
	@UiField
	ListBox country;
	@UiField
	TextBox countryText;
	@UiField
	ListBox state;
	@UiField
	TextBox stateText;
	@UiField
	TextBox regDate;
	@UiField
	TextBox status;
	@UiField
	Label responseLabel;

	@UiField
	Button saveButton;
	@UiField
	Button validateButton;
	@UiField
	Button cancelButton;

	@UiField
	Hyperlink logout;
}
