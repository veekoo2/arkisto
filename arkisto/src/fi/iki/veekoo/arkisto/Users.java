package fi.iki.veekoo.arkisto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class Users {

	private IndexedContainer container = new IndexedContainer();
	private File userFile = null;
	public static final String USER_PROPERTY_USER = "user";
	public static final String USER_PROPERTY_PASSWORD = "password";
	public static final String USER_PROPERTY_EMAIL = "email";
	public static final String USER_PROPERTY_STATUS = "change";
	public static final String USER_PROPERTY_LOGGED = "logged";

	private String path = null;

	public Users(String path) {

		this.path = path;

		readUserFile();

	}

	@SuppressWarnings("unchecked")
	public synchronized boolean addUser(String user, String password, String eMail) {
		
		

		readUserFile();

		
		// if "old" user
		if (isExistingUser(user)) {
			return false;
		}

		// is "new"user
		if (isNewUser(user)) {
			container.removeItem(user);
		}
		
		// add user
		Item item = container.addItem(user);
		
		item.getItemProperty(USER_PROPERTY_USER).setValue(user);

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
		}
		md.update(password.trim().getBytes());

		byte[] digest = md.digest();

		StringBuffer sb = new StringBuffer();

		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}

		item.getItemProperty(USER_PROPERTY_PASSWORD).setValue(sb.toString());

		item.getItemProperty(USER_PROPERTY_EMAIL).setValue(eMail);

		item.getItemProperty(USER_PROPERTY_STATUS).setValue("new");

		item.getItemProperty(USER_PROPERTY_LOGGED).setValue("_");

		writeUserFile();

		return true;
	}

	@SuppressWarnings("unchecked")
	public synchronized void changePassword(String user, String password, String status) {

		readUserFile();

		Item item = container.getItem(user);

		item.getItemProperty(USER_PROPERTY_STATUS).setValue(status);

		MessageDigest md = null;

		try {

			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
		}

		md.update(password.getBytes());

		byte[] digest = md.digest();

		StringBuffer sb = new StringBuffer();

		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}

		item.getItemProperty(USER_PROPERTY_PASSWORD).setValue(sb.toString());

		writeUserFile();

	}

	@SuppressWarnings("unchecked")
	public void logIn(String user, String ip) {
		
		readUserFile();

		Item item = container.getItem(user);

		item.getItemProperty(USER_PROPERTY_LOGGED).setValue(ip);

		writeUserFile();

	}

	@SuppressWarnings("unchecked")
	public void logOut(String user) {
		
		readUserFile();

		Item item = container.getItem(user);

		item.getItemProperty(USER_PROPERTY_LOGGED).setValue("_");

		writeUserFile();

	}
	
	public boolean isLogged(String user) {

		Item item = container.getItem(user);

		if (item == null) {

			return false;

		}

		if (!item.getItemProperty(USER_PROPERTY_LOGGED).getValue().equals("_")) {

			return true;

		}

		return false;

	}

	public boolean isNewUser(String user) {

		Item item = container.getItem(user);

		if (item == null) {

			return false;

		}

		if (item.getItemProperty(USER_PROPERTY_STATUS).getValue().equals("new")) {

			return true;

		}

		return false;

	}

	public boolean isExistingUser(String user) {

		Item item = container.getItem(user);

		if (item == null) {

			return false;

		}

		if (item.getItemProperty(USER_PROPERTY_STATUS).getValue().equals("old")) {

			return true;

		}

		return false;

	}

	public boolean isUser(String user, String password) {

		Item item = container.getItem(user);

		if (item == null) {

			System.out.println("Users.isUser: false: "+user);

			return false;
			
		} else {
			
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e1) {

				e1.printStackTrace();
			}
			md.update(password.getBytes());

			byte[] digest = md.digest();

			StringBuffer sb = new StringBuffer();

			for (byte b : digest) {
				sb.append(Integer.toHexString((int) (b & 0xff)));
			}

			if (sb.toString().equals(item.getItemProperty(USER_PROPERTY_PASSWORD).getValue().toString())) {

				return true;
			}

		}

		return false;

	}

	public String getEmail(String user) {

		Item item = container.getItem(user);

		if (item == null) {
			return null;
		} else {

			return item.getItemProperty(USER_PROPERTY_EMAIL).getValue().toString();

		}

	}
	
	public String getIP(String user) {

		Item item = container.getItem(user);

		if (item == null) {
			return null;
		} else {

			return item.getItemProperty(USER_PROPERTY_LOGGED).getValue().toString();

		}

	}

	@SuppressWarnings("unchecked")
	private void readUserFile() {

		container.removeAllItems();
		container.addContainerProperty(USER_PROPERTY_USER, String.class, "");
		container.addContainerProperty(USER_PROPERTY_PASSWORD, String.class, "");
		container.addContainerProperty(USER_PROPERTY_EMAIL, String.class, "");
		container.addContainerProperty(USER_PROPERTY_STATUS, String.class, "");
		container.addContainerProperty(USER_PROPERTY_LOGGED, String.class, "");

		Item item;

		userFile = new File(path);

		if (userFile.exists()) {

			// read link file

			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(userFile), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			/*
			 * // skip extra character (JAVA
			 * ERROR????????????????????????????????????????????????????????????
			 * ???????????????????????????????) try { int utf8 = in.read(); }
			 * catch (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 */

			String line = null;
			try {
				line = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// compile file
			while (line != null) {
				StringTokenizer token = new StringTokenizer(line, ",");
				String token1 = token.nextToken();

				if (token1 != null) {

					item = container.addItem(token1);

					item.getItemProperty(USER_PROPERTY_USER).setValue(token1);
					token1 = token.nextToken();

					if (token1 != null) {
						item.getItemProperty(USER_PROPERTY_PASSWORD).setValue(token1);
						token1 = token.nextToken();

						if (token1 != null) {
							item.getItemProperty(USER_PROPERTY_EMAIL).setValue(token1);
							token1 = token.nextToken();

							if (token1 != null) {
								item.getItemProperty(USER_PROPERTY_STATUS).setValue(token1);

								token1 = token.nextToken();

								if (token1 != null) {

									item.getItemProperty(USER_PROPERTY_LOGGED).setValue(token1);

								}

							}
						}
					}
				}

				try {
					line = in.readLine();
				} catch (IOException e) {

					e.printStackTrace();
				}
				if (line != null) {
					if (line.contains(",")) {
					} else {
						line = null;
					}
				}

			}
			try {
				in.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		} else {
			try {
				userFile.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	private void writeUserFile() {

		if (userFile.exists()) {

			// copy to temporary file
			File temporaryFile = new File(userFile.getAbsolutePath() + ".temp");

			BufferedWriter bufferedWriter = null;

			try {

				// Construct the BufferedWriter object
				bufferedWriter = new BufferedWriter(new FileWriter(temporaryFile));
			} catch (FileNotFoundException ex) {
				System.out.println("temporaryFile 1: " + temporaryFile.getAbsolutePath());
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println("temporaryFile 2: " + temporaryFile.getAbsolutePath());
				ex.printStackTrace();
			}

			String line = null;

			Collection<?> containerItems = container.getItemIds();

			// compile file
			for (Iterator<?> iterator = containerItems.iterator(); iterator.hasNext();) {

				String itemId = (String) iterator.next();

				Item item = (Item) container.getItem(itemId);

				line = item.getItemProperty(USER_PROPERTY_USER).getValue().toString() + ",";

				line = line + item.getItemProperty(USER_PROPERTY_PASSWORD).getValue().toString() + ",";

				line = line + item.getItemProperty(USER_PROPERTY_EMAIL).getValue() + ",";

				line = line + item.getItemProperty(USER_PROPERTY_STATUS).getValue() + ",";

				line = line + item.getItemProperty(USER_PROPERTY_LOGGED).getValue() + ",";

				// copy
				try {
					bufferedWriter.write(line);

				} catch (IOException e) {

					e.printStackTrace();
				}
				try {
					bufferedWriter.newLine();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}

			// Close the BufferedWriter
			try {

				bufferedWriter.flush();
				bufferedWriter.close();

			} catch (IOException ex) {

				ex.printStackTrace();
			}

			userFile.delete();

			// Renaming requires a File object for the target.

			temporaryFile.renameTo(userFile);

		}

	}

}
