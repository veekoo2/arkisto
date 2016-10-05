package fi.iki.veekoo.arkisto;

import java.util.Calendar;
import java.util.Random;

public class GeneratePassword {

	private static String[] characters = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "a", "B", "b", "C", "c",
			"D", "d", "E", "e", "H", "h", "K", "k", "M", "M", "N", "n", "P", "p", "R", "r", "S", "s", "T", "t", "U",
			"u", "X", "x", "Z", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	public static String generate() {

		int random = new Random().nextInt();

		Calendar cal = Calendar.getInstance();

		for (int i = 0; i < cal.getTimeInMillis() % 1000; i++) {
			random = new Random().nextInt();
		}

		if (random < 0) {
			random = 0 - random;
		}

		int index = random % characters.length;

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 16; i++) {
			sb.append(characters[index]);
			random = new Random().nextInt();
			if (random < 0) {
				random = 0 - random;
			}
			index = random % characters.length;
		}

		return sb.toString();

	}

}
