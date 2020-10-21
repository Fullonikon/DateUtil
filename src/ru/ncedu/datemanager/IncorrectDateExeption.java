package ru.ncedu.datemanager;

public class IncorrectDateExeption extends Exception {
    public IncorrectDateExeption(String errorMessage) {
        super(errorMessage);
    }
}
