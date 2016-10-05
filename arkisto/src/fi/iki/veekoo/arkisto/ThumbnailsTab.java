package fi.iki.veekoo.arkisto;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Page;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

public class ThumbnailsTab extends ArkistoUI {

	private static final long serialVersionUID = 2885157123356809145L;

	public VerticalLayout thumbnailsLayout = new VerticalLayout();

	protected GridLayout grid = null;

	private int columns = 0;

	private int rows = 0;

	private int gridWidth = 0;

	private int gridHeight = 0;

	private String[] thumbnailPaths = null;

	public ThumbnailsTab() {



		// Calculate grid size

		Page page = NavigatorUI.getCurrent().getPage();
		
		columns = page.getBrowserWindowWidth() / 128;

		gridWidth = new Integer((page.getBrowserWindowWidth() / columns));

		rows = page.getBrowserWindowHeight() / 146;

		gridHeight = new Integer(page.getBrowserWindowHeight() / rows);

		if (grid != null) {

			grid.removeAllComponents();

		}

		grid = new GridLayout(columns, rows);

		grid.setHeight(gridHeight + "px");

		grid.setWidth(gridWidth + "px");

		grid.setSizeFull();

		thumbnailPaths = new String[columns * rows];
		
		thumbnailsLayout.addComponent(grid);

		grid.addLayoutClickListener(new LayoutClickListener() {

			private static final long serialVersionUID = 4085758177831410119L;

			@Override
			public void layoutClick(LayoutClickEvent event) {

				int x = event.getClientX();

				int y = event.getClientY();

				int row = new Integer(((y - 40) / (gridHeight - 12)));

				int column = x / gridWidth;

				String selectedPath = thumbnailPaths[(columns * row) + column];

				if (selectedPath != null) {

					// setCurrentPath(selectedPath);

					// setCurrentTimeContainer(getCurrentTextFile());

				}

				// getInst().showTab.updateShowTab();

				getTabSheet().setSelectedTab(getShowTab());

			}

		});

	}

	protected void update() {

		/*
		 * 
		 * int index = 0;
		 * 
		 * loop: for (int r = 0; r < rows; r++) {
		 * 
		 * for (int c = 0; c < columns; c++) {
		 * 
		 * VerticalLayout thumbnailsView = new VerticalLayout();
		 * 
		 * thumbnailsView.setHeight(gridHeight+"px");
		 * 
		 * String pictureDate = getCurrentTextFile().getDateString();
		 * 
		 * File thumb = getThumbnailFile();
		 * 
		 * if (thumb == null || (!thumb.exists())) {
		 * 
		 * break; }
		 * 
		 * // dummy image
		 * 
		 * FileResource dummyRes = new FileResource(getInst().dummyFile);
		 * 
		 * // real image
		 * 
		 * FileResource res = new FileResource(thumb);
		 * 
		 * Image image = new Image(pictureDate, res);
		 * 
		 * image.setDescription(getCurrentTextFile().getHeader());
		 * 
		 * //image.setHeight("120px");
		 * 
		 * res.setCacheTime(0);
		 * 
		 * image.markAsDirty();
		 * 
		 * thumbnailsView.addComponent(image);
		 * 
		 * getInst().grid.addComponent(thumbnailsView);
		 * 
		 * getInst().setThumbnailPath(index, getCurrentPath());
		 * 
		 * getInst().lastThumbnailPath = getCurrentPath();
		 * 
		 * ////// //System.out.println("ThumbnailsTab.update:lastThumbnailPath
		 * ////// 2: " + getInst().lastThumbnailPath);
		 * 
		 * // get next image
		 * 
		 * String nextPath = getNextPath();
		 * 
		 * //System.out.println("ThumbnailsTab.update:getNextPath: "+nextPath);
		 * 
		 * setCurrentPath(nextPath);
		 * 
		 * if (nextPath == null) {
		 * 
		 * for (int j = index + 1; j < columns * rows; j++) {
		 * 
		 * //System.out.println("ThumbnailsTab.update:j: "+j);
		 * 
		 * getInst().setThumbnailPath(j, null);
		 * 
		 * if (j < columns) {
		 * 
		 * // make dummy
		 * 
		 * Image dummyImage = new Image(" ", dummyRes);
		 * 
		 * dummyImage.setDescription(j+"");
		 * 
		 * thumbnailsView.addComponent(dummyImage);
		 * 
		 * try { getInst().grid.addComponent(thumbnailsView); } catch (Exception
		 * e) { //System.out.println("ThumbnailsTab.update:catch:j: "
		 * +j);///////////////////////////////// //e.printStackTrace(); break
		 * loop; }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * break loop;
		 * 
		 * }
		 * 
		 * index++;
		 * 
		 * }
		 * 
		 * }
		 * 
		 * //// //System.out.println("ThumbnailsTab.update:thumbCount: " + ////
		 * getInst().thumbCount);
		 * 
		 * if (getInst().thumbCount < index) {
		 * 
		 * getInst().thumbCount = index;
		 * 
		 * }
		 * 
		 * getInst().lastThumb = getCurrentPath();
		 * 
		 * //// //System.out.println("ThumbnailsTab.update:lastThumb: " + ////
		 * getInst().lastThumb);
		 * 
		 * thumbnailsLayout.removeAllComponents();
		 * 
		 * // thumbnailsView.removeAllComponents();
		 * 
		 * //thumbnailsLayout.setStyleName(getInst().backgroundpicture);
		 * 
		 * thumbnailsLayout.addComponent(getInst().grid);
		 * 
		 * // thumbnailsLayout.addComponent(thumbnailsView);
		 * 
		 * //// //System.out.println("ThumbnailsTab.update.gridSize: " +
		 * (columns //// * rows));
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */

	}

	protected String getThumbnailPath(int position) {

		return thumbnailPaths[position];

	}

}
