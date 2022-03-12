package dia.units.refuelapp.model;

import androidx.room.TypeConverters;

import java.util.Date;

import dia.units.refuelapp.db.DateConverter;

public class RefuelItemWithPlantInfo {
    private String name;
    private String flagCompany;
    private int id;
    private double money;
    private double liters;
    private double price_by_liter;
    private double kilometers;
    @TypeConverters(DateConverter.class)
    private Date updateDate;

    public RefuelItemWithPlantInfo(String name, String flagCompany, int id, double money, double liters, double price_by_liter, double kilometers, Date updateDate) {
        this.name = name;
        this.flagCompany = flagCompany;
        this.id = id;
        this.money = money;
        this.liters = liters;
        this.price_by_liter = price_by_liter;
        this.kilometers = kilometers;
        this.updateDate = updateDate;
    }

    public String getName() {
        return name;
    }

    public String getFlagCompany() {
        return flagCompany;
    }

    public int getId() {
        return id;
    }

    public double getMoney() {
        return money;
    }

    public double getLiters() {
        return liters;
    }

    public double getPrice_by_liter() {
        return price_by_liter;
    }

    public double getKilometers() {
        return kilometers;
    }

    public Date getUpdateDate() {
        return updateDate;
    }
}
