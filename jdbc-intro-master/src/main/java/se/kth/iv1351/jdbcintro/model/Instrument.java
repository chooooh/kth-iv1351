package se.kth.iv1351.jdbcintro.model;

import java.sql.Date;
import java.sql.Time;

public class Instrument {
    private int rentalId;
    private int instrumentId;
    private String brand;
    private int fee;
    private Date date;
    private Time time;
    private int duration;
    private String name;

    public Instrument(int rentalId, int instrumentId, String brand, int fee, Date date, Time time, int duration, String name) throws Exception {
        this.rentalId = rentalId;
        this.instrumentId = instrumentId;
        this.brand = brand;
        this.fee = fee;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.name = name;
    }

    public Instrument(int rentalId, int instrumentId, String brand, int fee) {
        this.rentalId = rentalId;
        this.instrumentId = instrumentId;
        this.brand = brand;
        this.fee = fee;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public String getBrand() {
        return brand;
    }

    public int getFee() {
        return fee;
    }

    public int getRentalId() {
        return rentalId;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "rentalId=" + rentalId +
                ", instrumentId=" + instrumentId +
                ", brand='" + brand + '\'' +
                ", fee=" + fee +
                ", date=" + date +
                ", time=" + time +
                ", duration=" + duration +
                ", name='" + name + '\'' +
                '}';
    }
}
