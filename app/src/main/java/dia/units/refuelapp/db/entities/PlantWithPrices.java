package dia.units.refuelapp.db.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Collections;
import java.util.List;

public class PlantWithPrices {
    @Embedded public GasolinePlant gasolinePlant;
    @Relation(
            parentColumn = "idPlant",
            entityColumn = "idPlant"
    )
    public List<GasolinePrice> gasolinePrices;
}
