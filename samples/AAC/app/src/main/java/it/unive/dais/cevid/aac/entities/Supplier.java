package it.unive.dais.cevid.aac.entities;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import it.unive.dais.cevid.aac.util.FornitoriParser;
import it.unive.dais.cevid.datadroid.lib.util.MapItem;

/**
 * Created by fbusolin on 13/11/17.
 */

public class Supplier extends MapItem implements Serializable {
    private String address;
    private String type;
    private String title;
    private double latitude;
    private double longitude;
    private String description;
    private String piva;
    public Supplier(FornitoriParser.Data data){
        this.title = data.getTitle();
        this.description = data.getDescription();
        this.latitude = data.getPosition().latitude;
        this.longitude = data.getPosition().longitude;
        this.piva = data.getPiva();
        this.type = data.getTipo_soc();
        this.address = data.getAddress();
    }

    public String getType() {
        return type;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude,longitude);
    }

    @Override
    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getPiva() {
        return piva;
    }
}
