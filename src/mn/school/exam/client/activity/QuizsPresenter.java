package mn.goody.exam.client.activity;

import java.util.List;

import mn.goody.exam.client.ExamServiceAsync;
import mn.goody.exam.client.event.AddQuizEvent;
import mn.goody.exam.shared.Quiz;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class QuizsPresenter implements Presenter {
	private List<Quiz> quizs;

	public interface Display {
		HasClickHandlers getAddQuizButton();

		HasClickHandlers getDeleteQuizButton();

		HasClickHandlers getQuizList();

		void setQuizData(List<String> data);

		int getClickedQuizRow(ClickEvent event);

		List<Integer> getSelectedQuizRows();

		Widget asWidget();

		HasClickHandlers getAddTopicButton();

		HasClickHandlers getDeleteTopicButton();

		HasClickHandlers getTopicList();
	}

	private final ExamServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public QuizsPresenter(ExamServiceAsync rpcService, HandlerManager eventBus,
			Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}

	public void bind() {
		// үзэгдлүүдийг холбох
		display.getAddQuizButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new AddQuizEvent());
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		fetchQuizs();
	}

	private void fetchQuizs() {
		// rpcService
	}

}
