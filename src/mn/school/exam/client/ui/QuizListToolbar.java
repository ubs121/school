package mn.goody.exam.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class QuizListToolbar extends Composite {

	private static QuizListToolbarUiBinder uiBinder = GWT
			.create(QuizListToolbarUiBinder.class);

	interface QuizListToolbarUiBinder extends UiBinder<Widget, QuizListToolbar> {
	}

	@UiField
	TextBox searchBox;
	@UiField
	PushButton btnNewQuiz;
	@UiField
	PushButton btnSaveQuiz;
	@UiField
	PushButton btnDeleteQuiz;

	private QuizList quizList;

	public QuizListToolbar(QuizList ql) {
		quizList = ql;
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("btnNewQuiz")
	public void btnNewQuiz_click(ClickEvent e) {
		quizList.addQuiz();
	}

	@UiHandler("btnSaveQuiz")
	public void btnSaveQuiz_click(ClickEvent e) {
		quizList.saveQuizs();
	}

	@UiHandler("btnDeleteQuiz")
	public void btnDeleteQuiz_click(ClickEvent e) {
		quizList.deleteQuizs();
	}
}
