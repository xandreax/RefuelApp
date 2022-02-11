package dia.units.refuelapp.db.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dia.units.refuelapp.db.DateConverter;

@Entity(tableName = GasolinePrice.TABLE_NAME,
        foreignKeys = {@ForeignKey(entity = GasolinePlant.class,
                parentColumns = "idPlant",
                childColumns = "idPlant",
                onDelete = ForeignKey.CASCADE)})
public class GasolinePrice {
    public static final String TABLE_NAME = "prices_table";
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idPlant;
    private String fuelType;
    private double price;
    private int isSelf;
    @TypeConverters(DateConverter.class)
    private Date updateDate;

    public GasolinePrice() {
    }

    public GasolinePrice(int idPlant, String fuelType, double price, int isSelf, String updateDate) {
        this.idPlant = idPlant;
        this.fuelType = fuelType;
        this.price = price;
        this.isSelf = isSelf;
        try {
            this.updateDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
            this.updateDate = null;
        }
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

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setIsSelf(int isSelf) {
        this.isSelf = isSelf;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getDaysOfLastUpdate (Date date){
        long diffInMillis = Math.abs(date.getTime() - updateDate.getTime());
        return (int) (diffInMillis/ (1000 * 60 * 60* 24));
    }
}
