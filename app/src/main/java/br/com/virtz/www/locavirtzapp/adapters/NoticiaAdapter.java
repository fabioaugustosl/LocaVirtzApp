package br.com.virtz.www.locavirtzapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.virtz.www.locavirtzapp.R;
import br.com.virtz.www.locavirtzapp.async.DownloadImageTask;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.beans.NoticiaBean;
import br.com.virtz.www.locavirtzapp.util.RecuperadorImagem;


public class NoticiaAdapter extends ArrayAdapter<NoticiaBean> {

    public NoticiaAdapter(Context context, List<NoticiaBean> lista) {
        super(context, 0, lista);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.noticia_lista, parent, false);
        }

        NoticiaBean noticia = getItem(position);

        TextView titulo = (TextView) itemView.findViewById(R.id.txt_titulo_noticia);
        titulo.setText(noticia.getTitulo());

        TextView descricao = (TextView) itemView.findViewById(R.id.txt_descricao_noticia);
        descricao.setText(noticia.getTitulo());

        if(noticia.getUrlImagem() != null && !"".equals(noticia.getUrlImagem().trim())){
            ImageView img = (ImageView) itemView.findViewById(R.id.imgNoticia);

            img.setVisibility(View.VISIBLE);

            new DownloadImageTask(img).execute(noticia.getUrlImagem());
        }

//        TextView data = (TextView) itemView.findViewById(R.id.txt_descricao);
//        data.setText(noticia.getTextoDescricao());

        return itemView;
    }
}
