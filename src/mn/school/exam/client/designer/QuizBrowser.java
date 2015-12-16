package mn.goody.exam.client.designer;

import java.util.ArrayList;
import java.util.List;

import mn.goody.exam.client.Exampro;
import mn.goody.exam.shared.Quiz;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author ub
 * 
 */
public class QuizBrowser extends Composite {

	Exampro host;
	private CellList<Quiz> quizList;
	private static final List<Quiz> quizs = new ArrayList<Quiz>();

	Label labelQuizId;
	ListBox comboQuizLevel;
	IntegerBox iboxQuizDuration;
	IntegerBox iboxQuizScore;
	HTML htmlQuizContent;

	public static class QuizCell extends AbstractCell<Quiz> {
		public QuizCell() {
		}

		@Override
		public void render(Quiz value, Object key, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}

			HTML html = new HTML(value.getContent());
			String val = html.getText();
			if (val.length() > 80) {
				val = val.substring(0, 77) + "...";
			}
			sb.appendHtmlConstant("<div style='margin: 1em;'>");
			sb.appendHtmlConstant(val);
			sb.appendHtmlConstant("</div>");
			/*
			 * sb.appendHtmlConstant("<table>");
			 * sb.appendHtmlConstant("<tr><td style='font-size:90%;'>");
			 * sb.appendEscaped((value.getId() == null ? "1" : value.getId()));
			 * sb.appendHtmlConstant(
			 * "</td><td rowspan='3' style='font-size:90%; vertical-align: top;text-align: left;width:20em;'>"
			 * ); sb.appendEscaped(value.getContent());
			 * sb.appendHtmlConstant("</td></tr>");
			 * sb.appendHtmlConstant("<tr><td style='font-size:90%;'>");
			 * sb.appendEscaped(String.valueOf(value.getLevel()));
			 * sb.appendHtmlConstant("</td></tr>");
			 * sb.appendHtmlConstant("<tr><td style='font-size:90%;'>");
			 * sb.appendEscaped(String.valueOf(value.getDuration()));
			 * sb.appendHtmlConstant("</td></tr>");
			 * sb.appendHtmlConstant("</table>");
			 */
		}
	}

	public QuizBrowser(Exampro exam) {
		this.host = exam;

		comboQuizLevel = new ListBox();
		comboQuizLevel.addItem("Маш хялбар");
		comboQuizLevel.addItem("Хялбар");
		comboQuizLevel.addItem("Ердийн");
		comboQuizLevel.addItem("Хүнд");
		comboQuizLevel.addItem("Маш хүнд");
		comboQuizLevel.setSelectedIndex(2);

		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
		initWidget(dockLayoutPanel);
		dockLayoutPanel.setSize("90em", "50em");

		SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel();
		dockLayoutPanel.add(splitLayoutPanel);
		splitLayoutPanel.setSize("90em", "50em");

		Tree treeTopic = new Tree();
		splitLayoutPanel.addWest(treeTopic, 220.0);
		treeTopic.setWidth("");

		DockLayoutPanel dockLayoutPanel_1 = new DockLayoutPanel(Unit.EM);
		splitLayoutPanel.add(dockLayoutPanel_1);
		dockLayoutPanel_1.setWidth("100%");

		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setStyleName("toolbar");
		dockLayoutPanel_1.addNorth(absolutePanel, 2.0);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		dockLayoutPanel_1.addSouth(horizontalPanel, 2.0);
		horizontalPanel.setWidth("100%");

		Label label = new Label("Асуултын тоо");
		horizontalPanel.add(label);

		TextBox searchBox = new TextBox();
		absolutePanel.add(searchBox, 0, 0);
		searchBox.setSize("765px", "17px");

		PushButton pushButton = new PushButton("Шинэ");
		pushButton.getUpFace().setImage(new Image("images/new.png"));
		absolutePanel.add(pushButton, 866, 0);
		pushButton.setSize("24px", "17px");

		PushButton pushButton_1 = new PushButton("Хадгалах");
		pushButton_1.getUpFace().setImage(new Image("images/save.png"));
		absolutePanel.add(pushButton_1, 902, 0);
		pushButton_1.setSize("24px", "17px");

		PushButton btnFind = new PushButton("Хайх");
		btnFind.getUpFace().setImage(new Image("images/search.png"));
		absolutePanel.add(btnFind, 776, 0);
		btnFind.setSize("24px", "17px");

		quizList = new CellList<Quiz>(new QuizCell());
		quizList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		final SingleSelectionModel<Quiz> selectionModel = new SingleSelectionModel<Quiz>();
		quizList.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						Quiz q = selectionModel.getSelectedObject();
						labelQuizId.setText(q.getId());
						comboQuizLevel.setSelectedIndex(q.getLevel());
						iboxQuizDuration.setValue(q.getDuration());
						iboxQuizScore.setValue(q.getScore());
						htmlQuizContent.setHTML(q.getContent());
					}
				});

		quizs.add(new Quiz(
				Quiz.LEVEL_NORMAL,
				1,
				"<h3>Тааг болон элементийн хоорондох ялгаа юу вэ?</h3><textarea rows=\"5\" cols=\"70\"></textarea>"));
		quizs.add(new Quiz(
				Quiz.LEVEL_NORMAL,
				1,
				"<h3>Доор харуулсан DOM бүтцийг ашиглан дараахь хүснэгтэд заасан элементүүдийг улаан өнгөтэй болгох CSS дүрмийг бичнэ үү</h3><br/><img src=\"test/dom_sample.png\"/>"));
		quizs.add(new Quiz(Quiz.LEVEL_EASY, 1,
				"<h3>CLS гэж ямар үгүүдийн товчлол вэ?</h3><ul><li><input type='radio' name='cls'>Common Language Specification</input></li><li><input type='radio' name='cls'>Common Library System</input></li>	<li><input type='radio' name='cls'>Csharp Language	Specification</input></li></ul>"));
		quizs.add(new Quiz(Quiz.LEVEL_NORMAL, 1, "test4"));
		quizList.setRowData(0, quizs);
		dockLayoutPanel_1.addWest(quizList, 18.2);

		DockLayoutPanel quizPanel = new DockLayoutPanel(Unit.EM);
		quizPanel.setStyleName("form");
		dockLayoutPanel_1.add(quizPanel);
		quizPanel.setSize("", "");

		FlexTable flexTable = new FlexTable();
		flexTable.setBorderWidth(0);
		quizPanel.addNorth(flexTable, 3.0);
		flexTable.setSize("", "20px");

		labelQuizId = new Label();
		flexTable.setWidget(0, 0, labelQuizId);
		flexTable.getCellFormatter().setHeight(0, 6, "20");
		flexTable.getCellFormatter().setHeight(0, 6, "20");
		flexTable.getCellFormatter().setHeight(0, 4, "20");
		flexTable.getCellFormatter().setHeight(0, 7, "20");

		flexTable.setWidget(0, 2, comboQuizLevel);

		Label label_2 = new Label("Хугацаа(мин):");
		flexTable.setWidget(0, 4, label_2);

		iboxQuizDuration = new IntegerBox();
		flexTable.setWidget(0, 5, iboxQuizDuration);
		iboxQuizDuration.setWidth("3em");

		Label label_1 = new Label("Оноо:");
		flexTable.setWidget(0, 6, label_1);

		iboxQuizScore = new IntegerBox();
		flexTable.setWidget(0, 7, iboxQuizScore);
		iboxQuizScore.setWidth("3em");

		htmlQuizContent = new HTML("<h2>Асуулт</h2>", true);
		quizPanel.add(htmlQuizContent);
		
		
		treeTopic.addItem("Математик");
		treeTopic.addItem("Програмчлал");
		treeTopic.addItem("Вэб");

	}
}
