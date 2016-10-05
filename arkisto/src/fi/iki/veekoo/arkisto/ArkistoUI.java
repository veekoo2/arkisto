package fi.iki.veekoo.arkisto;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class ArkistoUI extends CustomComponent implements View {

	private static final long serialVersionUID = 6361937963947145073L;

	public static final String NAME = "main";

	private TabSheet tabSheet = new TabSheet();

	private SearchTab searchTab = null;
	static final int SEARCH_TAB = 0;
	static final String SEARCH_TAB_NAME = "Search";
	static final String SEARCH_PAGE = "Find an existing file.";

	private ThumbnailsTab thumbnailsTab = null;
	static final int THUMBNAILS_TAB = 1;
	static final String THUMBNAILS_TAB_NAME = "Thumbnails";
	static final String THUMBNAILS_PAGE = "Select a file.";

	private ShowTab showTab = null;
	static final int SHOW_TAB = 2;
	static final String SHOW_TAB_NAME = "Show";
	static final String SHOW_PAGE = "Show this.";

	private SearchEngine searchEngine = null;

	private TimeContainer timeContainer = null;

	public ArkistoUI() {

	}

	@Override
	public void enter(ViewChangeEvent event) {

		System.out.println("ArkistoUI.enter");

		// Get the user name from the session
		String username = NavigatorUI.getCurrentUser();

		if (username == null) {

			getUI().getNavigator().navigateTo(SimpleLoginView.NAME);

			return;

		}

		createTabs();

	}

	public void createTabs() {

		setSizeFull();

		setCompositionRoot(new CssLayout(getC().tabSheet));

		getC().tabSheet.removeAllComponents();

		getC().searchTab = new SearchTab();

		getC().tabSheet.addTab(getC().searchTab.searchLayout, SEARCH_TAB_NAME).setDescription(SEARCH_PAGE);
		getC().tabSheet.setSelectedTab(getC().searchTab);

		getC().thumbnailsTab = new ThumbnailsTab();
		getC().tabSheet.addTab(getC().thumbnailsTab.thumbnailsLayout, THUMBNAILS_TAB_NAME)
				.setDescription(THUMBNAILS_PAGE);

		getC().showTab = new ShowTab();
		getC().tabSheet.addTab(getC().showTab.showLayout, SHOW_TAB_NAME).setDescription(SHOW_PAGE);

	}

	public static ArkistoUI getC() {

		return NavigatorUI.getCurrentArkisto();
	}

	public static TabSheet getTabSheet() {

		return NavigatorUI.getCurrentArkisto().tabSheet;
	}

	public static VerticalLayout getSearchTab() {

		return NavigatorUI.getCurrentArkisto().searchTab.searchLayout;
	}

	public static VerticalLayout getThumbnailsTab() {

		return NavigatorUI.getCurrentArkisto().thumbnailsTab.thumbnailsLayout;
	}

	public static VerticalLayout getShowTab() {

		return NavigatorUI.getCurrentArkisto().showTab.showLayout;
	}

	public static SearchEngine getSearchEngine() {

		return NavigatorUI.getCurrentArkisto().searchEngine;
	}

	public static void setSearchEngine(SearchEngine searchEngine) {

		NavigatorUI.getCurrentArkisto().searchEngine = searchEngine;
	}

	public static TimeContainer getTimeContainer() {

		return NavigatorUI.getCurrentArkisto().timeContainer;
	}

	public static void setTimeContainer(TimeContainer timeContainer) {

		NavigatorUI.getCurrentArkisto().timeContainer = timeContainer;
	}

}