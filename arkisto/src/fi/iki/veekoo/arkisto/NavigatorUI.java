package fi.iki.veekoo.arkisto;

import java.io.File;

import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;

public class NavigatorUI extends UI {

	private static final long serialVersionUID = -5220998662494108922L;
	
	@Theme("arkisto")
	
	@PreserveOnRefresh

	@WebServlet(value = "/*", asyncSupported = true)

	@VaadinServletConfiguration(productionMode = true, ui = NavigatorUI.class)

	public static class Servlet extends VaadinServlet {

		private static final long serialVersionUID = 1303166524045056723L;
	}

	private static Config config = null;

	private Users users = null;

	private String newUser = null;

	private String currentUser = null;

	private String error = "";
	
	private ArkistoUI arkisto = new ArkistoUI();
	

	static Navigator navigator;

	protected static final String LOGINVIEW = "";

	protected static final String CHANGEVIEW = "change";

	protected static final String REGISTERVIEW = "register";

	protected static final String NEWUSERVIEW = "newuser";

	protected static final String MAINVIEW = "main";

	protected void init(VaadinRequest request) {

		config = new Config();

		System.out.println("NavigatorUI.init");

		System.out.println("Arkisto.getDocBase: " + config.get("docBase"));

		File docBase = new File(config.get("docBase"));

		if (!docBase.exists()) {

			docBase.mkdir();

		}

		if (!docBase.exists()) {

			System.out.println("Arkisto.getDocBase: luonti epäonnistui" + config.get("docBase"));

		}
		
		// make folders if needed
		File time = new File(docBase.getAbsolutePath()+File.separator+"time");
		
		if (!time.exists()) {
			time.mkdir();
		}

		System.out.println("Arkisto.getservletBase: " + config.getServletBase());

		System.out.println("Arkisto.localHost: " + config.get("localHost"));

		System.out.println("Arkisto.users: " + config.get("docBase") + File.separator + "user.txt");

		users = new Users(docBase.getAbsolutePath() + File.separator + "user.txt");

		getPage().setTitle("Arkisto");

		// Create a navigator to control the views
		navigator = new Navigator(this, this);

		// Create and register the views
		navigator.addView(LOGINVIEW, new SimpleLoginView());
		navigator.addView(CHANGEVIEW, new ChangePasswordView());
		navigator.addView(MAINVIEW, new ArkistoUI());
		navigator.addView(REGISTERVIEW, new RegisterView());
		navigator.addView(NEWUSERVIEW, new NewUserView());

	}

	protected static String getIP() {

		Page page = NavigatorUI.getCurrent().getPage();

		WebBrowser webBrowser = page.getWebBrowser();

		String IP = null;

		if (webBrowser != null) {

			IP = webBrowser.getAddress();

			if (IP == null) {
				IP = "localhost";
			}
		}
		
		return IP;
	}

	protected static void setNewUser(String user) {

		((NavigatorUI) UI.getCurrent()).newUser = user;

	}

	protected static String getNewUser() {

		return ((NavigatorUI) UI.getCurrent()).newUser;
	}

	public static void setCurrentUser(String user) {

		((NavigatorUI) UI.getCurrent()).currentUser = user;

	}

	protected static String getCurrentUser() {

		return ((NavigatorUI) UI.getCurrent()).currentUser;
	}

	public static void setError(String errorText) {

		((NavigatorUI) UI.getCurrent()).error = errorText;

	}

	public static  String getError() {

		return ((NavigatorUI) UI.getCurrent()).error;

	}

	protected static Users getUsers() {

		return ((NavigatorUI) UI.getCurrent()).users;
	}

	protected static  String getConfig(String parameter) {

		return NavigatorUI.config.get(parameter);
	}
	
	protected static  ArkistoUI getCurrentArkisto() {

		return ((NavigatorUI) UI.getCurrent()).arkisto;
	}

}
