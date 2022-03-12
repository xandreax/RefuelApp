package dia.units.refuelapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import java.util.Objects;

@Entity(tableName = GasolinePlant.TABLE_NAME)
public class GasolinePlant {
    public static final String TABLE_NAME = "plants_table";
    @PrimaryKey
    @CsvBindByName(column = "idImpianto", required = true)
    private int idPlant;
    @CsvBindByName(column = "Gestore")
    private String manager;
    @CsvBindByName(column = "Bandiera")
    private String flagCompany;
    @CsvBindByName(column = "Tipo Impianto")
    private String type;
    @CsvBindByName(column = "Nome Impianto")
    private String name;
    @CsvBindByName(column = "Indirizzo")
    private String address;
    @CsvBindByName(column = "Comune")
    private String city;
    @CsvBindByName(column = "Provincia")
    private String province;
    @CsvBindByName(column = "Latitudine")
    private double latitude;
    @CsvBindByName(column = "Longitudine")
    private double longitude;

    @Ignore
    public GasolinePlant() {
    }

    public GasolinePlant(int idPlant, String manager, String flagCompany, String type, String name, String address, String city, String province, double latitude, double longitude) {
        this.idPlant = idPlant;
        this.manager = manager;
        this.flagCompany = flagCompany;
        this.type = type;
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getIdPlant() {
        return idPlant;
    }

    public String getManager() {
        return manager;
    }

    public String getFlagCompany() {
        return flagCompany;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setFlagCompany(String flagCompany) {
        this.flagCompany = flagCompany;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GasolinePlant that = (GasolinePlant) o;
        return idPlant == that.idPlant && Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && manager.equals(that.manager) && flagCompany.equals(that.flagCompany) && type.equals(that.type) && name.equals(that.name) && address.equals(that.address) && city.equals(that.city) && province.equals(that.province);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlant, manager, flagCompany, type, name, address, city, province, latitude, longitude);
    }
}
