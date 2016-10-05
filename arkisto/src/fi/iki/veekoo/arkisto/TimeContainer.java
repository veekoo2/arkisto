package fi.iki.veekoo.arkisto;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;

public class TimeContainer extends ArkistoUI {

	private static final long serialVersionUID = -6130638403353158275L;

	Date beginDate = null;

	Date endDate = null;

	Date currentDate = null;

	String currentYear = null;

	String currentMonth = null;

	String currentDay = null;

	String currentTime = null;

	int[] years = new int[0];

	int yearIndex = -1;

	int[] months = new int[0];

	int monthIndex = -1;

	int[] days = new int[0];

	int dayIndex = -1;

	String[] times = new String[0];

	int timeIndex = -1;

	String timeBase = NavigatorUI.getConfig("docBase") + File.separator + "time";
	
	

	public TimeContainer(Date beginDate, Date endDate) {

		this.beginDate = beginDate;

		this.endDate = endDate;

		if (beginDate == null) {

			beginDate = newDate(1, 1, 1);
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		StringBuilder dateMonthYear = new StringBuilder(df.format(beginDate));
		currentYear = dateMonthYear.toString();

		//// System.out.println("TimeContainer:currentYear1: " + currentYear +
		//// ", " + yearIndex);

		df = new SimpleDateFormat("MM");
		dateMonthYear = new StringBuilder(df.format(beginDate));
		currentMonth = dateMonthYear.toString();

		df = new SimpleDateFormat("dd");
		dateMonthYear = new StringBuilder(df.format(beginDate));
		currentDay = dateMonthYear.toString();

		//currentDate = newDate(currentYear, currentMonth, currentDay);

		//// System.out.println("TimeContainer:nextYear:4: ");

		//// System.out.println("TimeContainer:nextYear: yearIndex == -1");

		readYears();

		yearIndex = -1;

		//// System.out.println("TimeContainer:yearIndex:0:" + yearIndex);

		//// System.out.println("TimeContainer:currentDate1: " + currentDate);

		//// System.out.println("TimeContainer:currentYear2: " + currentYear +
		//// ", " + yearIndex);
		//// System.out.println("TimeContainer:currentMonth: " + currentMonth +
		//// ", " + monthIndex);
		//// System.out.println("TimeContainer:currentDay: " + currentDay + ", "
		//// + dayIndex);
		// System.out.println("TimeContainer:currentTime: " + currentTime + ", "
		//// + timeIndex);

		// System.out.println("TimeContainer:currentDate3: " + currentDate);

		// System.out.println("");

	}

	public String getNext() {
		
		String nextTime = nextTime() ;
		
		while ((nextTime != null) && (currentDate.before(beginDate))) {
			
			
			nextTime = nextTime() ;
			
		}
		
		if (nextTime == null) {
			return null;
		}
		return nextTime;
		
	}
	

	public String nextTime() {

		//System.out.println("TimeContainer:nextTime:timeIndex: " + timeIndex);
		


		timeIndex++;

		if (timeIndex >= times.length) {

			// read next day

			if (nextDay() == null) {

				// System.out.println("TimeContainer:nextTime:nextDay() ==
				// null");

				return null;

			}

		}

		// System.out.println("TimeContainer:nextTime:timeIndex: " +
		// timeIndex+": "+times[timeIndex]);
		
		String path = timeBase + File.separator + currentYear + File.separator + currentMonth + File.separator + currentDay
				+ File.separator + times[timeIndex];
		
		path = path.replace("/", File.separator);
		
		path = path.replace("\\", File.separator);		
		
		return path;

	}

	private String nextDay() {

		/*
		 * System.out.println("TimeContainer:nextDay:dayIndex:2: " + dayIndex+
		 * " < "+days.length);
		 */

		// get next day

		dayIndex++;

		day: while (dayIndex < days.length) {

			currentDay = days[dayIndex] + "";

			// System.out.println( "TimeContainer:nextDay:currentYear: " +
			// currentYear + ", " + yearIndex + ": " + years[yearIndex]);
			// System.out.println("TimeContainer:nextDay:currentMonth: " +
			// currentMonth + ", " + monthIndex + ": " + months[monthIndex]);
			// System.out.println( "TimeContainer:nextDay:currentDay: " +
			// currentDay + ", " + dayIndex + ": " + days[dayIndex]);
			// //System.out.println("TimeContainer:nextDay:currentTime: " +
			// currentTime + ", " + timeIndex + ": " + times[timeIndex]);

	

			if (!readTimes()) {

				dayIndex++;

				break day;
			}

			// System.out.println("TimeContainer:nextDay:dayIndex:2a: " +
			// dayIndex);

			currentDay = days[dayIndex] + "";
			
			currentDate = newDate(years[yearIndex], months[monthIndex], days[dayIndex]);

			// System.out.println("TimeContainer:currentDate: " + currentDate);

			if (currentDate.after(endDate)) {

				return null;

			}

			return currentDay;

		}

		// month ended; read next month

		if (nextMonth() == null) {

			// System.out.println("TimeContainer:nextDay:nextMonth() == null");

			return null;

		}

		// System.out.println("TimeContainer:nextDay:currentMonth: " +
		// currentMonth);

		currentDay = days[dayIndex] + "";
		
		currentDate = newDate(years[yearIndex], months[monthIndex], days[dayIndex]);

		// System.out.println("TimeContainer:currentDate: " + currentDate);

		if (currentDate.after(endDate)) {

			return null;

		}

		return currentDay;

	}

	private boolean readTimes() {

		// System.out.println("TimeContainer:readTimes: " + File.separator +
		// currentYear + File.separator + currentMonth + File.separator +
		// currentDay);

		times = new File(
				timeBase + File.separator + currentYear + File.separator + currentMonth + File.separator + currentDay)
						.list();

		// System.out.println("TimeContainer:readTimes:times: " + times);

		if (times == null) {

			return false;

		}

		Arrays.sort(times);

		timeIndex = 0;

		// System.out.println("TimeContainer:readTimes:times[0]: " + times[0]);

		return true;

	}

	private String nextMonth() {

		// System.out.println("TimeContainer:nextMonth:monthIndex3a: " +
		// monthIndex + " < " + months.length);

		monthIndex++;

		// System.out.println("TimeContainer:nextMonth:monthIndex3b: " +
		// monthIndex + " < " + months.length);

		month: while (monthIndex < months.length) {

			// System.out.println("TimeContainer:nextMonth:monthIndex:3c: " +
			// monthIndex); // 2

			currentMonth = months[monthIndex] + "";

			currentDate = newDate(years[yearIndex], months[monthIndex], 1);

			// System.out.println("TimeContainer:nextMonth:currentDate: " +
			// currentDate);

			if ((currentDate.after(endDate))) {

				return null;

			}

			if (!readDays()) {

				monthIndex++;

				break month;
			}

			if (nextDay() == null) {

				return null;
			}

			// System.out.println("TimeContainer:nextMonth:monthIndex:3d: " +
			// monthIndex); // 2

			currentMonth = months[monthIndex] + "";

			return currentMonth;

		}

		// get next year

		if (nextYear() == null) {

			// System.out.println("TimeContainer:nextMonth:nextYear() == null");

			return null;

		}

		// currentDate = newDate(years[yearIndex], months[monthIndex], 1);

		if (currentDate.after(endDate)) {

			return null;

		}

		// //System.out.println("TimeContainer:nextMonth:currentMonth:3c: " +
		// currentMonth);

		currentMonth = months[monthIndex] + "";

		return currentMonth;

	}

	private String nextYear() {

		// System.out.println("TimeContainer:nextYear:yearIndex:8a:" +
		// yearIndex);

		yearIndex++;

		// //System.out.println("TimeContainer:nextYear:8b:" + yearIndex);

		year: while (yearIndex < years.length) {

			// System.out.println("TimeContainer:nextYear:yearIndex:9: " +
			// yearIndex);

			currentYear = years[yearIndex] + "";

			currentDate = newDate(years[yearIndex], 1, 1);

			if (currentDate.after(endDate)) {

				return null;

			}

			if (!readMonths()) {

				yearIndex++;

				// System.out.println("TimeContainer:nextYear:yearIndex:9bb: " +
				// yearIndex);

				break year;

			}

			if (nextMonth() == null) {

				return null;
			}

			// //System.out.println("TimeContainer:nextYear:yearIndex:9b: " +
			// yearIndex);

			// //System.out.println("TimeContainer:nextYear:monthIndex:9b: " +
			// monthIndex);

			// currentDate = newDate(years[yearIndex], months[monthIndex], 1);

			break year;

		}

		// System.out.println("TimeContainer:nextYear:currentDate:9c: " +
		// currentDate);

		if ((yearIndex >= years.length) || (currentDate.after(endDate))) {

			// System.out.println("TimeContainer:nextYear: yearIndex >=
			// years.length");

			return null;

		}

		// //System.out.println("TimeContainer:nextYear:currentYear:9d: " +
		// currentYear);

		return currentYear;

	}

	private boolean readYears() {
		
		System.out.println("TimeContainer:readYears.timebase: " + timeBase );

		String[] years1 = new File(timeBase).list();

		System.out.println("TimeContainer:readYears():years1.length:5: " + years1.length);

		if (years1.length == 0) {

			currentYear = null;

			return false;

		}

		years = new int[years1.length];
		for (int i = 0; i < years1.length; i++) {

			years[i] = new Integer(years1[i]);

			// //System.out.println("TimeContainer:readYears():years[i]: " +
			// years[i] + ", " + i);

		}

		Arrays.sort(years);

		yearIndex = 0;

		// //System.out.println("TimeContainer:yearIndex:6: " + yearIndex);

		while (years[yearIndex] < new Integer(currentYear)) {

			yearIndex++;

			if (yearIndex >= years.length) {

				return false;

			}

		}

		currentYear = years[yearIndex] + "";

		// //System.out.println("TimeContainer:readYears()yearIndex:7: " +
		// yearIndex);

		return true;

	}

	private boolean readMonths() {

		// System.out.println("TimeContainer:readMonths():10: " + File.separator
		// + currentYear);

		String[] months1 = new File(timeBase + File.separator + currentYear).list();

		if (months1 == null) {

			return false;

		}

		months = new int[months1.length];
		for (int i = 0; i < months1.length; i++) {
			months[i] = new Integer(months1[i]);

			// System.out.println("TimeContainer:readMonths():months: " +
			// months[i]);
		}
		Arrays.sort(months);

		monthIndex = -1;

		// //System.out.println("TimeContainer:readMonths():monthIndex: " +
		// monthIndex);

		return true;

	}

	private boolean readDays() {

		// System.out.println( "TimeContainer:readDays():10: " + File.separator
		// + currentYear + File.separator + currentMonth); // \2016\1

		String[] days1 = new File(timeBase + File.separator + currentYear + File.separator + currentMonth).list();

		if (days1 == null) {

			return false;

		}

		days = new int[days1.length];
		for (int i = 0; i < days1.length; i++) {
			days[i] = new Integer(days1[i]);

			// System.out.println("TimeContainer:readMonths():days: " +
			// days[i]);
		}
		Arrays.sort(days);

		dayIndex = -1;

		return true;

	}

	private Date newDate(String year, String month, String day) {

		return newDate(new Integer(year), new Integer(month), new Integer(day));

	}

	private Date newDate(int year, int month, int day) {

		// //System.out.println("TimeContainer:newDate: " + year +", " + month
		// +",
		// " + day);

		SimpleDateFormat formatter = new SimpleDateFormat("d.M.y");

		String dateInString = day + "." + month + "." + (year);

		Date date = null;
		try {

			date = formatter.parse(dateInString);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// //System.out.println("TimeContainer:newDate: " + date);

		return date;

	}

}
