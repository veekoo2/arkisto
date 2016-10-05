package fi.iki.veekoo.arkisto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.StringTokenizer;

public class SearchEngine extends ArkistoUI {

	private static final long serialVersionUID = 7130964925280131297L;

	TimeContainer timeContainer = null;

	Date beginDate = null;

	Date endDate = null;

	String searchCriteria = null;

	public SearchEngine(Date beginDate, Date endDate, String searchCriteria) {

		this.beginDate = beginDate;

		this.endDate = endDate;

		this.searchCriteria = searchCriteria;

		setTimeContainer(new TimeContainer(beginDate, endDate));

		timeContainer = getTimeContainer();

		System.out.println("SearchEngine:timeContainer: " + timeContainer);

		// for testing

		/*
		 * while (true) {
		 * 
		 * String time = timeContainer.getNext();
		 * 
		 * if (time == null) { break; } else { System.out.println(
		 * "SearchEngine:timeContainer:nextTime(): " + time);
		 * 
		 * }
		 * 
		 * }
		 */

	}

	public String getNext() {

		// find first
		String path = timeContainer.nextTime();

		System.out.println("SearchEngine:getNext: " + path);

		if (searchCriteria.length() < 4) {

			return path;
		}

		loop_time_folder: while (path != null) {

			boolean found = false;

			loop_next_time: while (!found) {

				File linkFile = new File(path + File.separator + "index.txt");

				if (!linkFile.exists()) {

					System.out.println("SearchEngine:getNext: !linkFile.exists()" + path);
					

					return path;

				} else {

					System.out.println("SearchEngine:getNext: linkFile.exists()" + path);

					// read index.txt
					BufferedReader in = null;
					try {
						in = new BufferedReader(new InputStreamReader(new FileInputStream(linkFile), "UTF-8"));
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}

					String line = null;
					try {
						line = in.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}

					// loop index.txt
					loop_index_txt: while (line != null) {

						line = line.trim();

						System.out.println("SearchEngine:getNext:line " + line);

						StringTokenizer st = new StringTokenizer(searchCriteria);

						loop_search_criteria: while (st.hasMoreElements()) {

							// System.out.println(st.nextElement());

							String keyword = (String) st.nextElement();

							System.out.println("SearchEngine:getNext:keyword " + keyword);

							int result = keyword.compareTo(line);

							if (result < 0) {

								// this keyword not in index

								break loop_index_txt;

							} else if (result == 0) {

								// found

								found = true;

								break loop_time_folder;

							}

							// continue search in index file

						}

						try {
							line = in.readLine();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				System.out.println("SearchEngine:getNext: found " + found);
				
				if (found) {

					return path;
					
				} 

				// find next
				path = timeContainer.nextTime();

			}

			if (found) {

				return path;
				
			} 

		}
		
		return null;

	}

}
