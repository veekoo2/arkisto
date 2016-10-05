package fi.iki.veekoo.arkisto;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class SimpleLoginMainView extends CustomComponent implements View {

	private static final long serialVersionUID = -5660710901270658133L;

	public static final String NAME = "main";

	Label text = new Label();

	Button logout = new Button("Logout", new Button.ClickListener() {

		private static final long serialVersionUID = 6330380219334280019L;

		@Override
		public void buttonClick(ClickEvent event) {

			// "Logout" the user
			if (!NavigatorUI.getCurrentUser().equals("")) {

				NavigatorUI.getUsers().logOut(NavigatorUI.getCurrentUser());

				NavigatorUI.setCurrentUser("");

			}

			// Refresh this view, should redirect to login view
			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);
		}
	});

	public SimpleLoginMainView() {
		setCompositionRoot(new CssLayout(text, logout));

	}

	@Override
	public void enter(ViewChangeEvent event) {

		// Get the user name from the session
		String username = NavigatorUI.getCurrentUser();

		if (username == null) {

			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);
			return;

		}

		// And show the username
		text.setValue("Hello " + username);
	}
}
