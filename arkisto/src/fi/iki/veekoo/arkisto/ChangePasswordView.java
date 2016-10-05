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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ChangePasswordView extends CustomComponent implements View {

	private static final long serialVersionUID = -6439379352966996100L;

	public static final String NAME = "change";

	private final Label user;

	private final PasswordField password;

	private final PasswordField verify;

	private Button changeButton;

	private Button cancelButton;

	private Label errorLabel;

	public ChangePasswordView() {

		setSizeFull();

		// Create the user input field
		user = new Label();
		user.setCaption("User:");

		// Create password field
		password = new PasswordField("New password");
		password.setRequired(true);
		password.setColumns(30);

		// Create verify field
		verify = new PasswordField("Retype password");
		verify.setColumns(30);
		verify.setRequired(true);

		// Create register Link
		changeButton = new Button("Change password");

		// Create cancel button
		cancelButton = new Button("Cancel");

		// Create error label
		errorLabel = new Label("");

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, password, verify, changeButton, cancelButton, errorLabel);
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

		changeButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -3386552210308395395L;

			@Override
			public void buttonClick(ClickEvent event) {

				String typedPassword = password.getValue();

				String verifyPassword = verify.getValue();

				int passwordMinLength = new Integer(NavigatorUI.getConfig("passwordMinLength"));

				if (typedPassword.length() < passwordMinLength || verifyPassword.length() < passwordMinLength) {

					NavigatorUI.setError("Password must be at least " + passwordMinLength + " characters.");

					Notification.show("Password must be at least " + passwordMinLength + " characters.");

					errorLabel.setValue(NavigatorUI.getError());

					return;

				}

				if (!typedPassword.equals(verifyPassword)) {

					NavigatorUI.setError("Verify error.");

					Notification.show("Verify error.");

					errorLabel.setValue(NavigatorUI.getError());

					return;

				}

				NavigatorUI.setNewUser("");

				NavigatorUI.setError("");

				NavigatorUI.getUsers().changePassword(NavigatorUI.getCurrentUser(), typedPassword, "old");

				getUI().getNavigator().navigateTo(SimpleLoginMainView.NAME);

				return;

			}

		});

		cancelButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2184190973316191734L;

			@Override
			public void buttonClick(ClickEvent event) {

				NavigatorUI.setNewUser("");

				NavigatorUI.setCurrentUser("");

				getUI().getNavigator().navigateTo(SimpleLoginView.NAME);

			}

		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		// Get the user name from the session
		String username = NavigatorUI.getCurrentUser();
		
		if (username == null) {
			
			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);
			return;
			
		}

		user.setValue(NavigatorUI.getCurrentUser());

		errorLabel.setValue(NavigatorUI.getError());

		password.focus();

	}

}
