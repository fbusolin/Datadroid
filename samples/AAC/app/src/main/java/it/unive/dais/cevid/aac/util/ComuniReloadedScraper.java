package it.unive.dais.cevid.aac.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Semplice scraper per il sito http://soldipubblici.thefool.it/comuni/0
 * dal quale si estraggono i dati dei comuni.
 *
 * TODO: renderlo un AsyncTask
 * TODO: generalizzare il numero di pagine da scaricare
 * Created by fbusolin on 08/11/17.
 */

public class ComuniReloadedScraper {
    final String base = "http://soldipubblici.thefool.it/";

    List<Data> scrape(String query){
        String key = "hidden-phone";
        List<Data> result = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(base + query).get();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if(doc != null){
            Elements rows = doc.select("tr");
            Elements rawElements = rows.select("td");
            List<String> content = rawElements.eachText();

            for(int i = 0; i < content.size();i+=4){
                Data data = new Data();

                data.nome = content.get(i);
                data.tag = content.get(i+1);
                data.spesaTotale = content.get(i+2);
                data.spesaProcapite = content.get(i+3);

                result.add(data);
            }
        }
        return result;
    }


    public class Data implements Serializable{
        String nome;
        String tag;
        String spesaTotale;
        String spesaProcapite;
    }
}
