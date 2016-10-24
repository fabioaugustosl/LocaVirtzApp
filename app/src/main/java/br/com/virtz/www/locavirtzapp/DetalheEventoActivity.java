package br.com.virtz.www.locavirtzapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.dialog.AlertaEventoActivity;

public class DetalheEventoActivity extends LocaVirtzSuperActivity {


    private EventoBean evento  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_evento);

        Intent intent = getIntent();
        evento = (EventoBean) intent.getSerializableExtra("EVENTO");

        TextView txtTitulo = (TextView) findViewById(R.id.txt_det_titulo);
        txtTitulo.setText(evento.getTitulo());

        TextView txtDescricao = (TextView) findViewById(R.id.txt_det_descricao);
        txtDescricao.setText(evento.getTextoDescricao());


        if("VIDEO".equals(evento.getTipoEvento())) {
            ajustarBotaoVideo();
        }

        if("SITE".equals(evento.getTipoEvento())) {
            ajustarBotaoSite();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        initServiceBeacon(null);
    }

    private void ajustarBotaoSite() {
        Button botao = (Button) findViewById(R.id.btn_det_site);
        botao.setVisibility(View.VISIBLE);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(evento.getTexto()));
                startActivity(intent);
            }
        });
    }

    private void ajustarBotaoVideo() {
        Button botao = (Button) findViewById(R.id.btn_det_video);
        botao.setVisibility(View.VISIBLE);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(evento.getTexto()));
                startActivity(intent);
            }
        });
    }

    public void voltarMonitoramento(View view) {
        Intent intent = new Intent(this, MonitoramentoBeaconActivity.class);
        startActivity(intent);
    }
}
