package webdriver;

import java.text.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 * Original Common Functions
 *
 */
public final class CommonFunctionsOrigin {

    /**
     * You can not create an instance of the class, all methods are static
     */
    private CommonFunctionsOrigin() {
    }

    //==============================================================================================
    // Методы для работы с регулярными выражениями Methods to work with regular expressions

    /**
     * Creating a template object
     * @param regex pattern of expression
     * @param matchCase Consider whether the case when comparing characters
     * @return Pattern
     */
    private static Pattern regexGetPattern(final String regex, final boolean matchCase) {
        int flags;
        if (matchCase) {
            flags = 0;
        } else {
            flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
        }
        return Pattern.compile(regex, flags);
    }

    /**
     * Getting a first match in a string
     * @param text text
     * @param regex pattern of expression
     * @return first match String
     */
    public static String regexGetMatch(final String text, final String regex) {
        return regexGetMatch(text, regex, false);
    }

    /**
     *  Getting a first match in a string
     */
    public static String regexGetMatch(String text, String regex, boolean matchCase) {
        return regexGetMatchGroup(text, regex, 0, matchCase);
    }

    /**
     * Checking a string for a match pattern
     */
    public static boolean regexIsMatch(String text, String regex) {
        return regexIsMatch(text, regex, false);
    }

    /**
     * Checking a string for a match pattern
     */
    public static boolean regexIsMatch(String text, String regex, boolean matchCase) {
        Pattern p = regexGetPattern(regex, matchCase);
        Matcher m = p.matcher(text);
        return m.find();
    }

    /**
     * Getting the first group of matches
     */
    public static String regexGetMatchGroup(String text, String regex) {
        return regexGetMatchGroup(text, regex, 1, false);
    }

    /**
     * Getting this group of matches
     * @param text test
     * @param regex pattern
     * @param groupIndex The group number (from 1)
     */
    public static String regexGetMatchGroup(String text, String regex, int groupIndex) {
        return regexGetMatchGroup(text, regex, groupIndex, false);
    }

    /**
     * Getting this group of matches
     * @param text
     * @param regex
     * @param groupIndex (from 1)
     * @param matchCase sensitive
     */
    public static String regexGetMatchGroup(String text, String regex, int groupIndex, boolean matchCase) {
        Pattern p = regexGetPattern(regex, matchCase);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(groupIndex);
        } else {
            return null;
        }
    }

    /**
     * Getting the count of groups of matches
     * @param text text
     * @param regex pattern
     */
    public static int regexGetNumberMatchGroup(String text, String regex) {
        return regexGetNumberMatchGroup(text, regex, false);
    }

    /**
     * Getting the count of groups of matches
     * @param text text
     * @param regex pattern
     * @param matchCase sensitive
     */
    public static int regexGetNumberMatchGroup(String text, String regex, boolean matchCase) {
        int number = 0;
        Pattern p = regexGetPattern(regex, matchCase);
        Matcher m = p.matcher(text);
        while (m.find()) {
            m.group();
            number++;
        }
        return number;
    }

    /**
     * Getting the line with the current date on the pattern "dd.MM.yyyy"
     * @return String "dd.MM.yyyy"
     */
    public static String getCurrentDate() {
        return getCurrentDate("dd.MM.yyyy");
    }

    /**
     * Getting the line with the current date to a specified pattern
     */
    public static String getCurrentDate(String pattern) {
        return formatDate(new Date(), pattern);
    }

    /**
     * Getting a pseudo unique string that contains the timestamp
     * @return String timestamp
     */
    public static String getTimestamp() {
        return getCurrentDate("yyyyMMddHHmmss");
    }

    /**
     * Transfer line with the date in the Calendar object
     * @param s date in string
     */
    public static Calendar dateString2Calendar(String s) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Date d1 = new SimpleDateFormat("dd.mm.yyyy").parse(s);
        cal.setTime(d1);
        return cal;
    }

    /**
     * Translate this date to a string pattern matching
     * @param date date
     * @param pattern pattern
     */
    public static String formatDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Translate this date to a string for a pattern "dd.mm.yyyy"
     * @param date date
     * @return parsed Date
     */
    public static Date parseDate(final String date) {
        return parseDate(date, "dd.mm.yyyy");
    }

    /**
     * Get a date from a string pattern matching
     * @param date date
     * @param pattern pattern date
     */
    public static Date parseDate(String date, String pattern) {
        Date result = null;
        try {
            result = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * increase date by one day
     * @param date Date
     * @return new Date
     */
    public static Date increaseDateByOneDay(final Date date) {
        return increaseDateByXDays(date, 1);
    }

    /**
     * Zoom on the date N days
     * @param date date
     * @param days days number
     */
    public static Date increaseDateByXDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    //==============================================================================================
    // Методы для экранирования спецсимволов

    /**
     * Escaping a backslash and brackets
     * @param text text
     */
    public static String escapeMetaCharacters(String text) {
        text = text.replaceAll("\\\\", "\\\\\\\\"); // Экранировка \
        text = text.replaceAll("\\(", "\\\\("); // Экранировка (
        text = text.replaceAll("\\)", "\\\\)"); // Экранировка )
        return text;
    }

    /**
     * Escaping the backslash and double quotes
     * @param text text
     */
    public static String escapeSeleniumCharacters(String text) {
        text = text.replaceAll("\\\\", "\\\\\\\\"); // Экранировка \
        text = text.replaceAll("\"", "\\\\\\\""); // Экранировка "
        return text;
    }

    /**
     * Adding a wildcard \ s to the figures in the line (for a match in the event of large numbers in groups of 3 digits)
     * @param text text
     * @return formatted text
     */
    public static String escapeSpacesFromNumbers(final String text) {
        return text.replaceAll("(\\d)", "$1\\\\s*");
    }
}
