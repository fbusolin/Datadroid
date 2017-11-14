package it.unive.dais.cevid.aac.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.aac.R;
import it.unive.dais.cevid.aac.entities.Fornitore;
import it.unive.dais.cevid.datadroid.lib.parser.AbstractAsyncParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;
import it.unive.dais.cevid.datadroid.lib.util.MapItem;
import it.unive.dais.cevid.datadroid.lib.util.ProgressStepper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by admin on 13/11/17.
 */

public class FornitoriParser extends AbstractAsyncParser<FornitoriParser.Data,ProgressStepper> {
    String query = "http://dati.consip.it/api/action/datastore_search?resource_id=f476dccf-d60a-4301-b757-829b3e030ac6";
    List<Fornitore> items;
    Context context;
    public FornitoriParser(Context ctx, List<Fornitore> list){
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
        int n = result.getInt("total");
        JSONArray array = result.getJSONArray("records");
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
            r.add(d);
        }
        return r;

    }

    public class Data extends MapItem{
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
        LatLng position;


        public void setPosition() {
             this.position = getLatLngFromAddress(String.format("%s %s %s %s %s",
                     this.indirizzo,this.comune_sede,this.prov_sede,this.reg_sede,this.nazione_sede));
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

        @Override
        public LatLng getPosition() {
            return this.position;
        }
        public String getDescription(){
            return String.format("%s\n%s", tipo_soc,piva);
        }
        public String getTitle(){
            return this.rag_sociale;
        }
    }

    @Override
    protected void onPostExecute(List<Data> r) {
        List<FornitoriParser.Data> fornitori = r;
        for(FornitoriParser.Data fornitore : fornitori){
            LatLng position = fornitore.getPosition();
            Fornitore f = new Fornitore(fornitore.getTitle(),fornitore.getDescription(),fornitore.getPosition().latitude,fornitore.getPosition().longitude);
            this.items.add(f);

        }
    }
}
