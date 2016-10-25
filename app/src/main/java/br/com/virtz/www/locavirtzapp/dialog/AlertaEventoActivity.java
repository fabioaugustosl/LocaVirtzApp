package br.com.virtz.www.locavirtzapp.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import br.com.virtz.www.locavirtzapp.DetalheEventoActivity;
import br.com.virtz.www.locavirtzapp.R;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;



public class AlertaEventoActivity extends Activity {

    private EventoBean eventoAlerta = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alerta_evento);
        this.setFinishOnTouchOutside(false);

        eventoAlerta = (EventoBean) getIntent().getSerializableExtra("EVENTO");

        TextView txtAlerta = (TextView) findViewById(R.id.txt_titulo_alerta);
        txtAlerta.setText(eventoAlerta.getTitulo());

    }


    public void fechar(View v) {
        this.finish();
    }

    public void ver(View v){
        Intent detalheIntent = new Intent(getApplicationContext(), DetalheEventoActivity.class);
        detalheIntent.putExtra("EVENTO", eventoAlerta);
        startActivity(detalheIntent);
        this.finish();
    }

}
