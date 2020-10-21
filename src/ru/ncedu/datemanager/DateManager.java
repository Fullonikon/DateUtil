package ru.ncedu.datemanager;

import static java.lang.Math.*;

public class DateManager {

    long sec = System.currentTimeMillis() / 1000;
    int days = (int) (sec / 60 / 60 / 24);
    Date curDate = new Date(1970, 1, 1);
    int curDayOfWeek;

    private void initiateToday() {
        int daysCounter = days;
        curDayOfWeek = ((days - 4) % 7);
        DateManager a = new DateManager();
        while ((daysCounter > 366 && DateUtils.isLeapYear(curDate.getYear())) || (daysCounter > 365 && !DateUtils.isLeapYear(curDate.getYear()))) {
            if (DateUtils.isLeapYear(curDate.getYear())) {
                daysCounter -= 366;
            } else {
                daysCounter -= 365;
            }
            curDate.setYear(curDate.getYear()+1);
        }

        while ((daysCounter > 31 && curDate.getMonth() % 2 == 0) || (daysCounter > 30 && curDate.getMonth() % 2 == 1)) {

            if (curDate.getMonth() == 2 && DateUtils.isLeapYear(curDate.getYear())) {
                daysCounter -= 30;
            } else if (curDate.getMonth() % 2 == 0 && DateUtils.isLeapYear(curDate.getYear())) daysCounter -= 31;
            else if (DateUtils.isLeapYear(curDate.getYear())) daysCounter -= 30;
            if (curDate.getMonth() == 2 && !DateUtils.isLeapYear(curDate.getYear())) {
                daysCounter = -28;
            } else if (curDate.getMonth() % 2 == 0 && !DateUtils.isLeapYear(curDate.getYear())) daysCounter -= 31;
            else if (!DateUtils.isLeapYear(curDate.getYear())) daysCounter -= 30;
            curDate.setMonth(curDate.getMonth()+1);
        }
        curDate.setDay(daysCounter);
    }


    enum Day {
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday"),
        SATURDAY("Saturday"),
        SUNDAY("Sunday");
        private String dayOfWeek;

        Day(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }
    }

    enum Month {
        Jan("Jan"),
        Feb("Feb"),
        Mar("Mar"),
        Apr("Apr"),
        May("May"),
        Jun("Jun"),
        Jul("Jul"),
        Aug("Aug"),
        Sep("Sep"),
        Oct("Oct"),
        Nov("Nov"),
        Dec("Dec");
        private String monthOfYear;

        Month(String monthOfYear) {
            this.monthOfYear = monthOfYear;
        }

        public String getMonthOfYear() {
            return monthOfYear;
        }
    }

    boolean isValidDate(Date date) {
        DateManager a = new DateManager();
        if (date.getYear() < 0) return false; // counting a.c.
        if (date.getMonth() > 12 || date.getMonth() < 1) return false;
        if (date.getDay() < 1 || (date.getMonth() == 2 && date.getDay() > 29 && DateUtils.isLeapYear(date.getYear())) || (date.getMonth() == 2 && date.getDay() > 28 && !DateUtils.isLeapYear(date.getYear())))
            return false;
        return true;
    }

    int getDayOfWeek(Date date) throws IncorrectDateExeption {
        int res = 0;
        this.initiateToday();
        DateManager a = new DateManager();
        res = a.countDays(date);
        if (date.getYear() > curDate.getYear()) {
            res = -1 * res;
        } else if (date.getYear() == curDate.getYear()) {
            if (date.getMonth() > curDate.getMonth()) {
                res = -1 * res;
            } else if (date.getMonth() == curDate.getMonth()) {
                if (date.getDay() > curDate.getDay()) {
                    res = -1 * res;
                }
            }
        }
        res = (days - res - 4) % 7; // -4 because 1970 1 1 is Thursday
        if (res < 0) res += 7;
        return res;
    }

    String toString(Date date) throws IncorrectDateExeption {
        DateManager a = new DateManager();
        if (!a.isValidDate(date)) throw new IncorrectDateExeption("Incorrect date input");
        String res = "";
        res += Day.values()[a.getDayOfWeek(date)].getDayOfWeek() + " ";
        res += date.getDay() + " ";
        res += Month.values()[date.getMonth() - 1].getMonthOfYear() + " ";

        return res + date.getYear();
    }

    int countDays(Date date) throws IncorrectDateExeption {
        int res = 0;
        this.initiateToday();
        boolean f = true;
        if (date.getYear() > curDate.getYear()) {
            f = false;
        } else if (date.getYear() == curDate.getYear()) {
            if (date.getMonth() > curDate.getMonth()) {
                f = false;
            } else if (date.getMonth() == curDate.getMonth()) {
                if (date.getDay() > curDate.getDay()) {
                    f = false;
                }
            }
        }
        DateManager a = new DateManager();
        if (!isValidDate(date)) throw new IncorrectDateExeption("Incorrect date input");
        for (int i = min(curDate.getYear(), date.getYear()) + 1; i < max(curDate.getYear(), date.getYear()); i++) {
            if (DateUtils.isLeapYear(i)) {
                res += 366;
            } else res += 365;
        }

        if (f && date.getYear() != curDate.getYear()) {
            res += a.countDaysInYear(DateUtils.isLeapYear(curDate.getYear()), curDate.getMonth());
            res += curDate.getDay();
            int tmp = a.countDaysInYear(DateUtils.isLeapYear(date.getYear()), date.getMonth());
            tmp += date.getDay();
            if (DateUtils.isLeapYear(date.getYear())) res += (366 - tmp);
            else res += (365 - tmp);
        } else {
            if (curDate.getYear() != date.getYear()) {
                res +=  a.countDaysInYear(DateUtils.isLeapYear(date.getYear()), date.getMonth());
                res += curDate.getDay();
                int tmp = a.countDaysInYear(DateUtils.isLeapYear(curDate.getYear()), curDate.getMonth());
                tmp += date.getDay();
                if (DateUtils.isLeapYear(curDate.getYear())) res += (366 - tmp);
                else res += (365 - tmp);
            } else {
                res += a.countDaysBetweenMonths(DateUtils.isLeapYear(date.getYear()), date.getMonth(), curDate.getMonth());
                if (f) res += curDate.getDay() - date.getDay();
                else res += date.getDay() - curDate.getDay();
            }
        }
        return res;
    }


    private int countDaysInYear(boolean isLeapYear, int month) {
        int res = 0;
        for (int i = 1; i < month; i++) {
            res += countDaysInMonth(i, isLeapYear);
        }
        return res;
    }

    private int countDaysBetweenMonths(boolean isLeapYear, int month, int curMonth) {
        int res = 0;
        for (int i = min(month, curMonth); i < max(month, curMonth); i++) {
            res += countDaysInMonth(i, isLeapYear);
        }
        return res;
    }

    private int countDaysInMonth(int month, boolean isLeapYear) {
        int res = 0;
        if (isLeapYear) {
            if (month == 2) res += 29;
            else if (month % 2 == 0) {
                if (month < 7) res += 30;
                else res += 31;
            } else {
                if (month <= 7) res += 31;
                else res += 30;
            }
        } else {
            if (month == 2) res += 28;
            else if (month % 2 == 0) {
                if (month < 7) res += 30;
                else res += 31;
            } else {
                if (month <= 7) res += 31;
                else res += 30;
            }
        }
        return res;
    }
}

