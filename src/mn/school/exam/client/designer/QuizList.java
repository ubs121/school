package mn.goody.exam.client.designer;

import java.util.ArrayList;
import java.util.List;

import mn.goody.exam.client.Exampro;
import mn.goody.exam.shared.Quiz;
import mn.goody.exam.shared.Topic;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;

public class QuizList extends Composite {

	Exampro host;

	static final int COLUMN_CHECK = 0;
	static final int COLUMN_QID = 1;
	static final int COLUMN_LEVEL = 2;
	static final int COLUMN_SCORE = 3;
	static final int COLUMN_DURATION = 4;
	static final int COLUMN_CONTENT = 5;

	@UiField
	FlexTable listHeader;
	@UiField
	FlexTable listQuiz;

	CheckBox checkAll = new CheckBox();

	public QuizList(Exampro host) {
		this.host = host;

		Label label = new Label("New label");
		initWidget(label);

		final int header_row = listHeader.getRowCount();
		listHeader.setWidget(header_row, COLUMN_CHECK, checkAll);
		listHeader.setWidget(header_row, COLUMN_QID, new Label("Код"));
		listHeader.setWidget(header_row, COLUMN_LEVEL, new Label("Түвшин"));
		listHeader.setWidget(header_row, COLUMN_DURATION, new Label("Хугацаа"));
		listHeader.setWidget(header_row, COLUMN_SCORE, new Label("Оноо"));
		listHeader.setWidget(header_row, COLUMN_CHECK, new Label("Асуулт"));

		listHeader.getColumnFormatter().setWidth(COLUMN_QID, "60px");
		listHeader.getColumnFormatter().setWidth(COLUMN_LEVEL, "80px");
		listHeader.getColumnFormatter().setWidth(COLUMN_DURATION, "40px");
		listHeader.getColumnFormatter().setWidth(COLUMN_SCORE, "40px");
		listHeader.getColumnFormatter().setWidth(COLUMN_CONTENT, "100%");

		listQuiz.getColumnFormatter().setWidth(COLUMN_CHECK, "20px");
		listQuiz.getColumnFormatter().setWidth(COLUMN_QID, "60px");
		listQuiz.getColumnFormatter().setWidth(COLUMN_LEVEL, "80px");
		listQuiz.getColumnFormatter().setWidth(COLUMN_DURATION, "40px");
		listQuiz.getColumnFormatter().setWidth(COLUMN_SCORE, "40px");
		listQuiz.getColumnFormatter().setWidth(COLUMN_CONTENT, "100%");

		// асуултаар дүүргэх
		populateQuizList(null);
	}

	private void populateQuizList(Topic t) {
		host.getEditServiceProxy().getQuizs(t, new AsyncCallback<List<Quiz>>() {
			@Override
			public void onSuccess(List<Quiz> list) {
				listHeader.getRowFormatter().setStyleName(0, "grid-header");

				for (Quiz q : list) {
					addQuizToTable(q);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void change_topic(Topic t) {
		// TODO: Сонгосон сэдэвт хамаарах асуултууд харуулах
	}

	// add quiz in preview mode
	private void addQuizToTable(Quiz q) {
		final int row = listQuiz.getRowCount();
		listQuiz.setWidget(row, COLUMN_CHECK, new CheckBox());
		listQuiz.setWidget(row, COLUMN_QID,
				new Hyperlink(q.getId(), "qid=" + q.getId()));
		listQuiz.setWidget(row, COLUMN_LEVEL,
				new Label(String.valueOf(q.getLevel())));
		listQuiz.setWidget(row, COLUMN_DURATION,
				new Label(String.valueOf(q.getDuration())));
		listQuiz.setWidget(row, COLUMN_SCORE,
				new Label(String.valueOf(q.getScore())));

		HTML html_content = new HTML(q.getContent());
		listQuiz.setWidget(row, COLUMN_CONTENT,
				new Label(html_content.getText()));
	}

	@UiField
	PushButton new_quiz;

	@UiHandler("new_quiz")
	public void new_quiz(ClickEvent e) {
		final Quiz q = new Quiz();
		host.getEditServiceProxy().saveQuiz(q, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String qid) {
				q.setId(qid);
				addQuizToTable(q);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Алдаа: " + caught.toString());
			}
		});
	}

	@UiField
	PushButton delete_quiz;

	@UiHandler("delete_quiz")
	public void delete_quiz(ClickEvent e) {
		// устгах асуултуудыг ялгах
		List<String> ids = new ArrayList<String>();
		for (int i = listQuiz.getRowCount() - 1; i >= 0; i--) {
			CheckBox tick = (CheckBox) listQuiz.getWidget(i, COLUMN_CHECK);
			if (tick.getValue() == true)
				ids.add(((Label) listQuiz.getWidget(i, COLUMN_QID)).getText());
		}

		if (ids.size() > 0) {
			// асуултуудыг устгах
			host.getEditServiceProxy().removeQuizs(ids,
					new AsyncCallback<Void>() {
						@Override
						public void onSuccess(Void result) {
							// хүснэгтээс хасах
							for (int i = listQuiz.getRowCount() - 1; i >= 0; i--) {
								CheckBox tick = (CheckBox) listQuiz.getWidget(
										i, COLUMN_CHECK);
								if (tick.getValue() == true) {
									listQuiz.removeRow(i);
								}
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Алдаа: " + caught);
						}
					});
		}
	}

	@UiField
	PushButton change_topic;
	@UiField
	PushButton add2test;

}
