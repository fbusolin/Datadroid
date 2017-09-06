package it.unive.dais.cevid.lib.experimental.comm;

import it.unive.dais.cevid.lib.experimental.IActivity;
import it.unive.dais.cevid.lib.util.Function;

import java.io.Serializable;

public abstract class Command<Receiver extends IActivity<?>>
        implements Function<Receiver, Void>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public abstract Void eval(Receiver o);

}
