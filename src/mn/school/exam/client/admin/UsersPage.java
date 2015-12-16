package mn.goody.exam.client.admin;

import java.io.Serializable;
import java.util.List;

import mn.goody.exam.client.Exampro;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UsersPage extends Composite {

	private static UsersPageUiBinder uiBinder = GWT
			.create(UsersPageUiBinder.class);

	interface UsersPageUiBinder extends UiBinder<Widget, UsersPage> {
	}

	private Exampro root;
	@UiField
	SimplePanel leftPanel;
	@UiField
	TextBox txtValue;
	@UiField
	PushButton btnFind;
	@UiField
	PushButton btnNew;

	TreePanel<ModelData> groupTree;

	public UsersPage(Exampro root) {
		initWidget(uiBinder.createAndBindUi(this));

		buildTree();
	}

	private void buildTree() {
		final UserGroupNode model = getTreeModel();

		final TreeStore<ModelData> store = new TreeStore<ModelData>();
		store.add(model.getChildren(), true);

		groupTree = new TreePanel<ModelData>(store);
		groupTree.setDisplayProperty("name");
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
					UserGroupNode child = new UserGroupNode("Шинэ групп");
					store.add(folder, child, false);
					groupTree.setExpanded(folder, true);
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
					store.remove(sel);
				}
			}
		});
		contextMenu.add(remove);

		groupTree.setContextMenu(contextMenu);
	}

	public UserGroupNode getTreeModel() {
		// TODO: серверээс группын мэдээллийг татах
		UserGroupNode[] groups = new UserGroupNode[] {
				new UserGroupNode("Багш нар"),
				new UserGroupNode("Оюутан", new UserGroupNode[] {
						new UserGroupNode("1A"), new UserGroupNode("1B"),
						new UserGroupNode("2A"), new UserGroupNode("2B"), }) };

		UserGroupNode root = new UserGroupNode("root");
		for (int i = 0; i < groups.length; i++) {
			root.add((UserGroupNode) groups[i]);
		}
		return root;
	}

	@UiHandler("btnNew")
	public void onClickAdd(ClickEvent e) {
		txtValue.getText();
	}

	@SuppressWarnings("serial")
	class UserGroupNode extends BaseTreeModel implements Serializable {

		public UserGroupNode(String name) {
			set("name", name);
		}

		public UserGroupNode(String name, BaseTreeModel[] children) {
			this(name);
			for (int i = 0; i < children.length; i++) {
				add(children[i]);
			}
		}

		public Integer getId() {
			return (Integer) get("id");
		}

		public String getName() {
			return (String) get("name");
		}

		public String toString() {
			return getName();
		}
	}
}
