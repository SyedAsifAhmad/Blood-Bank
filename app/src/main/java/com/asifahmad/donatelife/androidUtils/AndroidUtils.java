package com.asifahmad.donatelife.androidUtils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.asifahmad.donatelife.model.model;

public class AndroidUtils {

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void passUserModelAsIntent(Intent intent, model model){
        intent.putExtra("Name",model.getName());
    }
}
