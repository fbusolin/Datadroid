package it.unive.dais.cevid.aac.entities;

import it.unive.dais.cevid.aac.util.PartecipazioniParser;

/**
 * Created by admin on 15/11/17.
 */

public class Partecipation {
    String esito;
    String idLotto;
    String partita_iva;

    public Partecipation(PartecipazioniParser.Data data){
        this.esito = data.getEsito();
        this.idLotto = data.getId_lotto();
        this.partita_iva = data.getPiva();
    }
}
