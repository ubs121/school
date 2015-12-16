package mn.goody.exam.client.designer;

import java.util.ArrayList;
import java.util.List;

import mn.goody.exam.client.Exampro;
import mn.goody.exam.shared.Topic;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class QuizTopicTree extends Composite {

	Exampro host;
	QuizList qlist;

	@UiField
	Tree navTree;

	public QuizTopicTree(Exampro host, QuizList qlist) {
		this.host = host;
		this.qlist = qlist;

		navTree.addSelectionHandler(treeSelectionHanlder);

		List<Topic> topics = getRootTopics();
		for (final Topic t : topics) {
			TreeItem item = createTreeItem(t);
			navTree.addItem(item);
		}
	}

	SelectionHandler<TreeItem> treeSelectionHanlder = new SelectionHandler<TreeItem>() {
		@Override
		public void onSelection(SelectionEvent<TreeItem> event) {
			TreeItem sel_item = event.getSelectedItem();
			qlist.change_topic((Topic) sel_item.getUserObject());
		}
	};

	protected TreeItem createTreeItem(Topic t) {
		TreeItem item = new TreeItem(t.getName());
		item.setState(true);
		item.setUserObject(t);
		for (Topic child : getChildren(t)) {
			item.addItem(createTreeItem(child));
		}
		return item;
	}

	protected List<Topic> getRootTopics() {
		List<Topic> categories = new ArrayList<Topic>();
		categories.add(new Topic("Math"));
		categories.add(new Topic("IT"));
		return categories;
	}

	// test method
	protected List<Topic> getChildren(Topic qc) {
		List<Topic> children = new ArrayList<Topic>();
		if (qc.getName().equals("Math")) {
			children.add(new Topic("Тоон дараалал"));
			children.add(new Topic("Тэгшитгэл"));
		} else if (qc.getName().equals("IT")) {
			children.add(new Topic("Програмчлал"));
			children.add(new Topic("Өгөгдлийн сан"));
		}
		return children;
	}

	public void new_topic(ClickEvent e) {
		NewTopicDialog dlgNewTopic = new NewTopicDialog();
		dlgNewTopic.center();
		dlgNewTopic.show();
	}

	class NewTopicDialog extends DialogBox implements ClickHandler {
		TextBox name;
		Button okButton;

		public NewTopicDialog() {
			setText("Сэдэв нэмэх");

			okButton = new Button("Нэмэх", this);
			name = new TextBox();

			Grid grid = new Grid(4, 2);
			grid.setWidget(0, 0, new Label("Нэр:"));
			grid.setWidget(0, 1, name);
			grid.setWidget(1, 0, new Button("Болих", this));
			grid.setWidget(1, 1, okButton);
			setWidget(grid);
		}

		@Override
		public void onClick(ClickEvent e) {
			if (e.getSource().equals(okButton)) {
				final Topic new_child = new Topic(name.getText());
				TreeItem sel_node = navTree.getSelectedItem();
				if (sel_node != null) {
					new_child.setParent((String) sel_node.getUserObject());
				}

				host.getEditServiceProxy().saveTopic(new_child,
						new AsyncCallback<String>() {
							@Override
							public void onSuccess(String result) {
								TreeItem sel_node = navTree.getSelectedItem();
								if (sel_node != null) {
									sel_node.addItem(createTreeItem(new_child));
								} else {
									navTree.addItem(createTreeItem(new_child));
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Сэдэв нэмэхэд алдаа гарлаа");
							}
						});
			}
			this.hide();
		}
	}

	void onPushButtonClick(ClickEvent event) {
	}
}
