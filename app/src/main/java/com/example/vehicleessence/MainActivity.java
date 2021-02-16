package com.example.vehicleessence;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);

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

    public JSONObject createJsonTicket(int id, String date, String kms, String liters, String cost, String place){
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

    public void onClickSave(View view){
        int i = 0;
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
            Log.d(TAG, "Coucou");
        }
        catch (Exception e) {
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

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-YYYY_HH:mm:ss_aaa");
        String dateString = date.format(this.date);

        String fileName = dateString + "_" + this.editPlace.getText().toString() + ".json";
        Log.d(TAG, fileName);
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
            Log.d(TAG, "COucou");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}