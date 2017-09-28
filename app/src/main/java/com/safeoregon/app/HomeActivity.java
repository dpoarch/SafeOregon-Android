package com.safeoregon.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.etech.util.Header;

/**
 * Created by etech8 on 11/2/16.
 */
public class HomeActivity extends HeaderActivity {

    private Button btnCallHelp;
    private Button btnMyTips;
    private Button btnSubmitTips;
    private Button btnResources;
    private TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setHeader();
        initliazeView();

    }

    private void setHeader() {
        try {
            super.setTitle(getString(R.string.title_home_Activity));
            Header header = (Header) findViewById(R.id.header1);
            header.hideLeftBtn();
           header.hideRightBtn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initliazeView() {
        try {

            txtVersion = (TextView) findViewById(R.id.txtAppVersion);

            btnCallHelp = (Button) findViewById(R.id.btnCallHelp);
            btnCallHelp.setOnClickListener(btnCallHelpClickListener);

            btnMyTips = (Button) findViewById(R.id.btnMyTips);
            btnMyTips.setOnClickListener(btnMyTipsClickListener);

            btnSubmitTips = (Button) findViewById(R.id.btnSubmitTips);
            btnSubmitTips.setOnClickListener(btnSubmitTipsClickListener);

            btnResources = (Button) findViewById(R.id.btnResources);
            btnResources.setOnClickListener(btnResourcesClickListener);

            txtVersion.setText("Version : "+getApkVersion());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View.OnClickListener btnCallHelpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            callHelpDialog();

        }
    };

    private View.OnClickListener btnMyTipsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(HomeActivity.this,MyTipsListActivity.class);
            startActivity(intent);

        }
    };

    private View.OnClickListener btnSubmitTipsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent=new Intent(HomeActivity.this,FormActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener btnResourcesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent webIntent = new Intent(HomeActivity.this,WebViewActivity.class);
            startActivity(webIntent);


        }
    };

    private void callHelpDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+getString(R.string.dial_call)));
                        startActivity(intent);

                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.call_help_line)).setTitle(getString(R.string.app_name)).setPositiveButton(getString(R.string.call), dialogClickListener).setNegativeButton(getString(R.string.cancel), dialogClickListener).show();

    }

    private String getApkVersion()
    {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
       return pInfo.versionName;

    }
}
