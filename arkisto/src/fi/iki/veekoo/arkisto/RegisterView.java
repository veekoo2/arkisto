package fi.iki.veekoo.arkisto;

import javax.mail.MessagingException;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import com.vaadin.ui.themes.ValoTheme;

public class RegisterView extends CustomComponent implements View {

	private static final long serialVersionUID = -1543966391051482958L;

	public static final String NAME = "register";

	private TextField user;

	private TextField eMail;

	private Button registerButton;

	private Button cancelButton;

	private Label errorLabel;

	public RegisterView() {

		setSizeFull();

		// Create the user input field
		user = new TextField();
		user.setCaption("User:");
		user.setInputPrompt("Your username");

		// Create email field
		eMail = new TextField("E-mail");
		eMail.setColumns(30);
		eMail.setRequired(true);

		// Create register Link
		registerButton = new Button("Continue registering");

		// Create cancel button
		cancelButton = new Button("Cancel");

		// Create error label
		errorLabel = new Label("");

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, eMail, registerButton, cancelButton, errorLabel);
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

			private static final long serialVersionUID = -3878729581394085797L;

			@Override
			public void buttonClick(ClickEvent event) {

				// System.out.println("RegisterView:registerButton");

				String username2 = user.getValue();
				if (username2 != null) {
					username2 = username2.trim();
				} else {
					username2 = "";
				}

				String validatedUserName = username2.replace(" ", "");

				validatedUserName = validatedUserName.replace(",", "");

				validatedUserName = validatedUserName.replace("\\", "");

				validatedUserName = validatedUserName.replace("\"", "");

				validatedUserName = validatedUserName.toLowerCase();

				int userNameMinLength = new Integer(NavigatorUI.getConfig("userNameMinLength"));

				if (validatedUserName.length() < userNameMinLength) {

					NavigatorUI.setError(
							"Username must be at least " + userNameMinLength + " characters (letters and numbers).");

					Notification.show(
							"Username must be at least " + userNameMinLength + " characters (letters and numbers).");

					getUI().getNavigator().navigateTo(SimpleLoginView.NAME);

					return;

				}

				if (NavigatorUI.getUsers().isExistingUser(validatedUserName)) {

					NavigatorUI.setError("User exists already; choose another one.");

					Notification.show("User exists already; choose another one.");

					getUI().getNavigator().navigateTo(SimpleLoginView.NAME);

					return;

				}

				NavigatorUI.setNewUser(validatedUserName);

				// validate e-mail

				String email = eMail.getValue().toString().trim();

				if ((email.length() >= 6) && (email.contains("@"))) {

					// e-mail OK

					String temporaryPassword = GeneratePassword.generate();

					// send e-mail

					SendMail sendMail = new SendMail(NavigatorUI.getConfig("mailFrom"),
							NavigatorUI.getConfig("mailFrom"),

							"<" + email + ">", NavigatorUI.getConfig("subject"),
							NavigatorUI.getConfig("message") + validatedUserName + ", " + temporaryPassword + "\n\n"

									+ email + "\n\n",
							null, null);

					// System.out.println("NewAccountWindow.SendMail 2");
					try {
						sendMail.send();
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// user record

					if (NavigatorUI.getUsers().isNewUser(validatedUserName)) {

						// change password in existing new record

						NavigatorUI.getUsers().changePassword(validatedUserName, temporaryPassword, "new");

					} else {

						// add initial user record

						NavigatorUI.getUsers().addUser(validatedUserName, temporaryPassword, email);

					}

					getUI().getNavigator().navigateTo(NewUserView.NAME);

					return;

				}

				errorLabel.setValue("Wrong e-mail.");

				Notification.show("Wrong e-mail.");

				return;

			}

		});

		cancelButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -358602619614684065L;

			@Override
			public void buttonClick(ClickEvent event) {

				NavigatorUI.setNewUser("");

				getUI().getNavigator().navigateTo(SimpleLoginView.NAME);
			}

		});

	}

	public void enter(ViewChangeEvent event) {
		
		System.out.println("RegisterView:enter");


		//user.setValue(NavigatorUI.getNewUser());

		errorLabel.setValue(NavigatorUI.getError());

		// System.out.println("RegisterView:NavigatorUI.newUser: " +
		// NavigatorUI.getNewUser());

	}
}
