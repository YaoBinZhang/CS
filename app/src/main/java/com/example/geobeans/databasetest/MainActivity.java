package com.example.geobeans.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
        final Button create_database=(Button) findViewById(R.id.create_database);
        create_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.getWritableDatabase();

            }
        });
        Button add_data=(Button) findViewById(R.id.add_data);
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                contentValues.put("name","JAVA 设计模式");
                contentValues.put("author","Zhang san");
                contentValues.put("pages",454);
                contentValues.put("price",19.56);
                db.insert("Book",null,contentValues);
                contentValues.clear();
                contentValues.put("name","C++");
                contentValues.put("author","Li si");
                contentValues.put("pages",264);
                contentValues.put("price",15.56);
                db.insert("Book",null,contentValues);

            }
        });

        Button update_data=(Button) findViewById(R.id.update_data);
        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues contentValues=new ContentValues();

                contentValues.put("price",30.56);
                db.update("Book",contentValues,"name=?",new String[]{"C++"});

            }
        });


        Button delete_data=(Button) findViewById(R.id.delete_data);
        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.delete("Book","pages>?",new String[]{"400"});

            }
        });

        Button query_data=(Button) findViewById(R.id.query_data);
        query_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                Cursor cursor=db.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author=cursor.getString(cursor.getColumnIndex("author"));
                        int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.e("cursor:","name:"+name+",author:"+author+",pages:"+pages+",price:"+price);


                    }while (cursor.moveToNext());

                }
                cursor.close();

            }
        });


    }
}
