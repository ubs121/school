package mn.goody.exam.client.ui;

import java.util.ArrayList;
import java.util.List;

import mn.goody.exam.client.Exampro;
import mn.goody.exam.shared.Quiz;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class QuizList extends Composite {

	private static QuizListUiBinder uiBinder = GWT
			.create(QuizListUiBinder.class);

	interface QuizListUiBinder extends UiBinder<Widget, QuizList> {
	}

	@UiField(provided = true)
	CellList<Quiz> quizList;
	@UiField(provided = true)
	SimplePager pager;

	private ListDataProvider<Quiz> dataProvider = new ListDataProvider<Quiz>();
	private EditQuizView quizEditor;
	private String currentTopicId;

	public QuizList(EditQuizView qe) {
		quizEditor = qe;
		quizList = new CellList<Quiz>(new QuizCell());

		// pager тохируулах
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(quizList);

		initWidget(uiBinder.createAndBindUi(this));

		quizList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		final SingleSelectionModel<Quiz> selectionModel = new SingleSelectionModel<Quiz>();
		quizList.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						Quiz q = selectionModel.getSelectedObject();

						// сонгосон асуултыг засварлагч дээр харуулах
						if (q != null) {
							quizEditor.setQuiz(q);
							quizEditor.setVisible(true);
							quizEditor.updateUI();
							dataProvider.refresh();
						} else {
							quizEditor.setVisible(false);
						}
					}
				});

		dataProvider.addDataDisplay(quizList);
	}

	public static class QuizCell extends AbstractCell<Quiz> {

		public QuizCell() {
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Quiz q, SafeHtmlBuilder sb) {
			if (q == null) {
				return;
			}

			HTML html = new HTML(q.getContent());
			String val = html.getText();
			if (val.length() > 80) {
				val = val.substring(0, 77) + "...";
			}
			sb.appendHtmlConstant("<div style='margin: 1em;'>");
			// засагдсан асуултыг өөр өнгөөр ялган харуулах
			if (q.isEdited()) {
				sb.appendHtmlConstant("<span style='font-size:small;color:red'>");
			} else {
				sb.appendHtmlConstant("<span style='font-size:small;color:gray'>");
			}
			sb.appendHtmlConstant("#" + String.valueOf(q.getId()));
			sb.appendHtmlConstant(" Оноо: " + String.valueOf(q.getScore()));
			sb.appendHtmlConstant(" Хугацаа: "
					+ String.valueOf(q.getDuration()) + " мин</span>");
			sb.appendHtmlConstant("<p>");
			sb.appendHtmlConstant(val);
			sb.appendHtmlConstant("</p></div>");

		}
	}

	public void filter(String topic_id) {
		currentTopicId = topic_id;
		Exampro.getService().queryQuizs(topic_id,
				new AsyncCallback<List<Quiz>>() {

					@Override
					public void onSuccess(List<Quiz> result) {
						quizEditor.setVisible(false);
						dataProvider.setList(result);
						dataProvider.refresh();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Асуултуудыг уншихад алдаа гарлаа!");
					}
				});
	}

	public void deleteQuizs() {
		final List<Quiz> quizsToDel = new ArrayList<Quiz>();
		final List<Quiz> quizs = dataProvider.getList();
		for (Quiz q : quizs) {
			if (quizList.getSelectionModel().isSelected(q)) {
				quizsToDel.add(q);
			}
		}

		if (quizsToDel.size() > 0) {
			List<Long> idsToDel = new ArrayList<Long>();
			for (Quiz q : quizsToDel) {
				idsToDel.add(q.getId());
			}
			// асуултуудыг устгах
			Exampro.getService().removeQuizs(idsToDel,
					new AsyncCallback<Void>() {
						@Override
						public void onSuccess(Void result) {
							// хүснэгтээс хасах
							quizs.removeAll(quizsToDel);
							dataProvider.refresh();
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Алдаа: " + caught);
						}
					});
		}
	}

	/**
	 * Жагсаалтад асуулт нэмэх
	 */
	public void addQuiz() {
		Quiz newQuiz = new Quiz();
		newQuiz.setTopic(currentTopicId);
		newQuiz.setEdited(true);

		final List<Quiz> quizs = dataProvider.getList();
		quizs.add(newQuiz);

		quizList.getSelectionModel().setSelected(newQuiz, true);
		dataProvider.refresh();
	}

	/**
	 * Бүх засагдсан асуултуудыг хадгалах
	 */
	public void saveQuizs() {
		List<Quiz> quizs = dataProvider.getList();
		for (final Quiz q : quizs) {
			if (q.isEdited()) {
				Exampro.getService().saveQuiz(q, new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {

						// List<Quiz> quizs = dataProvider.getList();
						// quizs.remove(q);
						q.setId(result);
						// quizs.add(q);

						quizEditor.setStatus("Асуултыг амжилттай хадгаллаа.");
						dataProvider.refresh();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Асуултыг хадгалахад алдаа гарлаа!");
					}
				});
			}
		}
	}

}
