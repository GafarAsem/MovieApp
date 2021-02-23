package com.movies.movieapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AddMovie_Activity extends AppCompatActivity {
    EditText nameMovie;
    EditText storyText;
    EditText publishText;
    double Rating=7.0;
    Bitmap posterimage;

    SeekBar BarRating;
    TextView RateText;
    ImageView posterView;
    TextView waitText;

    TextView idText;
    Button addbtn ;
    Button closebtn;
    Uri filepath=null;

    DatBase db=new DatBase(this);
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        Getpermission();


        nameMovie=findViewById(R.id.UpdatenameMovieText);
        storyText=findViewById(R.id.UpdatestoryText);
        publishText=findViewById(R.id.UpdatepublishText);

        BarRating=findViewById(R.id.UpdateBarRating);
        RateText=findViewById(R.id.RateText);
        waitText=findViewById(R.id.waitText);


        posterView=findViewById(R.id.UpdateposterView);

        addbtn=findViewById(R.id.Update_btn);
        closebtn =findViewById(R.id.Updateclose_btn);
        idText=findViewById(R.id.idAddMovie);

            //ArrayList<String> Ides = db.GetAllMovies(db.CO[0]);Integer.parseInt(Ides.get(Ides.size() - 1)) + 1
         int i =db.GetlastMovie()+1;
         idText.setText(String.valueOf(i));


        changeText();
        BarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ChangRating();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }
    public void ChangRating(){
         Rating=BarRating.getProgress()/2.0;
        RateText.setText("التقييم   "+Rating);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getImage(View view){

        if(Getpermission()){
            Intent i = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_FIRST_USER);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_FIRST_USER && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            posterView = findViewById(R.id.UpdateposterView);
            posterView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            posterimage=BitmapFactory.decodeFile(picturePath);

        }
    }

    public void close(View view){


        AddMovie_Activity.this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addFilm(View view)
    {
         filepath= saveImage();

         if(!filepath.equals(null)) {
             try {
                 int i = Integer.parseInt(publishText.getText().toString());
                 try {
                     waitText.setVisibility(View.VISIBLE);
                     boolean result = db.insertFilm(
                             Integer.parseInt(idText.getText().toString()),
                             nameMovie.getText().toString(),
                             storyText.getText().toString(),
                             publishText.getText().toString(),
                             String.valueOf(Rating),
                             filepath.toString());
                     waitText.setVisibility(View.VISIBLE);

                     if (result) {


                         Toast.makeText(this, "تم إضافة الفيلم بنجاح", Toast.LENGTH_SHORT).show();
                         AddMovie_Activity.this.finish();
                     } else Toast.makeText(this, "خطأ ,أعد المحاولة ", Toast.LENGTH_SHORT).show();
                 } catch (Exception e) {
                     Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
                 
                 MainActivity.Update();
             } catch (Exception e) {
                 Toast.makeText(this, "التاريخ يجب ان يتكون من أرقام فقط مثال 2010", Toast.LENGTH_LONG).show();

             }

// waitText.setVisibility(View.INVISIBLE);
             //addbtn.setVisibility(View.VISIBLE);
         }
         else Toast.makeText(this, "خطأ ,أعد المحاولة ", Toast.LENGTH_SHORT).show();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public Uri saveImage(){
        Uri savedImageURI = null;
        if(Getpermission()&& savedImageURI != null);
        {


            Drawable drawable = posterView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();


            File file;
            String fileName = nameMovie.getText().toString() + publishText.getText().toString();
            String path = Environment.getExternalStorageDirectory().toString();
            file = new File(path, fileName + ".jpg");
            try {

                OutputStream stream = null;

                stream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.flush();
                stream.close();
                savedImageURI = Uri.parse(file.getAbsolutePath());
                Toast.makeText(this, "Image saved in external storage.\n" + savedImageURI, Toast.LENGTH_SHORT).show();
                return savedImageURI;
            } catch (IOException e) // Catch the exception
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

            //iv_saved.setImageURI(savedImageURI);

        return  savedImageURI;
    }


    public void notNull(){
        if( !nameMovie.getText().toString().equals("")&&
                !storyText.getText().toString().equals("")&&
                !publishText.getText().toString().equals("")&&
                !String.valueOf(BarRating.getProgress()).equals("")){

            addbtn.setVisibility(View.VISIBLE);
        }
        else{
            addbtn.setVisibility(View.INVISIBLE);
        }
    }
    private void changeText(){
        nameMovie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notNull();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        storyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notNull();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        publishText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notNull();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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