package com.safeoregon.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.etech.bean.DbMyTips;
import com.etech.util.Header;

/**
 * Created by etech8 on 11/2/16.
 */
public class MyTipsDetailActivity extends HeaderActivity {

    TextView txtTipsNo;
    TextView txtTipsDate;
    DbMyTips myTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_detail);

        setHeader();

        init();

    }

    private void setHeader() {
        try {

            super.setTitle(getString(R.string.title_tips_detail));
            Header header = (Header) findViewById(R.id.header1);
            header.setLeftBtnImage(R.drawable.ic_back_arrow);
            header.hideRightBtn();
            header.setLeftBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init()
    {
        Intent intent = getIntent();
        myTips = (DbMyTips) intent.getSerializableExtra("myTips");

        txtTipsNo = (TextView) findViewById(R.id.txtTipsNo);
        txtTipsDate = (TextView) findViewById(R.id.txtTipsDate);

        if(myTips!=null && myTips.getTips_number()!=null)
            txtTipsNo.setText(myTips.getTips_number());

        if(myTips!=null && myTips.getTips_date()!=null)
            txtTipsDate.setText(myTips.getTips_date());

    }
}
