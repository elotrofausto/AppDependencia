package com.example.vesprada.appdependencia.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CreateRedButtonIntent {

    //Pequeña clase para crear el intennt de la Activity que llama al asistente tras pulsar el botón rojo
    //Se hace uso del putExtras para indicar el tipo de emergencia

    private static final String TIPOLLAMADA = "tipoLlamada";

    private Context context;
    private Intent intent;
    private String tipo;

    public CreateRedButtonIntent(Context Context, Intent Intent, String Tipo){
        context=Context;
        intent=Intent;
        tipo=Tipo;
    }

    public void lanzarActivity(){
        Bundle bundle = new Bundle();
        bundle.putString(TIPOLLAMADA, tipo);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
