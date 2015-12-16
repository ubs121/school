package mn.goody.exam.client.ui;

import mn.goody.exam.shared.Quiz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;

public class EditQuizView extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, EditQuizView> {
	}

	private EditQuizView quizEditor;
	private Formatter styleTextFormatter;

	private Quiz quiz;

	public EditQuizView() {
		this(null);
	}

	public EditQuizView(Quiz q) {
		initWidget(uiBinder.createAndBindUi(this));

		styleTextFormatter = quizContent.getFormatter();

		setQuiz(q);
		updateUI();
	}

	public void setQuiz(Quiz q) {
		// өмнөх асуултыг хадгалах
		if (this.quiz != null && this.quiz.isEdited()) {
			updateQuiz();
		}

		// шинэ асуултыг авах
		this.quiz = q;

		if (q != null) {
			if (q.getId() != null) {
				//
			} else {
				this.quiz.setEdited(true);
			}
		}
	}

	public Quiz getQuiz() {
		return this.quiz;
	}

	/**
	 * Хэрэглэгчийн интерфэйсээс өгөгдлийг уншиж Quiz объектыг шинэчилэх
	 */
	void updateQuiz() {
		if (quiz == null) {
			return;
		}

		quiz.setContent(quizContent.getHTML());

		NodeList<Element> inputs = quizContent.getElement()
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

		quiz.setContent(quizContent.getHTML());
		quiz.setDuration(quizDuration.getValue());
		quiz.setScore(quizScore.getValue());
		quiz.setLevel(quizLevel.getValue(quizLevel.getSelectedIndex()));
	}

	/**
	 * Quiz объектоос хэрэглэгчийн интерфэйсийг шинэчлэх
	 */
	public void updateUI() {
		if (quiz == null) {
			clear();
			return;
		}

		quizId.setText(String.valueOf(quiz.getId()));
		quizLevel.setSelectedIndex(Integer.parseInt(quiz.getLevel()));
		quizDuration.setValue(quiz.getDuration());
		quizScore.setValue(quiz.getScore());
		quizContent.setHTML(quiz.getContent());
	}

	public void clear() {
		quizId.setText("");
		quizLevel.setSelectedIndex(-1);
		quizDuration.setValue(0);
		quizScore.setValue(0);
		quizContent.setHTML("");
	}

	public void create() {
		if (this.quiz != null) {
			this.quiz.setEdited(true);
		}
		clear();
	}

	public void setStatus(String str) {
		lblStatus.setText(str);
	}

	public RichTextArea getRichTextArea() {
		return quizContent;
	}

	public boolean isHTMLMode() {
		return texthtml.isDown();
	}

	/**
	 * Private method to set the toggle buttons and disable/enable buttons which
	 * do not work in html-mode
	 **/
	private void updateStatus() {
		if (styleTextFormatter != null) {
			bold.setDown(styleTextFormatter.isBold());
			italic.setDown(styleTextFormatter.isItalic());
			underline.setDown(styleTextFormatter.isUnderlined());
			subscript.setDown(styleTextFormatter.isSubscript());
			superscript.setDown(styleTextFormatter.isSuperscript());
			stroke.setDown(styleTextFormatter.isStrikethrough());
		}

		if (isHTMLMode()) {
			remove_formatting.setEnabled(false);
			indent_left.setEnabled(false);
		} else {
			remove_formatting.setEnabled(true);
			indent_left.setEnabled(true);
		}
	}

	public void onKeyUp(KeyUpEvent event) {
		updateStatus();
	}

	@UiHandler(value = { "fonts", "colors" })
	void handleChange(ChangeEvent event) {
		if (event.getSource().equals(fonts)) {
			if (isHTMLMode()) {
				changeHtmlStyle(
						"<span style=\"font-family: "
								+ fonts.getValue(fonts.getSelectedIndex())
								+ ";\">", "</span>");
			} else {
				styleTextFormatter.setFontName(fonts.getValue(fonts
						.getSelectedIndex()));
			}
		} else if (event.getSource().equals(colors)) {
			if (isHTMLMode()) {
				changeHtmlStyle(
						"<span style=\"color: "
								+ colors.getValue(colors.getSelectedIndex())
								+ ";\">", "</span>");
			} else {
				styleTextFormatter.setForeColor(colors.getValue(colors
						.getSelectedIndex()));
			}
		}
	}

	@UiHandler(value = { "bold", "italic", "underline", "stroke", "subscript",
			"superscript", "align_left", "align_middle", "align_right",
			"order_list", "unorder_list", "indent_left", "indent_right",
			"insert_line", "insert_image", "remove_formatting", "texthtml",
			"insert_single_choice", "insert_multi_choice", "insert_true_false",
			"insert_fillbox", "insert_match" })
	void handleClick(ClickEvent e) {
		if (e.getSource().equals(bold)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<span style=\"font-weight: bold;\">",
						"</span>");
			} else {
				styleTextFormatter.toggleBold();
			}
		} else if (e.getSource().equals(italic)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<span style=\"font-style: italic;\">",
						"</span>");
			} else {
				styleTextFormatter.toggleItalic();
			}
		} else if (e.getSource().equals(underline)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<span style=\"text-decoration: underline;\">",
						"</span>");
			} else {
				styleTextFormatter.toggleUnderline();
			}
		} else if (e.getSource().equals(stroke)) {
			if (isHTMLMode()) {
				changeHtmlStyle(
						"<span style=\"text-decoration: line-through;\">",
						"</span>");
			} else {
				styleTextFormatter.toggleStrikethrough();
			}
		} else if (e.getSource().equals(subscript)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<sub>", "</sub>");
			} else {
				styleTextFormatter.toggleSubscript();
			}
		} else if (e.getSource().equals(superscript)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<sup>", "</sup>");
			} else {
				styleTextFormatter.toggleSuperscript();
			}
		} else if (e.getSource().equals(align_left)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<div style=\"text-align: left;\">", "</div>");
			} else {
				styleTextFormatter
						.setJustification(RichTextArea.Justification.LEFT);
			}
		} else if (e.getSource().equals(align_middle)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<div style=\"text-align: center;\">", "</div>");
			} else {
				styleTextFormatter
						.setJustification(RichTextArea.Justification.CENTER);
			}
		} else if (e.getSource().equals(align_right)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<div style=\"text-align: right;\">", "</div>");
			} else {
				styleTextFormatter
						.setJustification(RichTextArea.Justification.RIGHT);
			}
		} else if (e.getSource().equals(order_list)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<ol><li>", "</li></ol>");
			} else {
				styleTextFormatter.insertOrderedList();
			}
		} else if (e.getSource().equals(unorder_list)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<ul><li>", "</li></ul>");
			} else {
				styleTextFormatter.insertUnorderedList();
			}
		} else if (e.getSource().equals(indent_right)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<div style=\"margin-left: 40px;\">", "</div>");
			} else {
				styleTextFormatter.rightIndent();
			}
		} else if (e.getSource().equals(indent_left)) {
			if (isHTMLMode()) {
				// TODO nothing can be done here at the moment
			} else {
				styleTextFormatter.leftIndent();
			}
		} else if (e.getSource().equals(insert_image)) {
			String url = Window.prompt("Зургийн URL:", "http://");
			if (url != null) {
				if (isHTMLMode()) {
					changeHtmlStyle("<img src=\"" + url + "\">", "");
				} else {
					styleTextFormatter.insertImage(url);
				}
			}
		} else if (e.getSource().equals(insert_line)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<hr style=\"width: 100%; height: 2px;\">", "");
			} else {
				styleTextFormatter.insertHorizontalRule();
			}
		} else if (e.getSource().equals(insert_single_choice)) {
			if (isHTMLMode()) {
				changeHtmlStyle(SINGLE_CHOICE, "");
			} else {
				styleTextFormatter.insertHTML(SINGLE_CHOICE);
			}
		} else if (e.getSource().equals(insert_multi_choice)) {
			if (isHTMLMode()) {
				changeHtmlStyle(MULTI_CHOICE, "");
			} else {
				styleTextFormatter.insertHTML(MULTI_CHOICE);
			}
		} else if (e.getSource().equals(insert_true_false)) {
			if (isHTMLMode()) {
				changeHtmlStyle(TRUE_FALSE, "");
			} else {
				styleTextFormatter.insertHTML(TRUE_FALSE);
			}
		} else if (e.getSource().equals(insert_fillbox)) {
			if (isHTMLMode()) {
				changeHtmlStyle("<textarea rows='1' cols='20'>", "</textarea>");
			} else {
				styleTextFormatter
						.insertHTML("<textarea rows='1' cols='20'></textarea>");
			}
		} else if (e.getSource().equals(remove_formatting)) {
			if (isHTMLMode()) {
				// TODO nothing can be done here at the moment
			} else {
				styleTextFormatter.removeFormat();
			}
		} else if (e.getSource().equals(texthtml)) {
			if (texthtml.isDown()) {
				quizContent.setText(quizContent.getHTML());
			} else {
				quizContent.setHTML(quizContent.getText());
			}
		} else if (e.getSource().equals(quizContent)) {
			// Change invoked by the richtextArea
		}
		updateStatus();
	}

	/**
	 * Native JavaScript that returns the selected text and position of the
	 * start
	 **/
	public static native JsArrayString getSelection(Element elem) /*-{
		var txt = "";
		var pos = 0;
		if (elem.contentWindow.getSelection) {
		txt = elem.contentWindow.getSelection();
		pos = elem.contentWindow.getSelection().getRangeAt(0).startOffset;
		} else if (elem.contentWindow.document.getSelection) {
		txt = elem.contentWindow.document.getSelection();
		pos = elem.contentWindow.document.getSelection().getRangeAt(0).startOffset;
		} else if (elem.contentWindow.document.selection) {
		txt = elem.contentWindow.document.selection.createRange().text;
		pos = elem.contentWindow.document.selection.getRangeAt(0).startOffset;
		}
		return [""+txt,""+pos];
	}-*/;

	/** Method called to toggle the style in HTML-Mode **/
	private void changeHtmlStyle(String startTag, String stopTag) {
		JsArrayString tx = getSelection(quizContent.getElement());
		String txbuffer = quizContent.getText();
		Integer startpos = Integer.parseInt(tx.get(1));
		String selectedText = tx.get(0);
		quizContent.setText(txbuffer.substring(0, startpos) + startTag
				+ selectedText + stopTag
				+ txbuffer.substring(startpos + selectedText.length()));
	}

	public static final String SINGLE_CHOICE = "<table>"
			+ "<tr><td><input type='radio' id='c1' name='sc'/></td><td><textarea rows='1' cols='50'></textarea></td></tr>"
			+ "<tr><td><input type='radio' id='c2' name='sc'/></td><td><textarea rows='1' cols='50'></textarea></td></tr>"
			+ "<tr><td><input type='radio' id='c3' name='sc'/></td><td><textarea rows='1' cols='50'></textarea></td></tr>"
			+ "<tr><td><input type='radio' id='c4' name='sc'/></td><td><textarea rows='1' cols='50'></textarea></td></tr>"
			+ "</table>";

	public static final String MULTI_CHOICE = "<table>"
			+ "<tr><td><input type='checkbox' id='c1'/></td><td><textarea rows='1' cols='50'></textarea></td></tr>"
			+ "<tr><td><input type='checkbox' id='c2'/></td><td><textarea rows='1' cols='50'></textarea></td></tr>"
			+ "<tr><td><input type='checkbox' id='c3'/></td><td><textarea rows='1' cols='50'></textarea></td></tr>"
			+ "<tr><td><input type='checkbox' id='c4'/></td><td><textarea rows='1' cols='50'></textarea></td></tr>"
			+ "</table>";

	public static final String TRUE_FALSE = "<input type='radio' id='c1' name='sc'/><label for='c1'>Үнэн</label>"
			+ "<input type='radio' id='c2' name='sc'/><label for='c2'>Худал</label>";

	@UiField
	Label quizId;
	@UiField
	ListBox quizLevel;
	@UiField
	IntegerBox quizDuration;
	@UiField
	IntegerBox quizScore;
	@UiField
	Label lblStatus;

	@UiField
	RichTextArea quizContent;

	@UiField
	ListBox fonts;
	@UiField
	ListBox colors;
	@UiField
	ToggleButton bold;
	@UiField
	ToggleButton italic;
	@UiField
	ToggleButton underline;
	@UiField
	ToggleButton stroke;
	@UiField
	ToggleButton subscript;
	@UiField
	ToggleButton superscript;
	@UiField
	PushButton align_left;
	@UiField
	PushButton align_middle;
	@UiField
	PushButton align_right;
	@UiField
	PushButton order_list;
	@UiField
	PushButton unorder_list;
	@UiField
	PushButton indent_left;
	@UiField
	PushButton indent_right;
	@UiField
	PushButton insert_line;
	@UiField
	PushButton insert_image;
	@UiField
	PushButton insert_single_choice;
	@UiField
	PushButton insert_multi_choice;
	@UiField
	PushButton insert_true_false;
	@UiField
	PushButton insert_fillbox;
	@UiField
	PushButton insert_match;
	@UiField
	PushButton remove_formatting;
	@UiField
	ToggleButton texthtml;
}
