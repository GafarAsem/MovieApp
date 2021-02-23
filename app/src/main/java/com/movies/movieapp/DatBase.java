package com.movies.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatBase extends SQLiteOpenHelper {

    public static String namedatabase ="database.db";
    public  String tableName="MoviesTable";
    public  String[] CO ={"id","namefilm","story","publish","rating","poster"};
    public DatBase(@Nullable Context context) {
        super(context, namedatabase, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+tableName+" ( "
                +CO[0]+" INTEGER PRIMARY KEY  , "
                +CO[1]+" TEXT , "
                +CO[2]+" TEXT , "
                +CO[3]+" TEXT , "
                +CO[4]+" TEXT , "
                +CO[5]+" TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
    }


    public boolean insertFilm(int id,String namefilm, String story, String publish, String rating, String poster ) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();


        contentValues.put(CO[0],id);
        contentValues.put(CO[1],namefilm);
        contentValues.put(CO[2],story);
        contentValues.put(CO[3],publish);
        contentValues.put(CO[4],rating);
        contentValues.put(CO[5],poster);

        long result=db.insert(tableName,null,contentValues);

        if (result==-1)
            return false;
        else
            return true;


    }

    public boolean updateFilm(String id,String namefilm, String story, String publish, String rating, String poster ) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();


        //contentValues.put(CO[0],id);
        contentValues.put(CO[1],namefilm);
        contentValues.put(CO[2],story);
        contentValues.put(CO[3],publish);
        contentValues.put(CO[4],rating);
        contentValues.put(CO[5],poster);

        long result=db.update(tableName,contentValues,CO[0]+" =?",new String[]{id});

        if (result==-1)
            return false;
        else
            return true;


    }

    public boolean updateID(String Newid,String namefilm) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();


        contentValues.put(CO[0],Newid);


        long result=db.update(tableName,contentValues,CO[1]+" =?",new String[]{namefilm});

        if (result==-1)
            return false;
        else
            return true;


    }

    public ArrayList<String> GetAllMovies(String RowName){

        ArrayList<String> ArrayData = new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor res =db.rawQuery("SELECT "+RowName+" FROM "+tableName,null);

        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                String name = res.getString(res.getColumnIndex(RowName));

                ArrayData.add(name);
                res.moveToNext();
            }
        }
        return ArrayData;
    }

    public int GetlastMovie(){

        int ArrayData = 0;
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor res =db.rawQuery("SELECT "+CO[0]+" FROM "+tableName,null);
        int name=0;
        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                 name = res.getInt(res.getColumnIndex(CO[0]));


                res.moveToNext();
            }
        }
        ArrayData=name;
        return ArrayData;
    }


    public boolean DeleteData(String idMovie){
        SQLiteDatabase db=this.getWritableDatabase();
        int result=db.delete(tableName,CO[0]+"=?",new String[]{idMovie});

        if (result==-1)
            return false;
        else
            return true;
    }


    public ArrayList<Bitmap> GetAllBitmaps()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<Bitmap> ArrayData = new ArrayList<Bitmap>();
        Cursor res =db.rawQuery("SELECT "+CO[5]+" FROM "+tableName,null);

        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                byte[] imgByte = res.getBlob(res.getColumnIndex(CO[5]));

                ArrayData.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                res.moveToNext();
            }
        }



        return ArrayData;

    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
