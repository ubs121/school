package mn.goody.exam.client;

import mn.goody.exam.client.admin.UsersPage;
import mn.goody.exam.client.designer.QuizBrowser;
import mn.goody.exam.client.designer.TestPanel;
import mn.goody.exam.shared.User;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Exampro implements EntryPoint {
	private static final String SERVER_ERROR = "Сервертэй холбогдоход алдаа гарлаа."
			+ " Сүлжээний холболтоо шалгаад дахин оролдоно уу.";

	/**
	 * Сервистэй харилцах проксинууд үүсгэх
	 */
	private TestEditingServiceAsync editService = (TestEditingServiceAsync) GWT
			.create(TestEditingService.class);

	private TestingServiceAsync testService = (TestingServiceAsync) GWT
			.create(TestingService.class);

	public TestEditingServiceAsync getEditServiceProxy() {
		return editService;
	}

	// session data
	private User user;
	private boolean loggedIn;

	private HomePage homePage;
	private MenuBar mainMenu;
	
	// асуултын самбар
	private QuizBrowser quizBrowser;
	private UsersPage usersPage;
	private TestPanel testPane;

	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// create menu bar
		mainMenu = new MenuBar();
		MenuBar menuBar_5 = new MenuBar(true);

		MenuItem mntmExampro = new MenuItem("ExamPro", false, menuBar_5);

		MenuItem menuSignInOut = new MenuItem("Нэвтрэх", false, new Command() {
			public void execute() {
				showHomePage();
			}
		});
		menuBar_5.addItem(menuSignInOut);
		mainMenu.addItem(mntmExampro);
		MenuBar menuBar_4 = new MenuBar(true);

		MenuItem menuItem = new MenuItem("Тест", false, menuBar_4);

		MenuItem menuTesting = new MenuItem("Тест өгөх", false, (Command) null);
		menuBar_4.addItem(menuTesting);
		mainMenu.addItem(menuItem);
		MenuBar menuBar_3 = new MenuBar(true);

		MenuItem menuTeacher = new MenuItem("Тест засвар", false, menuBar_3);

		MenuItem menuTest = new MenuItem("Тест", false, (Command) null);
		menuBar_3.addItem(menuTest);

		MenuItem menuQuizs = new MenuItem("Асуулт", false, new Command() {
			public void execute() {
				showQuizBrowser();
			}
		});
		menuBar_3.addItem(menuQuizs);
		mainMenu.addItem(menuTeacher);
		MenuBar menuBar_2 = new MenuBar(true);

		MenuItem menuAdmin = new MenuItem("Админ", false, menuBar_2);

		MenuItem menuTopic = new MenuItem("Сэдэв", false, (Command) null);
		menuBar_2.addItem(menuTopic);

		MenuItem menuUsers = new MenuItem("Хэрэглэгч", false, (Command) null);
		menuBar_2.addItem(menuUsers);
		mainMenu.addItem(menuAdmin);

		
		MenuBar menuBar = new MenuBar(true);

		MenuItem menuReport = new MenuItem("Тайлан", false, menuBar);

		MenuItem menuItem_3 = new MenuItem("Дүн харах", false, (Command) null);
		menuBar.addItem(menuItem_3);
		mainMenu.addItem(menuReport);
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

		showHomePage();
	}

	private void showHomePage() {
		if (homePage == null) {
			homePage = new HomePage(this);
		}
		RootPanel.get("content").clear();
		RootPanel.get("content").add(homePage);
	}

	private void showQuizBrowser() {
		if (quizBrowser == null) {
			quizBrowser = new QuizBrowser(this);
		}
		RootPanel.get("content").clear();
		RootPanel.get("content").add(quizBrowser);
	}

	private void showStudentUI() {

	}

	public void setLoggedIn(boolean loggedIn) {
		
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void showAdminUI() {
		// TODO: цэснүүдийг тохируулах
		
		// үндсэн цэсийг харуулах
		RootPanel rootPanel = RootPanel.get("nav");
		rootPanel.add(mainMenu);
		
		// TODO: хэрэглэгчдийн хуудас харуулах
		
		if (usersPage == null) {
			usersPage = new UsersPage(this);
		}
		
		RootPanel.get("content").clear();
		RootPanel.get("content").add(usersPage);
		
	}
}
