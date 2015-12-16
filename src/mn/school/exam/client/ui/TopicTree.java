package mn.goody.exam.client.ui;

import java.io.Serializable;
import java.util.List;

import mn.goody.exam.client.Exampro;
import mn.goody.exam.shared.Topic;

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
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicTree extends Composite {

	private static TopicTreeUiBinder uiBinder = GWT
			.create(TopicTreeUiBinder.class);

	interface TopicTreeUiBinder extends UiBinder<Widget, TopicTree> {
	}

	@UiField(provided = true)
	TreePanel<ModelData> topicTree;

	private TreeStore<ModelData> treeStore;
	private TopicNode selectedNode;
	private QuizList quizList;

	public TopicTree(QuizList ql) {
		quizList = ql;
		buildTree();

		initWidget(uiBinder.createAndBindUi(this));
	}

	private void buildTree() {
		final TopicNode model = getTreeModel();

		treeStore = new TreeStore<ModelData>();
		treeStore.add(model, true);

		topicTree = new TreePanel<ModelData>(treeStore);
		topicTree.setDisplayProperty("name");
		topicTree.setAutoExpand(true);
		topicTree
				.setTitle("Жагсаалтыг шинэчилэх бол зангилаан дээр хоёр удаа дарна уу");

		topicTree.addListener(Events.OnClick,
				new Listener<TreePanelEvent<ModelData>>() {
					public void handleEvent(TreePanelEvent<ModelData> be) {
						TopicNode currentNode = (TopicNode) be.getNode()
								.getModel();

						if (currentNode != null) {
							quizList.filter(currentNode.getTopicId());
						}
					};
				});

		Menu contextMenu = new Menu();

		MenuItem insert = new MenuItem();
		insert.setText("Сэдэв нэмэх");
		// insert.setIcon(Resources.ICONS.add());
		insert.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				ModelData folder = topicTree.getSelectionModel()
						.getSelectedItem();
				if (folder != null) {

					AddTopicDlg dlg = new AddTopicDlg(folder);
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
				List<ModelData> selected = topicTree.getSelectionModel()
						.getSelectedItems();
				for (ModelData sel : selected) {
					treeStore.remove(sel);
				}
			}
		});
		contextMenu.add(remove);

		MenuItem addUser = new MenuItem();
		addUser.setText("Сонгосон асуултуудыг нэмэх");
		addUser.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				ModelData folder = topicTree.getSelectionModel()
						.getSelectedItem();
				if (folder != null) {
					// TODO: Сэдэв хооронд асуултууд шилжүүлэх
				}
			}
		});
		contextMenu.add(addUser);

		topicTree.setContextMenu(contextMenu);

		// асуултуудыг харуулах
		quizList.filter("");
	}

	private void addChildren(final TopicNode node) {
		Exampro.getService().queryTopics(node.getTopicId(),
				new AsyncCallback<List<Topic>>() {

					@Override
					public void onSuccess(List<Topic> result) {
						for (Topic t : result) {
							TopicNode childNode = new TopicNode(t.getName(), t
									.getId());

							treeStore.add(node, childNode, false);
							// дэд сэдвүүд нэмэх
							// addChildren(node);
						}

						topicTree.setExpanded(node, true);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Сэдвүүдийг татахад алдаа гарлаа!");
					}
				});
	}

	public TopicNode getTreeModel() {

		final TopicNode root = new TopicNode("Бүгд", "");

		addChildren(root);

		return root;
	}

	@SuppressWarnings("serial")
	class TopicNode extends BaseTreeModel implements Serializable {
		public TopicNode(String name, String tid) {
			set("name", name);
			set("tid", tid);
		}

		public TopicNode(String name, String tid, BaseTreeModel[] children) {
			this(name, tid);
			for (int i = 0; i < children.length; i++) {
				add(children[i]);
			}
		}

		public void addChildren(BaseTreeModel[] children) {
			for (int i = 0; i < children.length; i++) {
				add(children[i]);
			}
		}

		public void addChild(BaseTreeModel child) {
			add(child);
		}

		public Integer getId() {
			return (Integer) get("id");
		}

		public String getTopicId() {
			return get("tid");
		}

		public String getName() {
			return get("name");
		}

		public String toString() {
			return getName();
		}
	}

	class AddTopicDlg extends DialogBox {
		private TextBox tbName;
		private ModelData node;

		public AddTopicDlg(ModelData node) {
			this.node = node;
			setHTML("Сэдэв нэмэх");

			VerticalPanel panel = new VerticalPanel();
			setWidget(panel);
			tbName = new TextBox();
			tbName.setSize("200px", "17px");
			panel.add(tbName);

			Button btnAdd = new Button("Нэмэх");
			btnAdd.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					addTopic();
				}
			});
			panel.add(btnAdd);
		}

		private void addTopic() {
			final Topic t = new Topic();
			t.setName(tbName.getText());
			if (selectedNode != null) {
				t.setParent(selectedNode.getTopicId());
			}
			Exampro.getService().saveTopic(t, new AsyncCallback<String>() {

				@Override
				public void onSuccess(String result) {
					// сэдвийг модонд нэмэх
					TopicNode child = new TopicNode(t.getName(), result);
					treeStore.add(node, child, false);
					topicTree.setExpanded(node, true);
					hide();
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Сэдэв нэмэхэд алдаа гарлаа:" + caught);
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

					addTopic();

					break;
				case KeyCodes.KEY_ESCAPE:
					hide();
					break;
				}
			}
		}
	}
}
