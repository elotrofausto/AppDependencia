package com.example.vesprada.appdependencia.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CreateIntent {

    private static final String TIPOLLAMADA = "tipoLlamada";

    private Context context;
    private Intent intent;
    private String tipo;

    public CreateIntent (Context Context, Intent Intent, String Tipo){
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
