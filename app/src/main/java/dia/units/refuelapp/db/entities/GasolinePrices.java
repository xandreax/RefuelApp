package dia.units.refuelapp.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import dia.units.refuelapp.db.DateConverter;

@Entity(tableName = "prices_table")
public class GasolinePrices {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idPlant;
    private String fuelType;
    private double price;
    private int isSelf;
    @TypeConverters(DateConverter.class)
    private Date updateDate;

    public GasolinePrices(int idPlant, String fuelType, double price, int isSelf, Date updateDate) {
        this.idPlant = idPlant;
        this.fuelType = fuelType;
        this.price = price;
        this.isSelf = isSelf;
        this.updateDate = updateDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIdPlant() {
        return idPlant;
    }

    public String getFuelType() {
        return fuelType;
    }

    public double getPrice() {
        return price;
    }

    public int getIsSelf() {
        return isSelf;
    }

    public Date getUpdateDate() {
        return updateDate;
    }
}
