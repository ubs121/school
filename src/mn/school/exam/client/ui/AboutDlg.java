package mn.goody.exam.client.ui;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class AboutDlg extends DialogBox {

	public AboutDlg() {
		final DialogBox me = this;
		setHTML("Програмын тухай");

		VerticalPanel verticalPanel = new VerticalPanel();
		setWidget(verticalPanel);
		verticalPanel.setSize("386px", "147px");

		HTMLPanel panel = new HTMLPanel(
				"<p><b>ExamPro 1.0</b></p>\n<br/>\n<p>&copy;2010.</p>");
		verticalPanel.add(panel);

		Button buttonOk = new Button("New button");
		buttonOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				me.hide();
			}
		});
		buttonOk.setText("Ok");
		verticalPanel.add(buttonOk);
		buttonOk.setWidth("100px");
		verticalPanel.setCellHorizontalAlignment(buttonOk,
				HasHorizontalAlignment.ALIGN_RIGHT);
	}

}
