package dia.units.refuelapp.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import dia.units.refuelapp.db.DateConverter;

@Entity(tableName = GasolinePrice.TABLE_NAME, primaryKeys = {"idPlant", "fuelType", "isSelf"})
public class GasolinePrice {
    public static final String TABLE_NAME = "prices_table";

    @CsvBindByName(column = "idImpianto")
    private int idPlant;
    @CsvBindByName(column = "descCarburante")
    @NonNull
    private String fuelType;
    @CsvBindByName(column = "prezzo")
    private double price;
    @CsvBindByName(column = "isSelf")
    private int isSelf;
    @TypeConverters(DateConverter.class)
    @CsvBindByName(column = "dtComu")
    @CsvDate("dd/MM/yyyy HH:mm:ss")
    private Date updateDate;

    public GasolinePrice() {
    }

    @SuppressLint("SimpleDateFormat")
    public GasolinePrice(int idPlant, @NonNull String fuelType, double price, int isSelf, String updateDate) {
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

    public int getIdPlant() {
        return idPlant;
    }

    @NonNull
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

    public void setFuelType(@NonNull String fuelType) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GasolinePrice that = (GasolinePrice) o;
        return idPlant == that.idPlant && Double.compare(that.price, price) == 0 && isSelf == that.isSelf && fuelType.equals(that.fuelType) && updateDate.equals(that.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlant, fuelType, price, isSelf, updateDate);
    }
}
