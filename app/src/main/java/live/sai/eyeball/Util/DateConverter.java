package live.sai.eyeball.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static final String INPUT_DATE_FORMAT = "MM/dd/yyyy";
    private static final String OUTPUT_DATE_FORMAT = "MMM dd, yyyy";

    public static String convertToWordsDate(String inputDate) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat(INPUT_DATE_FORMAT, Locale.ENGLISH);
            Date date = inputDateFormat.parse(inputDate);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.ENGLISH);
            String wordsDate = outputDateFormat.format(date);

            return wordsDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String inputDate = "01/01/2023";
        String wordsDate = convertToWordsDate(inputDate);
        System.out.println("Words Date: " + wordsDate);
    }
}
