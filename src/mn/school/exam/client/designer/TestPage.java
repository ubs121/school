package mn.goody.exam.client.designer;

import java.util.List;

import mn.goody.exam.client.Exampro;
import mn.goody.exam.shared.Quiz;
import mn.goody.exam.shared.Test;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class TestPage extends Composite {

	public static final String TOKEN = "tests";
	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, TestPage> {
	}

	@UiField
	CellList<Test> testList;
	@UiField
	Button btnStartStop;
	@UiField
	TextBox tbTestName;
	@UiField
	ListBox lstTestType;
	@UiField
	ListBox lstTestGroup;
	@UiField
	ListBox lstTestStartType;
	@UiField
	Label lblTestDuration;
	@UiField
	Label lblTestScore;
	@UiField
	Button btnAddQuiz;
	@UiField
	Button btnRemoveQuiz;
	@UiField
	Button btnSave;
	@UiField
	Button btnAddTest;
	@UiField
	HorizontalPanel panelStartTime;
	@UiField
	CellTable<Quiz> quizTable;

	// сонгогдсон тест
	private Test currentTest;
	private ListDataProvider<Test> testDataProvider = new ListDataProvider<Test>();

	public TestPage() {
		initWidget(uiBinder.createAndBindUi(this));
		final SingleSelectionModel<Test> selectionModel = new SingleSelectionModel<Test>();
		testList.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						Test t = selectionModel.getSelectedObject();
						setCurrentTest(t);
					}
				});

		testDataProvider.addDataDisplay(testList);

		loadTests();
	}

	@UiFactory
	CellList<Test> createCellList() {
		return new CellList<Test>(new TestCell());
	};

	private void loadTests() {
		Exampro.getService().queryTests(Exampro.getCurrentUser(),
				new AsyncCallback<List<Test>>() {

					@Override
					public void onSuccess(List<Test> result) {
						testDataProvider.setList(result);
						testDataProvider.refresh();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Тестүүд уншихад алдаа гарлаа!");
					}
				});
	}
	
	

	@UiHandler("btnAddTest")
	public void btnAddTest_click(ClickEvent e) {
		AddTestDlg dlg = new AddTestDlg(this);
		dlg.center();
	}

	@UiHandler("btnStartStop")
	public void onStartClick(ClickEvent e) {

		if (getCurrentTest() != null) {
			// TODO: Тестийг засах боломжгүй болгох
			if (getCurrentTest().getStatus() == Test.STATUS_DRAFT) {
				getCurrentTest().setStatus(Test.STATUS_RUNNING);
				btnStartStop.setText("Зогсоох");
			} else if (getCurrentTest().getStatus() == Test.STATUS_RUNNING) {
				getCurrentTest().setStatus(Test.STATUS_DRAFT);
				btnStartStop.setText("Одоо эхлэх");
			}
		}
	}

	@UiHandler("lstTestStartType")
	public void onStartTypeChanged(ChangeEvent e) {
		if (lstTestStartType.getSelectedIndex() == 0) {
			btnStartStop.setVisible(true);
			panelStartTime.setVisible(false);
		} else {
			btnStartStop.setVisible(false);
			panelStartTime.setVisible(true);
		}
	}

	public static class TestCell extends AbstractCell<Test> {
		public TestCell() {
		}

		@Override
		public void render(Test t, Object key, SafeHtmlBuilder sb) {
			if (t == null) {
				sb.appendHtmlConstant("<p></p>");
			}

			sb.appendHtmlConstant("<span>" + t + "</span>");
		}
	}

	public void addTest(final Test t) {
		Exampro.getService().saveTest(t, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				List<Test> tests = testDataProvider.getList();
				tests.remove(t);
				t.setId(result);
				tests.add(t);
				testDataProvider.refresh();
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Тест хадгалахад алдаа гарлаа!");
			}
		});

	}

	private void setCurrentTest(Test t) {
		this.currentTest = t;
		tbTestName.setText(t.getName());
	}

	private Test getCurrentTest() {
		return currentTest;
	}
}
