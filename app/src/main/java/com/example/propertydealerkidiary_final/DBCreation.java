package com.example.propertydealerkidiary_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCreation extends SQLiteOpenHelper{

    public static final String dbName = "PDKDDB";
    private static  final int version = 1;

    public DBCreation (Context context){
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Table Creation
        String sql = "CREATE TABLE RegUsers (_id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Email TEXT, Password TEXT, Contact TEXT, Type TEXT )";
        sqLiteDatabase.execSQL(sql);

        String sql1 ="CREATE TABLE Client (_id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Email TEXT, Contact TEXT, _idUser INTERGER NOT NULL," +
                "FOREIGN KEY (_idUser) REFERENCES RegUsers(_id))";
        sqLiteDatabase.execSQL(sql1);

        String sql2 ="CREATE TABLE Demands (_id INTEGER PRIMARY KEY AUTOINCREMENT, Type TEXT, Date TEXT, Price REAL, Area TEXT, _idUser INTERGER NOT NULL," +
                "FOREIGN KEY (_idUser) REFERENCES RegUsers(_id))";
        sqLiteDatabase.execSQL(sql2);

        String sql3 ="CREATE TABLE RentedOut (_id INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, RentedAtPrice REAL, _idClient INTEGER NOT NULL, _idUser INTEGER NOT NULL, _idRentA INTEGER NOT NULL," +
                "FOREIGN KEY (_idClient) REFERENCES Client (_id)," +
                "FOREIGN KEY (_idUser) REFERENCES RegUsers(_id)," +
                "FOREIGN KEY (_idRentA) REFERENCES RentA (_id))";
        sqLiteDatabase.execSQL(sql3);

        String sql5="CREATE TABLE Sold (_id INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, SoldAtPrice REAL, _idClient INTEGER NOT NULL, _idUser INTEGER NOT NULL, _idProperty INTEGER NOT NULL," +
                "FOREIGN KEY (_idClient) REFERENCES Client (_id)," +
                "FOREIGN KEY (_idUser) REFERENCES RegUsers(_id)," +
                "FOREIGN KEY (_idProperty) REFERENCES Property(_id))";
        sqLiteDatabase.execSQL(sql5);

        String sql4 ="CREATE TABLE Property (_id INTEGER PRIMARY KEY AUTOINCREMENT, PropName TEXT, Type TEXT, Location TEXT, Date TEXT, Description TEXT, Price REAL, Status TEXT DEFAULT \"Available\" NOT NULL, Name TEXT NOT NULL, Contact TEXT, _idClient INTEGER NOT NULL, _idUser INTEGER NOT NULL," +
                "FOREIGN KEY (_idClient) REFERENCES Client (_id)," +
                "FOREIGN KEY (_idUser) REFERENCES RegUsers(_id)) ";
        sqLiteDatabase.execSQL(sql4);

        String sql6 ="CREATE TABLE RentA (_idRentA INTEGER PRIMARY KEY AUTOINCREMENT, RentAName TEXT, Type TEXT, LocationR TEXT, DateR TEXT, DescriptionR TEXT, PriceRentA REAL, StatusRentA TEXT DEFAULT \"Available\" NOT NULL, Name TEXT NOT NULL, Contact TEXT, _idClient INTEGER NOT NULL, _idUser INTEGER NOT NULL," +
                "FOREIGN KEY (_idClient) REFERENCES Client(_id)," +
                "FOREIGN KEY (_idUser) REFERENCES RegUsers(_id))";
        sqLiteDatabase.execSQL(sql6);





 insertDataReg("Muhammad Adil","muhammad92adil105@gmail.com","Mughal333","0335-5615648","Individual", sqLiteDatabase);
    }

    public void insertDataReg(String name,String email, String password, String contact, String type, SQLiteDatabase db)
    {

        ContentValues values=new ContentValues();
        values.put("Type", name);
        values.put("Email", email);
        values.put("Password", password);
        values.put("Contact", contact);
        values.put("Type", type);

        db.insert("RegUsers", null, values);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}