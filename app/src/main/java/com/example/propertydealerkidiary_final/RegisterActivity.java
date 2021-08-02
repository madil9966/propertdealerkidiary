package com.example.propertydealerkidiary_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editFullName,editTextEmail,editTextPassword,editTextContact;
    String textSpinner;
    Button btnRegister;

    DBCreation dbCR;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbCR = new DBCreation(this);
        db = dbCR.getWritableDatabase();

        editFullName=(EditText) findViewById(R.id.edit_text_name);
        editTextEmail=(EditText) findViewById(R.id.edit_text_Email);
        editTextPassword=(EditText) findViewById(R.id.edit_text_Password);
        editTextContact=(EditText) findViewById(R.id.edit_text_contact);

        btnRegister = findViewById(R.id.button_Register);


        Spinner spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=  ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterUser();

            }
        });
    }

    private void RegisterUser() {

                String temFN = editFullName.getText().toString().trim();
                String tempEmail = editTextEmail.getText().toString().trim();
                String tempPassword = editTextPassword.getText().toString().trim();
                String tempContact = editTextContact.getText().toString().trim();
                String tempSpinTxt = textSpinner;

                if (temFN.isEmpty()) {
                    editFullName.setError("Enter Full Name");
                    editFullName.requestFocus();
                    return;
                }
                if (tempEmail.isEmpty()) {
                    editTextEmail.setError("Enter E-mail");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()) {
                    editTextEmail.setError("Enter Valid Email");
                    editTextEmail.requestFocus();
                    return;
                }
                if (tempPassword.isEmpty()) {
                    editTextPassword.setError("Enter Password");
                    editTextPassword.requestFocus();
                    return;
                }
                if (tempContact.isEmpty()) {
                    editTextContact.setError("Enter Contact");
                    editTextContact.requestFocus();
                    return;
                }
        ContentValues values=new ContentValues();
        values.put("Name",temFN);
        values.put("Email",tempEmail);
        values.put("Password",tempPassword);
        values.put("Contact",tempContact);
        values.put("Type",tempSpinTxt);
        db.insert("RegUsers",null, values);
        //db.close();

        Toast.makeText(getApplicationContext(), "Registered",Toast.LENGTH_SHORT).show();
        Log.d("S", "Data Entered Successfully");

        Intent intent=new Intent(RegisterActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
         textSpinner = parent.getItemAtPosition(position).toString();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        String str="Nothing Selected";
    Toast.makeText(parent.getContext(),str,Toast.LENGTH_SHORT).show();
    }



}