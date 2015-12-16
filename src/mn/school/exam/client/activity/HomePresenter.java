package mn.goody.exam.client.activity;

import mn.goody.exam.client.ExamServiceAsync;
import mn.goody.exam.client.event.LoginEvent;
import mn.goody.exam.client.event.LoginEventHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class HomePresenter implements Presenter {
	public interface Display {
		HasClickHandlers getLoginButton();

		Widget asWidget();
	}

	private final ExamServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public HomePresenter(ExamServiceAsync rpcService, HandlerManager eventBus,
			Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}

	public void bind() {
		// үзэгдлүүдийг холбох
		display.getLoginButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LoginEvent());
			}
		});

		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {

			@Override
			public void onLogin(LoginEvent event) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

}
