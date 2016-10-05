package fi.iki.veekoo.arkisto;



import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ShowTab extends ArkistoUI {

	private static final long serialVersionUID = 1290074941199733607L;

	public VerticalLayout showLayout = new VerticalLayout();
	
	TextField text = new TextField();
	
	Button buttonShow = new Button("Show");
	

	public ShowTab() {
		

		
		
		showLayout.addComponent(text);
		
		showLayout.addComponent(buttonShow);
		
		
		
		buttonShow.addClickListener(new Button.ClickListener() {



			private static final long serialVersionUID = 8332266778962460770L;

			@Override
			public void buttonClick(ClickEvent event) {
				
	

				
				getTabSheet().setSelectedTab(getThumbnailsTab());
				

				
			

			}

		});
		
		
		
	}
	


	

}
