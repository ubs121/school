package mn.goody.exam.client;

import mn.goody.exam.client.ui.HomePage;
import mn.goody.exam.client.ui.QuizBrowser;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;

public class ClientFactoryImpl implements ClientFactory {
	private static final EventBus eventBus = new SimpleEventBus();
	private static final PlaceController placeController = new PlaceController(
			eventBus);

	private static final HomePage homePage = new HomePage();
	private static final QuizBrowser quizBrowser = new QuizBrowser();

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public QuizBrowser getQuizBrowser() {
		return quizBrowser;
	}

	@Override
	public HomePage getHomePage() {
		return homePage;
	}

}