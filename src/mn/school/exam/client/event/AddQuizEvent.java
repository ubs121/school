package mn.goody.exam.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AddQuizEvent extends GwtEvent<AddQuizEventHandler> {
	public static Type<AddQuizEventHandler> TYPE = new Type<AddQuizEventHandler>();

	@Override
	public Type<AddQuizEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddQuizEventHandler handler) {
		handler.onAddQuiz(this);
	}
}
