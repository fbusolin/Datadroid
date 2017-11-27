package it.unive.dais.cevid.aac.item;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import it.unive.dais.cevid.datadroid.lib.util.MapItem;

/**
 * Created by Fonto on 04/09/17.
 */

public class UniversityItem extends MapItem implements Serializable{
    private String title;
    private double latitude;
    private double longitude;
    private String description;
    private List<URL> urls;
    private String codiceEnte;

    private static String codice_comparto = "UNI";

    public UniversityItem(String title, double latitude, double longitude, String description, List<URL> urls, String codiceEnte) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.urls = urls;
        this.codiceEnte = codiceEnte;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public String getCodiceEnte() {
        return codiceEnte;
    }

    public static String getCodiceComparto() {
        return codice_comparto;
    }
}
