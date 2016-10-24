package br.com.virtz.www.locavirtzapp.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.virtz.www.locavirtzapp.DetalheEventoActivity;
import br.com.virtz.www.locavirtzapp.R;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;

public class AlertaEvento extends DialogFragment{

    private EventoBean evento = null;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alerta!");
        Bundle args = getArguments();
        evento = (EventoBean) args.get("EVENTO");
        builder.setMessage(evento.getTitulo());

        builder.setPositiveButton("Quero ver!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent detalheIntent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    detalheIntent = new Intent(getContext(), DetalheEventoActivity.class);
                    detalheIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    detalheIntent.putExtra("EVENTO", evento);
                    startActivity(detalheIntent);
                }

                dismiss();
            }
        });

        builder.setNegativeButton("Não, obrigado.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

}
