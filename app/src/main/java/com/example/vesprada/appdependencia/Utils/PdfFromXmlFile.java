package com.example.vesprada.appdependencia.Utils;

import android.util.Log;
import java.net.MalformedURLException;
import java.net.URL;

public class PdfFromXmlFile {

    private final String INIT = "http:";
    private final String HOST = "//149.202.8.235";
    private final String PORT = ":8080";
    private final String REPOSITORY = "/jasperserver/rest_v2/reports/reports/grup2/";
    private final String ESPANSE = ".pdf";
    private final String USER = "j_username=grup2";
    private final String PASS = "j_password=Grupo-312";
    private final String DNI = "user=";

    private String DOC;//nombre del informe que deseamos
    private String DependentDNI;
    private URL url;

    public PdfFromXmlFile (int document, String dni){

        DependentDNI = dni;

        if(document==1){
            DOC = "InformeMedicamentoList";
        } else {
            DOC = "InformeAvisosList";
        }

        try {
            url = new URL(INIT + HOST + PORT + REPOSITORY + DOC + ESPANSE + "?" + USER + "&" + PASS + "&" + DNI + DependentDNI);
        } catch (MalformedURLException e) {
            Log.e("ERROR:", e.getMessage());
        }

    }

    public URL getUrl(){
        return url;
    }

}
