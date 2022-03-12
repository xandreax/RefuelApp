package dia.units.refuelapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = FavoritePlant.TABLE_NAME)
public class FavoritePlant {
    public static final String TABLE_NAME = "favorites_table";
    @PrimaryKey
    private int idPlant;

    public FavoritePlant(int idPlant) {
        this.idPlant = idPlant;
    }

    public int getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }
}
