package mn.school.enrollment.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FindDlg extends DialogBox {

    private static FindDlgUiBinder uiBinder = GWT.create(FindDlgUiBinder.class);

    interface FindDlgUiBinder extends UiBinder<Widget, FindDlg> {
    }

    public FindDlg() {
	setWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("findButton")
    public void findButton(ClickEvent e) {
	// TODO: хайлт
    }

    @UiHandler("cancelButton")
    public void cancelButton(ClickEvent e) {
	// TODO: хайлт
	this.hide();
    }

    @UiField
    TextBox name;
    @UiField
    TextBox last_name;
    @UiField
    TextBox email;
    @UiField
    TextBox regno;
    @UiField
    TextBox escore;
}
