package fi.iki.veekoo.arkisto;

import java.util.Date;
import java.util.Locale;

import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class SearchTab extends ArkistoUI {

	private static final long serialVersionUID = -2814868346635805042L;

	public VerticalLayout searchLayout = new VerticalLayout();

	public HorizontalLayout dateFields = new HorizontalLayout();

	DateField beginDateField = new DateField();
	Date beginDate = null;

	DateField endDateField = new DateField();
	Date endDate = null;

	TextArea searchCriteria = new TextArea("Limit search to");

	Button buttonSearch = new Button("Search");

	public SearchTab() {
		


		// Set the date and time to present
		beginDateField.setLocale(new Locale("fi", "FI"));
		endDateField.setLocale(new Locale("fi", "FI"));

		endDateField.setValue(new Date());

		dateFields.addComponent(beginDateField);

		dateFields.addComponent(new Label(" --- "));
		dateFields.addComponent(endDateField);
		dateFields.setCaption("Limit search between");
		searchLayout.addComponent(dateFields);

		searchCriteria.setRows(12);
		searchCriteria.setMaxLength(1000);
		searchCriteria.setDescription("Words to be used as search criteria.");
		searchCriteria.setSizeFull();

		searchLayout.addComponent(searchCriteria);

		searchLayout.addComponent(buttonSearch);

		searchLayout.setSizeFull();

		buttonSearch.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 7473582305123148220L;

			@Override
			public void buttonClick(ClickEvent event) {
				
	
				setSearchEngine(new SearchEngine(beginDateField.getValue(),  endDateField.getValue(), searchCriteria.getValue()));
				
				
				System.out.println("SearchTab:getNext: "+getSearchEngine().getNext());
				
				
				
				
				
				
				
				
				System.out.println("SearchTab: " );
				
				
				
				
				getTabSheet().setSelectedTab(getThumbnailsTab());


				
			}

		});

	}

}
