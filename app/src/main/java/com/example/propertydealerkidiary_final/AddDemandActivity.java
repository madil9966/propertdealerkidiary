package com.example.propertydealerkidiary_final;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddDemandActivity extends AppCompatActivity {
    EditText etDate,etLoc,etPrice,etType;
    Button btnAddDem;

    String date,email;

    DBCreation dbCR;
    SQLiteDatabase db;

    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demand);

        dbCR = new DBCreation(this);
        db = dbCR.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            email = extras.getString("email");
           // Log.d("EmailD", "Email transported"+email);
        }

        etDate=findViewById(R.id.etDate);
        etLoc=(EditText) findViewById(R.id.edit_text_loc);
        etPrice=(EditText) findViewById(R.id.edit_text_price);
        etType=(EditText) findViewById(R.id.edit_text_type);

        btnAddDem=findViewById(R.id.button_addDemand);


         Calendar calendar = Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(AddDemandActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        m= m+1;
                         date= d+"/"+m+"/"+y;
                        etDate.setText(date);


                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        btnAddDem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AddDemand();
            }
        });

    }

    private void AddDemand() {

                String tempDate=date;
                String tempLoc=etLoc.getText().toString().trim();
                String tempPrice=etPrice.getText().toString().trim();
                String tempType=etType.getText().toString().trim();

                if(tempDate.isEmpty())
                {
                    etDate.setError("Select Date");
                    etDate.requestFocus();
                    return;
                }

                if(tempLoc.isEmpty())
                {
                    etLoc.setError("Enter Location");
                    etLoc.requestFocus();
                    return;
                }

                if(tempPrice.isEmpty())
                {
                    etPrice.setError("Enter Price");
                    etPrice.requestFocus();
                    return;
                }

                if(tempType.isEmpty())
                {
                    etType.setError("Enter Type");
                    etType.requestFocus();
                    return;
                }



        Cursor cursor=db.rawQuery("SELECT _id FROM RegUsers WHERE email= ?", new String[]{email});
        int tempID;
        float tempPriceReal;
        tempPriceReal = Float.parseFloat(tempPrice);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        do{
           // Log.d("hello", (String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")))));
            //Log.d("ID", "Got It"+tempID);

             tempID=cursor.getInt(cursor.getColumnIndex("_id"));
           // Log.d("hello", "ID"+tempID);
        }while (cursor.moveToNext());

        /*String sql= "INSERT INTO Demands (Type,Date,Price,Area,_idUser) Values (" +tempType+ ", '', '', '0', '')";
        db.execSQL(sql);*/
        ContentValues values=new ContentValues();
        values.put("Type",tempType);
        values.put("Date",tempDate);
        values.put("Price",tempPriceReal);
        values.put("Area",tempLoc);
        values.put("_idUser",tempID);

        db.insert("Demands",null, values);
       // db.close();
        Toast.makeText(getApplicationContext(), "Demand Stored", Toast.LENGTH_SHORT).show();

       /* Cursor cursor1=db.rawQuery("SELECT * FROM Demands",new  String[]{});
        if(cursor1!=null)
        {
            cursor1.moveToFirst();
        }

        do{
            tempType=cursor1.getString(cursor1.getColumnIndex("Type"));
            tempDate=cursor1.getString(cursor1.getColumnIndex("Date"));
            tempPrice=cursor1.getString(cursor1.getColumnIndex("Price"));
            tempLoc=cursor1.getString(cursor1.getColumnIndex("Area"));
            tempID=cursor1.getInt(cursor1.getColumnIndex("_idUser"));
            Log.d("Check","Got it--"+tempID+"  "+tempType+"  "+tempDate+"  "+tempPrice+"  "+tempLoc);
        }while (cursor1.moveToNext());*/
        // Log.d("Demand", "Demand Stored");
        etDate.setText("");
        etLoc.setText("");
        etPrice.setText("");
        etType.setText("");


        if(tempType.equals("Buy"))
        {
            Cursor cursor1=db.rawQuery("SELECT * FROM Property WHERE Price = ? AND Location = ?",new String[]{tempPrice,tempLoc});

            if(cursor1.getCount()>0)
            {
                AlertDialog.Builder builder =new AlertDialog.Builder(AddDemandActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Alert!");
                builder.setMessage("Demanded Buying Property Exist in System");
                builder.show();
            }


        } else if (tempType.equals("Rental"))
        {
            Cursor cursor2=db.rawQuery("SELECT * FROM RentA WHERE PriceRentA = ? AND LocationR = ?",new String[]{tempPrice,tempLoc});

            if(cursor2.getCount()>0)
            {
                AlertDialog.Builder builder =new AlertDialog.Builder(AddDemandActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Alert!");
                builder.setMessage("Demanded Rental Property Exist in System");
                builder.show();
            }


        }
    }


}