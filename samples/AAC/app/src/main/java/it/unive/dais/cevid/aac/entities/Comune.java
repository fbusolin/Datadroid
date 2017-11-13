package it.unive.dais.cevid.aac.entities;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import it.unive.dais.cevid.datadroid.lib.util.MapItem;

/**
 * Created by fbusolin on 13/11/17.
 */

public class Comune extends MapItem implements Serializable {
    private String title;
    private double latitude;
    private double longitude;
    private String description;
    private List<URL> urls;
    private String codiceEnte;
    private static String codiceComparto = "PRO";

    public Comune(String title,double latitude,double longitude, String description,String codiceEnte){
        this.codiceEnte = codiceEnte;
        this.description = description;
        this.title = title;

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLng getPosition(){
        return new LatLng(latitude,longitude);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public String getCodiceEnte() {
        return codiceEnte;
    }

    public void setCodiceEnte(String codiceEnte) {
        this.codiceEnte = codiceEnte;
    }

    public static String getCodiceComparto() {
        return codiceComparto;
    }

    public static void setCodiceComparto(String codiceComparto) {
        Comune.codiceComparto = codiceComparto;
    }

}
