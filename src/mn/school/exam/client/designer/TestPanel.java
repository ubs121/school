package mn.goody.exam.client.designer;

import mn.goody.exam.client.Exampro;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TestPanel extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, TestPanel> {
	}

	private Exampro exam;

	public TestPanel(Exampro p) {
		exam = p;
		initWidget(uiBinder.createAndBindUi(this));
	}

}
