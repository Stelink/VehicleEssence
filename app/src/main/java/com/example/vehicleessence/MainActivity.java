package com.example.vehicleessence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends  AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    private String TAG = MainActivity.class.getSimpleName();
    private TextView tvAverageCons;
    private TextView tvTicket;
    public MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("start", "Start App");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tvTicket = findViewById(R.id.tvTicket);
        this.tvAverageCons = findViewById(R.id.tvAverageCons);
        fillRecyclerView();
        //======================
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public void consAverage(ArrayList<String> list){
        ArrayList<Double> arraySumCons = new ArrayList<>();

        for (String value : list){
            try {
                JSONObject json = convertStringToJson(value);
                String liters = (String) json.get("liters");
                String kms = (String) json.get("kms");
                double literParser = Double.parseDouble(liters);
                double kmsParser = Double.parseDouble(kms);
                arraySumCons.add(literParser / kmsParser * 100);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        double sum = 0.00;
        for (double value : arraySumCons){
            sum += value;
        }

        double averageCons = sum/list.size();

        this.tvAverageCons.setText(this.tvAverageCons.getText().toString() + Math.floor(averageCons*100)/100 + " L / 100 km");

    }

    /**
     * Parcours le tableau de json convertit en String
     * Récupère les différents élements des tickets pour les concaténer
     */
    public void fillRecyclerView(){
        // data to populate the RecyclerView with
        ArrayList<String> list = getAllDatas();
        ArrayList<String> list2 = new ArrayList<>();
        System.out.println(list);
        consAverage(list);

        for (String value : list){
            try {
                JSONObject json = convertStringToJson(value);
                String date = (String) json.get("date");
                String kms = (String) json.get("kms");
                String liters = (String) json.get("liters");
                String cost = (String) json.get("cost");
                String place = (String) json.get("place");

                double litersParser = Double.parseDouble(liters);
                double kmsParser = Double.parseDouble(kms);
                double average = litersParser / kmsParser * 100;
                list2.add(date + " | " + liters + "L | " + kms + "km | " + cost + "€ | " +
                        place + " | " + Math.floor(average*100)/100 + "L / 100 km");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvTickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, list2);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    public JSONObject convertStringToJson(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
        return new JSONObject();
    }

    public ArrayList<String> getAllDatas() {
        File[] files = getFilesList();
        ArrayList<String> jsonStrings = new ArrayList<>();

        if(files == null){
            jsonStrings.add("Aucun ticket créé pour le moment.");
            return jsonStrings;
        }

        for(int i = 0; i < files.length; i++){
            jsonStrings.add(readJsonFile(files[i]));
        }

        return jsonStrings;
    }

    public File[] getFilesList() {
        String path = Environment.getExternalStorageDirectory().toString()+"/Android/data/com.example.vehicleessence/files/private/";
        //Log.d("Files", "Path: " + path);
        File directory = new File(path);
        //Log.d("dir", directory.toString());
        File[] files = directory.listFiles();

        //System.out.println(files);

        if(files == null){
            return null;
        }
        //Log.d("Files", "Size: "+ files.length);

        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

        return files;
    }

    public String readJsonFile(File file){
        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }

    public void onClickAddTicket(View view) {
        try {
            Intent i = new Intent(MainActivity.this, AddTicket.class);
            startActivity(i);
        }catch(Exception e){
            Log.d(TAG, e.toString());
        }
    }
}