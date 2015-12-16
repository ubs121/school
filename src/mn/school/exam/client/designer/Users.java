package mn.goody.exam.client.designer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Image;

public class Users extends Composite {

	public Users() {
		
		SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel();
		initWidget(splitLayoutPanel);
		splitLayoutPanel.setSize("90em", "50em");
		
		Tree tree = new Tree();
		splitLayoutPanel.addWest(tree, 292.0);
		
		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
		splitLayoutPanel.add(dockLayoutPanel);
		
		AbsolutePanel toolsPanel = new AbsolutePanel();
		toolsPanel.setStyleName("toolbar");
		dockLayoutPanel.addNorth(toolsPanel, 2.3);
		toolsPanel.setWidth("100%");
		
		TextBox searchBox = new TextBox();
		searchBox.setVisibleLength(200);
		toolsPanel.add(searchBox);
		searchBox.setWidth("733px");
		
		PushButton pushButton = new PushButton("Find");
		pushButton.getUpFace().setImage(new Image("images/search.png"));
		toolsPanel.add(pushButton, 747, 0);
		pushButton.setSize("20px", "20px");
		
		PushButton pushButton_1 = new PushButton("Find");
		pushButton_1.getUpFace().setImage(new Image("images/new.png"));
		toolsPanel.add(pushButton_1, 828, 0);
		pushButton_1.setSize("20px", "20px");
		
		CellTable userTable = new CellTable();
		userTable.setPageSize(25);
		dockLayoutPanel.add(userTable);
	}

}
