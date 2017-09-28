package com.safeoregon.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.etech.bean.DbMyTips;
import com.etech.model.myTipsHelper;
import com.etech.util.ActionCallback;
import com.etech.util.Header;

import java.util.ArrayList;

/**
 * Created by etech8 on 11/2/16.
 */
public class MyTipsListActivity extends HeaderActivity {

    private ListView listTips;
    Context context = MyTipsListActivity.this;
    private ArrayList<DbMyTips> alistmyTips;
    DbMyTips myTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_list);

        setHeader();

        alistmyTips = new ArrayList<DbMyTips>();

        getTipsList();

    }

    private void setHeader() {
        try {

            super.setTitle(getString(R.string.title_tips_list));
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

        listTips = (ListView) findViewById(R.id.listTips);

    }

    private void getTipsList()
    {
        try
        {
            myTipsHelper tipsHelper = new myTipsHelper(context);
            tipsHelper.apiGetTipsList(tipsCallback);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private ActionCallback tipsCallback = new ActionCallback() {

        @Override
        public void onActionComplete(int statusCode, String callbackString,
                                     Object res) {

            if(alistmyTips!=null && alistmyTips.size()>0)
                alistmyTips.clear();

            init();

            myTips = new DbMyTips();
            myTips.setTips_id("1");
            myTips.setTips_number("tips 1");
            myTips.setTips_date("1-1-2015");
            alistmyTips.add(myTips);

            myTips = new DbMyTips();
            myTips.setTips_id("2");
            myTips.setTips_number("tips2");
            myTips.setTips_date("02-02-2015");
            alistmyTips.add(myTips);

            myTips = new DbMyTips();
            myTips.setTips_id("3");
            myTips.setTips_number("tips 3");
            myTips.setTips_date("3-02-2015");
            alistmyTips.add(myTips);

            myTips = new DbMyTips();
            myTips.setTips_id("4");
            myTips.setTips_number("tips 4");
            myTips.setTips_date("4-02-2015");
            alistmyTips.add(myTips);

            MyTipsListAdapter tipsAdapter = new MyTipsListAdapter(context);
            listTips.setAdapter(tipsAdapter);

        }
    };


    class MyTipsListAdapter extends BaseAdapter {

        Context context;

        private LayoutInflater mLayoutInflater = null;

        public MyTipsListAdapter(Context context) {
            this.context = context;
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return alistmyTips.size();
        }

        @Override
        public Object getItem(int position) {
            try {
                return alistmyTips.get(position);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            ViewHolder holder;

            try {
                if (view == null) {
                    holder = new ViewHolder();

                    //view = mLayoutInflater.inflate(R.layout., null);
                    view = mLayoutInflater.inflate(R.layout.common_tips_view, viewGroup, false);
                    holder.txtTipsNo = (TextView) view.findViewById(R.id.txtTipsNo);
                    holder.txtTipsDate = (TextView) view.findViewById(R.id.txtTipsDate);
                    holder.btnView = (Button) view.findViewById(R.id.btnView);

                    // the setTag is used to store the data within this view
                    view.setTag(holder);
                } else {

                    holder = (ViewHolder) view.getTag();
                }

                final DbMyTips myTips = alistmyTips.get(position);
                holder.btnView.setTag(myTips.getTips_id());

                holder.txtTipsNo.setText("" + myTips.getTips_number());
                holder.txtTipsDate.setText("" + myTips.getTips_date());

                holder.btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyTipsListActivity.this,MyTipsDetailActivity.class);
                        intent.putExtra("myTips",myTips);
                        startActivity(intent);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        private class ViewHolder {

            private TextView txtTipsNo;
            private TextView txtTipsDate;
            private Button btnView;

        }

    }

}
