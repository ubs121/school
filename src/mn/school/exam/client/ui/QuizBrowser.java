package mn.goody.exam.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import mn.goody.exam.client.activity.QuizsPresenter;

/**
 * @author ub
 * 
 */
public class QuizBrowser extends Composite implements QuizsPresenter.Display {
	public static final String TOKEN = "quizs";

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, QuizBrowser> {
	}

	@UiField(provided = true)
	TopicTree topicTree;
	@UiField(provided = true)
	EditQuizView quizEditor;
	@UiField(provided = true)
	QuizList quizList;
	@UiField(provided = true)
	QuizListToolbar quizListToolbar;

	@UiConstructor
	public QuizBrowser() {
		quizEditor = new EditQuizView();
		quizList = new QuizList(quizEditor);
		quizListToolbar = new QuizListToolbar(quizList);
		topicTree = new TopicTree(quizList);

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasClickHandlers getAddQuizButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getDeleteQuizButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getQuizList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setQuizData(List<String> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getClickedQuizRow(ClickEvent event) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> getSelectedQuizRows() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getAddTopicButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getDeleteTopicButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getTopicList() {
		// TODO Auto-generated method stub
		return null;
	}

	public Widget asWidget() {
		return this;
	}
}
