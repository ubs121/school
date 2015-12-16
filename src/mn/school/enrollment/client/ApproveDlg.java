package mn.school.enrollment.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

public class ApproveDlg extends DialogBox {

    Button closeButton;
    Grid grid;
    EnrollRequestClient enroll;
    ApproveDlg self;

    public ApproveDlg(EnrollRequestClient en) {
	self = this;
	enroll = en;
	setText("Элсэх хүсэлт");

	if (en == null) {
	    this.hide();
	    return;
	}

	grid = new Grid(15, 4);
	// 1-р багана
	grid.setWidget(0, 0, new Label("Нэр:"));
	grid.setWidget(0, 1, new Label(en.getНэр()));
	grid.setWidget(1, 0, new Label("Эцгийн нэр:"));
	grid.setWidget(1, 1, new Label(en.getЭцгийнНэр()));
	grid.setWidget(2, 0, new Label("И-мэйл"));
	grid.setWidget(2, 1, new Label(en.getИмэйл()));
	grid.setWidget(3, 0, new Label("Регистр:"));
	grid.setWidget(3, 1, new Label(en.getРегистр()));
	grid.setWidget(4, 0, new Label("Пасспорт:"));
	grid.setWidget(4, 1, new Label(en.getПасспорт()));
	grid.setWidget(5, 0, new Label("Огноо:"));
	grid.setWidget(5, 1, new Label(en.getОгноо().toString()));
	grid.setWidget(6, 0, new Label("Төлөв:"));
	grid.setWidget(6, 1, new Label(en.getТөлөв()));
	// 2-р багана
	grid.setWidget(0, 2, new Label("Элсэх мэргэжил:"));
	grid.setWidget(0, 3, new Label(en.getЭлсэхМэргэжил()));
	grid.setWidget(1, 2, new Label("Төгссөн сургууль:"));
	grid.setWidget(1, 3, new Label(en.getСургууль()));
	grid.setWidget(2, 2, new Label("Гэрчилгээ:"));
	grid.setWidget(2, 3, new Label(en.getГэрчилгээ()));
	grid.setWidget(3, 2, new Label("Төгссөн үнэлгээ:"));
	grid.setWidget(3, 3, new Label(String.valueOf(en.getҮнэлгээ())));
	grid.setWidget(4, 2, new Label("ЕШ оноо:"));
	grid.setWidget(4, 3, new Label(String.valueOf(en.getЕшОноо())));
	grid.setWidget(5, 2, new Label("Иргэний харъяалал:"));
	grid.setWidget(5, 3, new Label(en.getХаръяалал()));
	grid.setWidget(5, 2, new Label("Аймаг/муж:"));
	grid.setWidget(5, 3, new Label(en.getАймаг()));
	// хаах товч
	closeButton = new Button("Хаах");
	closeButton.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		self.hide();
	    }
	});
	grid.setWidget(14, 3, closeButton);
	setWidget(grid);

    }

    public void setAcceptButton(Button b) {
	grid.setWidget(14, 0, b);
	b.setVisible(enroll.getТөлөв().equals(
		EnrollRequestClient.DRAFT));
    }

    public void setRejectButton(Button b) {
	grid.setWidget(14, 1, b);
	b.setVisible(enroll.getТөлөв().equals(
		EnrollRequestClient.DRAFT));
    }

}
