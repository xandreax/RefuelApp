package dia.units.refuelapp.ui.util;

import com.example.refuelapp.R;

import java.util.Date;

import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;

public class UiUtilMethods {

    public static String setDate(GasolinePrice gasolinePrice){
        Date today = new Date();
        if(gasolinePrice.getDaysOfLastUpdate(today) == 0 )
            return "Oggi";
        else if(gasolinePrice.getDaysOfLastUpdate(today) == 1 )
            return "Ieri";
        else {
            return "(" + gasolinePrice.getDaysOfLastUpdate(today) + " giorni fa)";
        }
    }

    public static String setStradaStatale (GasolinePlant plant){
        if(plant.getType().equals("Strada Statale")){
            String address = plant.getAddress();
            if(address.toLowerCase().startsWith("statale")) {
                return "Strada " + plant.getAddress();
            }
            else if(address.toLowerCase().startsWith("strada statale")){
                return plant.getAddress();
            }
            else{
                return "Strada statale" + plant.getAddress();
            }
        }
        else {
            return plant.getAddress();
        }
    }

    public static int setLogo (String flagCompany){
        int idLogo;
        switch (flagCompany){
            case "Api-Ip":
                idLogo = R.drawable.api_ip;
                break;
            case "Beyfin":
                idLogo = R.drawable.beyfin;
                break;
            case "Costantin":
                idLogo = R.drawable.costantin;
                break;
            case "Ego":
                idLogo = R.drawable.ego;
                break;
            case "Energas":
                idLogo = R.drawable.energas;
                break;
            case "Agip Eni":
                idLogo = R.drawable.eni;
                break;
            case "Esso":
                idLogo = R.drawable.esso;
                break;
            case "Europam":
                idLogo = R.drawable.europam;
                break;
            case "Giap":
                idLogo = R.drawable.giap;
                break;
            case "KEROPETROL":
                idLogo = R.drawable.keropetrol;
                break;
            case "Oil Italia":
                idLogo = R.drawable.oil_italia;
                break;
            case "Pompe Bianche":
                idLogo = R.drawable.pompebianche;
                break;
            case "Q8":
                idLogo = R.drawable.q8;
                break;
            case "Repsol":
                idLogo = R.drawable.repsol;
                break;
            case "Shell":
                idLogo = R.drawable.shell;
                break;
            case "Sia fuel":
                idLogo = R.drawable.sia_fuel;
                break;
            case "San Marco Petroli":
                idLogo = R.drawable.ic_smp;
                break;
            case "Tamoil":
                idLogo = R.drawable.tamoil;
                break;
            case "Total Erg":
                idLogo = R.drawable.totalerg;
                break;
            case "Vega":
                idLogo = R.drawable.vega;
                break;
            default:
                idLogo = R.drawable.distributore;
        }
        return idLogo;
    }

    public static int setIconFuelPump (GasolinePrice price){
        int idIcon;
        switch(price.getFuelType()){
            case "Benzina":
                idIcon = R.drawable.ic_fuel_pump_green;
                break;
            case "Blue Diesel":
            case "Blue Super":
                idIcon = R.drawable.ic_fuel_pump_blue;
                break;
            case "Metano":
                idIcon = R.drawable.ic_fuel_pump_brown;
                break;
            case "Hi-Q Diesel":
                idIcon = R.drawable.ic_fuel_pump_orange;
                break;
            case "Gasolio Gelo":
            case "Gasolio artico":
            case "Gasolio Artico":
            case "Gasolio Alpino":
                idIcon = R.drawable.ic_fuel_pump_azure;
                break;
            case "GPL":
                idIcon = R.drawable.ic_fuel_pump_grey;
                break;
            case "Gasolio Oro Diesel":
                idIcon = R.drawable.ic_fuel_pump_yellow;
                break;
            case "Gasolio Premium":
                idIcon = R.drawable.ic_fuel_pump_red;
                break;
            case "HiQ Perform+":
                idIcon = R.drawable.ic_fuel_pump_skyblue;
                break;
            default:
                idIcon = R.drawable.ic_fuel_pump_black;
        }
        return idIcon;
    }
}
