package ru.ncedu.datemanager;

public class Main {
    public static void main(String[] args) throws Exception {
        DateManager a = new DateManager();
        Date b = new Date(2020, 10, 18);
        Date c = new Date(1970, 1, 1);
        System.out.println(a.countDays(c));
        System.out.println(a.toString(b));
    }
}