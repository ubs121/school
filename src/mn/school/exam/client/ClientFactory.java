package mn.goody.exam.client;

import mn.goody.exam.client.ui.HomePage;
import mn.goody.exam.client.ui.QuizBrowser;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;

public interface ClientFactory {
	EventBus getEventBus();

	PlaceController getPlaceController();

	QuizBrowser getQuizBrowser();

	HomePage getHomePage();
}
