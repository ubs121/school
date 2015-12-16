package mn.goody.exam.client.tester;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TestingPage extends Composite {

	private static TestingPageUiBinder uiBinder = GWT
			.create(TestingPageUiBinder.class);
	
	public static final String TOKEN = "testing";

	interface TestingPageUiBinder extends UiBinder<Widget, TestingPage> {
	}

	public TestingPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
