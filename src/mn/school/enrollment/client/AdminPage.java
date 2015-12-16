package mn.school.enrollment.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminPage extends Pane {

    private static AdminPageUiBinder uiBinder = GWT
	    .create(AdminPageUiBinder.class);

    interface AdminPageUiBinder extends UiBinder<Widget, AdminPage> {
    }

    @UiField
    HorizontalSplitPanel splitPanel;
    @UiField
    Tree navTree;
    @UiField
    VerticalPanel rightPanel;
    @UiField
    FlexTable gridHeader;
    @UiField
    FlexTable requestList;
    @UiField
    Label statusBar;

    static final int CHECK_COLUMN = 0;
    static final int STATE_COLUMN = 1;
    static final int NAME_COLUMN = 2;
    static final int LASTNAME_COLUMN = 3;
    static final int REGNO_COLUMN = 4;
    static final int ESCORE_COLUMN = 5;
    static final int PROF_COLUMN = 6;

    private int range_start = 0;
    private int range_end = 0;

    CheckBox checkAll;

    private ArrayList<Integer> selectedRows = new ArrayList<Integer>();
    private List<EnrollRequestClient> enrolls;

    TreeItem selectedNode;

    public AdminPage() {
	initWidget(uiBinder.createAndBindUi(this));

	// элсэх хүсэлтүүд
	final TreeItem nodeEnroll = new TreeItem(
		"<strong>Элсэх хүсэлт</strong>");
	// nodeEnroll.setText("Элсэх хүсэлт");
	navTree.addItem(nodeEnroll);

	final TreeItem nodeDraft = new TreeItem("Ноорог");
	nodeEnroll.addItem(nodeDraft);

	final TreeItem nodeValid = new TreeItem("Элссэн");
	nodeEnroll.addItem(nodeValid);

	final TreeItem nodeCancel = new TreeItem("Татгалзсан, Цуцалсан");
	nodeEnroll.addItem(nodeCancel);

	nodeEnroll.setState(true);

	navTree.addSelectionHandler(new SelectionHandler<TreeItem>() {

	    @Override
	    public void onSelection(SelectionEvent<TreeItem> event) {
		selectedNode = event.getSelectedItem();

		if (event.getSelectedItem() == nodeEnroll) {
		    populateList();
		} else if (event.getSelectedItem() == nodeDraft) {
		    populateList(
			    "state == '" + EnrollRequestClient.DRAFT + "'", "",
			    "");
		} else if (event.getSelectedItem() == nodeValid) {
		    populateList("state == '" + EnrollRequestClient.VALIDATED
			    + "'", "", "");
		} else if (event.getSelectedItem() == nodeCancel) {
		    populateList("state == '" + EnrollRequestClient.CANCELLED
			    + "' || state == '" + EnrollRequestClient.REJECTED
			    + "'", "", "");
		}
	    }
	});

	gridHeader.getColumnFormatter().setWidth(CHECK_COLUMN, "20px");
	gridHeader.getColumnFormatter().setWidth(STATE_COLUMN, "60px");
	gridHeader.getColumnFormatter().setWidth(NAME_COLUMN, "200px");
	gridHeader.getColumnFormatter().setWidth(LASTNAME_COLUMN, "200px");
	gridHeader.getColumnFormatter().setWidth(REGNO_COLUMN, "60px");
	gridHeader.getColumnFormatter().setWidth(ESCORE_COLUMN, "20px");
	gridHeader.getColumnFormatter().setWidth(PROF_COLUMN, "100%");

	checkAll = new CheckBox();
	gridHeader.setWidget(0, CHECK_COLUMN, checkAll);
	gridHeader.setWidget(0, STATE_COLUMN, new Label("Төлөв"));
	gridHeader.setWidget(0, NAME_COLUMN, new Label("Нэр"));
	gridHeader.setWidget(0, LASTNAME_COLUMN, new Label("Эцгийн нэр"));
	gridHeader.setWidget(0, REGNO_COLUMN, new Label("Регистр"));
	gridHeader.setWidget(0, ESCORE_COLUMN, new Label("ЕШ оноо"));
	gridHeader.setWidget(0, PROF_COLUMN, new Label("Элсэх мэргэжил"));

	requestList.getColumnFormatter().setWidth(CHECK_COLUMN, "20px");
	requestList.getColumnFormatter().setWidth(STATE_COLUMN, "60px");
	requestList.getColumnFormatter().setWidth(NAME_COLUMN, "200px");
	requestList.getColumnFormatter().setWidth(LASTNAME_COLUMN, "200px");
	requestList.getColumnFormatter().setWidth(REGNO_COLUMN, "60px");
	requestList.getColumnFormatter().setWidth(ESCORE_COLUMN, "20px");
	requestList.getColumnFormatter().setWidth(PROF_COLUMN, "100%");

	// жагсаалтыг дүүргэх
	populateList();

	History.addValueChangeHandler(new ValueChangeHandler<String>() {
	    public void onValueChange(ValueChangeEvent<String> event) {
		String historyToken = event.getValue();
		if (historyToken != null) {
		    int row_id = -1;
		    try {
			row_id = Integer.parseInt(historyToken);

		    } catch (Exception ex) {

		    }
		    if (row_id < 0)
			return;

		    if (0 <= row_id && row_id < enrolls.size()) {
			final EnrollRequestClient en = enrolls.get(row_id);
			Button acceptButton = new Button("Зөвшөөрөх");
			Button rejectButton = new Button("Татгалзах");
			final ApproveDlg dlg = new ApproveDlg(en);
			final int row = row_id;
			dlg.setAcceptButton(acceptButton);
			acceptButton.addClickHandler(new ClickHandler() {
			    @Override
			    public void onClick(ClickEvent event) {
				getServiceProxy().зөвшөөрөх(en.getKey(),
					new AsyncCallback<String>() {
					    @Override
					    public void onSuccess(String result) {
						((Label) requestList.getWidget(
							row, STATE_COLUMN))
							.setText(EnrollRequestClient.VALIDATED);
					    }

					    @Override
					    public void onFailure(
						    Throwable caught) {

					    }
					});
				dlg.hide();
			    }
			});
			dlg.setRejectButton(rejectButton);
			rejectButton.addClickHandler(new ClickHandler() {

			    @Override
			    public void onClick(ClickEvent event) {
				getServiceProxy().татгалзах(en.getKey(), "",
					new AsyncCallback<String>() {
					    @Override
					    public void onSuccess(String result) {
						((Label) requestList.getWidget(
							row, STATE_COLUMN))
							.setText(EnrollRequestClient.CANCELLED);
					    }

					    @Override
					    public void onFailure(
						    Throwable caught) {
					    }
					});
				dlg.hide();

			    }
			});
			dlg.center();
			dlg.show();
		    }
		}
	    }
	});
    }

    public void populateList() {
	selectedRows.clear();
	requestList.removeAllRows();

	getServiceProxy().элсэхХүсэлтүүд(
		new AsyncCallback<List<EnrollRequestClient>>() {

		    @Override
		    public void onSuccess(List<EnrollRequestClient> result) {
			enrolls = result;
			for (EnrollRequestClient e : enrolls) {
			    addEnroll(e);
			}
		    }

		    @Override
		    public void onFailure(Throwable caught) {
			Window
				.alert("Элсэлтийн өгөгдлийг ачаалахад алдаа гарлаа");
		    }
		});
    }

    public void populateList(String where, String order, String range) {
	selectedRows.clear();
	requestList.removeAllRows();

	getServiceProxy().элсэхХүсэлтүүд(where, order, range,
		new AsyncCallback<List<EnrollRequestClient>>() {

		    @Override
		    public void onSuccess(List<EnrollRequestClient> result) {
			enrolls = result;
			for (EnrollRequestClient e : enrolls) {
			    addEnroll(e);
			}
		    }

		    @Override
		    public void onFailure(Throwable caught) {
			Window
				.alert("Элсэлтийн өгөгдлийг ачаалахад алдаа гарлаа");
		    }
		});
    }

    public void addEnroll(final EnrollRequestClient en) {
	final int row = requestList.getRowCount();
	final CheckBox tick = new CheckBox();
	tick.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		handleCheck(row, tick.isChecked());
	    }
	});
	requestList.setWidget(row, CHECK_COLUMN, tick);
	requestList.setWidget(row, STATE_COLUMN, new Label(en.getТөлөв()));
	Hyperlink link = new Hyperlink(en.getНэр(), String.valueOf(row));
	requestList.setWidget(row, NAME_COLUMN, link);
	requestList.setWidget(row, LASTNAME_COLUMN,
		new Label(en.getЭцгийнНэр()));
	requestList.setWidget(row, REGNO_COLUMN, new Label(en.getРегистр()));
	requestList.setWidget(row, ESCORE_COLUMN, new Label(String.valueOf(en
		.getЕшОноо())));
	requestList.setWidget(row, PROF_COLUMN,
		new Label(en.getЭлсэхМэргэжил()));

    }

    private void handleCheck(int row, boolean checked) {
	if (checked)
	    selectedRows.add(row);
	else
	    selectedRows.remove(new Integer(row));
    }

    private String getVisibleRange() {
	String s = "";
	if (range_start < range_end) {
	    s = range_start + "," + range_end;
	}
	return s;
    }

    @UiHandler("delete")
    public void delete(ClickEvent e) {
	// устгах бичлэгүүдийг ялгах
	List<String> idsToDelete = new ArrayList<String>();
	for (Integer row : selectedRows) {
	    EnrollRequestClient en = enrolls.get(row);
	    idsToDelete.add(en.getKey());
	}

	if (idsToDelete.size() > 0) {
	    // устгах хүсэлт илгээх
	    getServiceProxy().хүсэлтУстгах(idsToDelete,
		    new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			    statusBar
				    .setText("Бичлэгүүдийг амжилттай устгалаа.");

			    // жагсаалтыг шинэчлэх
			    populateList();
			}

			@Override
			public void onFailure(Throwable caught) {
			    Window
				    .alert("Сонгосон мөрүүдийг устгахад алдаа гарлаа!");
			}
		    });
	}
    }

    @UiHandler("find")
    public void find(ClickEvent e) {
	FindDialog dlgFind = new FindDialog();
	dlgFind.center();
	dlgFind.show();
    }

    class FindDialog extends DialogBox implements ClickHandler {
	TextBox findName;
	TextBox findRegno;
	TextBox findEmail;
	ListBox scoreOp;
	TextBox findScore;

	Button doFind;
	Label error;

	public FindDialog() {
	    setText("Хайлт");

	    doFind = new Button("Хайх", this);
	    findName = new TextBox();
	    findRegno = new TextBox();
	    findEmail = new TextBox();
	    scoreOp = new ListBox();
	    scoreOp.addItem("==");
	    scoreOp.addItem(">");
	    scoreOp.addItem("<");

	    findScore = new TextBox();
	    error = new Label();
	    error.setStyleName("errorLabel");

	    Grid grid = new Grid(6, 2);
	    grid.setWidget(0, 0, new Label("Нэр:"));
	    grid.setWidget(0, 1, findName);
	    grid.setWidget(1, 0, new Label("Регистр:"));
	    grid.setWidget(1, 1, findRegno);
	    grid.setWidget(2, 0, new Label("И-мэйл:"));
	    grid.setWidget(2, 1, findEmail);
	    Panel scoreFilter = new HorizontalPanel();
	    scoreFilter.add(new Label("ЕШ оноо:"));
	    scoreFilter.add(scoreOp);
	    grid.setWidget(3, 0, scoreFilter);
	    grid.setWidget(3, 1, findScore);
	    grid.setWidget(4, 0, new Button("Хаах", this));
	    grid.setWidget(4, 1, doFind);
	    grid.setWidget(5, 1, error);
	    setWidget(grid);
	}

	@Override
	public void onClick(ClickEvent e) {
	    if (e.getSource().equals(doFind)) {
		String where = "";
		String order = "";

		if (selectedNode != null) {
		    if (selectedNode.getText().equals("Ноорог"))
			where = " state == 'draft' ";
		    else if (selectedNode.getText().equals("Элссэн"))
			where = " state == 'validated' ";
		    else if (selectedNode.getText().equals(
			    "Татгалзсан, Цуцалсан"))
			where = " (state == 'cancelled' || state == 'rejected' )";
		}

		if (findName.getText().length() > 0) {
		    if (where.length() > 0)
			where = where + " && "; // залгах үйлдэл

		    // name LIKE pattern + '%'
		    where = where + " (name >= '" + findName.getText()
			    + "' && name < '" + findName.getText() + "\ufffd')";
		}

		if (findRegno.getText().length() > 0) {
		    if (where.length() > 0)
			where = where + " && ";
		    where = where + " (regno >= '" + findRegno.getText()
			    + "' && regno < '" + findRegno.getText()
			    + "\ufffd')";
		}

		if (findEmail.getText().length() > 0) {
		    if (where.length() > 0)
			where = where + " && ";
		    where = where + " (email >= '" + findEmail.getText()
			    + "' && email < '" + findEmail.getText()
			    + "\ufffd')";
		}

		if (findScore.getText().length() > 0) {
		    if (where.length() > 0)
			where = where + " && ";
		    int selOp = scoreOp.getSelectedIndex();
		    if (selOp < 0)
			selOp = 0;

		    where = where + " escore " + scoreOp.getItemText(selOp)
			    + Float.valueOf(findScore.getText());
		}

		// нөхцөл хангаж байгаа бичлэгүүдийг харуулах
		if (where.length() > 0) {
		    populateList(where, order, getVisibleRange());
		}

	    } else {
		this.hide();
	    }
	}
    }
}
