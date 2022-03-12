package dia.units.refuelapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import dia.units.refuelapp.db.DateConverter;

@Entity(tableName = RefuelItem.TABLE_NAME)
public class RefuelItem {
    public static final String TABLE_NAME = "refuel_items_table";
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idPlant;
    private double money;
    private double liters;
    private double price_by_liter;
    private double kilometers;
    @TypeConverters(DateConverter.class)
    private Date updateDate;

    public RefuelItem(int idPlant, double money, double liters, double price_by_liter, double kilometers, Date updateDate) {
        this.idPlant = idPlant;
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

    public int getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
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
