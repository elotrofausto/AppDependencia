//package com.example.vesprada.appdependencia.SyncDBTask;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.lang.ref.WeakReference;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//
//public class U5T9HttpClientActivity extends AppCompatActivity implements View.OnClickListener{
//
//    //constants
//    private final static String URL_GEONAMES = "http://api.geonames.org/wikipediaSearchJSON";
//    private final static String USER_NAME = "elotrofausto";
//    private final static String LANGUAGE = "ES";
//    private final static int ROWS = 10;
//
//    //attributes
//    private EditText etPlaceName;
//    private Button btSearch;
//    private ListView lvSearchResult;
//    private ArrayList<String> listSearchResult;
//    private GetHttpDataTask getHttpDataTask;
//    private GetHttpWeatherTask weather;
//    private static ProgressBar pb;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_u5_t9_http_client);
//
//        setUI();
//    }
//
//    private void setUI() {
//        etPlaceName = findViewById(R.id.etPlaceName);
//        btSearch = findViewById(R.id.btSearch);
//        btSearch.setOnClickListener(this);
//
//        //ProgressBar
//        pb = findViewById(R.id.progressBar);
//
//        //create an empty ArrayList
//        listSearchResult = new ArrayList<>();
//
//        lvSearchResult = findViewById(R.id.lvSearchResult);
//
//        //set adapter to listView
//        lvSearchResult.setAdapter(new ArrayAdapter<>( this,
//                android.R.layout.simple_list_item_1,
//                listSearchResult));
//
//        //OnItemClickListener
//        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //Retrieve object data
//                GeonamesPlace object = (GeonamesPlace) parent.getAdapter().getItem(position);
//
//                //Check previous AsyncTask and cancel it if necessary
//                cancelTask(weather);
//
//                //Create URL and call API
//                weather = new GetHttpWeatherTask(getApplicationContext());
//                URL weatherUrl = null;
//                try {
//                    weatherUrl = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + object.getLat() + "&lon=" + object.getLng() + "&units=metric" + "&appid=99ad00ddba52639bcc32792555c402a6");
//                    weather.execute(weatherUrl);
//                } catch (MalformedURLException e) {
//                    Toast.makeText(getApplicationContext(),"URL bad format: " + weatherUrl, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        if (isNetworkAvailable()){
//            String place = etPlaceName.getText().toString();
//
//            if (!place.isEmpty()){
//                URL url;
//                try{
//                    url = new URL(URL_GEONAMES + "?q=" + place + "&maxrows=" + ROWS + "&lang=" + LANGUAGE + "&username=" + USER_NAME);
//
//                    //Check previous AsyncTask and cancel it if necessary
//                    cancelTask(getHttpDataTask);
//
//                    //start new AsyncTask
//                    getHttpDataTask = new GetHttpDataTask(lvSearchResult);
//                    getHttpDataTask.execute(url);
//                }catch(MalformedURLException e){
//                    Log.i("URL", e.getMessage());
//                }
//            }else Toast.makeText(this,"Write a place to search", Toast.LENGTH_LONG);
//        }else Toast.makeText(this, "Sorry, network is not available", Toast.LENGTH_LONG);
//        hideKeyB();
//    }
//
//    public void cancelTask(AsyncTask task){
//        if (task != null){
//            if (task.getStatus().equals(AsyncTask.Status.PENDING) || task.getStatus().equals(AsyncTask.Status.RUNNING)){
//                task.cancel(true);
//            }else{
//                Log.e("onDestroy()", " ASYNCTASK " + task.getClass().getSimpleName() + " = NULL, was not canceled");
//            }
//        }
//    }
//
//    //check if network is available
//    public boolean isNetworkAvailable(){
//        Boolean networkAvailable = false;
//
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (cm != null){
//            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//
//            if (networkInfo != null && networkInfo.isConnected()){
//                networkAvailable = true;
//            }
//        }
//        return networkAvailable;
//    }
//
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        cancelTask(getHttpDataTask);
//        cancelTask(weather);
//    }
//
//    public void hideKeyB(){
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()){
//            View view = getCurrentFocus();
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    //static class for AsyncTask
//    private static class GetHttpDataTask extends AsyncTask<URL, Void, ArrayList<GeonamesPlace>> {
//
//        private final int CONNECTION_TIMEOUT = 15000;
//        private final int READ_TIMEOUT = 10000;
//
//        //Weak reference
//        private final WeakReference<ListView> listViewWeakReference;
//
//        public GetHttpDataTask(ListView listView) {
//            listViewWeakReference = new WeakReference<>(listView);
//        }
//
//        //Runs in background Thread
//        @Override
//        protected ArrayList<GeonamesPlace> doInBackground(URL... urls) {
//            HttpURLConnection urlConnection = null;
//            ArrayList<GeonamesPlace> searchResult = new ArrayList<>();
//
//            try{
//                urlConnection = (HttpURLConnection) urls[0].openConnection();
//                //tell to close always underlying socket, unless run into StrictMode Violations
//                urlConnection.setRequestProperty("Connection", "close");
//
//                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//                urlConnection.setReadTimeout(READ_TIMEOUT);
//
//                //test if response was OK (200)
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
//                    //read data stream from url
//                    String resultStream = readStream(urlConnection.getInputStream());
//
//                    //Create JSON
//                    JSONObject json = new JSONObject(resultStream);
//                    JSONArray jArray = json.getJSONArray("geonames");
//
//                    if (jArray.length() > 0){
//                        //fill list with summary attribute data
//                        for (int i = 0; i < jArray.length(); i++){
//                            JSONObject item = jArray.getJSONObject(i);
//                            searchResult.add(new GeonamesPlace(item.getString("summary"),item.getString("lat"),item.getString("lng")));
//                        }
//                    }else{
//                        searchResult.add(new GeonamesPlace("No information found"));
//                    }
//                }else{
//                    Log.i("URL","ErrorCode: " + urlConnection.getResponseCode());
//                }
//            } catch (IOException e) {
//                Log.i("IOException", e.getMessage());
//            } catch (JSONException e) {
//                Log.i("IOException", e.getMessage());
//            }finally{
//                if (urlConnection != null) urlConnection.disconnect();
//            }
//
//            return searchResult;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pb.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<GeonamesPlace> searchResult){
//            //get the real reference to activity listView
//            ListView listView = listViewWeakReference.get();
//            if (listView != null){
//                if (searchResult != null && searchResult.size() > 0){
//                    //set the results to list adapter
//                    ArrayAdapter<GeonamesPlace> adapter = (ArrayAdapter<GeonamesPlace>) listView.getAdapter();
//                    adapter.clear();
//                    adapter.addAll(searchResult);
//                    adapter.notifyDataSetChanged();
//                }else{ //use if possible applicationContext()
//                    Toast.makeText(listView.getContext().getApplicationContext(),
//                            "Not possible to contact", Toast.LENGTH_LONG).show();
//                }
//                pb.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        private String readStream(InputStream in){
//            StringBuilder sb = new StringBuilder();
//            try{
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
//                String nextLine = "";
//                while ((nextLine = reader.readLine()) != null){
//                    sb.append(nextLine);
//                }
//            } catch (UnsupportedEncodingException e) {
//                Log.i("Encoding exception", e.getMessage());
//            } catch (IOException e) {
//                Log.i("IOException-ReadStream", e.getMessage());
//            }
//            return sb.toString();
//        }
//
//        @Override
//        protected void onCancelled(){
//            super.onCancelled();
//            Log.e("onCancelled", "ASYNCTASK " + this.getClass().getSimpleName() + ": I've been canceled and ready to GC clean");
//        }
//    }
//
//
//    //static class for AsyncTask
//    private static class GetHttpWeatherTask extends AsyncTask<URL, Void, ArrayList<String>> {
//
//        private final int CONNECTION_TIMEOUT = 15000;
//        private final int READ_TIMEOUT = 10000;
//        private Context context;
//
//
//        public GetHttpWeatherTask(Context context) {
//            this.context = context;
//        }
//
//        //Runs in background Thread
//        @Override
//        protected ArrayList<String> doInBackground(URL... urls) {
//            HttpURLConnection urlConnection = null;
//            ArrayList<String> searchResult = new ArrayList<>();
//
//            try{
//                urlConnection = (HttpURLConnection) urls[0].openConnection();
//                //tell to close always underlying socket, unless run into StrictMode Violations
//                urlConnection.setRequestProperty("Connection", "close");
//
//                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//                urlConnection.setReadTimeout(READ_TIMEOUT);
//
//                //test if response was OK (200)
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
//                    //read data stream from url
//                    String resultStream = readStream(urlConnection.getInputStream());
//
//                    //Create JSON
//                    JSONObject json = new JSONObject(resultStream);
//                    //JSONObject jObject = json.getJSONObject("weather");
//
//                    if (json.length() > 0){
//                        String description =  json.getJSONArray("weather").getJSONObject(0).getString("description");
//                        Double temp = json.getJSONObject("main").getDouble("temp");
//                        String humidity = json.getJSONObject("main").getString("humidity");
//                        searchResult.add("Description: " + description + " Temp: " + temp + " Humidity: " + humidity);
//
//                    }else{
//                        searchResult.add("No information found");
//                    }
//                }else{
//                    Log.i("URL","ErrorCode: " + urlConnection.getResponseCode());
//                }
//            } catch (IOException e) {
//                Log.i("IOException", e.getMessage());
//            } catch (JSONException e) {
//                Log.i("IOException", e.getMessage());
//            }finally{
//                if (urlConnection != null) urlConnection.disconnect();
//            }
//
//            return searchResult;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pb.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<String> searchResult){
//            //get the real reference to activity listView
//            Toast.makeText(context, searchResult.get(0), Toast.LENGTH_LONG).show();
//            pb.setVisibility(View.INVISIBLE);
//        }
//
//        private String readStream(InputStream in){
//            StringBuilder sb = new StringBuilder();
//            try{
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
//                String nextLine = "";
//                while ((nextLine = reader.readLine()) != null){
//                    sb.append(nextLine);
//                }
//            } catch (UnsupportedEncodingException e) {
//                Log.i("Encoding exception", e.getMessage());
//            } catch (IOException e) {
//                Log.i("IOException-ReadStream", e.getMessage());
//            }
//            return sb.toString();
//        }
//
//        @Override
//        protected void onCancelled(){
//            super.onCancelled();
//            Log.e("onCancelled", "ASYNCTASK " + this.getClass().getSimpleName() + ": I've been canceled and ready to GC clean");
//        }
//    }
//}