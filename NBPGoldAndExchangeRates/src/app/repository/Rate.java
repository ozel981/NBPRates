package app.repository;

import java.time.LocalDate;

public class Rate {
    public Rate(double value, String date) {
        this.date = LocalDate.parse(date);
        this.value = value;
    }

    private LocalDate date;
    private double value;

    public double getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date;
    }
}
