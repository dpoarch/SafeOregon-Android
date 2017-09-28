package com.etech.model;

import android.content.Context;
import android.provider.SyncStateContract;

import com.etech.util.ActionCallback;
import com.etech.util.AsyncTaskCompleteListener;
import com.etech.util.Constant;
import com.etech.util.ETechAsyncTask;
import com.etech.util.EtechLog;

import java.util.HashMap;

/**
 * Created by etech8 on 11/2/16.
 */
public class myTipsHelper implements AsyncTaskCompleteListener<String> {

    private Context cont;
    ActionCallback actionCallback;

    public myTipsHelper(Context context) {
        cont = context;

    }

    public void apiGetTipsList(ActionCallback actionCallback) {
        try {

            this.actionCallback = actionCallback;

            HashMap<String, Object> map = new HashMap<String, Object>();
         /*   HashMap<String, Object> map = AppUtils
                    .getDefaultParameterMap(cont);*/
            //     map.put("user_id","363");



          /*  ETechAsyncTask task = new ETechAsyncTask(cont,myTipsHelper.this,UrlCall,map, SyncStateContract.Constants.GET_REQUEST);
            task.execute(UrlCall);*/

            actionCallback.onActionComplete(Constant.RESPONSE_STATUS_SUCCESS, Constant.URL_GET_TIPS,"");
        } catch (Exception e) {
            EtechLog.error(myTipsHelper.class + " : apiGetUserFeedIDs() : ", e);
        }
    }

    @Override
    public void onTaskComplete(int statusCode, String result, String webserviceCb) {

    }
}
