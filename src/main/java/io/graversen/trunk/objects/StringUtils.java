package io.graversen.trunk.objects;

public class StringUtils
{
    /**
     * Splits a String based on a single character, which is usually faster than regex-based String.split().
     * NOTE: split("AA;BB;;", ';') == ["AA", "BB", "", ""], this may be different from String.split()
     *  */
    public String[] split(String target, char delimiter) {
        int delimeterCount = 0;
        int start = 0;
        int end;
        while ((end = target.indexOf(delimiter, start)) != -1) {
            delimeterCount++;
            start = end + 1;
        }

        String[] result = new String[delimeterCount + 1];
        start = 0;
        for (int i = 0; i < delimeterCount; i++) {
            end = target.indexOf(delimiter, start);
            result[i] = target.substring(start, end);
            start = end + 1;
        }
        result[delimeterCount] = target.substring(start, target.length());
        return result;
    }

    /**
     * Cuts the string at the end if it's longer than maxLength and appends "..." to it. The length of the resulting
     * string including "..." is always less or equal to the given maxLength. It's valid to pass a null text; in this
     * case null is returned.
     */
    public String ellipsize(String text, int maxLength) {
        return ellipsize(text, maxLength, "...");
    }

    /**
     * Cuts the string at the end if it's longer than maxLength and appends the given end string to it. The length of
     * the resulting string is always less or equal to the given maxLength. It's valid to pass a null text; in this
     * case null is returned.
     */
    public String ellipsize(String text, int maxLength, String end) {
        if (text != null && text.length() > maxLength) {
            return text.substring(0, maxLength - end.length()) + end;
        }
        return text;
    }
}
