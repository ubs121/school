package mn.goody.exam.client;

import mn.goody.exam.client.activity.HomePresenter;
import mn.goody.exam.client.activity.Presenter;
import mn.goody.exam.client.activity.QuizsPresenter;
import mn.goody.exam.client.event.AddQuizEvent;
import mn.goody.exam.client.event.AddQuizEventHandler;
import mn.goody.exam.client.ui.AboutDlg;
import mn.goody.exam.client.ui.HomePage;
import mn.goody.exam.client.ui.QuizBrowser;
import mn.goody.exam.client.ui.ReportPage;
import mn.goody.exam.client.ui.TestPage;
import mn.goody.exam.client.ui.TestingPage;
import mn.goody.exam.client.ui.UsersPage;
import mn.goody.exam.shared.User;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.RootPanel;

public class AppController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private final ExamServiceAsync rpcService;
	private HasWidgets container;

	// TODO: хэрэгтэй эсэхийг нягтлах
	private static User currentUser;

	public AppController(ExamServiceAsync rpcService, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;

		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);

		eventBus.addHandler(AddQuizEvent.TYPE, new AddQuizEventHandler() {
			public void onAddQuiz(AddQuizEvent event) {
				doAddNewQuiz();
			}
		});

	}

	private void doAddNewQuiz() {
		History.newItem(QuizBrowser.TOKEN);
	}

	private void doEditQuizCancelled() {
		History.newItem(QuizBrowser.TOKEN);
	}

	private void doQuizUpdated() {
		History.newItem(QuizBrowser.TOKEN);
	}

	public void go(final HasWidgets container) {
		this.container = container;

		if ("".equals(History.getToken())) {
			History.newItem(HomePage.TOKEN);
		} else {
			History.fireCurrentHistoryState();
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();

		if (token != null) {
			Presenter presenter = null;

			if (token.equals(HomePage.TOKEN)) {
				presenter = new HomePresenter(rpcService, eventBus,
						new HomePage());
			} else if (token.equals(QuizBrowser.TOKEN)) {
				presenter = new QuizsPresenter(rpcService, eventBus,
						new QuizBrowser());
			} else {
				History.fireCurrentHistoryState();
			}

			if (presenter != null) {
				presenter.go(container);
			}
		}
	}

	private static MenuBar buildMenuForCurrentUser() {
		MenuBar mainMenu = new MenuBar();
		mainMenu.setAutoOpen(true);
		MenuBar menuBar_5 = new MenuBar(true);

		MenuItem menuExampro = new MenuItem("ExamPro", false, menuBar_5);

		MenuItem menuSignOut = new MenuItem("Гарах", false, new Command() {
			public void execute() {
				logout();
			}
		});
		menuBar_5.addItem(menuSignOut);
		mainMenu.addItem(menuExampro);

		MenuBar testMenu = new MenuBar(true);

		MenuItem menuItem = new MenuItem("Тест", false, testMenu);

		MenuItem menuTesting = new MenuItem("Тест өгөх", false, (Command) null);
		testMenu.addItem(menuTesting);

		// admin цэс
		if (currentUser.hasPermission(User.ADMIN_GROUP)) {
			MenuBar menuBar_2 = new MenuBar(true);

			MenuItem menuAdmin = new MenuItem("Админ", false, menuBar_2);

			MenuItem menuUsers = new MenuItem("Хэрэглэгч", false,
					new Command() {
						public void execute() {
							History.newItem(UsersPage.TOKEN, true);
						}
					});
			menuBar_2.addItem(menuUsers);
			mainMenu.addItem(menuAdmin);
		}

		if (currentUser.hasPermission(User.TEACHER_GROUP)
				|| currentUser.hasPermission(User.ADMIN_GROUP)) {
			MenuItem createTestMenu = new MenuItem("Тест бэлтгэх", false,
					new Command() {
						public void execute() {
							History.newItem(TestPage.TOKEN);
						}
					});
			testMenu.addItem(createTestMenu);
			MenuItem menuQuizs = new MenuItem("Асуулт засах", false,
					new Command() {
						public void execute() {
							History.newItem(QuizBrowser.TOKEN, true);
						}
					});
			testMenu.addItem(menuQuizs);
		}

		mainMenu.addItem(menuItem);

		// тайлангийн цэс
		MenuBar menuBar = new MenuBar(true);

		MenuItem menuReport = new MenuItem("Тайлан", false, menuBar);

		MenuItem menuItem_3 = new MenuItem("Дүн харах", false, new Command() {
			public void execute() {
				History.newItem(ReportPage.TOKEN, true);
			}
		});
		menuBar.addItem(menuItem_3);
		mainMenu.addItem(menuReport);

		// тусламж цэс
		MenuBar menuBar_1 = new MenuBar(true);

		MenuItem menuHelp = new MenuItem("Тусламж", false, menuBar_1);

		MenuItem menuItem_4 = new MenuItem("Тест зохиох заавар", false,
				(Command) null);
		menuBar_1.addItem(menuItem_4);

		MenuItem menuItem_2 = new MenuItem("Тест өгөх заавар", false,
				(Command) null);
		menuBar_1.addItem(menuItem_2);

		MenuItemSeparator separator = new MenuItemSeparator();
		menuBar_1.addSeparator(separator);

		MenuItem menuAbout = new MenuItem("Програмын тухай", false,
				new Command() {
					public void execute() {
						AboutDlg about = new AboutDlg();
						about.center();
					}
				});
		menuBar_1.addItem(menuAbout);
		mainMenu.addItem(menuHelp);

		return mainMenu;
	}

	public static void logout() {
		RootPanel.get("nav").clear();
		currentUser = null;
		History.newItem(HomePage.TOKEN, true);
	}

	public static void setCurrentUser(User u) {
		currentUser = u;
		if (currentUser != null) {
			RootPanel.get("nav").clear();
			RootPanel.get("nav").add(buildMenuForCurrentUser());

			if (currentUser.hasPermission(User.ADMIN_GROUP)) {
				History.newItem(UsersPage.TOKEN, true);
			} else if (currentUser.hasPermission(User.TEACHER_GROUP)) {
				History.newItem(QuizBrowser.TOKEN, true);
			} else {
				History.newItem(TestingPage.TOKEN, true);
			}
		} else {
			History.newItem(HomePage.TOKEN, true);
		}
	}

	public static User getCurrentUser() {
		return currentUser;
	}
}
