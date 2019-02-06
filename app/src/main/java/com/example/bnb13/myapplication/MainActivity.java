package com.example.bnb13.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String url_Provice = "", url_Amphur = "", url_District = "";

    private ArrayList<String> arrProvince = new ArrayList<>();
    private ArrayList<String> arrProvinceID = new ArrayList<>();

    private ArrayList<String> arrAmphur = new ArrayList<>();
    private ArrayList<String> arrAmphurID = new ArrayList<>();

    private ArrayList<String> arrDistrict = new ArrayList<>();
    private ArrayList<String> arrDistrictID = new ArrayList<>();

    private ArrayAdapter<String> adpProvince, adpAmphur, adpDistrict;
    private Spinner spProvince, spAmphur, spDistrict;
    private int rubIDprovince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spProvince = (Spinner) findViewById(R.id.spinProvince);
        spAmphur = (Spinner) findViewById(R.id.spinAmphur);
        spDistrict = (Spinner) findViewById(R.id.spinDistrict);


        adpProvince = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrProvince);
        spProvince.setAdapter(adpProvince);

        adpAmphur = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrAmphur);
        spAmphur.setAdapter(adpAmphur);

        adpDistrict = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrDistrict);
        spDistrict.setAdapter(adpDistrict);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new DataProvince().execute();
        new DataAmphur().execute("1");
        //new DataDistrict().execute("1", "1");
    }

    public class DataProvince extends AsyncTask<String, Void, String> {

        String result;
        ArrayList<String> listprovice;
        ArrayList<String> listprovinceid;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_LONG).show();
            listprovice = new ArrayList<>();
            listprovinceid = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            url_Provice = "http://192.168.1.30/android/php/selectprovince.php";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url_Provice)
                    .build();

            try {
                Response response = client.newCall(request)
                        .execute();

                result = response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    listprovice.add(jsonObject.getString("thai"));
                    listprovinceid.add(jsonObject.getString("pid"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            arrProvince.addAll(listprovice);
            arrProvinceID.addAll(listprovinceid);
            adpProvince.notifyDataSetChanged();


            spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spProvince.getSelectedItem() != null) {
                        new DataAmphur().execute(listprovinceid.get(position));
                        rubIDprovince = Integer.parseInt(listprovinceid.get(position));
                        arrDistrict.clear();
                        arrAmphur.clear();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    public class DataAmphur extends AsyncTask<String, Void, String> {

        String result;
        private ArrayList<String> listamphur;
        private ArrayList<String> listamphurid;

        @Override
        protected void onPreExecute() {
            listamphur = new ArrayList<>();
            listamphurid = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            url_Amphur = "http://192.168.1.30/android/php/selectdistrict.php";

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("pid", strings[0])
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url_Amphur)
                    .post(requestBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                result = response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    listamphurid.add(jsonObject.getString("did"));
                    listamphur.add(jsonObject.getString("thai"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            arrAmphur.addAll(listamphur);
            arrAmphurID.addAll(listamphurid);
            adpAmphur.notifyDataSetChanged();

            spAmphur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spAmphur.getSelectedItem()!=null) {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
