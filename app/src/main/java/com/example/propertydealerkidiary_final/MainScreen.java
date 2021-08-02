package com.example.propertydealerkidiary_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainScreen extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Email = "emailKey";
    public static final String Password = "passKey";
    SharedPreferences sharedpreferences;

    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
             email = extras.getString("email");
             password=extras.getString("password");

            Log.d("EmailM", "Email transported"+email+password);
        }


        SharedPreferences.Editor editor = sharedpreferences.edit();


        editor.putString(Email, email);
        editor.putString(Password, password);
        editor.commit();

        CardView cardView1 = findViewById(R.id.cardViewProperties);
        CardView cardView2 = findViewById(R.id.cardViewRent);
        CardView cardView3 = findViewById(R.id.cardViewCustomer);
        CardView cardView4 = findViewById(R.id.cardViewDemand);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this, AddPropertyActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this, AddRentPropActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this, AddClientActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this, AddDemandActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menumain, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {

            case R.id.item2:{
                Intent intent=new Intent(MainScreen.this,SignInActivity.class);
                startActivity(intent);
                SharedPreferences sharedpreferences = getSharedPreferences(MainScreen.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}