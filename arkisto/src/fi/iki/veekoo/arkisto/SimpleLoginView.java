package fi.iki.veekoo.arkisto;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SimpleLoginView extends CustomComponent implements View {

	private static final long serialVersionUID = -7721453777600645515L;

	public static final String NAME = "";

	private final TextField user;

	private final PasswordField password;

	private final Button loginButton;

	private final Button changeButton;

	private final Button registerButton;

	private final Label errorLabel;

	public SimpleLoginView() {

		setSizeFull();

		// Create the user input field
		user = new TextField("User:");
		user.setColumns(30);
		user.setRequired(true);
		user.setInputPrompt("Your username");

		// Create the password input field
		password = new PasswordField("Password:");
		password.setColumns(30);
		password.setRequired(true);
		password.setValue("");
		password.setNullRepresentation("");

		// Create login button
		loginButton = new Button("Login");

		// Change password button
		changeButton = new Button("Change password");

		// Create register button
		registerButton = new Button("Register");

		// Create error label
		errorLabel = new Label("");

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, password, loginButton, changeButton, registerButton,
				errorLabel);
		fields.setCaption("Please login to access the application.");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(ValoTheme.LAYOUT_CARD);
		setCompositionRoot(viewLayout);

		loginButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -7341758081190257374L;

			@Override
			public void buttonClick(ClickEvent event) {

				String username2 = user.getValue();
				if (username2 != null) {
					username2 = username2.trim();
				} else {
					username2 = "";
				}

				getSession().setAttribute("newuser", username2);

				String password2 = password.getValue();
				if (password2 != null) {
					password2 = password2.trim();
				} else {
					password2 = "";
				}

				boolean isValid = NavigatorUI.getUsers().isUser(username2, password2);

				if (isValid) {

					// check if logged somewhere else

					if (NavigatorUI.getUsers().isLogged(username2)) {

						// if
						// (!NavigatorUI.getIP().equals(NavigatorUI.getUsers().getIP(username2)))
						// {

						// logged in from elsewhere

						errorLabel.setValue("User is already logged in at " + NavigatorUI.getUsers().getIP(username2));

						NavigatorUI.setError("User is already logged in at " + NavigatorUI.getUsers().getIP(username2));

						Notification.show("User is already logged in at " + NavigatorUI.getUsers().getIP(username2));

						password.setValue(null);

						// return;

						// }

					}

					// Store the current user in the service session

					NavigatorUI.setCurrentUser(username2);

					NavigatorUI.getUsers().logIn(username2, NavigatorUI.getIP());

					// Navigate to main view

					getUI().getNavigator().navigateTo(ArkistoUI.NAME);

					return;

				}

				// Wrong password clear the password field and refocuses it

				errorLabel.setValue("Wrong password or username.");

				NavigatorUI.setError("Wrong password or username.");

				password.setValue(null);
				password.focus();
				Notification.show("Wrong password or username.");
				return;

			}

		});

		changeButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2678159084850400374L;

			@Override
			public void buttonClick(ClickEvent event) {

				String username2 = user.getValue();
				if (username2 != null) {
					username2 = username2.trim();
				} else {
					username2 = "";
				}

				String password2 = password.getValue();
				if (password2 != null) {
					password2 = password2.trim();
				} else {
					password2 = "";
				}

				boolean isValid = NavigatorUI.getUsers().isUser(username2, password2);

				if (!isValid) {

					// Wrong password clear the password field and refocuses it

					errorLabel.setValue("Wrong password or username.");

					NavigatorUI.setError("Wrong password or username.");

					password.setValue(null);
					password.focus();
					Notification.show("Wrong password or username.");
					return;
				}

				if (NavigatorUI.getUsers().isLogged(username2)) {

					errorLabel.setValue("User is already logged in at " + NavigatorUI.getUsers().getIP(username2));

					NavigatorUI.setError("User is already logged in at " + NavigatorUI.getUsers().getIP(username2));

					Notification.show("User is already logged in at " + NavigatorUI.getUsers().getIP(username2));

				}

				NavigatorUI.setCurrentUser(username2);

				// Navigate to change password view

				NavigatorUI.setError("");

				getUI().getNavigator().navigateTo(ChangePasswordView.NAME);

				return;

			}

		});

		registerButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -7571514517999108249L;

			@Override
			public void buttonClick(ClickEvent event) {

				System.out.println("SimpleLoginView:registerButton: ");
				
				NavigatorUI.setError("");

				getUI().getNavigator().navigateTo(RegisterView.NAME);

				return;

			}

		});

	}

	@Override
	public void enter(ViewChangeEvent event) {

		errorLabel.setValue(NavigatorUI.getError());

		password.setValue(null);

		user.focus();
	}

}