package com.example.vehicleessence;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddTicket extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private EditText editDate;
    private EditText editKms;
    private EditText editLiters;
    private EditText editCost;
    private EditText editPlace;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        this.editDate = findViewById(R.id.editDate);
        this.editKms = findViewById(R.id.editKms);
        this.editLiters = findViewById(R.id.editLiters);
        this.editCost = findViewById(R.id.editCost);
        this.editPlace = findViewById(R.id.editPlace);

        this.date = new Date();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/YYYY");
        String dateString = date.format(this.date);
        this.editDate.setText(dateString);
        /*this.editCost.setText("78");
        this.editKms.setText("78");
        this.editPlace.setText("78");
        this.editLiters.setText("78");*/
    }

    public JSONObject createJsonTicket(int id, String date, String kms, String liters, String cost, String place) {
        JSONObject jsonTicket = new JSONObject();
        try {
            jsonTicket.put("id", id);
            jsonTicket.put("date", date);
            jsonTicket.put("kms", kms);
            jsonTicket.put("liters", liters);
            jsonTicket.put("cost", cost);
            jsonTicket.put("place", place);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonTicket;
    }

    public void onClickSave(View view) {
        int i = 0;

        ArrayList<EditText> etList = new ArrayList<>();
        etList.add(editDate);
        etList.add(editKms);
        etList.add(editPlace);
        etList.add(editCost);
        etList.add(editLiters);

        ArrayList<String> etText = new ArrayList<>();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/YYYY");
        String dateString = date.format(this.date);
        etText.add(dateString);

        etText.add("0");
        etText.add("0");
        etText.add("0");
        etText.add("0");

        checkInputs(etList, etText);

        try {
            JSONObject jsonTicket = createJsonTicket(
                    i++,
                    editDate.getText().toString(),
                    editKms.getText().toString(),
                    editLiters.getText().toString(),
                    editCost.getText().toString(),
                    editPlace.getText().toString()
            );
            System.out.println(jsonTicket.toString());
            writeFileExternalStorage(jsonTicket.toString());
            //Log.d(TAG, "Add ticket");

            onClickHome(null);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    public void checkInputs(ArrayList<EditText> views, ArrayList<String> textList){
        int i = 0;
        for(EditText view : views){
            String etText = view.getText().toString().trim();
            if(TextUtils.isEmpty(etText)){
                view.setText(textList.get(i));
            }
            i++;
        }
    }

    public void onClickHome(View view) {
        try {
            Intent i = new Intent(AddTicket.this, MainActivity.class);
            startActivity(i);
        }catch(Exception e){
            Log.d(TAG, e.toString());
        }
    }

    public void writeFileExternalStorage(String jsonString) {

        //Text of the Document
        String textToWrite = jsonString;

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

            //If it isn't mounted - we can't write into it.
            return;
        }

        SimpleDateFormat date = new SimpleDateFormat("dd_MM_YYYY___hh_mm_ss_aaa");
        String dateString = date.format(this.date);

        String fileName = dateString + "_" + this.editPlace.getText().toString() + ".json";
        //Create a new file that points to the root directory, with the given name:
        File file = new File(getExternalFilesDir("private"), fileName);

        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, true);

            outputStream.write(textToWrite.getBytes());
            outputStream.flush();
            outputStream.close();
            Log.d("AddTicketPath", file.getAbsolutePath());
            Log.d(TAG, file.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
