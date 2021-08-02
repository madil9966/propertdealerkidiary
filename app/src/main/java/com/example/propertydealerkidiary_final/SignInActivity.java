package com.example.propertydealerkidiary_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    EditText editTextEmail,editTextPassword;
    TextView tvMsg;
    Button btnSignIn;
    int flag;

    DBCreation dbCR;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

         dbCR = new DBCreation(this);
         db = dbCR.getReadableDatabase();

        editTextEmail = (EditText) findViewById(R.id.edit_text_Email);
        editTextPassword = (EditText) findViewById(R.id.edit_text_Password);

         tvMsg = findViewById(R.id.text_view_Register);
         btnSignIn = findViewById(R.id.button_Login);


         btnSignIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SignIn();
             }
         });

         tvMsg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(SignInActivity.this, RegisterActivity.class);
                 startActivity(intent);
             }
         });

    }

    private void SignIn() {

        String tempEmail=editTextEmail.getText().toString().trim();
        String tempPassword=editTextPassword.getText().toString().trim();

        if(tempEmail.isEmpty())
        {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(tempPassword.isEmpty())
        {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches())
        {
            editTextEmail.setError("Enter Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        checkFromDB(tempEmail, tempPassword);

    }

    private void checkFromDB(String EmailTemp, String PassTemp) {
        Cursor cursor=db.rawQuery("SELECT Email, Password FROM RegUsers",new  String[]{});

        //Log.d("main","Email"+ EmailTemp+"Password"+PassTemp);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        do {
            String email=cursor.getString(0);
            String pass=cursor.getString(1);
            //Log.d("S1","Email"+ email+"Password"+pass);


            if(email.equals(EmailTemp) && pass.equals(PassTemp))
            {
                Toast.makeText(getApplicationContext(), "Successful!",Toast.LENGTH_SHORT).show();
              //  Log.d("S2","Email"+ email+"Password"+pass);
                Intent intent = new Intent(SignInActivity.this, MainScreen.class);
                intent.putExtra("email",EmailTemp);
                intent.putExtra("password",PassTemp);
                startActivity(intent);
                return;
            }
            else
                {
                flag=1;
            }
        }while(cursor.moveToNext());

        if (flag==1)
            Toast.makeText(getApplicationContext(), "Unsuccessful",Toast.LENGTH_SHORT).show();

    }


}