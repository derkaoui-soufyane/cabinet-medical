package com.example.cabinetmedical.Authh;

import android.content.Context;

import com.example.cabinetmedical.Entity.user;
import com.example.cabinetmedical.database.DatabaseHelper;

public class Authh {

    public static user login(Context context, String email, String password) {

        DatabaseHelper db = new DatabaseHelper(context);

        return db.login(email, password);
    }
}