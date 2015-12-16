package mn.school.enrollment.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Enrollment extends Pane implements EntryPoint {

    public void onModuleLoad() {
	// EnrollPage mainPanel = new EnrollPage();
	// mainPanel.setSize("100%", "100%");

	GWT.log("Элсэлтийн бүртгэлийн програм эхэллээ...");

	нэвтрэхХуудас();
    }

}
