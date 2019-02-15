package com.example.vesprada.appdependencia.Utils;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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
    private final String METHOD = "GET";

    private String DOC;//nombre del informe que deseamos
    private String FILE;//nombre del fichero donde escribir
    private String DependentDNI;
    private URL url;
    private HttpURLConnection conection;
    private Context context;
    private OutputStream fos;

    public PdfFromXmlFile (String file, int document, String dni, Context context) throws IOException {

        FILE = file;
        DependentDNI = dni;
        this.context = context;

        if(document==1){
            DOC = "InformeMedicamentoList";
        } else {
            DOC = "InformeAvisosList";
        }

        setUI();

    }

    private void setUI() throws IOException {

        int responseCode;
        File pdfFile = new File(context.getFilesDir(),FILE + ESPANSE);
        Log.i("RUTA PDF-", context.getFilesDir().toString() + "/"+FILE +ESPANSE);
        fos = new FileOutputStream(pdfFile);
        url = new URL(INIT + HOST + PORT + REPOSITORY + DOC + ESPANSE + "?" + USER + "&" + PASS + "&" + DNI + DependentDNI);

        /*conection = (HttpURLConnection) url.openConnection();
        conection.setRequestMethod(METHOD);
        responseCode =  conection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            InputStream is = conection.getInputStream();
            while(is.available()>0){
                fos.write(is.read());
                fos.flush();
            }
        }*/

    }

    public OutputStream getFos(){
        return fos;
    }

    public URL getUrl(){
        return url;
    }

    public String getMETHOD(){
        return METHOD;
    }



}
