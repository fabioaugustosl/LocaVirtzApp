package br.com.virtz.www.locavirtzapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.virtz.www.locavirtzapp.R;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;



public class EventoAdapter extends ArrayAdapter<EventoBean> {

    public EventoAdapter(Context context, List<EventoBean> lista) {
        super(context, 0, lista);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.evento_lista, parent, false);
        }

        EventoBean evento = getItem(position);

        TextView titulo = (TextView) itemView.findViewById(R.id.txt_titulo);
        titulo.setText(evento.getTitulo());

        TextView data = (TextView) itemView.findViewById(R.id.txt_descricao);
        data.setText(evento.getTextoDescricao());

        return itemView;
    }
}
