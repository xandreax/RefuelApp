package dia.units.refuelapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import javax.annotation.Nullable;

import dia.units.refuelapp.db.DateConverter;

@Entity(tableName = RefuelItem.TABLE_NAME)
public class RefuelItem {
    public static final String TABLE_NAME = "refuel_items_table";
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double money;
    private double liters;
    private double price_by_liter;
    private double kilometers;
    @TypeConverters(DateConverter.class)
    private Date updateDate;
    private String namePlant;

    public RefuelItem(String namePlant, double money, double liters, double price_by_liter, double kilometers, Date updateDate) {
        this.namePlant = namePlant;
        this.money = money;
        this.liters = liters;
        this.price_by_liter = price_by_liter;
        this.kilometers = kilometers;
        this.updateDate = updateDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamePlant() {
        return namePlant;
    }

    public void setNamePlant(String namePlant) {
        this.namePlant = namePlant;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getLiters() {
        return liters;
    }

    public void setLiters(double liters) {
        this.liters = liters;
    }

    public double getPrice_by_liter() {
        return price_by_liter;
    }

    public void setPrice_by_liter(double price_by_liter) {
        this.price_by_liter = price_by_liter;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
