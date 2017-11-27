package it.unive.dais.cevid.aac.parser;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unive.dais.cevid.datadroid.lib.parser.AbstractAsyncParser;
import it.unive.dais.cevid.datadroid.lib.util.Function;
import it.unive.dais.cevid.datadroid.lib.util.ProgressStepper;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by fbusolin on 13/11/17.
 */

public class SupplierParser extends AbstractAsyncParser<SupplierParser.Data, ProgressStepper> {
    public static final String TAG = "SupplierParser";
    private static final String QUERY = "http://dati.consip.it/api/action/datastore_search_sql?" +
            "sql=SELECT%20*%20" +
            "FROM%20%22f476dccf-d60a-4301-b757-829b3e030ac6%22%20" +
            "ORDER%20BY%22Numero_Aggiudicazioni%22%20DESC%20LIMIT%20100";

//    private final View container;
//    private final ArrayList<SupplierItem> items;
//    private final Context context;
    private final Function<List<Data>, Void> onPostExecute;

    // TODO: controllare meglio l'uso del campo context
    public SupplierParser(Function<List<Data>, Void> onPostExecute) {
//        this.items = list;
//        this.context = ctx;
//        this.container = container;
        this.onPostExecute = onPostExecute;
    }

    @NonNull
    @Override
    public List<Data> parse() throws IOException {
        Request request = new Request.Builder()
                .url(QUERY)
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

    protected List<SupplierParser.Data> parseJSON(String data) throws JSONException {
        List<SupplierParser.Data> r = new ArrayList<>();
        JSONObject jo = new JSONObject(data);
        JSONObject result = jo.getJSONObject("result");
        JSONArray array = result.getJSONArray("records");
        ProgressStepper prog = new ProgressStepper(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            SupplierParser.Data d = new SupplierParser.Data();
            d.id = obj.optString("id");
            d.n_abilitazioni = obj.optString("Numero_Abilitazioni");
            d.n_aggiudicati = obj.optString("Numero_Aggiudicazioni");
            d.forma_societaria = obj.optString("Forma_Societaria");
            d.indirizzo = obj.optString("Indirizzo_Sede_legale");
            d.piva = obj.optString("#Partita_Iva");
            d.provincia = obj.optString("Provincia_Sede_legale");
            d.n_transazioni = obj.optString("Numero_Transazioni");
            d.ragione_sociale = obj.optString("Ragione_Sociale");
            d.regione = obj.optString("Regione_Sede_legale");
            d.n_attivi = obj.optString("Numero_Contratti_Attivi");
            d.comune = obj.optString("Comune_Sede_legale");
            d.nazione = obj.optString("Nazione_Sede_legale");
            if (!Objects.equals(d.n_aggiudicati, "") && !Objects.equals(d.n_aggiudicati, "0")) {
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
        this.onPostExecute.apply(r);

//        if (r == null || r.size() <= 0) return;
//        List<Data> fornitori = new ArrayList<>(r);
//        for (SupplierParser.Data fornitore : fornitori) {
//            SupplierItem f = new SupplierItem(context, fornitore);
//            this.items.add(f);
//            BottomNavigationView bnv = (BottomNavigationView) this.container.findViewById(R.id.navigation);
//            for (int i = 0; i < bnv.getMenu().size(); i++) {
//                MenuItem item = bnv.getMenu().getItem(i);
//                if (!item.isEnabled()) {
//                    item.setEnabled(true);
//                }
//            }
//
//        }

        // TODO: valutare se vale la pena riusare la notifica al sistema quando finisce il download
//        NotificationManager mManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this.context)
//                        .setSmallIcon(R.drawable.ic_file_download_black_24dp)
//                        .setContentTitle(context.getResources().getString(R.string.notification_title))
//                        .setContentText(context.getResources().getString(R.string.notification_msg));
//        //.setContentIntent(pendingIntent);
//        Notification notification = mBuilder.build();
//        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
//        int id = context.getResources().getInteger(R.integer.id_notification);
//        assert mManager != null;
//        mManager.notify(id, notification);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    public class Data implements Serializable {
        public String n_abilitazioni,
                n_aggiudicati,
                forma_societaria,
                indirizzo,
                piva,
                provincia,
                n_transazioni,
                ragione_sociale,
                regione,
                id,
                nazione,
                comune,
                n_attivi;
    }

    /*
        private Position position;

        private class Position implements Serializable {
            private double latitute;
            private double longitude;

            private Position(LatLng position) {
                this.latitute = position.latitude;
                this.longitude = position.longitude;
            }

            private LatLng getLatLng() {
                return new LatLng(this.latitute, this.longitude);
            }
        }


        public void setPosition() {
            this.position = new Position(getLatLngFromAddress(String.format("%s %s %s %s %s",
                    this.indirizzo, this.comune, this.provincia, this.regione, this.nazione)));
        }

        public LatLng getLatLngFromAddress(String address) {
            Geocoder geocode = new Geocoder(context, Locale.getDefault());
            List<Address> names = new ArrayList<>();
            try {
                names = geocode.getFromLocationName(address, 10);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (names.size() <= 0) return new LatLng(0, 0);
            return new LatLng(names.get(0).getLatitude(), names.get(0).getLongitude());
        }

        public LatLng getPosition() {
            return this.position.getLatLng();
        }

        public String getDescription() {
            return String.format("%s\n%s", forma_societaria, piva);
        }

        public String getAddress() {
            return String.format("%s, %s, %s, %s (%s)",
                    this.indirizzo, this.comune, this.provincia, this.regione, this.nazione);
        }
    }
    */
}
