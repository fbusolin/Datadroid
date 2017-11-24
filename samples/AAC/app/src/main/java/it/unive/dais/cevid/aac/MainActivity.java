package it.unive.dais.cevid.aac;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationSettingsStates;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.aac.entities.Municipality;
import it.unive.dais.cevid.aac.entities.Supplier;
import it.unive.dais.cevid.aac.entities.University;
import it.unive.dais.cevid.aac.fragments.MapFragment;
import it.unive.dais.cevid.aac.util.FornitoriParser;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 500;
    protected static final int PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION = 501;
    public static final String SAVE_SUPPLY = "SAVED_SUPPLY";
    FragmentManager fManager;
    MapFragment mapFragment;
    BottomNavigationView bnv;


    private List<University> uni;
    private List<Municipality> comuni;
    private ArrayList<Supplier> fornitori;
    private FornitoriParser fornitoriParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        mapFragment = new MapFragment();

        this.bnv = (BottomNavigationView) findViewById(R.id.navigation);
        this.bnv.setOnNavigationItemSelectedListener(this);
        for(int i = 0; i < bnv.getMenu().size();i++){
            MenuItem item = bnv.getMenu().getItem(i);
            if (item.getTitle().equals(getString(R.string.bottom_menu_winners))){
                item.setEnabled(false);
            }

        }
        this.fornitori = new ArrayList<>();


        if (savedInstanceState == null) {
            Intent intent = getIntent();
            //fallisce sempre, intent non ha mai extra, vedi onPostExecute in FornitoriParser.
            if (intent.hasExtra(MainActivity.SAVE_SUPPLY)) {
                this.fornitori = (ArrayList<Supplier>) intent.getSerializableExtra(MainActivity.SAVE_SUPPLY);
            } else {
                fornitoriParser = new FornitoriParser(this,findViewById(R.id.main_view),this.fornitori);
                fornitoriParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } else {
            this.fornitori = (ArrayList<Supplier>) savedInstanceState.getSerializable(MainActivity.SAVE_SUPPLY);
        }

        //------- INIZIALIZZAZIONE COMUNI  E FORNITORI---------------------------
        //add Ca Foscari
        uni = new ArrayList<>();
        try {
            List<URL> urls = new ArrayList<>();
            urls.add(new URL("http://www.unive.it/avcp/datiAppalti2016.xml"));
            uni.add(new University("Ca'Foscari", 45.437576, 12.3289554, "Università degli studi di Venezia", urls, "000704968000000"));
        } catch (MalformedURLException e) {
            Log.w(TAG, "malformed url");
            e.printStackTrace();
        }

        //add Padova
        try {
            List<URL> urls = new ArrayList<>();
            urls.add(new URL("http://www.unipd.it/sites/unipd.it/files/dataset_2016_s1_01.xml"));
            urls.add(new URL("http://www.unipd.it/sites/unipd.it/files/dataset_2016_s1_02.xml"));
            urls.add(new URL("http://www.unipd.it/sites/unipd.it/files/dataset_2016_s1_03.xml"));
            urls.add(new URL("http://www.unipd.it/sites/unipd.it/files/dataset_2016_s2_01.xml"));
            urls.add(new URL("http://www.unipd.it/sites/unipd.it/files/dataset_2016_s2_02.xml"));
            uni.add(new University("Università di Padova", 45.406766, 11.8774462, "Università degli studi di Padova", urls, "000058546000000"));
        } catch (MalformedURLException e) {
            Log.w(TAG, "malformed url");
            e.printStackTrace();
        }

        //add Trento
        try {
            List<URL> urls = new ArrayList<>();
            urls.add(new URL("http://approvvigionamenti.unitn.it/bandi-di-gara-e-contratti/2017/ricerca_2016.xml"));
            urls.add(new URL("http://approvvigionamenti.unitn.it/bandi-di-gara-e-contratti/2017/amministrazione_2016.xml"));
            uni.add(new University("Università di Trento", 46.0694828, 11.1188738, "Università degli studi di Trento", urls, "000067046000000"));
        } catch (MalformedURLException e) {
            Log.w(TAG, "malformed url");
            e.printStackTrace();
        }


        //add Comuni
        comuni = new ArrayList<>();

        comuni.add(new Municipality("Venezia", 45.4375466, 12.3289983, "Comune di Venezia", "000066862"));
        comuni.add(new Municipality("Milano", 45.4628327, 9.1075204, "Comune di Milano", "800000013"));

        comuni.add(new Municipality("Torino", 45.0735885, 7.6053946, "Comune di Torino", "000098618"));
        comuni.add(new Municipality("Bologna", 44.4992191, 11.2614736, "Comune di Bologna", "000250878"));

        comuni.add(new Municipality("Genova", 44.4471368, 8.7504034, "Comune di Genova", "000164848"));
        comuni.add(new Municipality("Firenze", 43.7800606, 11.1707559, "Comune di Firenze", "800000038"));

        comuni.add(new Municipality("Roma", 41.9102411, 12.3955688, "Comune di Roma", "800000047"));
        comuni.add(new Municipality("Napoli", 40.854042, 14.1763903, "Comune di Napoli", "000708829"));

        comuni.add(new Municipality("Palermo", 38.1406577, 13.2870764, "Comune di Palermo", "800000060"));
        comuni.add(new Municipality("Cagliari", 39.2254656, 9.0932726, "Comune di Cagliari", "000021556"));

        //Ora che MainActivity è pronta, mostro il fragment della mappa.
        setupMapFragment(mapFragment);
        setContentFragment(R.id.contentFrame,mapFragment);
    }

    private void setupMapFragment(MapFragment fragment) {
        fragment.setParentActivity(this);
        fragment.setUniversities(uni);
        fragment.setSuppliers(fornitori);
        fragment.setMunicipalities(comuni);
    }


    private void setContentFragment(int container, Fragment fragment) {
        fManager.beginTransaction().replace(container,fragment,fragment.getTag()).commit();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "location service connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "location service connection suspended");
        Toast.makeText(this, R.string.conn_suspended, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "location service connection lost");
        Toast.makeText(this, R.string.conn_failed, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permissions granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                } else {
                    Log.e(TAG, "permissions not granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                    Snackbar.make(this.findViewById(R.id.map), R.string.msg_permissions_not_granted, Snackbar.LENGTH_LONG);
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_with_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MENU_SETTINGS:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.MENU_INFO:
                startActivity(new Intent(this, InfoActivity.class));
                break;
        }
        return false;
    }
    /**
     * Quando arriva un Intent viene eseguito questo metodo.
     * Può essere esteso e modificato secondo le necessità.
     *
     * @see Activity#onActivityResult(int, int, Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // inserire codice qui
                        break;
                    case Activity.RESULT_CANCELED:
                        // o qui
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Applica le impostazioni (preferenze) della mappa ad ogni chiamata.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int id = this.getBaseContext().getResources().getInteger(R.integer.id_notification);
        mNotificationManager.cancel(id);
    }

    /**
     * Pulisce la mappa quando l'app viene distrutta.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public MenuItem getActiveItem() {
        this.bnv = (BottomNavigationView) findViewById(R.id.navigation);
        return (bnv.getMenu().findItem(bnv.getSelectedItemId()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.mapFragment.changeMenuSelection(item); // TODO: temporaneo!
        /*bisogna controllare quale fragment è attivo al momento e agire di conseguenza*/
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(MainActivity.SAVE_SUPPLY, this.fornitori);
        super.onSaveInstanceState(savedInstanceState);
    }
}
