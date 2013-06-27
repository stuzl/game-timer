package com.giraffeandson.myapp;
import android.app.*;
import android.content.*;

public class PopUp
{
public static void show(Context context,String message){
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	AlertDialog dialog = builder.setMessage(message).create();
	dialog.show();
}
}
