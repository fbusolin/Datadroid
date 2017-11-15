package it.unive.dais.cevid.aac.util;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.unive.dais.cevid.datadroid.lib.parser.AbstractAsyncParser;
import it.unive.dais.cevid.datadroid.lib.util.ProgressStepper;

/**
 * Sottoclasse di {@code AbstractAsyncParser} che implementa un downloader e parser per il sito http://soldipubblici.thefool.it/
 * Questa classe &egrave; usabile direttamente e non necessita di essere ereditata.
 * Non richiede il generic FiltrableData perch&eacute; utilizza una classe innestata apposita per rappresentare i dati.
 * La classe {@link ReloadedParser.Data} ha a sua volta due sottoclassi {@link ReloadedParser.Comune} e
 * {@link ReloadedParser.Siope} che rappresentano i due possibili dati trovabili nel sito.
 * In fase di scraping viene individuato il tipo corretto tra i due in base al contenuto della pagina.
 * Un esempio d'uso
 * <blockquote><pre>
 * {@code
 * {@link ReloadedParser scraper = new ReloadedParser("comuni",5);
 * List<{@link ReloadedParser.Data}> data = scraper.parse();
 * for(Data d : data){
 *     //do something
 * }
 * }
 * List<SoldipubbliciParser.FiltrableData> rows = parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
 * for (CsvRowParser.Row row : rows) {
 *     String id = row.get("ID"), nome = row.get("NAME");
 *     // fai qualcosa con id e nome
 * }
 * }
 * </pre></blockquote>
 * @author fbusolin
 *
 *     */

public class ReloadedParser extends AbstractAsyncParser<ReloadedParser.Data, ProgressStepper> {
    private static final String TAG = "ReloadedParser";
    private static final String home = "http://soldipubblici.thefool.it/";
    String url;
    int depth;

    public ReloadedParser(String query, int depth){
        this.url = home + query;
        this.depth = depth;
    }

    public List<Data> scrape(){
        ProgressStepper progress = new ProgressStepper(depth);
        List<Data> data = new ArrayList<>();
        for(int level = 0; level < this.depth;level++){
            Connection connection = Jsoup.connect(url + "/" + level);
            Document document = null;
            try{
                document = connection.get();
            }catch(IOException ex){
                Logger.getLogger(ReloadedParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(document == null)return data;
            Elements contentTable = document.select(".table-hover");
            Elements rows = contentTable.select("tr");
            ProgressStepper parseStepper = progress.getSubProgressStepper(rows.size() - 1);
            for(Element elm : rows.subList(1,rows.size())){
                StringBuilder texts = new StringBuilder();
                Elements tokens = elm.select("td");
                for(Element piece: tokens){
                    texts.append(piece.text()).append(";");
                }
                switch(texts.charAt(0)){
                    case 'C':
                        data.add(produceComuni(texts.toString()));
                        break;
                    case 'S':
                        data.add(produceSiopi(texts.toString()));
                        break;
                }
                parseStepper.step();
            }
            progress.step();
            publishProgress(progress);
        }
        return data;

    }
    private Siope produceSiopi(String str){
        String[] tokens = str.split(";");
        Siope result = new Siope();
        result.codice = tokens[0];
        result.descrizione = tokens[1];
        result.pagamento = tokens[2];
        result.procapite = tokens[3];
        result.percentuale = tokens[4];
        return result;
    }
    private Comune produceComuni(String str){
        String[] tokens = str.split(";");
        // System.out.println(""+Arrays.toString(tokens));
        Comune result = new Comune();
        result.codice = tokens[0];
        result.nome = tokens[1];
        result.spesaTotale = tokens[2];
        result.spesaProcapite = tokens[3];
        return result;
    }

    @NonNull
    @Override
    public List<Data> parse() throws IOException {
        return scrape();
    }

    public class Data implements Serializable{String codice;}

    public class Comune extends Data{
        String nome,
                spesaTotale,
                spesaProcapite;
    }

    public class Siope extends Data{
        String descrizione,
                pagamento,
                procapite,
                percentuale;
    }
}
