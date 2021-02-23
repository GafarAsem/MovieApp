package com.movies.movieapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    //private DatabaseReference mDatabase;
    static DatBase db;
    static arrayAdapter Ad;

    static ArrayList<String> idText;
    static ArrayList<String> nameMovie;
    static ArrayList<String> story;
    static ArrayList<String> publish;
    static ArrayList<String> rating;
    static ArrayList<String> poster;

    static ListView listMovies;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Getpermission();
        db=new DatBase(this);

        listMovies=findViewById(R.id.listMovies);
        registerForContextMenu(listMovies);
        updateID();
        Update();
        listMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String Story = story.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true);
            builder.setTitle("قصة الفلم");
            builder.setMessage(Story);
            builder.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("حسنا", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
});




    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("خيارات إضافية");
        menu.add(0,v.getId(),0,"تعديل");
        menu.add(0,v.getId(),0,"حذف");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() == "حذف") {
            try {
               // updateID();
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index = info.position;
                View view = info.targetView;
                ;

                TextView textView = view.findViewById(R.id.idText);

                String filename = db.GetAllMovies(db.CO[5]).get(index);
                db.DeleteData(textView.getText().toString());
                if (DeleteFile(filename)) {
                    Toast.makeText(this, "تم حذف الفيلم", Toast.LENGTH_SHORT).show();
                }

                Update();


            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void updateID() {
        ArrayList<String> Movies=db.GetAllMovies(db.CO[1]);
        ArrayList<String> Ids=db.GetAllMovies(db.CO[0]);
        int size=Ids.size();
       // int position=Movies.indexOf(NameMovie);
        for (int i = 0; i <=size-1 ; i++) {
            db.updateID(String.valueOf(i+1),Movies.get(i));
        }


    }

    public void AddActivityMovie(View view){
        Intent i=new Intent(this,AddMovie_Activity.class);
        startActivity(i);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void Update(){
        idText=db.GetAllMovies(db.CO[0]);
        nameMovie=db.GetAllMovies(db.CO[1]);
        story=db.GetAllMovies(db.CO[2]);
        publish=db.GetAllMovies(db.CO[3]);
        rating=db.GetAllMovies(db.CO[4]);
        poster=db.GetAllMovies(db.CO[5]);

        Ad = new arrayAdapter(MyAppliction.getAppContext(),idText, nameMovie, publish, rating, poster);

        listMovies.setAdapter(Ad);





    }

    public boolean DeleteFile(String pathFile){
        boolean deleted;
        try{
            File dir = new File(pathFile) ;
            //File file = new File(dir, pathFile);
             deleted = dir.delete();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
             deleted=false;
        }
        return deleted;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean Getpermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            return false;

        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            return false;
        }
        else{
            return true;
        }
    }


}