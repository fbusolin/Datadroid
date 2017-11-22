package it.unive.dais.cevid.aac.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import it.unive.dais.cevid.aac.MapsActivity;
import it.unive.dais.cevid.aac.R;
import it.unive.dais.cevid.aac.entities.Supplier;
import it.unive.dais.cevid.datadroid.lib.parser.AbstractAsyncParser;
import it.unive.dais.cevid.datadroid.lib.util.MapItem;
import it.unive.dais.cevid.datadroid.lib.util.ProgressStepper;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by fbusolin on 13/11/17.
 */

public class FornitoriParser extends AbstractAsyncParser<FornitoriParser.Data,ProgressStepper> {
    public static final String TAG = "FornitoriParser";
    private String query = "http://dati.consip.it/api/action/datastore_search_sql?" +
            "sql=SELECT%20*%20" +
            "FROM%20%22f476dccf-d60a-4301-b757-829b3e030ac6%22%20" +
            "ORDER%20BY%22Numero_Aggiudicazioni%22%20DESC%20LIMIT%20100";
    /** per ora solo i primi 100 fornitori attivi nel 2016,
     * vengono filtrati poi solo quelli che hanno vinto bandi
     * esclusi quelli nominati
    **/
    private ArrayList<Supplier> items;
    private Context context;

    public FornitoriParser(Context ctx, ArrayList<Supplier> list){
        this.items = list;
        this.context = ctx;
    }
    @NonNull
    @Override
    public List<Data> parse() throws IOException {

        Request request = new Request.Builder()
                .url(query)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept", "Application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();

        try {
            return parseJSON(new OkHttpClient().newCall(request).execute().body().string());
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }
    protected List<FornitoriParser.Data> parseJSON(String data) throws JSONException {
        List<FornitoriParser.Data> r = new ArrayList();
        JSONObject jo = new JSONObject(data);
        JSONObject result = jo.getJSONObject("result");
        JSONArray array = result.getJSONArray("records");
        ProgressStepper prog = new ProgressStepper(array.length());
        for(int i = 0; i < array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            FornitoriParser.Data d = new FornitoriParser.Data();
            d.id = obj.optString("_id");
            d.n_abilitazioni = obj.optString("Numero_Abilitazioni");
            d.n_aggiudicati = obj.optString("Numero_Aggiudicazioni");
            d.tipo_soc = obj.optString("Forma_Societaria");
            d.indirizzo = obj.optString("Indirizzo_Sede_legale");
            d.piva = obj.optString("#Partita_Iva");
            d.prov_sede = obj.optString("Provincia_Sede_legale");
            d.n_transazioni = obj.optString("Numero_Transazioni");
            d.rag_sociale = obj.optString("Ragione_Sociale");
            d.reg_sede = obj.optString("Regione_Sede_legale");
            d.n_attivi = obj.optString("Numero_Contratti_Attivi");
            d.comune_sede = obj.optString("Comune_Sede_legale");
            d.nazione_sede = obj.optString("Nazione_Sede_legale");
            d.setPosition();
            if(!Objects.equals(d.n_aggiudicati, "") && !Objects.equals(d.n_aggiudicati, "0")) {
                r.add(d);
            }
            prog.step();
            publishProgress(prog);
        }
        return r;

    }


    @Override
    protected void onPostExecute(List<Data> r) {
        super.onPostExecute(r);
        if(r == null || r.size() <=0)return;
        List<Data> fornitori = new ArrayList<>(r);
        for(FornitoriParser.Data fornitore : fornitori){
            LatLng position = fornitore.getPosition();
            Supplier f = new Supplier(fornitore);
            this.items.add(f);

        }
        /**
         * finito il download mandiamo una notifica
         */
        /* NON FUNZIONA COME DOVREBBE
           non riesco a inviare gli extra, l'intent in MapsActivity
           ha sempre mExtra = null.
        Intent intent = new Intent(this.context,MapsActivity.class);
        intent.putExtra(MapsActivity.SAVE_SUPPLY,this.items);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP );
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        */
        NotificationManager mManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.context)
                        .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                        .setContentTitle(context.getResources().getString(R.string.notification_title))
                        .setContentText(context.getResources().getString(R.string.notification_msg));
                        //.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
        int id = context.getResources().getInteger(R.integer.id_notification);
        mManager.notify(id,notification);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }



    public class Data implements Serializable{
        String n_abilitazioni,
                n_aggiudicati,
                tipo_soc,
                indirizzo,
                piva,
                prov_sede,
                n_transazioni,
                rag_sociale,
                reg_sede,
                id,
                nazione_sede,
                comune_sede,
                n_attivi;

        /*
        LatLng non è Serializable, mi serve una classe che la wrappi.
        o usare transient e implementare read e write per LatLng.
        */
        private Position position;

        private class Position implements Serializable{
            private double latitute;
            private double longitude;
            private Position(LatLng position){
                this.latitute = position.latitude;
                this.longitude = position.longitude;
            }
            private LatLng getLatLng(){
                return new LatLng(this.latitute,this.longitude);
            }
        }


        public void setPosition() {
            this.position = new Position(getLatLngFromAddress(String.format("%s %s %s %s %s",
                    this.indirizzo,this.comune_sede,this.prov_sede,this.reg_sede,this.nazione_sede)));
        }

        public LatLng getLatLngFromAddress(String address){
            Geocoder geocode = new Geocoder(context,Locale.getDefault());
            List<Address> names = new ArrayList<>();
            try {
                names = geocode.getFromLocationName(address,10);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(names.size() <= 0) return new LatLng(0,0);
            return new LatLng(names.get(0).getLatitude(),names.get(0).getLongitude());
        }

        public LatLng getPosition() {
            return this.position.getLatLng();
        }
        public String getDescription(){
            return String.format("%s\n%s", tipo_soc,piva);
        }
        public String getTitle(){
            return this.rag_sociale;
        }

        public String getN_abilitazioni() {
            return n_abilitazioni;
        }

        public String getN_aggiudicati() {
            return n_aggiudicati;
        }

        public String getTipo_soc() {
            return tipo_soc;
        }

        public String getIndirizzo() {
            return indirizzo;
        }

        public String getPiva() {
            return piva;
        }

        public String getProv_sede() {
            return prov_sede;
        }

        public String getN_transazioni() {
            return n_transazioni;
        }

        public String getRag_sociale() {
            return rag_sociale;
        }

        public String getReg_sede() {
            return reg_sede;
        }

        public String getId() {
            return id;
        }

        public String getNazione_sede() {
            return nazione_sede;
        }

        public String getComune_sede() {
            return comune_sede;
        }

        public String getN_attivi() {
            return n_attivi;
        }
        public String getAddress(){
            return String.format("%s, %s, %s, %s (%s)",
                    this.indirizzo,this.comune_sede,this.prov_sede,this.reg_sede,this.nazione_sede);
        }
    }
}
