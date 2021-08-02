package com.example.propertydealerkidiary_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddClientActivity extends AppCompatActivity {

    EditText etFN,etEmail,etContact;
    Button btnAddClient;

    String email;

    DBCreation dbCR;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        dbCR = new DBCreation(this);
        db = dbCR.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            email = extras.getString("email");
            Log.d("EmailD", "Email transported"+email);
        }

        etFN=(EditText) findViewById(R.id.edit_text_name);
        etEmail=(EditText) findViewById(R.id.edit_text_Email);
        etContact=(EditText) findViewById(R.id.edit_text_contact);

        btnAddClient=findViewById(R.id.button_addClient);

        btnAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClient();
            }
        });
    }

    private void AddClient() {
        String tempFN=etFN.getText().toString().trim();
        String tempEmail=etEmail.getText().toString().trim();
        String tempContact=etContact.getText().toString().trim();

        if(tempFN.isEmpty())
        {
            etFN.setError("Enter Full Name");
            etFN.requestFocus();
            return;
        }
        if(tempEmail.isEmpty())
        {
            etEmail.setError("Enter E-Mail");
            etEmail.requestFocus();
            return;
        }
        if(tempContact.isEmpty())
        {
            etContact.setError("Enter Contact");
            etContact.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches())
        {
            etEmail.setError("Enter Valid Email");
            etEmail.requestFocus();
            return;
        }

        Cursor cursor=db.rawQuery("SELECT _id FROM RegUsers WHERE email= ?", new String[]{email});
        int tempID;

        if(cursor != null) {
            cursor.moveToFirst();
        }
        do{
             //Log.d("hello", (String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")))));
            //Log.d("ID", "Got It"+tempID);
            tempID=cursor.getInt(cursor.getColumnIndex("_id"));
     //       tempID=cursor.getInt(0);
      //     Log.d("hello", "ID"+tempID);
        }while (cursor.moveToNext());

        ContentValues values=new ContentValues();
        values.put("Name",tempFN);
        values.put("Email",tempEmail);
        values.put("Contact",tempContact);
        values.put("_idUser",tempID);

        db.insert("Client",null, values);
        //db.close();
        Toast.makeText(getApplicationContext(), "Client Added", Toast.LENGTH_SHORT).show();
        Log.d("Client", "Client Added");

        Cursor cursor1=db.rawQuery("SELECT * FROM Client",new  String[]{});
        if(cursor1!=null)
        {
            cursor1.moveToFirst();
        }

        do{
            tempFN=cursor1.getString(cursor1.getColumnIndex("Name"));
            tempEmail=cursor1.getString(cursor1.getColumnIndex("Email"));
            tempContact=cursor1.getString(cursor1.getColumnIndex("Contact"));
            tempID=cursor1.getInt(cursor1.getColumnIndex("_idUser"));
            Log.d("Check1","Got it--"+tempID+"  "+tempFN+"  "+tempEmail+"  "+tempContact);
        }while (cursor1.moveToNext());

        //startActivity(new Intent(AddClientActivity.this, MainScreen.class));
        etFN.setText("");
        etEmail.setText("");
        etContact.setText("");
    }
}