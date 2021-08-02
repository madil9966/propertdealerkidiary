package com.example.propertydealerkidiary_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

public class AddPropertyActivity extends AppCompatActivity {


    EditText etDate1,etPropName,etPropOwnerName,etPropOwnerCon,etType,etLoc,etDes,etPrice;

    String date,email;

    Button btnAddProp,btnViewProp;

    DBCreation dbCR;
    SQLiteDatabase db;

    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        dbCR = new DBCreation(this);
        db = dbCR.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            email = extras.getString("email");
            //Log.d("EmailD", "Email transported"+email);
        }

        etDate1=findViewById(R.id.etDate1);
        etPropName=(EditText) findViewById(R.id.etPropName);
        etPropOwnerName=(EditText) findViewById(R.id.etOwnerName);
        etPropOwnerCon=(EditText) findViewById(R.id.etOwnerCon);
        etType=(EditText) findViewById(R.id.et_type);
        etLoc=(EditText) findViewById(R.id.etLoc);
        etDes=(EditText) findViewById(R.id.etDes);
        etPrice=(EditText) findViewById(R.id.etDem);

        btnAddProp=findViewById(R.id.button_addDemand);
        btnViewProp=findViewById(R.id.button_viewProperties);

        btnAddProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProperty();
            }
        });

        btnViewProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=db.rawQuery("SELECT * FROM Property", new String[]{});

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
                    String PropName=cursor.getString(cursor.getColumnIndex("PropName"));
                    String PropOwner=cursor.getString(cursor.getColumnIndex("Name"));
                    String PropPrice=cursor.getString(cursor.getColumnIndex("Price"));
                    String PropLoc=cursor.getString(cursor.getColumnIndex("Location"));

                    buffer.append("Property Name:"+PropName+"\n");
                    buffer.append("Owner Name:"+PropOwner+"\n");
                    buffer.append("Price:"+PropPrice+"\n");
                    buffer.append("Location"+PropLoc+"\n");

                    buffer.append("\n\n");
                }while (cursor.moveToNext());

                AlertDialog.Builder builder =new AlertDialog.Builder(AddPropertyActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Properties");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });


        Calendar calendar = Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);

        etDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(AddPropertyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        m= m+1;
                         date= d+"/"+m+"/"+y;
                        etDate1.setText(date);


                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }

    private void AddProperty() {
        String tempDate=date;
        String tempPropOName=etPropOwnerName.getText().toString().trim();
        String tempPropOCon=etPropOwnerCon.getText().toString().trim();
        String temPropName=etPropName.getText().toString().trim();
        String tempType=etType.getText().toString().trim();
        String tempLoc=etLoc.getText().toString().trim();
        String tempDes=etDes.getText().toString().trim();
        String tempPrice=etPrice.getText().toString().trim();

        if(tempDate.isEmpty())
        {
            etDate1.setError("Select Date");
            etDate1.requestFocus();
            return;
        }
        if(temPropName.isEmpty())
        {
            etPropName.setError("Enter Property Name");
            etPropName.requestFocus();
            return;
        }
        if(tempPropOName.isEmpty())
        {
            etPropOwnerName.setError("Enter Property Owner Name");
            etPropOwnerName.requestFocus();
            return;
        }
        if(tempPropOCon.isEmpty())
        {
            etPropOwnerCon.setError("Enter Property Owner Contact");
            etPropOwnerCon.requestFocus();
            return;
        }

        if(tempType.isEmpty())
        {
            etType.setError("Enter Type");
            etType.requestFocus();
            return;
        }
        if(tempLoc.isEmpty())
        {
            etLoc.setError("Enter Location");
            etLoc.requestFocus();
            return;
        }
        if(tempDes.isEmpty())
        {
            etDes.setError("Enter Description");
            etDes.requestFocus();
            return;
        }
        if(tempPrice.isEmpty())
        {
            etPrice.setError("Enter Price");
            etPrice.requestFocus();
            return;
        }

        int tempUID, tempCID;

        Cursor cursor1=db.rawQuery("SELECT _id FROM RegUsers WHERE email= ?", new String[]{email});
        if(cursor1 != null) {
            cursor1.moveToFirst();
        }

        String cName="";
        do {
            tempUID = cursor1.getInt(cursor1.getColumnIndex("_id"));
            Log.d("User", "User ID"+tempUID);
        }while (cursor1.moveToNext());

        Cursor cursor2=db.rawQuery("SELECT _id, Name FROM Client WHERE Name=?", new String[]{tempPropOName});
        if (cursor2!=null){
            cursor2.moveToFirst();
        }
        do {
            tempCID = cursor2.getInt(cursor2.getColumnIndex("_id"));
            cName= cursor2.getString(cursor2.getColumnIndex("Name"));
            Log.d("Client", "Client ID"+tempCID);
        }while (cursor2.moveToNext());


        if(!cName.equals(tempPropOName))
        {
            Toast.makeText(getApplicationContext(),"Client Does Not Exist",Toast.LENGTH_SHORT).show();
        }

        Log.d("Owner", "Name"+tempPropOName);

        ContentValues values=new ContentValues();
        values.put("PropName",temPropName);
        values.put("Type",tempType);
        values.put("Location",tempLoc);
        values.put("Date",tempDate);
        values.put("Description",tempDes);
        values.put("Price",tempPrice);
        values.put("Name",tempPropOName);
        values.put("Contact",tempPropOCon);
        values.put("_idClient",tempCID);
        values.put("_idUser",tempUID);


        db.insert("Property",null, values);
//        db.close();

        Toast.makeText(getApplicationContext(), "Property Added", Toast.LENGTH_SHORT).show();
        Log.d("Prop", "Property Added");

        etDate1.setText("");
        etPropOwnerName.setText("");
        etPropOwnerCon.setText("");
        etPropName.setText("");
        etType.setText("");
        etLoc.setText("");
        etDes.setText("");
        etPrice.setText("");

        //startActivity(new Intent(AddPropertyActivity.this, MainScreen.class));*/

    }
}