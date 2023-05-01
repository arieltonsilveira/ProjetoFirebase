package com.arieltonback.projetofirebase;

import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class AlertSnackBar {

    public void alertaSnackBarUsuario(View view, String message, boolean sucess){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.BLACK);
        snackbar.setBackgroundTint(Color.GREEN);
        if (!sucess) {
            snackbar.setBackgroundTint(Color.RED);
        }
        snackbar.show();
    }
}
