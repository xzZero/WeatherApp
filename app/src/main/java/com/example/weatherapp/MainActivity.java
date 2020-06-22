package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef, dbRef_;
    private ArrayList<String> cities, avgTemp, temp;
    private ListView list;
    private Button btnAdd;
    private String out, newTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list_item);
        btnAdd = findViewById(R.id.btnAdd);


        cities = new ArrayList<>();
        avgTemp = new ArrayList<>();
        temp = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/location");
        dbRef.addValueEventListener(changeListener);

        if (!cities.isEmpty()){
            ListviewAdapter listviewAdapter = new ListviewAdapter(this, cities, avgTemp, temp);
            list.setAdapter(listviewAdapter);
        }


        btnAdd.setOnClickListener((View.OnClickListener) this);


//        dbRef_ = database.getReference("/location/Stuttgart/2020-06-02");
////        dbRef_.push().setValue("30");
////        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
////        Date date = new Date();
////        Log.d("Time", String.valueOf(formatter.format(date)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                insertBox();
//                out = "Neckar";
//                newTemp = "40";
//                Log.d("out", out);
//                Log.d("newTemp", newTemp);
                break;
        }
    }


    ValueEventListener changeListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            cities = new ArrayList<>();
            avgTemp = new ArrayList<>();
            temp = new ArrayList<>();

            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                // TODO: handle the post
                Log.d("City", String.valueOf(postSnapshot.getKey()));
                cities.add(String.valueOf(postSnapshot.getKey()));
                Map<String, HashMap<String, String>> test = (HashMap<String, HashMap<String, String>>) postSnapshot.getValue();
                List<Map> t = new ArrayList<Map>(test.values());
                if (t.size() > 0){
                    float sum = 0;
                    String formatted;
                    ArrayList<String> child = new ArrayList<String>(t.get(0).values());
                    Log.d("child", String.valueOf(t.get(0).values()));
                    Log.d("child", String.valueOf(child.get(0)));
//                    temp.add(String.valueOf(t.get(0).values()).replace("[","").replace("]", ""));
                    temp.add(String.valueOf(child.get(0)));
                    if (child.size() == 1 && String.valueOf(child.get(0)).isEmpty()){
                        formatted = "";
                    } else{
                        for (int i = 0; i < child.size(); i++){
//                        Log.d("Test-Child-Key", String.valueOf(tt.keySet()));
                            Log.d("Test-Child-value", String.valueOf(child.get(i)));
//                        sum += Float.parseFloat(String.valueOf(tt.values()).replace("[","").replace("]", ""));
                            sum += Float.parseFloat(String.valueOf(child.get(i)));
                            Log.d("Sum", String.valueOf(sum));
                        }
                        DecimalFormat df = new DecimalFormat("#.##");
                        formatted = df.format(sum/child.size());
                    }


                    avgTemp.add(formatted);
                }
                else{
                    temp.add("");
                    avgTemp.add("");
                }

            }
            ListviewAdapter listviewAdapter = new ListviewAdapter(getApplicationContext(), cities, avgTemp, temp);
            list.setAdapter(listviewAdapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private void insertBox(){
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.box_, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Your Title Here");
        alertDialog.setCancelable(false);



        final EditText etCity = (EditText) view.findViewById(R.id.etCity);
        final EditText etTemp = (EditText) view.findViewById(R.id.etTemp);
        out = "";
        newTemp = "";
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (isString(etCity.getText().toString())){
                    out = etCity.getText().toString();
                }
                if (isNumeric(etTemp.getText().toString())){
                    newTemp = etTemp.getText().toString();
                }
                if (out != "" && newTemp != ""){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    Log.d("Time", String.valueOf(formatter.format(date)));
                    String path = "/location/" + out + "/" + String.valueOf(formatter.format(date));
                    dbRef_ = database.getReference(path);
                    dbRef_.push().setValue(newTemp);
                }
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                out = "";
                newTemp = "";
            }
        });


        alertDialog.setView(view);
        alertDialog.show();

    }

    String nullFilter(String s){
        if (s.isEmpty()){
            return "";
        }
        return s;
    }

    public static boolean isNumeric(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static boolean isString(String str){
        if (str.isEmpty() || str == ""){
            return false;
        }
        return true;
    }
}
