package it.unive.dais.cevid.datadroid.lib.parser;

import android.os.AsyncTask;

import java.util.List;

public interface AsyncParser<Data, Progress> extends Parser<Data> {
    AsyncTask<Void, Progress, List<Data>> getAsyncTask();
}
