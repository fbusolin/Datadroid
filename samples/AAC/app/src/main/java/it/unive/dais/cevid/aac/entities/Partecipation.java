package it.unive.dais.cevid.aac.entities;

import java.io.Serializable;

import it.unive.dais.cevid.aac.util.PartecipazioniParser;

/**
 * Created by fbusolin on 15/11/17.
 */

public class Partecipation implements Serializable {
    String nomePartecipante;
    String esito;
    String idLotto;
    String partita_iva;
    String nomeLotto;

    public Partecipation(PartecipazioniParser.Data data){
        this.esito = data.getEsito();
        this.idLotto = data.getId_lotto();
        this.partita_iva = data.getPiva();
        this.nomeLotto = data.getNome_lotto();
        this.nomePartecipante = data.getNome_partecipante();
    }

    public String getEsito() {
        return esito;
    }

    public String getLotto() {
        return idLotto;
    }

    public String getPiva() {
        return partita_iva;
    }

    public String getNomePartecipante() {
        return nomePartecipante;
    }

    public String getNomeLotto() {
        return nomeLotto;
    }
}
