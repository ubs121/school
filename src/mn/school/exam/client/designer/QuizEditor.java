package mn.goody.exam.client.designer;

import mn.goody.exam.shared.Quiz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuizEditor extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, QuizEditor> {
	}

	@UiField
	HTML content_view;
	@UiField
	RichTextArea content_edit;

	@UiField
	CheckBox select;
	@UiField
	Label qid;
	@UiField
	ListBox levels;
	@UiField
	IntegerBox score;
	@UiField
	IntegerBox duration;
	@UiField
	DockLayoutPanel mainPanel;
	@UiField
	VerticalPanel toolbarPanel;

	QuizToolbar toolbar2;

	Quiz quiz;

	public QuizEditor(Quiz q) {
		this.quiz = q;

		initWidget(uiBinder.createAndBindUi(this));

		levels.addItem("Маш хялбар", String.valueOf(Quiz.LEVEL_SUPER_EASY));
		levels.addItem("Хялбар", String.valueOf(Quiz.LEVEL_SUPER_EASY));
		levels.addItem("Ердийн", String.valueOf(Quiz.LEVEL_SUPER_EASY));
		levels.addItem("Хүнд", String.valueOf(Quiz.LEVEL_SUPER_EASY));
		levels.addItem("Маш хүнд", String.valueOf(Quiz.LEVEL_SUPER_EASY));

		updateUI();
	}

	public void setSelected(boolean b) {
		select.setValue(b);
	}

	public boolean getSelected() {
		return select.getValue().booleanValue();
	}

	public void setQuiz(Quiz q) {
		this.quiz = q;
		updateUI();
	}

	/**
	 * Засах асуултыг авах
	 * 
	 * @return Quiz
	 */
	public Quiz getQuiz() {
		return this.quiz;
	}

	/**
	 * Хэрэглэгчийн интерфэйсээс өгөгдлийг уншиж Quiz объектыг шинэчлэх
	 */
	void updateQuiz() {
		this.quiz.setContent(content_edit.getHTML());

		NodeList<Element> inputs = content_view.getElement()
				.getElementsByTagName("input");

		for (int i = 0; i < inputs.getLength(); i++) {
			Element inp = inputs.getItem(i);
			String inputType = inp.getPropertyString("type").toLowerCase();
			if (inputType.equals("text")) {
				inp.setAttribute("value", inp.getPropertyString("value"));
			} else if (inputType.equals("checkbox")) {

			} else if (inputType.equals("radio")) {

			}

			inp.setId("id" + i);
		}

		this.quiz.setContent(content_view.getHTML());

		this.quiz.setDuration(Integer.parseInt(duration.getText()));
		this.quiz.setScore(Integer.parseInt(score.getText()));
	}

	/**
	 * Quiz объектоос хэрэглэгчийн интерфэйсийг шинэчлэх
	 */
	void updateUI() {
		if (this.quiz == null)
			return;

		qid.setText(quiz.getId());
		content_view.setHTML(this.quiz.getContent());
		/*
		 * if (this.editing) { // add editing toolbar if (toolbar2 == null) {
		 * toolbar2 = new QuizToolbar(content_edit); toolbarPanel.add(toolbar2);
		 * }
		 * 
		 * content_edit.setHTML(this.quiz.getContent()); } else {
		 * 
		 * }
		 * 
		 * if (toolbar2 != null) { toolbar2.setVisible(this.editing); }
		 */

		duration.setText(String.valueOf(quiz.getDuration()));
		score.setText(String.valueOf(quiz.getScore()));
		levels.setSelectedIndex(Quiz.LEVEL_SUPER_EASY - 1);

	}

}
