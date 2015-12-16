package mn.goody.exam.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ReportPage extends Composite {

	public static final String TOKEN = "report";

	private static ReportPageUiBinder uiBinder = GWT
			.create(ReportPageUiBinder.class);

	interface ReportPageUiBinder extends UiBinder<Widget, ReportPage> {
	}

	@UiField
	Label lblTestName;
	@UiField
	Label lblTestDate;
	@UiField
	Label lblTestScore;
	@UiField
	Label lblTestDuration;
	@UiField
	HTML scoreSheet;

	public ReportPage() {
		initWidget(uiBinder.createAndBindUi(this));
		SafeHtmlBuilder html = new SafeHtmlBuilder();

		scoreSheet.setHTML(html.toSafeHtml());
	}

}
