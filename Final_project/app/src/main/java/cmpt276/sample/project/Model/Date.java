package cmpt276.sample.project.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * A enum class for converting date into String data type and LocalDate data type,
 * which others classes can get the date by calling DAY_MONTH or
 * DAY_MONTH_YEAR
 *
 * @author Gavin Dang, ttd6
 * @author Lu Xi Wang, lxwang
 * @author Shan Qing, sqing
 */
public enum Date {
    DAY_MONTH{
        @RequiresApi(api = Build.VERSION_CODES.O)
        public String getDateString(int date){
            return DAY_MONTH.getDateStringInLocalDate(convertIntDateToLocalDate(date));
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public String getDateStringInLocalDate(LocalDate dateString) {
            return dateString.format(DateTimeFormatter.ofPattern("dd LLL"));
        }
    },
    DAY_MONTH_YEAR{
        @RequiresApi(api = Build.VERSION_CODES.O)
        public String getDateString(int date){
            return DAY_MONTH_YEAR.getDateStringInLocalDate(convertIntDateToLocalDate(date));
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public String getDateStringInLocalDate(LocalDate dateString) {
            return dateString.format(DateTimeFormatter.ofPattern("dd LLL yyyy"));
        }

    };

    abstract public String getDateString(int date);
    abstract public String getDateStringInLocalDate(LocalDate convertIntDateToLocalDate);

    // https://stackoverflow.com/questions/27005861/calculate-days-between-two-dates-in-java-8
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long dayFromCurrent(int date) {
        LocalDate localDate = convertIntDateToLocalDate(date);
        return ChronoUnit.DAYS.between(localDate, LocalDate.now());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate convertIntDateToLocalDate(int date) {
        int n = date;
        int year = n/10000;
        n %= 10000;
        int month = n/100;
        n %= 100;
        int day = n;

        return LocalDate.of(year, month, day);
    }
}
