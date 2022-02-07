package dia.units.refuelapp.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plants_table")
public class GasolinePlant {
    @PrimaryKey
    private int idPlant;
    private String manager;
    private String flagCompany;
    private String type;
    private String name;
    private String address;
    private String city;
    private String province;
    private float latitude;
    private float longitude;

    public GasolinePlant(int idPlant, String manager, String flagCompany, String type, String name, String address, String city, String province, float latitude, float longitude) {
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

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
