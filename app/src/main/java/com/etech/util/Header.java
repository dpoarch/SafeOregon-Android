package com.etech.util;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safeoregon.app.R;


public class Header extends RelativeLayout {
    private final String tag = "Header";
    private ImageButton btnRight = null;
    private ImageButton btnLeft = null;
    private TextView txtTitle = null;
    private EditText fakeEdit = null;
    private RelativeLayout headerView;

    private String title = null;
    private Activity activity = null;


    public Header(Context context, AttributeSet attrs) {
        super(context, attrs);

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;

        li = (LayoutInflater) getContext().getSystemService(infService);
        li.inflate(R.layout.header, this, true);

        btnLeft = (ImageButton) findViewById(R.id.btnLeft);
        btnRight = (ImageButton) findViewById(R.id.btnRight);


        txtTitle = (TextView) findViewById(R.id.txtTitle);
        headerView = (RelativeLayout) findViewById(R.id.header);
    }

    public void init(Activity activity, String title) {
        init(activity, title, null, null, false);

    }

    public void init(Activity activity, String title, String rightBtntitle, OnClickListener rightBtnClickListener, Boolean addMenu) {

        this.activity = activity;
        this.title = title;
        if (title != null) {
            txtTitle.setText(this.title);
        }

        btnLeft.setOnClickListener(leftBtnClickListener);

       /* if(rightBtnClickListener != null) {
            btnRight.setOnClickListener(rightBtnClickListener);
        }
        else {
            btnRight.setVisibility(View.GONE);
        }*/

		/*	if(addMenu){
            menu=new SlidingMenu(activity, SlidingMenu.SLIDING_WINDOW);
			menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			menu.setMenu(new MenuView(activity));

		}
		 */
    }

    OnClickListener leftBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            //if(menu!=null) {
            //	menu.toggle();
			/*MenuView menuView = (MenuView) menu.getMenu();
				menuView.showLinNewbieView();*/
            //}

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(fakeEdit.getWindowToken(), 0);
            //activity.finish();
        }
    };


    public void hideLeftBtn() {
        btnLeft.setVisibility(View.GONE);
    }

    public void setRightBtnImage(int rightBtnImage){
        btnRight.setImageResource(rightBtnImage);
    }

    public void hideRightBtn(){
        btnRight.setVisibility(View.GONE);
    }

    public void btnRighClickListener(OnClickListener onClickListener){
        if(onClickListener!=null){
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setOnClickListener(onClickListener);
        }else{
            btnRight.setVisibility(View.INVISIBLE);
        }

    }

    public void setLeftBtnImage(int leftBtnImage) {
        btnLeft.setImageResource(leftBtnImage);

    }

    public void setLeftBtnTitle(int leftBtnTitle) {
        btnLeft.setImageResource(leftBtnTitle);
    }

    public void setLeftBtnClickListener(OnClickListener btnLeftClickListener) {
        btnLeft.setOnClickListener(btnLeftClickListener);
    }


    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setBackGroundColor(int resId) {
        //AppUtils.setStatusbarColor(getContext(),resId);
        headerView.setBackgroundResource(resId);
    }


}
