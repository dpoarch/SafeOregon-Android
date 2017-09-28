package com.safeoregon.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.etech.util.AppUtils;
import com.etech.util.AsyncTaskCompleteListener;
import com.etech.util.AutoCompleteAdapter;
import com.etech.util.Constant;
import com.etech.util.CustomAlertDialogue;
import com.etech.util.ETechAsyncTask;
import com.etech.util.JSONFileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FormActivity extends Activity implements AsyncTaskCompleteListener<String> {

    private static final int REQUEST_CODE_VIDEO_TRIM = 1001;
    ViewFlipper formFlipper;
    TextView txtHeaderTitle;
    AutoCompleteTextView autotxtSchoolName;
    Button btnHeaderNext;
    Button btnDate;
    EditText editBullyperson;
    EditText editPerson;
    EditText editDescription;
    EditText editName;
    EditText editPhNo;
    Spinner spIncident;
    private Uri fileUri;
    int currentDay;
    int currentMonth;
    int currentYear;
    //	VideoView videoPreview;
    String value_stateID = null;
    String value_schoolID = null;
    String value_schoolName = null;
    String value_incidentLocation = null;
    int incidentValue = 0;
    String value_incidentTime = "0001";
    String value_description = null;
    String value_reporterType = null;

    String user_lang = "en";

    ArrayList<String> schoolNameList = null;
    ArrayList<String> schoolIDList = null;
    ArrayList<String> schoolCityList = null;
    ArrayList<String> incidentValueList = null;
    ArrayList<String> reporterValueList = null;

    ArrayList<String> stateIDList = null;

    private Drawable x;
    private boolean dateDialogflag = true;

    JSONFileReader jsonFileReader;

    String APP_NAME = "Sprigeo Sample";
    String btn_diaOK = "Ok";
    String btn_diaCancel = "Cancel";
    private final int REQUEST_CAMERA = 101;
    private final int SELECT_IMAGE_FILE = 102;
    private final int SELECT_VIDEO_FILE = 103;
    private final int VIDEO_CAPTURE = 104;
    private final int CROP_PIC = 105;

	/*
    new Spinner added
	 */

    Spinner spIncidentAdult;
    Spinner spSituation;

    String[] incidentAdultList;
    String[] situationList;

    ArrayList<String> alistIncidentAdult;

    ArrayList<String> alistSituationList;

    String valueIncidentAdult = null;
    String valueSituation = null;
    EditText edtWho;
    ImageView imgUpload;
    Button btnUpload;
    String strWhois;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        formFlipper = (ViewFlipper) findViewById(R.id.formFlipper);
        btnHeaderNext = (Button) findViewById(R.id.btnHeaderNext);
        txtHeaderTitle = (TextView) findViewById(R.id.header_title);
        autotxtSchoolName = (AutoCompleteTextView) findViewById(R.id.autoschoolName);
        btnDate = (Button) findViewById(R.id.btnDateTime);
        editBullyperson = (EditText) findViewById(R.id.editTextBully);
        editPerson = (EditText) findViewById(R.id.editTextPerson);
        editDescription = (EditText) findViewById(R.id.editTextDescription);
        editName = (EditText) findViewById(R.id.editTextName);
        editPhNo = (EditText) findViewById(R.id.editTextPhNo);
        edtWho = (EditText) findViewById(R.id.edtWho);
        imgUpload = (ImageView) findViewById(R.id.imgUpload);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(uploadClickListener);

        Spinner spStateName = (Spinner) findViewById(R.id.spStateName);
        Spinner spTime = (Spinner) findViewById(R.id.spTime);
        Spinner spReporterType = (Spinner) findViewById(R.id.spReporterType);
        spIncident = (Spinner) findViewById(R.id.spIncident);

        spIncidentAdult = (Spinner) findViewById(R.id.spIncidentAdult);
        incidentAdultList = getResources().getStringArray(R.array.incidentAdult);
        alistIncidentAdult = new ArrayList<String>(Arrays.asList(incidentAdultList));
        spSituation = (Spinner) findViewById(R.id.spSituation);
        situationList = getResources().getStringArray(R.array.sutuationList);
        alistSituationList = new ArrayList<String>(Arrays.asList(situationList));

        //	SharedPreferences user_pref = getSharedPreferences(Constant.USER_PREF, MODE_PRIVATE);
        //	user_lang = user_pref.getString(Constant.selLang, "en");

        Log.i("USER LANGUAGE", user_lang);

        jsonFileReader = new JSONFileReader(loadJSONFromAsset());

        if (jsonFileReader.jsonObj == null) {
            Log.d("StateList", "sonFileReader.jsonObj ");
            jsonFileReader = new JSONFileReader(loadJSONFromAsset());
        }

        if (jsonFileReader.jsonObj != null) {

            setLabelNames();

            ArrayList<ArrayList<String>> stateList = jsonFileReader.getstateList();

            Log.d("StateList", "List : " + stateList);

            stateIDList = stateList.get(0);
            ArrayAdapter<String> stateNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateList.get(1) /*Constant.stateNames*/);
            stateNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spStateName.setPrompt(jsonFileReader.getLabelObj("LBL_SEL_STATE"));
            spStateName.setAdapter(stateNameAdapter);

            Constant.Time[0] = jsonFileReader.getLabelObj("LBL_SELECT");
            ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, Constant.Time);
            timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spTime.setPrompt(jsonFileReader.getLabelObj("LBL_SEL_TIME"));
            spTime.setAdapter(timeAdapter);

            ArrayAdapter<String> reporterAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, jsonFileReader.getReporterList());
            reporterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spReporterType.setPrompt(jsonFileReader.getLabelObj("LBL_TYPE"));
            spReporterType.setAdapter(reporterAdapter);
            reporterValueList = jsonFileReader.getReporterListValue();

            ArrayAdapter<String> incidentAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, jsonFileReader.getincidentList());
            incidentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spIncident.setPrompt(jsonFileReader.getLabelObj("MSG_SELECT_INCIDENT"));
            spIncident.setAdapter(incidentAdapter);
            incidentValueList = jsonFileReader.getincidentListValue();

			/*
            new Spinner Adapter
			 */


        }

        ArrayAdapter<String> incidentAdultAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alistIncidentAdult);
        incidentAdultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIncidentAdult.setPrompt(jsonFileReader.getLabelObj("LBL_TYPE"));
        spIncidentAdult.setAdapter(incidentAdultAdapter);

        ArrayAdapter<String> situationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alistSituationList);
        situationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSituation.setPrompt(jsonFileReader.getLabelObj("LBL_TYPE"));
        spSituation.setAdapter(situationAdapter);


        spStateName.setOnItemSelectedListener(SpStateItemSelectListener);
        spIncident.setOnItemSelectedListener(SpIncidentItemSelectListener);
        spTime.setOnItemSelectedListener(SpTimeItemSelectListener);
        spReporterType.setOnItemSelectedListener(SpReportTypeItemSelectListener);

		/*
        new Spiner onitemclick listeer
		 */

        spIncidentAdult.setOnItemSelectedListener(SpIncidentAdultItemSelectListener);
        spSituation.setOnItemSelectedListener(SpSituationItemSelectListener);

        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DATE);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        schoolNameList = new ArrayList<String>();
        schoolCityList = new ArrayList<String>();
        schoolIDList = new ArrayList<String>();

        x = getResources().getDrawable(R.drawable.close);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
        autotxtSchoolName.addTextChangedListener(autoTxtwatcher);

        autotxtSchoolName.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                value_schoolID = schoolIDList.get(position);
                value_schoolName = schoolNameList.get(position);
                AppUtils.hideSoftKeyboard(FormActivity.this);
            }

        });
        autotxtSchoolName.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {

                if (autotxtSchoolName.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > autotxtSchoolName.getWidth()
                        - autotxtSchoolName.getPaddingRight() - x.getIntrinsicWidth()) {
                    autotxtSchoolName.setText("");
                    autotxtSchoolName.setCompoundDrawables(null, null, null, null);
                    if (schoolNameList != null) {
                        schoolNameList.clear();
                    }
                    if (schoolCityList != null) {
                        schoolCityList.clear();
                    }
                    if (schoolIDList != null) {
                        schoolIDList.clear();
                    }
                    value_schoolID = null;
                }
                return false;
            }
        });

		/*HashMap<String,Object>paramValues=new HashMap<String, Object>();
        paramValues.put("token",Constant.token);
		ETechAsyncTask task=new ETechAsyncTask(this,this,"IncidentList", paramValues,ETechAsyncTask.REQUEST_METHOD_POST);
		task.execute(Constant.getIncidentLocationListURL);*/

    }


    View.OnClickListener uploadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            selectImage();

        }
    };

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("sprigeo_en.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void setLabelNames() {

        try {
            //	AppUtils.hideSoftKeyboard(FormActivity.this);
            TextView LBL_LIVE = (TextView) findViewById(R.id.LBL_LIVE);
            TextView LBL_SEL_SCHOOL = (TextView) findViewById(R.id.LBL_SEL_SCHOOL);
            TextView LBL_WHERE_INCIDENT = (TextView) findViewById(R.id.LBL_WHERE_INCIDENT);
            TextView LBL_WHEN_HAPPEN = (TextView) findViewById(R.id.LBL_WHEN_HAPPEN);
            TextView LBL_TIME = (TextView) findViewById(R.id.LBL_TIME);
            TextView LBL_BULLY = (TextView) findViewById(R.id.LBL_BULLY);
            TextView LBL_BULLY_PERSON = (TextView) findViewById(R.id.LBL_BULLY_PERSON);
            TextView LBL_WHAT_HAPPEN = (TextView) findViewById(R.id.LBL_WHAT_HAPPEN);
            TextView LBL_WHO = (TextView) findViewById(R.id.LBL_WHO);
            TextView LBL_NAME = (TextView) findViewById(R.id.LBL_NAME);
            TextView LBL_PHONE = (TextView) findViewById(R.id.LBL_PHONE);
            Button btnSubmit = (Button) findViewById(R.id.btnSubmit);

            btnSubmit.setOnClickListener(clickListener);
            Button btnBack = (Button) findViewById(R.id.btnBack);

            if (jsonFileReader != null) {

                APP_NAME = jsonFileReader.getLabelObj("APP_TITLE");

                if (isTablet(this)) {
                    txtHeaderTitle.setText(APP_NAME);
                } else {
                    String txtTitle = jsonFileReader.getLabelObj("TITLE_FORM1");
                    if (txtTitle != null) {
                        txtHeaderTitle.setText(txtTitle);
                    }

                }

                String txtLIVE = jsonFileReader.getLabelObj("LBL_LIVE");
                if (txtLIVE != null) {
                    LBL_LIVE.setText(txtLIVE);
                }

                String txtSchool = jsonFileReader.getLabelObj("LBL_SEL_SCHOOL");
                if (txtSchool != null) {
                    LBL_SEL_SCHOOL.setText(txtSchool);
                }

                String txtIncident = jsonFileReader.getLabelObj("LBL_WHERE_INCIDENT");
                if (txtIncident != null) {
                    LBL_WHERE_INCIDENT.setText(txtIncident);
                }

                String txtWhenHappen = jsonFileReader.getLabelObj("LBL_WHEN_HAPPEN");
                if (txtWhenHappen != null) {
                    LBL_WHEN_HAPPEN.setText(txtWhenHappen);
                }

                String txtTIME = jsonFileReader.getLabelObj("LBL_TIME");
                if (txtTIME != null) {
                    LBL_TIME.setText(txtTIME);
                }

                String txtBULLY = jsonFileReader.getLabelObj("LBL_BULLY");
                if (txtBULLY != null) {
                    LBL_BULLY.setText(txtBULLY);
                }

                String txtBULLYPERSON = jsonFileReader.getLabelObj("LBL_BULLY_PERSON");
                if (txtBULLYPERSON != null) {
                    LBL_BULLY_PERSON.setText(txtBULLYPERSON);
                }

                String txtWHATHAPPEN = jsonFileReader.getLabelObj("LBL_WHAT_HAPPEN");
                if (txtWHATHAPPEN != null) {
                    LBL_WHAT_HAPPEN.setText(txtWHATHAPPEN);
                }

                String txtWHO = jsonFileReader.getLabelObj("LBL_WHO");
                if (txtWHO != null) {
                    LBL_WHO.setText(txtWHO);
                }

                String txtName = jsonFileReader.getLabelObj("LBL_NAME");
                if (txtName != null) {
                    LBL_NAME.setText(txtName);
                }

                String txtPhone = jsonFileReader.getLabelObj("LBL_PHONE");
                if (txtPhone != null) {
                    LBL_PHONE.setText(txtPhone);
                }

                String txtBtnSubmit = jsonFileReader.getLabelObj("BTN_SUBMIT");
                if (txtBtnSubmit != null) {
                    btnSubmit.setText(txtBtnSubmit);
                }

                String txtBtnOk = jsonFileReader.getLabelObj("BTN_OK");
                if (txtBtnOk != null) {
                    btn_diaOK = txtBtnOk;
                }

                String txtBtnCancel = jsonFileReader.getLabelObj("BTN_CANCEL");
                if (txtBtnCancel != null) {
                    btn_diaCancel = txtBtnCancel;
                }

                String txtBtnBack = jsonFileReader.getLabelObj("BTN_BACK");
                if (txtBtnBack != null) {
                    btnBack.setText("  " + txtBtnBack);
                }

                String txtBtnNext = jsonFileReader.getLabelObj("BTN_NEXT");
                if (txtBtnNext != null) {
                    btnHeaderNext.setText(txtBtnNext);
                }

            }
        } catch (Exception e) {
            Log.e("FormActivity", "setLabelNames() " + e, e);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            value_description = editDescription.getText().toString();
            String validateMsg = validateValues(true);
            if (validateMsg != null) {
                CustomAlertDialogue.showAlert(FormActivity.this, APP_NAME, validateMsg, btn_diaOK, null, null, null);
            }
            else {
                try {
                    HashMap<String, Object> submitparams = new HashMap<String, Object>();
                    submitparams.put("token", Constant.token);
                    //			submitparams.put("stateId",URLEncoder.encode(value_stateID,"utf-8"));

                    submitparams.put("school", URLEncoder.encode(value_schoolName, "utf-8"));

                    if (value_schoolID != null && !"".equalsIgnoreCase(value_schoolID))
                        submitparams.put("schoolId", URLEncoder.encode(value_schoolID, "utf-8"));
                    else
                        submitparams.put("schoolId", "");

                    submitparams.put("incidentLocation", URLEncoder.encode(value_incidentLocation, "utf-8"));
                    submitparams.put("incidentDescription", URLEncoder.encode(value_description, "utf-8"));
                    submitparams.put("incidentDate", URLEncoder.encode(btnDate.getText().toString(), "utf-8"));
                    submitparams.put("incidentTime", URLEncoder.encode(value_incidentTime, "utf-8"));

                    submitparams.put("howManyTimes", URLEncoder.encode(valueSituation, "utf-8"));
                    submitparams.put("reportedToAdult", URLEncoder.encode(valueIncidentAdult, "utf-8"));
                    //submitparams.put("reportedToAdultName", URLEncoder.encode(strWhois, "utf-8"));
                    if (strWhois != null && !"".equalsIgnoreCase(strWhois))
                        submitparams.put("reportedToAdultName", URLEncoder.encode(strWhois, "utf-8"));
                    else
                        submitparams.put("reportedToAdultName", "");

                    submitparams.put("SentFromMobile", URLEncoder.encode("1", "utf-8"));
                    submitparams.put("bullyName", URLEncoder.encode(editBullyperson.getText().toString(), "utf-8"));
                    submitparams.put("victimName", URLEncoder.encode(editPerson.getText().toString(), "utf-8"));
                    submitparams.put("postedBy", URLEncoder.encode(value_reporterType, "utf-8"));
                    submitparams.put("postedByName", URLEncoder.encode(editName.getText().toString(), "utf-8"));
                    submitparams.put("postedByName", URLEncoder.encode(editName.getText().toString(), "utf-8"));
                    //get POST image

                    String imgresponse = "";

                    if(imgUpload.getDrawable() != null) {
                        Bitmap bitmap = ((BitmapDrawable) imgUpload.getDrawable()).getBitmap();
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        byte[] byteArray = bytes.toByteArray();
                        String encodedImage = Base64.encodeToString(byteArray, 0);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String currentDateandTime = sdf.format(new Date());

                        HashMap<String, Object> imageparams = new HashMap<String, Object>();
                        imageparams.put("image", URLEncoder.encode(encodedImage, "utf-8"));
                        imageparams.put("name", URLEncoder.encode("image" + currentDateandTime + ".jpg", "utf-8"));
                        ETechAsyncTask subMitImageTask = new ETechAsyncTask(FormActivity.this, FormActivity.this, "+", imageparams, ETechAsyncTask.REQUEST_METHOD_POST);
                        imgresponse = subMitImageTask.execute("http://safeoregon.com/test.php").get();

                        submitparams.put("imagename", URLEncoder.encode("image" + currentDateandTime + ".jpg", "utf-8"));

                    }
                    else{
                        submitparams.put("imagename", "");
                    }
                    //			submitparams.put("postedByContactInfo",URLEncoder.encode(editPhNo.getText().toString(),"utf-8"));

                  /*  if (strWhois != null && !"".equalsIgnoreCase(strWhois))
                        submitparams.put("reportedToAdultName", URLEncoder.encode(strWhois, "utf-8"));
                    else
                        submitparams.put("reportedToAdultName","");*/
                    Log.i("SubmitParams: ", submitparams.toString());

                    ETechAsyncTask subMitDataTask = new ETechAsyncTask(FormActivity.this, FormActivity.this, "SubmitData", submitparams, ETechAsyncTask.REQUEST_METHOD_POST);
                    subMitDataTask.execute(Constant.submitIncidentURL);

                } catch (Exception e) {
                    Log.e("SubmitParams", "Error : " + e.getMessage());
                }
            }
        }
    };

    String strText = "";
    int delay = 1000;

    TextWatcher autoTxtwatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {

            autotxtSchoolName.setCompoundDrawables(null, null, autotxtSchoolName.getText()
                    .toString().equals("") ? null : x, null);
	/*		if(value_stateID==null || "".equalsIgnoreCase(value_stateID)){
				String alertStateMsg = "Please Select a State.";
				if(jsonFileReader != null){
					String ALT_STATE = jsonFileReader.getLabelObj("ALT_STATE");
					if(ALT_STATE != null) {alertStateMsg = ALT_STATE; }
				}
				CustomAlertDialogue.showAlert(FormActivity.this,null,alertStateMsg,btn_diaOK,null,null,null);
			}
			else{*/
            strText = text.toString();
            if (text.length() > 1) {
                if (start != 0) {
                    handler.removeMessages(2);
                    handler.sendMessageDelayed(handler.obtainMessage(2), delay);
                }
            }
            if (start != 0) {
                if (schoolNameList != null) {
                    schoolNameList.clear();
                }
                if (schoolCityList != null) {
                    schoolCityList.clear();
                }
                if (schoolIDList != null) {
                    schoolIDList.clear();
                }
            }
            //		}
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("token", Constant.token);

            ETechAsyncTask task = new ETechAsyncTask(FormActivity.this, FormActivity.this, "SchoolName", params, ETechAsyncTask.REQUEST_METHOD_POST);
            task.execute("http://report.sprigeo.com/schoolLookup?stateid=" + Constant.STATE_ID + "&q=" + URLEncoder.encode(strText) + "&lan=" + user_lang);
        }
    };

    OnItemSelectedListener SpStateItemSelectListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {

            value_stateID = stateIDList.get(position);/*Constant.stateID[position];*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };


    OnItemSelectedListener SpTimeItemSelectListener = new OnItemSelectedListener() {

        @SuppressLint("LongLogTag")
        @Override
        public void onItemSelected(AdapterView<?> spinnerAdapter, View arg1, int position, long arg3) {
            value_incidentTime = Constant.TimeID[position];
            Log.i("Time Spinner Select Value:", value_incidentTime);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    OnItemSelectedListener SpIncidentItemSelectListener = new OnItemSelectedListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onItemSelected(AdapterView<?> spinner, View arg1, int position, long arg3) {
            incidentValue = position;
            value_incidentLocation = incidentValueList.get(position);/*spinner.getItemAtPosition(position).toString()*/
            ;
            Log.i("incidentLocation Spinner Select Value:", value_incidentLocation);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };


    OnItemSelectedListener SpReportTypeItemSelectListener = new OnItemSelectedListener() {

        @SuppressLint("LongLogTag")
        @Override
        public void onItemSelected(AdapterView<?> spinnerAdapter, View arg1, int position, long arg3) {
            value_reporterType = reporterValueList.get(position)/*spinnerAdapter.getItemAtPosition(position).toString()*/;
            if (value_reporterType.equalsIgnoreCase(jsonFileReader.getLabelObj("LBL_SELECT"))) {
                value_reporterType = "";
            }
            Log.i("ReporterType Spinner Select Value:", value_reporterType);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    OnItemSelectedListener SpIncidentAdultItemSelectListener = new OnItemSelectedListener() {

        @SuppressLint("LongLogTag")
        @Override
        public void onItemSelected(AdapterView<?> spinnerAdapter, View arg1, int position, long arg3) {

            valueIncidentAdult = alistIncidentAdult.get(position)/*spinnerAdapter.getItemAtPosition(position).toString()*/;

            if (valueIncidentAdult != null && valueIncidentAdult.equalsIgnoreCase("Yes")) {
                edtWho.setVisibility(View.VISIBLE);
                //Toast.makeText(FormActivity.this, "Please Select any one", Toast.LENGTH_SHORT).show();
            } else if (valueIncidentAdult.equalsIgnoreCase(jsonFileReader.getLabelObj("LBL_SELECT")) || valueIncidentAdult != null && (valueIncidentAdult.equalsIgnoreCase("No"))) {
                edtWho.setVisibility(View.GONE);
                edtWho.setText("");
            }

            Log.i("SpIncidentAdultItemSelectListener", "Incient Adult : " + valueIncidentAdult);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    OnItemSelectedListener SpSituationItemSelectListener = new OnItemSelectedListener() {

        @SuppressLint("LongLogTag")
        @Override
        public void onItemSelected(AdapterView<?> spinnerAdapter, View arg1, int position, long arg3) {
            valueSituation = alistSituationList.get(position)/*spinnerAdapter.getItemAtPosition(position).toString()*/;
			/*if(valueSituation.equalsIgnoreCase(jsonFileReader.getLabelObj("LBL_SELECT"))){
				valueSituation="";
				//Toast.makeText(FormActivity.this, "Please Select any one", Toast.LENGTH_SHORT).show();
			}
*/
            Log.i("SpIncidentAdultItemSelectListener", "valueSituation : " + valueSituation);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    public void btnDateTimeClick(View v) {
        dateDialogflag = true;
        DatePickerDialog dialog = new DatePickerDialog(this, mDateSetListener, currentYear, currentMonth, currentDay);
        //------->
        //dialog.setTitle("Select Date");
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (dateDialogflag) {
                dateDialogflag = false;
                if (isDateAfter(view)) {
                    String msg = jsonFileReader.getLabelObj("MSG_PREVIOUS_DATE");
                    if (msg == null) {
                        msg = "Please Select Previous Date.";
                    }
                    CustomAlertDialogue.showAlert(FormActivity.this, APP_NAME, msg, btn_diaOK, btn_diaCancel, alertClick, null);
                } else {
                    btnDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                    currentYear = year;
                    currentMonth = monthOfYear;
                    currentDay = dayOfMonth;
                }
            }
        }

        private boolean isDateAfter(DatePicker tempView) {
            Calendar mCalendar = Calendar.getInstance();
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.set(tempView.getYear(), tempView.getMonth(), tempView.getDayOfMonth(), 0, 0, 0);
            if (tempCalendar.after(mCalendar))
                return true;
            else
                return false;
        }
    };

    OnClickListener alertClick = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            btnDateTimeClick(null);

        }
    };

    public void btnHeaderNextClick(View v) {

        String validateMsg = null;
        if (formFlipper.getDisplayedChild() == 0)
            validateMsg = validateValues(false);
        else if (formFlipper.getDisplayedChild() == 1) {
            value_description = editDescription.getText().toString();
            validateMsg = validateValues(true);
        } else if (formFlipper.getDisplayedChild() == 2) {
            validateMsg = validate();
        }

        if (validateMsg != null) {
            CustomAlertDialogue.showAlert(this, APP_NAME, validateMsg, btn_diaOK, null, null, null);
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editDescription.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (formFlipper.getDisplayedChild() == 2) {
                btnHeaderNext.setVisibility(View.GONE);
            }
            formFlipper.showNext();
            changeHeaderTitle();
        }

    }

    private String validate() {


        if (valueSituation != null && valueSituation.equalsIgnoreCase(jsonFileReader.getLabelObj("LBL_SELECT"))) {

            return "Please Select another Situation.";
        }

        if (valueIncidentAdult != null && valueIncidentAdult.equalsIgnoreCase(jsonFileReader.getLabelObj("LBL_SELECT"))) {
            //	edtWho.setVisibility(View.GONE);
            return "Please Select another Incident Adult.";
        }


        if (edtWho.getVisibility() == View.VISIBLE) {

            strWhois = edtWho.getText().toString().trim();

            if (strWhois == null || strWhois.trim().equalsIgnoreCase("")) {

                return "Please Enter Value.";
            }
        }

        return null;
    }

    private String validateValues(Boolean checkDescription) {


        if (value_schoolName == null || value_schoolName.trim().length() <= 0) {
            value_schoolName = autotxtSchoolName.getText().toString().trim();
        }


        if (value_schoolName == null || value_schoolName.trim().equalsIgnoreCase("")) {
			/*if(jsonFileReader!= null){
				String ALT_SCHOOL = jsonFileReader.getLabelObj("ALT_SCHOOL");
				if(ALT_SCHOOL != null) { return ALT_SCHOOL; }
			}*/
            return "School name is Required.";
        } else if (incidentValue == 0) {
            if (jsonFileReader != null) {
                String ALT_INCIDENT = jsonFileReader.getLabelObj("ALT_INCIDENT");
                if (ALT_INCIDENT != null) {
                    return ALT_INCIDENT;
                }
            }
            return "Incident is Required.";
        } else if (checkDescription) {
            if (value_description == null || "".equalsIgnoreCase(value_description)) {
                if (jsonFileReader != null) {
                    String ALT_DESC = jsonFileReader.getLabelObj("ALT_DESC");
                    if (ALT_DESC != null) {
                        return ALT_DESC;
                    }
                }
                return "Description is Required.";
            }
        }
        return null;
    }

    @Override
    public void onTaskComplete(int statusCode, String result, String webserviceCb) {

        if (statusCode == ETechAsyncTask.COMPLETED) {
            if (webserviceCb.equalsIgnoreCase("SchoolName")) {
                Log.i("Result", result);
                try {

                    JSONArray schoolNames = new JSONArray(result);
                    for (int i = 0; i < schoolNames.length(); i++) {
                        JSONObject school = schoolNames.getJSONObject(i);
                        schoolNameList.add(school.getString("school"));
                        schoolCityList.add(school.getString("city"));
                        schoolIDList.add(school.getString("id"));
                    }
                    Log.i("schoolNameList", schoolNameList.toString());
                    Log.i("schoolCityList", schoolCityList.toString());
                    Log.i("schoolIDList", schoolIDList.toString());

                    ArrayAdapter<String> schoolListadapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, schoolNameList);
                    autotxtSchoolName.setAdapter(schoolListadapter);
                    autotxtSchoolName.showDropDown();
                } catch (JSONException e) {
                    Log.e("JSONError:" + webserviceCb, " Error: " + e, e);
                }
            } else if (webserviceCb.equalsIgnoreCase("SubmitData")) {
                Log.i("Submit Response: ", result);
                String original_response="";
                try {

                    String substr = "<BR>";
                    if(result.contains("<BR>")) {
                        String before = result.substring(0, result.indexOf(substr));
                        original_response = result.substring(result.indexOf(substr) + substr.length());
                    }
                    else
                        original_response=result;

                    //	Log.d("TestStrinng","Before Data : "+before);
                    //	Log.d("TestStrinng","After Data : "+original_response);
                    JSONArray response = new JSONArray(original_response);
                    String submitsuccess = response.getJSONObject(0).getString("success");
                    String message = response.getJSONObject(0).getString("message");
                    if (submitsuccess.equalsIgnoreCase("true")) {
                        String reportSubmitMsg = "Your Report has been sent. Thanks for using Sprigeo";
                        if (jsonFileReader != null) {
                            String ALT_MSG = jsonFileReader.getLabelObj("ALT_REPORT_SUBMT");
                            if (ALT_MSG != null) {
                                reportSubmitMsg = ALT_MSG;
                            }
                        }
                        CustomAlertDialogue.showAlert(this, APP_NAME, reportSubmitMsg, btn_diaOK, null, succesfulSubmitAlertClick, null);
                    } else {
                        CustomAlertDialogue.showAlert(this, APP_NAME, message, btn_diaOK, null, succesfulSubmitAlertClick, null);
                    }
                } catch (JSONException e) {
                    CustomAlertDialogue.showAlert(this, "Error", "Error Occured.", btn_diaOK, null, null, null);
                    e.printStackTrace();
                }
            }
			/*else if(webserviceCb.equalsIgnoreCase("IncidentList")){
				try {
					JSONArray response=new JSONArray(result);
					incidentLocationList=new ArrayList<String>();
					incidentLocationList.add("---Select---");
					for(int i=0;i<response.length();i++){
						incidentLocationList.add(response.getJSONObject(i).getString("location"));
					}
					if(incidentLocationList!=null){
						ArrayAdapter<String> incidentLocationListadapter=new AutoCompleteAdapter(this,android.R.layout.simple_spinner_item,incidentLocationList);
						incidentLocationListadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spIncident.setAdapter(incidentLocationListadapter);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}*/
        } else if (statusCode == ETechAsyncTask.ERROR_NETWORK) {
            String msg = jsonFileReader.getLabelObj("ALT_NETWORK");
            if (msg == null) {
                msg = result;
            }
            CustomAlertDialogue.showAlert(this, APP_NAME, msg, btn_diaOK, null, null, null);
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Gallery Image", "Gallery Video", "Capture Video", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					/*intent.putExtra(MediaStore.EXTRA_OUTPUT,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 0);
					intent.putExtra("aspectY", 0);
					intent.putExtra("outputX", 200);
					intent.putExtra("outputY", 150);
					intent.putExtra("return-data", true);*/
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Gallery Image")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_IMAGE_FILE);

                } else if (items[item].equals("Gallery Video")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Video"),
                            SELECT_VIDEO_FILE);
                } else if (items[item].equals("Capture Video")) {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                    // start the video capture Intent
                    startActivityForResult(intent, VIDEO_CAPTURE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

//				performCrop(data.getData());

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgUpload.setVisibility(View.VISIBLE);

                imgUpload.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_IMAGE_FILE) {
                Uri selectedImageUri = data.getData();
                performCrop(selectedImageUri);

            } else if (requestCode == SELECT_VIDEO_FILE || requestCode == VIDEO_CAPTURE) {
                // get the returned data
                try {
                    Uri selectedImageUri = data.getData();

                    // performCrop(selectedImageUri);

                    String video_path = getPath(selectedImageUri);



                    Intent intent = new Intent(FormActivity.this, VideoTrimActivity.class);
                    intent.putExtra("filepath", video_path);
                    startActivityForResult(intent, REQUEST_CODE_VIDEO_TRIM);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } /*else if (requestCode == VIDEO_CAPTURE) {
              *//*  File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".mp4");

                try {
                    destination.createNewFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*

                Uri videoUri = data.getData();


                String video_path = getPath(videoUri);

                Bitmap bmThumbnail;
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(video_path,
                        MediaStore.Video.Thumbnails.MICRO_KIND);
                imgUpload.setVisibility(View.VISIBLE);
                imgUpload.setImageBitmap(bmThumbnail);

            }*/ else if (requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                //	ImageView picView = (ImageView) findViewById(R.id.picture);
                imgUpload.setVisibility(View.VISIBLE);
                imgUpload.setImageBitmap(thePic);
            } else if (requestCode == REQUEST_CODE_VIDEO_TRIM) {
                if (data.hasExtra("filepath")) {
                    String trimFilepath = data.getStringExtra("filepath");
                   // Toast.makeText(FormActivity.this, trimFilepath + "", Toast.LENGTH_LONG).show();

                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(trimFilepath,
                            MediaStore.Video.Thumbnails.MICRO_KIND);
                    imgUpload.setVisibility(View.VISIBLE);
                    imgUpload.setImageBitmap(bmThumbnail);
                }
            }

        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void performCrop(Uri picUri) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        btnHeaderBackClick(null);
    }


    public void btnHeaderBackClick(View v) {
        if (formFlipper.getDisplayedChild() == 0) {
            finish();
        } else {
            formFlipper.showPrevious();
            changeHeaderTitle();
            if (formFlipper.getDisplayedChild() < 3) {
                btnHeaderNext.setVisibility(View.VISIBLE);
            }
        }

    }

    public void changeHeaderTitle() {

        if (formFlipper.getDisplayedChild() == 0) {
            if (jsonFileReader != null) {
                String titleForm1 = jsonFileReader.getLabelObj("TITLE_FORM1");
                txtHeaderTitle.setText(titleForm1);
            }
        } else if (formFlipper.getDisplayedChild() == 1) {
            if (jsonFileReader != null) {
                String titleForm2 = jsonFileReader.getLabelObj("TITLE_FORM2");
                txtHeaderTitle.setText(titleForm2);
            }
        } else if (formFlipper.getDisplayedChild() == 2) {
            //		if(jsonFileReader !=null){
            String titleForm3 = jsonFileReader.getLabelObj("TITLE_FORM3");
            txtHeaderTitle.setText("Form 3");
            //	}
        } else if (formFlipper.getDisplayedChild() == 3) {
            if (jsonFileReader != null) {
                String titleForm3 = jsonFileReader.getLabelObj("TITLE_FORM3");
                txtHeaderTitle.setText(titleForm3);
            }
        }
    }

    OnClickListener succesfulSubmitAlertClick = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(FormActivity.this, ThankYouActivity.class);
            startActivity(intent);
            finish();
        }
    };
}