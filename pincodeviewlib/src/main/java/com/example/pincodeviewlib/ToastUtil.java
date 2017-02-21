package com.example.pincodeviewlib;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

	public static void display(Context context, String message) {
//		Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();		
		displayShort(context, message);
//		displayAtTop(context, message);
	}

	public static void displayAtTop(Context context, String message) {
		Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 60);
        toast.show();		 		
	}

	public static void display(Context context, int messageResourceId) {
		Toast.makeText(context.getApplicationContext(), messageResourceId, Toast.LENGTH_LONG).show();
	}
	
	public static void displayAtTop(Context context, int messageResourceId) {
		Toast toast = Toast.makeText(context.getApplicationContext(), messageResourceId, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 60);
        toast.show();		 		
	}

	public static void displayAtBottom(Context context, String message) {
		Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 60);
		toast.show();
	}

	public static void displayShort(Context context, String message) {
		Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	public static void displayShortAtTop(Context context, String message) {
		Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 60);
        toast.show();		 				
	}
	
}
