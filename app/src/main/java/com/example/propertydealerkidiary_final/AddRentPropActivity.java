package com.example.propertydealerkidiary_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddRentPropActivity extends AppCompatActivity {
    EditText etDate, etRPropName, etRenType, etRetROwnerName, etROwnerCon, etRLoc, etRDes, etRDem;

    Button addRenProp,viewRenProp;

    String email, date;

    DBCreation dbCR;
    SQLiteDatabase db;


    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rent_prop);

        dbCR = new DBCreation(this);
        db = dbCR.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            //Log.d("EmailD", "Email transported"+email);
        }


        etDate = findViewById(R.id.etDate2);
        etRPropName = (EditText) findViewById(R.id.etRPropName);
        etRetROwnerName = (EditText) findViewById(R.id.etROwnerName);
        etROwnerCon = (EditText) findViewById(R.id.etROwnerCon);
        etRenType = (EditText) findViewById(R.id.et_Rtype);
        etRLoc = (EditText) findViewById(R.id.etRLoc);
        etRDes = (EditText) findViewById(R.id.etRDes);
        etRDem = (EditText) findViewById(R.id.etRDem);

        addRenProp = findViewById(R.id.button_addRentProp);
        viewRenProp = findViewById(R.id.button_viewRentProp);

        addRenProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRenProp();
            }
        });

        viewRenProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=db.rawQuery("SELECT * FROM RentA", new String[]{});

                if(cursor!=null)
                {
                    cursor.moveToNext();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Entries Exist",Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuffer buffer=new StringBuffer();
                do{
                    String PropName=cursor.getString(cursor.getColumnIndex("RentAName"));
                    String PropOwner=cursor.getString(cursor.getColumnIndex("Name"));
                    String PropPrice=cursor.getString(cursor.getColumnIndex("PriceRentA"));
                    String PropLoc=cursor.getString(cursor.getColumnIndex("LocationR"));

                    buffer.append("Property Name:"+PropName+"\n");
                    buffer.append("Owner Name:"+PropOwner+"\n");
                    buffer.append("Price:"+PropPrice+"\n");
                    buffer.append("Location"+PropLoc+"\n");

                    buffer.append("\n\n");
                }while (cursor.moveToNext());

                AlertDialog.Builder builder =new AlertDialog.Builder(AddRentPropActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Rental Properties");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

        etDate = findViewById(R.id.etDate2);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRentPropActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        m = m + 1;
                        date = d + "/" + m + "/" + y;
                        etDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    private void AddRenProp() {
        String tempDate = date;
        String tempPropOName = etRetROwnerName.getText().toString().trim();
        String tempPropOCon = etROwnerCon.getText().toString().trim();
        String temPropName = etRPropName.getText().toString().trim();
        String tempType = etRenType.getText().toString().trim();
        String tempLoc = etRLoc.getText().toString().trim();
        String tempDes = etRDes.getText().toString().trim();
        String tempPrice = etRDem.getText().toString().trim();

        if (tempDate.isEmpty()) {
            etDate.setError("Select Date");
            etDate.requestFocus();
            return;
        }
        if (temPropName.isEmpty()) {
            etRPropName.setError("Enter Property Name");
            etRPropName.requestFocus();
            return;
        }
        if (tempPropOName.isEmpty()) {
            etRPropName.setError("Enter Property Owner Name");
            etRPropName.requestFocus();
            return;
        }
        if (tempPropOCon.isEmpty()) {
            etROwnerCon.setError("Enter Property Owner Contact");
            etROwnerCon.requestFocus();
            return;
        }

        if (tempType.isEmpty()) {
            etRenType.setError("Enter Type");
            etRenType.requestFocus();
            return;
        }
        if (tempLoc.isEmpty()) {
            etRLoc.setError("Enter Location");
            etRLoc.requestFocus();
            return;
        }
        if (tempDes.isEmpty()) {
            etRDes.setError("Enter Description");
            etRDes.requestFocus();
            return;
        }
        if (tempPrice.isEmpty()) {
            etRDem.setError("Enter Price");
            etRDem.requestFocus();
            return;
        }

        int tempUID, tempCID;

        Cursor cursor1 = db.rawQuery("SELECT _id FROM RegUsers WHERE email= ?", new String[]{email});
        if (cursor1 != null) {
            cursor1.moveToFirst();
        }

        String cName = "";
        do {
            tempUID = cursor1.getInt(cursor1.getColumnIndex("_id"));
            Log.d("User", "User ID" + tempUID);
        } while (cursor1.moveToNext());

        Cursor cursor2 = db.rawQuery("SELECT _id, Name FROM Client WHERE Name=?", new String[]{tempPropOName});
        if (cursor2 != null) {
            cursor2.moveToFirst();
        }
        do {
            tempCID = cursor2.getInt(cursor2.getColumnIndex("_id"));
            cName = cursor2.getString(cursor2.getColumnIndex("Name"));
            Log.d("Client", "Client ID" + tempCID);
        } while (cursor2.moveToNext());


        if (!cName.equals(tempPropOName)) {
            Toast.makeText(getApplicationContext(), "Client Does Not Exist", Toast.LENGTH_SHORT).show();
        }

        Log.d("Owner", "Name" + tempPropOName);

        ContentValues values = new ContentValues();
        values.put("RentAName", temPropName);
        values.put("Type", tempType);
        values.put("LocationR", tempLoc);
        values.put("DateR", tempDate);
        values.put("DescriptionR", tempDes);
        values.put("PriceRentA", tempPrice);
        values.put("Name", tempPropOName);
        values.put("Contact", tempPropOCon);
        values.put("_idClient", tempCID);
        values.put("_idUser", tempUID);


        db.insert("RentA", null, values);
//        db.close();

        Toast.makeText(getApplicationContext(), "Rental Property Added", Toast.LENGTH_SHORT).show();
        Log.d("PropR", "Rental Property Added");

        etDate.setText("");
        etRetROwnerName.setText("");
        etROwnerCon.setText("");
        etRPropName.setText("");
        etRenType.setText("");
        etRLoc.setText("");
        etRDes.setText("");
        etRDem.setText("");

  /*      Cursor cursor3=db.rawQuery("SELECT * From RentA", new String[]{});
        if(cursor3 != null)
        {
            cursor3.moveToFirst();
        }

        do{
            tempType=cursor1.getString(cursor1.getColumnIndex("Type"));
            tempDate=cursor1.getString(cursor1.getColumnIndex("Date"));
            tempPrice=cursor1.getString(cursor1.getColumnIndex("Price"));
            tempLoc=cursor1.getString(cursor1.getColumnIndex("Area"));
            tempID=cursor1.getInt(cursor1.getColumnIndex("_idUser"));
            Log.d("Check","Got it--"+tempID+"  "+tempType+"  "+tempDate+"  "+tempPrice+"  "+tempLoc);

        }while (cursor3.moveToNext());


        //startActivity(new Intent(AddPropertyActivity.this, MainScreen.class));

    }*/
    }
}