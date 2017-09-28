package com.etech.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomAlertDialogue
{
	public static void showAlert(Context mContext, String title, String message, String firstBtnTitle, String secondBtnTitle,DialogInterface.OnClickListener btnClickListner,DialogInterface.OnClickListener secondbtnClickListner) 
	{
		AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		//alertDialog.setIcon(R.drawable.ic_launcher);
		if(firstBtnTitle != null)
			alertDialog.setButton(firstBtnTitle,btnClickListner);
		if(secondBtnTitle != null)
			alertDialog.setButton2(secondBtnTitle, secondbtnClickListner);
		alertDialog.show();
	}
}
