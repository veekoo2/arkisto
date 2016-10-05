package fi.iki.veekoo.arkisto;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class NewUserView extends CustomComponent implements View {

	private static final long serialVersionUID = -1543966391051482958L;

	public static final String NAME = "newuser";

	private final TextField user;

	private final PasswordField emailPassword;

	private final PasswordField password;

	private final PasswordField verify;

	private Button registerButton;

	private Button cancelButton;

	private Label errorLabel;

	public NewUserView() {

		setSizeFull();

		// Create the user input field
		user = new TextField("User:");
		user.setColumns(30);
		user.setRequired(true);
		user.setInputPrompt("Your username");

		// Create email password field
		emailPassword = new PasswordField("Password from E-mail");
		emailPassword.setColumns(30);
		emailPassword.setRequired(true);

		// Create password field
		password = new PasswordField("New password");
		password.setColumns(30);
		password.setRequired(true);

		// Create verify field
		verify = new PasswordField("Retype password");
		verify.setColumns(30);
		verify.setRequired(true);

		// Create register Link
		registerButton = new Button("Login");

		// Create cancel button
		cancelButton = new Button("Cancel");

		// Create error label
		errorLabel = new Label("");

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, emailPassword, password, verify, registerButton, cancelButton,
				errorLabel);
		fields.setCaption("Please register to access the application.");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(ValoTheme.LAYOUT_WELL);
		setCompositionRoot(viewLayout);

		registerButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 4500257429149594575L;

			@Override
			public void buttonClick(ClickEvent event) {

				System.out.println("RegisterView:registerButton");

				// check existing user

				String userName = NavigatorUI.getNewUser();

				String temporaryPassword = emailPassword.getValue();
				if (temporaryPassword != null) {
					temporaryPassword = temporaryPassword.trim();
				} else {
					
					NavigatorUI.setError("Type e-mail password.");

					Notification.show("Type e-mail password.");

					return;

				}

				String newPassword = password.getValue();
				if (newPassword != null) {
					newPassword = newPassword.trim();
				} else {
					newPassword = "";
				}

				String verify2 = verify.getValue();
				if (verify2 != null) {
					verify2 = newPassword.trim();
				} else {
					verify2 = "";
				}
				
				int passwordMinLength = new Integer(NavigatorUI.getConfig("passwordMinLength"));
				
				if (newPassword.length() < passwordMinLength || verify2.length() < passwordMinLength) {

					NavigatorUI.setError("Password must be at least 8 characters.");

					Notification.show("Password must be at least 8 characters.");
					
					NavigatorUI.setError(NavigatorUI.getError());

					return;
				}

				if (!newPassword.equals(verify2)) {

					NavigatorUI.setError("Retype new password.");

					Notification.show("Retype new password.");

					return;

				}



				if (!NavigatorUI.getUsers().isUser(userName, temporaryPassword)) {

					NavigatorUI.setError("Retype e-mail password.");

					Notification.show("Retype e-mail password.");

					return;

				}
				
				if (!NavigatorUI.getUsers().isNewUser(userName)) {

					NavigatorUI.setError("Something went wrong.");

					Notification.show("Something went wrong.");

					return;

				}

				// fix user record

				NavigatorUI.getUsers().changePassword(userName, newPassword, "old");

				NavigatorUI.setNewUser("");
				
				NavigatorUI.getUsers().logIn(userName, NavigatorUI.getIP());

				NavigatorUI.setCurrentUser(userName);

				getUI().getNavigator().navigateTo(SimpleLoginMainView.NAME);
				
				return;
			}

		});

		cancelButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 5287200434562187427L;

			@Override
			public void buttonClick(ClickEvent event) {

				NavigatorUI.setNewUser("");

				getUI().getNavigator().navigateTo(SimpleLoginView.NAME);

			}

		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		// Get the user name from the session
		String username = NavigatorUI.getNewUser();
		
		if (username == null) {
			
			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);
			return;
			
		}

		errorLabel.setValue(NavigatorUI.getError());

		user.setValue(NavigatorUI.getNewUser());
	}

}
