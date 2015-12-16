package mn.goody.exam.client.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mn.goody.exam.client.Exampro;
import mn.goody.exam.shared.User;
import mn.goody.exam.shared.UserGroup;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

public class UsersPage extends Composite {

	private static UsersPageUiBinder uiBinder = GWT
			.create(UsersPageUiBinder.class);

	interface UsersPageUiBinder extends UiBinder<Widget, UsersPage> {
	}

	@UiField
	SimplePanel leftPanel;
	@UiField
	TextBox txtValue;
	@UiField(provided = true)
	CellTable<User> userTable;
	@UiField(provided = true)
	SimplePager pager;
	@UiField
	PushButton btnNew;
	@UiField
	PushButton btnSearch;
	@UiField
	PushButton btnDelete;

	private TreePanel<ModelData> groupTree;
	private ListDataProvider<User> dataProvider = new ListDataProvider<User>();

	private UserGroupNode selectedNode;
	public static final String TOKEN = "users";
	private TreeStore<ModelData> treeStore;

	final UserGroupNode adminGroups = new UserGroupNode("Админ", "gr1");
	final UserGroupNode teacherGroups = new UserGroupNode("Багш нар", "gr2");
	final UserGroupNode studentGroups = new UserGroupNode("Оюутан", "gr3");

	public UsersPage() {

		userTable = new CellTable<User>();

		// pager тохируулах
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(userTable);

		final MultiSelectionModel<User> selectionModel = new MultiSelectionModel<User>();
		userTable.setSelectionModel(selectionModel);

		// багануудыг үүсгэх
		initTableColumns(selectionModel);

		dataProvider.addDataDisplay(userTable);

		initWidget(uiBinder.createAndBindUi(this));

		buildTree();

	}

	private void initTableColumns(final SelectionModel<User> selectionModel) {
		Column<User, Boolean> checkColumn = new Column<User, Boolean>(
				new CheckboxCell(true)) {
			@Override
			public Boolean getValue(User object) {
				return selectionModel.isSelected(object);
			}
		};
		checkColumn.setFieldUpdater(new FieldUpdater<User, Boolean>() {
			public void update(int index, User object, Boolean value) {
				selectionModel.setSelected(object, value);
				dataProvider.refresh();
			}
		});
		userTable
				.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br>"));

		TextColumn<User> loginColumn = new TextColumn<User>() {
			public String getValue(User u) {
				return u.getLogin();
			}
		};
		userTable.addColumn(loginColumn, "Код");

		Column<User, String> pwdColumn = new Column<User, String>(
				new EditTextCell()) {
			public String getValue(User u) {
				return u.getPwd();
			}
		};
		pwdColumn.setFieldUpdater(new FieldUpdater<User, String>() {
			public void update(int index, User object, String value) {
				object.setPwd(value);
				updateUser(object);
			}
		});
		userTable.addColumn(pwdColumn, "Нууц үг");

		Column<User, String> nameColumn = new Column<User, String>(
				new EditTextCell()) {
			public String getValue(User u) {
				return u.getFirstName();
			}
		};
		nameColumn.setFieldUpdater(new FieldUpdater<User, String>() {
			public void update(int index, User object, String value) {
				object.setFirstName(value);
				updateUser(object);
			}
		});
		userTable.addColumn(nameColumn, "Нэр");

		Column<User, String> lnameColumn = new Column<User, String>(
				new EditTextCell()) {
			public String getValue(User u) {
				return u.getLastName();
			}
		};
		lnameColumn.setFieldUpdater(new FieldUpdater<User, String>() {
			public void update(int index, User object, String value) {
				object.setLastName(value);
				updateUser(object);
			}
		});
		userTable.addColumn(lnameColumn, "Эцгийн нэр");

		Column<User, String> emailColumn = new Column<User, String>(
				new EditTextCell()) {
			public String getValue(User u) {
				return u.getEmail();
			}
		};
		emailColumn.setFieldUpdater(new FieldUpdater<User, String>() {
			public void update(int index, User object, String value) {
				object.setEmail(value);
				updateUser(object);
			}
		});
		userTable.addColumn(emailColumn, "Э-мэйл");

		TextColumn<User> statusColumn = new TextColumn<User>() {
			public String getValue(User u) {
				if (u.getStatus().equals(User.AVAILABLE)) {
					return "байна";
				} else if (u.getStatus().equals(User.ABSENT)) {
					return "байхгүй";
				} else if (u.getStatus().equals(User.TESTING)) {
					return "шалгуулж байна";
				} else {
					return u.getStatus();
				}
			}
		};
		userTable.addColumn(statusColumn, "Одоогийн төлөв");

		TextColumn<User> testColumn = new TextColumn<User>() {
			public String getValue(User u) {
				return u.getCurrentTest();
			}
		};
		userTable.addColumn(testColumn, "Тест");

		TextColumn<User> ipColumn = new TextColumn<User>() {
			public String getValue(User u) {
				return u.getLastIp();
			}
		};
		userTable.addColumn(ipColumn, "Хаанаас");
		TextColumn<User> dateColumn = new TextColumn<User>() {
			public String getValue(User u) {
				if (u.getLastLogin() == null)
					return "<хоосон>";
				else
					return u.getLastLogin().toString();
			}
		};
		userTable.addColumn(dateColumn, "Хэзээ");
	}

	private void buildTree() {
		final UserGroupNode model = getTreeModel();

		treeStore = new TreeStore<ModelData>();
		treeStore.add(model.getChildren(), true);

		groupTree = new TreePanel<ModelData>(treeStore);
		groupTree.setDisplayProperty("name");
		groupTree.setAutoExpand(true);
		groupTree
				.setTitle("Хэрэглэгчийн жагсаалтыг шинэчилэх бол зангилаан дээр хоёр удаа дарна уу");
		groupTree.addListener(Events.OnClick,
				new Listener<TreePanelEvent<ModelData>>() {
					public void handleEvent(TreePanelEvent<ModelData> be) {
						UserGroupNode currentNode = (UserGroupNode) be
								.getNode().getModel();

						if (currentNode == selectedNode) {
							// серверээс татах
							queryUserByGroup(currentNode.getGroupId());
						} else {
							selectedNode = currentNode;

							Exampro.logger.log(Level.SEVERE, "Selected: "
									+ selectedNode.getName());
							// байгаа өгөгдлийг харуулах
							List<User> users = selectedNode.getUsers();
							if (users != null) {
								dataProvider.setList(users);
								dataProvider.refresh();
							} else {
								// байхгүй бол серверээс татах
								queryUserByGroup(selectedNode.getGroupId());
							}
						}

					};
				});

		leftPanel.add(groupTree);

		Menu contextMenu = new Menu();

		MenuItem insert = new MenuItem();
		insert.setText("Групп нэмэх");
		// insert.setIcon(Resources.ICONS.add());
		insert.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				ModelData folder = groupTree.getSelectionModel()
						.getSelectedItem();
				if (folder != null) {

					AddUserGroupDlg dlg = new AddUserGroupDlg(folder);
					dlg.setModal(true);
					dlg.center();
				}
			}
		});
		contextMenu.add(insert);

		MenuItem remove = new MenuItem();
		remove.setText("Устгах");
		// remove.setIcon(Resources.ICONS.delete());
		remove.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				List<ModelData> selected = groupTree.getSelectionModel()
						.getSelectedItems();
				for (ModelData sel : selected) {
					treeStore.remove(sel);
				}
			}
		});
		contextMenu.add(remove);

		MenuItem addUser = new MenuItem();
		addUser.setText("Сонгосон хэрэглэгчдийг нэмэх");
		addUser.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				ModelData folder = groupTree.getSelectionModel()
						.getSelectedItem();
				if (folder != null) {
					// TODO: Групп хооронд хэрэглэгчид шилжүүлэх
				}
			}
		});
		contextMenu.add(addUser);

		groupTree.setContextMenu(contextMenu);
		groupTree.getSelectionModel().select(adminGroups, true);

		// хэрэглэгчдийг ачаалах
		queryUserByGroup(adminGroups.getGroupId());
	}

	public UserGroupNode getTreeModel() {

		UserGroupNode[] groups = new UserGroupNode[] { adminGroups,
				teacherGroups, studentGroups };

		studentGroups
				.addChildren(new UserGroupNode[] {
						new UserGroupNode("1A", "gr4"),
						new UserGroupNode("1B", "gr5"),
						new UserGroupNode("2A", "gr6"),
						new UserGroupNode("2B", "gr7") });

		Exampro.getService().queryGroups(User.STUDENT_GROUP,
				new AsyncCallback<List<UserGroup>>() {

					@Override
					public void onSuccess(List<UserGroup> result) {
						for (UserGroup ug : result) {
							studentGroups.add(new UserGroupNode(ug.getName(),
									ug.getId()));
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Хэрэглэгчдийн группүүдийг татахад алдаа гарлаа!");

					}
				});

		UserGroupNode root = new UserGroupNode("Бүгд", "");
		for (int i = 0; i < groups.length; i++) {
			root.add((UserGroupNode) groups[i]);
		}

		return root;
	}

	@UiHandler("btnNew")
	public void onNewUser(ClickEvent e) {
		// хэрэглэгч нэмэх диалоги харуулах
		AddUserDlg userDlg = new AddUserDlg();
		userDlg.setModal(true);
		userDlg.center();
	}

	@UiHandler("btnSearch")
	public void onSearch(ClickEvent e) {
		List<User> matches = queryUser(txtValue.getText());
		dataProvider.setList(matches);
	}

	@UiHandler("btnDelete")
	public void onDelete(ClickEvent e) {
		removeUsers();
	}

	public void addUser(final User u) {
		if (selectedNode != null) {
			u.setGroup(selectedNode.getGroupId());
		}

		Exampro.getService().saveUser(u, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				// нэмсэн хэрэглэгчийг дэлгэцэнд харуулах
				List<User> users = dataProvider.getList();
				// Remove the contact first so we don't add a duplicate.
				users.remove(u);
				users.add(u);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Хэрэглэгч нэмэхэд алдаа гарлаа: "
						+ caught.toString());
			}
		});
	}

	/**
	 * u хэрэглэгчийг сервер болон дэлгэц дээр шинэчлэх
	 * 
	 * @param u
	 */
	public void updateUser(final User u) {
		Exampro.getService().saveUser(u, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				dataProvider.refresh();
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Хэрэглэгч нэмэхэд алдаа гарлаа: "
						+ caught.toString());
			}
		});
	}

	/**
	 * Серверээс gid группэд хамаарах хэрэглэгчдийн мэдээллийг татаж дэлгэцэнд
	 * харуулна
	 * 
	 * @param gid
	 */
	public void queryUserByGroup(String gid) {

		Exampro.getService().queryUsersByGroup(gid,
				new AsyncCallback<List<User>>() {

					@Override
					public void onSuccess(List<User> result) {
						if (selectedNode != null) {
							selectedNode.setUsers(result);
						}
						dataProvider.setList(result);
						dataProvider.refresh();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Хэрэглэгчдийн мэдээллийг татахад алдаа гарлаа: "
								+ caught.toString());

					}
				});
	}

	private List<User> queryUser(String filter) {
		Exampro.logger.log(Level.SEVERE, "Хайлт: " + filter);

		List<User> matches = new ArrayList<User>();

		if (selectedNode != null) {
			for (User u : selectedNode.getUsers()) {
				if (u.getLogin().contains(filter)) {
					matches.add(u);
					continue;
				}

				if (u.getFirstName().contains(filter)) {
					matches.add(u);
					continue;
				}

				if (u.getLastName().contains(filter)) {
					matches.add(u);
					continue;
				}
			}
		}
		return matches;
	}

	@SuppressWarnings("serial")
	class UserGroupNode extends BaseTreeModel implements Serializable {
		private List<User> users;

		public UserGroupNode(String name, String gid) {
			set("name", name);
			set("gid", gid);
		}

		public UserGroupNode(String name, String gid, BaseTreeModel[] children) {
			this(name, gid);
			for (int i = 0; i < children.length; i++) {
				add(children[i]);
			}
		}

		public void addChildren(BaseTreeModel[] children) {
			for (int i = 0; i < children.length; i++) {
				add(children[i]);
			}
		}

		public Integer getId() {
			return (Integer) get("id");
		}

		public String getGroupId() {
			return get("gid");
		}

		public String getName() {
			return get("name");
		}

		public String toString() {
			return getName();
		}

		public void setUsers(List<User> users) {
			this.users = users;
		}

		public List<User> getUsers() {
			return users;
		}
	}

	class AddUserGroupDlg extends DialogBox {
		private TextBox tbName;
		private ModelData node;

		public AddUserGroupDlg(ModelData node) {
			this.node = node;
			setHTML("Групп нэмэх");

			VerticalPanel panel = new VerticalPanel();
			setWidget(panel);
			tbName = new TextBox();
			tbName.setSize("200px", "17px");
			panel.add(tbName);

			Button btnAdd = new Button("Нэмэх");
			btnAdd.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					addUserGroup();
				}
			});
			panel.add(btnAdd);
		}

		private void addUserGroup() {
			final UserGroup ug = new UserGroup();
			ug.setName(tbName.getText());
			if (selectedNode != null) {
				ug.setParent(selectedNode.getGroupId());
			}
			Exampro.getService().saveUserGroup(ug, new AsyncCallback<String>() {

				@Override
				public void onSuccess(String result) {
					// модонд нэмэх
					UserGroupNode child = new UserGroupNode(ug.getName(),
							result);
					treeStore.add(node, child, false);
					groupTree.setExpanded(node, true);
					hide();
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Групп нэмэхэд алдаа гарлаа:" + caught);
					hide();
				}
			});
		}

		@Override
		protected void onPreviewNativeEvent(NativePreviewEvent preview) {
			super.onPreviewNativeEvent(preview);

			NativeEvent evt = preview.getNativeEvent();
			if (evt.getType().equals("keydown")) {
				switch (evt.getKeyCode()) {
				case KeyCodes.KEY_ENTER:

					addUserGroup();

					break;
				case KeyCodes.KEY_ESCAPE:
					hide();
					break;
				}
			}
		}
	}

	class AddUserDlg extends DialogBox {
		private TextBox tbEmail;
		private TextBox tbFname;
		private TextBox tbLname;
		private TextBox tbLogin;
		private TextBox tbPwd;

		public AddUserDlg() {
			setHTML("Хэрэглэгч нэмэх");

			AbsolutePanel absolutePanel = new AbsolutePanel();
			setWidget(absolutePanel);
			absolutePanel.setSize("333px", "267px");

			Label label = new Label("Нэр:");
			absolutePanel.add(label, 10, 10);

			tbFname = new TextBox();
			tbFname.setText(txtValue.getText());
			absolutePanel.add(tbFname, 102, 10);
			tbFname.setSize("198px", "17px");

			Label label_1 = new Label("Эцгийн нэр:");
			absolutePanel.add(label_1, 10, 48);

			tbLname = new TextBox();
			absolutePanel.add(tbLname, 102, 48);
			tbLname.setSize("198px", "17px");

			Label label_2 = new Label("Нэвтрэх нэр:");
			absolutePanel.add(label_2, 10, 105);

			tbLogin = new TextBox();
			absolutePanel.add(tbLogin, 102, 105);
			tbLogin.setSize("198px", "17px");

			Label label_3 = new Label("Нууц үг:");
			absolutePanel.add(label_3, 10, 142);

			tbPwd = new TextBox();
			absolutePanel.add(tbPwd, 102, 142);
			tbPwd.setSize("198px", "17px");

			Button btnAdd = new Button("Нэмэх");
			btnAdd.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					checkAndAddUser();
				}
			});
			absolutePanel.add(btnAdd, 254, 226);

			Label label_4 = new Label("Э-мэйл хаяг:");
			absolutePanel.add(label_4, 10, 181);

			tbEmail = new TextBox();
			absolutePanel.add(tbEmail, 102, 181);
			tbEmail.setSize("198px", "17px");

			Button btnCancel = new Button("Болих");
			btnCancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					closeDialog();
				}
			});
			absolutePanel.add(btnCancel, 10, 226);
		}

		@Override
		protected void onPreviewNativeEvent(NativePreviewEvent preview) {
			super.onPreviewNativeEvent(preview);

			NativeEvent evt = preview.getNativeEvent();
			if (evt.getType().equals("keydown")) {
				switch (evt.getKeyCode()) {
				case KeyCodes.KEY_ENTER:

					checkAndAddUser();

					break;
				case KeyCodes.KEY_ESCAPE:
					closeDialog();
					break;
				}
			}
		}

		private void closeDialog() {
			hide();
		}

		private void checkAndAddUser() {
			User user = new User();
			user.setFirstName(tbFname.getText());
			user.setLastName(tbLname.getText());
			user.setLogin(tbLogin.getText());
			user.setPwd(tbPwd.getText());
			user.setEmail(tbEmail.getText());

			// эрхийг тохируулах
			if (selectedNode != null) {
				UserGroupNode current = selectedNode;
				while (current != null) {
					if (current.getGroupId().equals("g1")) {
						user.addPermission(User.ADMIN_GROUP);
						break;
					}
					if (current.getGroupId().equals("g2")) {
						user.addPermission(User.TEACHER_GROUP);
						break;
					}

					current = (UserGroupNode) current.getParent();
				}
			}

			addUser(user);

			this.hide();
		}
	}

	private void removeUsers() {
		final List<User> usersToDel = new ArrayList<User>();
		final List<User> users = dataProvider.getList();
		for (User u : users) {
			if (userTable.getSelectionModel().isSelected(u)) {
				usersToDel.add(u);
			}
		}

		if (usersToDel.size() > 0) {
			List<String> idsToDel = new ArrayList<String>();
			for (User u : usersToDel) {
				idsToDel.add(u.getId());
			}
			Exampro.getService().removeUsers(idsToDel,
					new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							users.removeAll(usersToDel);
							dataProvider.refresh();
							Window.alert(usersToDel.size()
									+ " хэрэглэгч устгалаа");
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Хэрэглэгчдийг устгах үйлдэл амжилтгүй боллоо!");
						}
					});
		}
	}
}