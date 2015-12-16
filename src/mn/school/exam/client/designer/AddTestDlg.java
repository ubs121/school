package mn.goody.exam.client.designer;

import mn.goody.exam.shared.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RadioButton;

public class AddTestDlg extends DialogBox {

	private static final Binder binder = GWT.create(Binder.class);
	@UiField
	Button btnCancel;
	@UiField
	Button btnAdd;
	@UiField
	TextBox tbName;
	@UiField
	RadioButton rbNormal;
	@UiField
	RadioButton rbFinal;
	@UiField
	RadioButton rbPractice;

	interface Binder extends UiBinder<Widget, AddTestDlg> {
	}

	private TestPage parent;

	public AddTestDlg(TestPage parent) {
		this.parent = parent;
		setWidget(binder.createAndBindUi(this));
	}

	@UiHandler("btnCancel")
	void onButtonClick(ClickEvent event) {
		this.hide();
	}

	@UiHandler("btnAdd")
	void onBtnAddClick(ClickEvent event) {
		Test t = new Test();
		t.setName(tbName.getText());
		if (rbFinal.getValue().booleanValue()) {
			t.setType(Test.TYPE_FINAL);
		} else if (rbPractice.getValue().booleanValue()) {
			t.setType(Test.TYPE_PRACTICE);
		} else {
			t.setType(Test.TYPE_EXAM);
		}
		parent.addTest(t);
		hide();
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent preview) {
		super.onPreviewNativeEvent(preview);

		NativeEvent evt = preview.getNativeEvent();
		if (evt.getType().equals("keydown")) {
			switch (evt.getKeyCode()) {
			case KeyCodes.KEY_ENTER:

				onBtnAddClick(null);

				break;
			case KeyCodes.KEY_ESCAPE:
				hide();
				break;
			}
		}
	}
}
