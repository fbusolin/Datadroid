package it.unive.dais.cevid.aac.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import it.unive.dais.cevid.aac.R;
import it.unive.dais.cevid.aac.entities.Partecipation;

/**
 * Created by fbusolin on 16/11/17.
 */

public class PartecipationAdapter extends RecyclerView.Adapter<PartecipationAdapter.PartecipationItem> {
    private List<Partecipation> data;

    public PartecipationAdapter(List<Partecipation> data){
        this.data = data;
    }
    @Override
    public PartecipationItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partecipation_element, parent, false);
        return new PartecipationItem(itemView);
    }

    @Override
    public void onBindViewHolder(PartecipationItem holder, int position) {
        holder.esito.setText(data.get(position).getEsito());
        holder.iva.setText(data.get(position).getPiva());
        holder.nome.setText(data.get(position).getNomePartecipante());
        holder.lotto.setText(data.get(position).getNomeLotto());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PartecipationItem extends RecyclerView.ViewHolder{
        TextView iva,esito,lotto,nome;

        public PartecipationItem(View itemView) {
            super(itemView);
            iva = (TextView) itemView.findViewById(R.id.text_part_iva);
            nome = (TextView) itemView.findViewById(R.id.text_part_nome);
            esito = (TextView) itemView.findViewById(R.id.text_part_esito);
            lotto = (TextView) itemView.findViewById(R.id.text_part_lotto);
        }
    }
}
