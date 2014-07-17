package com.ilona.cronDSL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class CronDSL {

	public static final String SECOND = "second";
	public static final String MINUTE = "minute";
	public static final String HOUR = "hour";
	public static final String AM = "am";
	public static final String PM = "pm";
	public static final String DAY = "day";
	public static final String WEEKDAY = "weekday";
	public static final String MONTH = "month";
	public static final String JANUARY = "January";
	public static final String FEBRUARY = "February";
	public static final String MARCH = "March";
	public static final String APRIL = "April";
	public static final String MAY = "May";
	public static final String JUNE = "June";
	public static final String JULY = "July";
	public static final String AUGUST = "August";
	public static final String SEPTEMBER = "September";
	public static final String OCTOBER = "October";
	public static final String NOVEMBER = "November";
	public static final String DECEMBER = "December";
	public static final String[] MONTHS = { JANUARY, FEBRUARY, MARCH, APRIL,
			MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER };
	public static final String SUNDAY = "Sunday";
	public static final String MONDAY = "Monday";
	public static final String TUESDAY = "Tuesday";
	public static final String WEDNESDAY = "Wednesday";
	public static final String THURSDAY = "Thursday";
	public static final String FRIDAY = "Friday";
	public static final String SATURDAY = "Saturday";
	public static final String[] DAYS_OF_WEEK = { SUNDAY, MONDAY, TUESDAY,
			WEDNESDAY, THURSDAY, FRIDAY, SATURDAY };
	public static final String YEAR = "year";

	public String convert(String dslString) {
		CronBuilder cronBuilder = new CronBuilder();

		// splits string to words and translates them to cron symbols
		translateSeconds(cronBuilder, dslString);
		translateMinutes(cronBuilder, dslString);
		translateHours(cronBuilder, dslString);
		translateDayOfMonth(cronBuilder, dslString);
		translateMonth(cronBuilder, dslString);
		translateDayOfWeek(cronBuilder, dslString);
		translateYear(cronBuilder, dslString);

		return cronBuilder.build();
	}

	private static void translateSeconds(CronBuilder cronBuilder,
			String dslString) {

		List<String> words = splitString(dslString);

		// check for "second"
		List<Integer> positions = findInArray(words, SECOND);

		if (!positions.isEmpty()) {
			// my dsl will check only first position for the "second" value
			// if there is another occurrence of second keyword I expect is not
			// important

			// check for "every"
			if ("every".equals(words.get(positions.get(0) - 1))) {
				cronBuilder.setSeconds("*");
			}
		}
	}

	private static void translateMinutes(CronBuilder cronBuilder,
			String dslString) {

		List<String> words = splitString(dslString);

		// check for "minute"
		List<Integer> positions = findInArray(words, MINUTE);

		if (!positions.isEmpty()) {
			// my dsl will check only first position for the "minute" value
			// if there is another occurrence of minute keyword I expect is not
			// important

			// check for "every"
			if ("every".equals(words.get(positions.get(0) - 1))) {
				cronBuilder.setMinutes("*");
			}
		}

	}

	private static void translateHours(CronBuilder cronBuilder, String dslString) {

		List<String> words = splitString(dslString);

		// check for "hour"
		List<Integer> positions = findInArray(words, HOUR);

		if (!positions.isEmpty()) {
			// my dsl will check only first position for the "hour" value
			// if there is another occurrence of hour keyword I expect is not
			// important

			// check for "every"
			if ("every".equals(words.get(positions.get(0) - 1))) {
				cronBuilder.setHours("*");
			}
		}
	}

	private static void translateDayOfMonth(CronBuilder cronBuilder,
			String dslString) {

		List<String> words = splitString(dslString);

		// check for "day"
		List<Integer> positions = findInArray(words, DAY);

		if (!positions.isEmpty() && !WEEKDAY.equals(words.get(positions.get(0)))) {
			// my dsl will check only first position for the "day" value
			// if there is another occurrence of day keyword I expect is not
			// important

			// check for "every"
			if ("every".equals(words.get(positions.get(0) - 1))) {
				cronBuilder.setDayOfMonth("*");
			}

			// check for last and x-to-last values
			if (words.get(positions.get(0) - 1).contains("last")) {
				String decrement = checkForNumericValue(words.get(positions
						.get(0) - 2));
				if (decrement.isEmpty()) {
					cronBuilder.setDayOfMonth("L");
				} else {
					cronBuilder.setDayOfMonth("L-" + decrement);
				}
			}

			// "every X" where x is numeric value and check for "starting from"
			// value
			if (!checkForNumericValue(words.get(positions.get(0) - 1))
					.isEmpty()) {
				if (words.get(positions.get(0) - 2).equals("every")) {

					// if month is perfixed by numeric value we are dealing with
					// increment
					String increment = checkForNumericValue(words.get(positions
							.get(0) - 1));
					// now we need to know starting point

					if (positions.size() > 1) {
						String startingPoint = checkForNumericValue(words
								.get(positions.get(1) - 1));
						cronBuilder.setDayOfMonth(startingPoint + "/"
								+ increment);
					} else {
						cronBuilder.setDayOfMonth("1/" + increment);
					}

				} else {
					if (words.get(positions.get(0) - 2).equals("and")) {
						List<String> numbers = new ArrayList<>();
						numbers = checkAndBuildNumericValues(numbers, words,
								positions.get(0));
						String days = "";
						for (int i = numbers.size(); i > 0; i--) {

							if (days.isEmpty()) {
								days = numbers.get(i - 1);
							} else {
								days = days + "," + numbers.get(i - 1);
							}
						}
						cronBuilder.setDayOfMonth(days);
					}
				}

			}

			// check for "from" and "to"
			if (positions.get(0) > 3
					&& "from".equals(words.get(positions.get(0) - 4))
					&& "to".equals(words.get(positions.get(0) - 2))) {
				cronBuilder
						.setDayOfMonth(checkForNumericValue(words.get(positions
								.get(0) - 3))
								+ "-"
								+ checkForNumericValue(words.get(positions
										.get(0) - 1)));
			}

	
		} else {
			positions = findInArray(words, WEEKDAY);
			// check for last and x-to-last values
			if (!positions.isEmpty()) {
				if (words.get(positions.get(0) - 1).contains("last")) {
					cronBuilder.setDayOfMonth("LW");
				} else {
					String closestDate = checkForNumericValue(words
							.get(positions.get(0) + 3));
					cronBuilder.setDayOfMonth(closestDate + "W");

				}
			}
		}

	}

	private static void translateMonth(CronBuilder cronBuilder, String dslString) {

		List<String> words = splitString(dslString);

		// check for "month"
		List<Integer> positions = findInArray(words, MONTH);

		if (!positions.isEmpty()) {
			// my dsl will check only first position for the "month" value
			// if there is another occurrence of month keyword I expect is not
			// important

			// check for "every"
			if ("every".equals(words.get(positions.get(0) - 1))) {
				cronBuilder.setMonth("*");
			}

			// "every X" where x is numeric value and check for "starting from"
			// value
			if (!checkForNumericValue(words.get(positions.get(0) - 1))
					.isEmpty()) {
				// if month is perfixed by numeric value we are dealing with
				// increment
				String increment = checkForNumericValue(words.get(positions
						.get(0) - 1));
				// now we need to know starting point
				positions = findInArray(words, MONTHS);
				if (!positions.isEmpty()) {
					String startingPoint = monthsToNumber(words.get(positions
							.get(0)));
					cronBuilder.setMonth(startingPoint + "/" + increment);
				} else {
					cronBuilder.setMonth("1/" + increment);
				}

			}

		} else {
			// check for months e.g. January, February etc
			// my DSL has a rule that either you put "month" keyword or use name
			// of the month like January etc.
			positions = findInArray(words, MONTHS);
			if (!positions.isEmpty()) {
				// check for "from" and "to"
				if (positions.get(0) > 0
						&& "from".equals(words.get(positions.get(0) - 1))
						&& "to".equals(words.get(positions.get(0) + 1))) {
					cronBuilder.setMonth(monthsToNumber(words.get(positions
							.get(0)))
							+ "-"
							+ monthsToNumber(words.get(positions.get(1))));
				} else {

					String months = "";
					for (Integer position : positions) {
						if (words.get(position).endsWith(",")) {

							if (months.isEmpty()) {
								months = monthsToNumber(words
										.get(position)
										.substring(
												0,
												words.get(position).length() - 1));
							} else {
								months = months
										+ ","
										+ monthsToNumber(words.get(position)
												.substring(
														0,
														words.get(position)
																.length() - 1));
							}

						} else if (!words.get(position).endsWith(",")) {
							if (months.isEmpty()) {
								months = monthsToNumber(words.get(position));
							} else {
								months = months + ","
										+ monthsToNumber(words.get(position));
							}

						}
					}
					cronBuilder.setMonth(months);
				}

			}
		}
	}

	private static void translateDayOfWeek(CronBuilder cronBuilder,
			String dslString) {

	}

	private static void translateYear(CronBuilder cronBuilder, String dslString) {

		List<String> words = splitString(dslString);

		// check for "hour"
		List<Integer> positions = findInArray(words, YEAR);

		if (!positions.isEmpty()) {
			// my dsl will check only first position for the "hour" value
			// if there is another occurrence of hour keyword I expect is not
			// important

			// check for "every"
			if ("every".equals(words.get(positions.get(0) - 1))) {
				cronBuilder.setYear("*");
			}
		}
	}

	private class CronBuilder {
		private String seconds = "";
		private String minutes = "";
		private String hours = "";
		private String dayOfMonth = "";
		private String month = "";
		private String dayOfWeek = "";
		private String year = "";

		public CronBuilder setSeconds(String seconds) {
			this.seconds = seconds;
			return this;
		}

		public CronBuilder setMinutes(String minutes) {
			this.minutes = minutes;
			return this;
		}

		public CronBuilder setHours(String hours) {
			this.hours = hours;
			return this;
		}

		public CronBuilder setDayOfMonth(String dayOfMonth) {
			this.dayOfMonth = dayOfMonth;
			return this;
		}

		public CronBuilder setMonth(String month) {
			this.month = month;
			return this;
		}

		public CronBuilder setDayOfWeek(String dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
			return this;
		}

		public CronBuilder setYear(String year) {
			this.year = year;
			return this;
		}

		private void appendSeconds(StringBuilder cron) {
			if (!seconds.isEmpty()) {
				cron.append(seconds);
			} else {
				cron.append("0");
			}
			if (seconds.equals("*")) {
				if (minutes.isEmpty()) {
					minutes = "*";
				}
				if (hours.isEmpty()) {
					hours = "*";
				}
				if (dayOfMonth.isEmpty()) {
					dayOfMonth = "*";
				}
				if (month.isEmpty()) {
					month = "*";
				}
			}
			cron.append(" ");

		}

		private void appendMinutes(StringBuilder cron) {
			if (!minutes.isEmpty()) {
				cron.append(minutes);
			} else {
				cron.append("0");
			}
			if (minutes.equals("*")) {
				if (hours.isEmpty()) {
					hours = "*";
				}
				if (dayOfMonth.isEmpty()) {
					dayOfMonth = "*";
				}
				if (month.isEmpty()) {
					month = "*";
				}
			}
			cron.append(" ");
		}

		private void appendHours(StringBuilder cron) {
			if (!hours.isEmpty()) {
				cron.append(hours);
			} else {
				cron.append("0");
			}
			if (hours.equals("*")) {
				if (dayOfMonth.isEmpty()) {
					dayOfMonth = "*";
				}
				if (month.isEmpty()) {
					month = "*";
				}
			}
			cron.append(" ");
		}

		private void appendDayOfMonth(StringBuilder cron) {
			if (dayOfMonth.isEmpty()) {
				if (dayOfWeek.isEmpty()) {
					cron.append("1");
					dayOfWeek = "?";
				} else {
					cron.append("?");
				}
			} else {
				if (dayOfWeek.isEmpty()) {
					cron.append(dayOfMonth);
					dayOfWeek = "?";
				} else {
					cron.append("?");
				}
			}
			if (dayOfMonth.equals("*")) {
				if (month.isEmpty()) {
					month = "*";
				}
			}

			cron.append(" ");
		}

		private void appendMonth(StringBuilder cron) {
			if (!month.isEmpty()) {
				cron.append(month);
			} else {
				cron.append("1");
			}
			cron.append(" ");
		}

		private void appendDayOfWeek(StringBuilder cron) {
			if (!dayOfWeek.isEmpty()) {
				cron.append(dayOfWeek);
			} else {
				throw new RuntimeException(
						"It should never happen. Day of the week should always be set.");
			}
		}

		private void appendYear(StringBuilder cron) {
			if (!year.isEmpty()) {
				cron.append(" ");
				cron.append(year);
			}

		}

		public String build() {
			StringBuilder cron = new StringBuilder();
			appendSeconds(cron);
			appendMinutes(cron);
			appendHours(cron);
			appendDayOfMonth(cron);
			appendMonth(cron);
			appendDayOfWeek(cron);
			appendYear(cron);
			return cron.toString();
		}

	}

	private static List<String> splitString(String sentence) {
		return Arrays.asList(sentence.split(" "));
	}

	private static List<Integer> findInArray(List<String> words,
			String... symbols) {
		List<Integer> positions = new ArrayList<>();
		for (int i = 0; i < words.size(); i++) {
			for (String symbol : symbols) {
				if (words.get(i).contains(symbol)) {
					positions.add(i);
				}
			}
		}
		return positions;
	}

	//recursive check for numeric values in words from start("day" in this particular example) until "on" keyword
	private static List<String> checkAndBuildNumericValues(
			List<String> numbers, List<String> words, int start) {
		if (!checkForNumericValue(words.get(start)).isEmpty()) {
			numbers.add(checkForNumericValue(words.get(start)));
		}
		if (!words.get(start - 2).equals("on")) {
			return checkAndBuildNumericValues(numbers, words, start - 1);
		} else {
			return numbers;
		}
	}

	private static String checkForNumericValue(String word) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher m = pattern.matcher(word);
		while (m.find()) {
			return m.group(0);
		}
		return "";
	}

	private static String monthsToNumber(String word) {
		switch (word) {
		case JANUARY:
			return "1";
		case FEBRUARY:
			return "2";
		case MARCH:
			return "3";
		case APRIL:
			return "4";
		case MAY:
			return "5";
		case JUNE:
			return "6";
		case JULY:
			return "7";
		case AUGUST:
			return "8";
		case SEPTEMBER:
			return "9";
		case OCTOBER:
			return "10";
		case NOVEMBER:
			return "11";
		case DECEMBER:
			return "12";

		}
		return "1";
	}

}
