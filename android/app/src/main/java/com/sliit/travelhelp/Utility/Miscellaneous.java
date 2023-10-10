package com.sliit.travelhelp.Utility;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class Miscellaneous {


        public static String getGreeting() {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            if (hour >= 5 && hour < 12) {
                return "Good Morning";
            } else if (hour >= 12 && hour < 18) {
                return "Good Afternoon";
            } else {
                return "Good Evening";
            }
        }

    public static String getDaysLeftString(LocalDate targetDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(currentDate, targetDate);

        int daysLeft = period.getDays();
        int weeksLeft = period.getDays() / 7;

        if (daysLeft < 0) {
            return "Date has passed";
        } else if (daysLeft == 0) {
            return "Today";
        } else if (daysLeft == 1) {
            return "1 day left";
        } else if (weeksLeft >= 1) {
            return weeksLeft + " week" + (weeksLeft > 1 ? "s" : "") + " left";
        } else {
            return daysLeft + " days left";
        }
    }

        public static void main(String[] args) {
            String greeting = getGreeting();
            System.out.println(greeting);
        }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
    }

