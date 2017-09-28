package com.etech.util;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class JSONFileReader {

	public JSONObject jsonObj = null;
	String response;

	public JSONFileReader(String filePath) {

		Log.d("JsonPath","Path : "+filePath);

		try{
		/*	BufferedReader inputReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath)));
			String inputString;
			StringBuffer stringBuffer = new StringBuffer();
			while ((inputString = inputReader.readLine()) != null) {
				stringBuffer.append(inputString + "\n");
			}*/
			jsonObj = new JSONObject(filePath);

		}
		catch (Exception e) {
			Log.e("JSONFileReader",e.getMessage());
		}
	}
	
	/*public JSONFileReader getLangFile(String filePath) {
		JSONFileReader fileReader = null;
		try{
			fileReader = new JSONFileReader();
			BufferedReader inputReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath)));
			String inputString;
			StringBuffer stringBuffer = new StringBuffer();
			while ((inputString = inputReader.readLine()) != null) {
				stringBuffer.append(inputString + "\n");
			}
			jsonObj = new JSONObject(stringBuffer.toString());

		}
		catch (Exception e) {
			fileReader = null;
			Log.e("JSONFileReader",e.getMessage());
		}
		return fileReader;
	}*/

	public String getLabelObj(String labelName){
		if(jsonObj != null){
			try{
				JSONObject jsonLabelObj = jsonObj.getJSONObject("Labels");
				return jsonLabelObj.getString(labelName);

			}
			catch (Exception e) {
				Log.e("JSONFileReader", "getLabelObj()  "+e,e);
			}
		}
		return null;
	}

	public ArrayList<ArrayList<String>> getstateList(){
		if(jsonObj != null){
			try
			{
				ArrayList<ArrayList<String>> stateList = new ArrayList<ArrayList<String>>();
				ArrayList<String> stateNames = new ArrayList<String>();
				ArrayList<String> stateID = new ArrayList<String>();
				stateID.add("");
				JSONObject stateObj = jsonObj.getJSONObject("CMB_STATE");
				Iterator keys = stateObj.keys();

				while(keys.hasNext()) {
					String key = (String) keys.next();
					stateNames.add(key);
					/*stateID.add(stateObj.getString(key));*/
				}
				Collections.sort(stateNames, new Comparator<String>() {
					@Override
					public int compare(String s1, String s2) {
						return s1.compareToIgnoreCase(s2);
					}
				});
				for (int i = 0; i < stateNames.size(); i++) {
					stateID.add(stateObj.getString(stateNames.get(i)));
				}
				stateNames.add(0,getLabelObj("LBL_SELECT"));
				
				stateList.add(stateID);
				stateList.add(stateNames);
				
				Log.i("StateNames", stateNames.toString());
				Log.i("StateId", stateID.toString());
				
				return stateList;
			}
			catch (Exception e) {
				Log.e("JSONFileReader","stateList()  "+e,e);
			}
		}
		return null;
	}

	public ArrayList<String> getincidentList(){
		if(jsonObj != null){
			try
			{
				ArrayList<String> incidentList = new ArrayList<String>();
				incidentList.add(getLabelObj("LBL_SELECT"));
				JSONObject incidentObj = jsonObj.getJSONObject("CMB_INCIDENT");
				Iterator keys = incidentObj.keys();

				while(keys.hasNext()) {
					String key = (String) keys.next();
					incidentList.add(key);
				}
				return incidentList;
			}
			catch (Exception e) {
				Log.e("JSONFileReader","incidentList()  "+e,e);
			}
		}
		return null;
	}
	
	public ArrayList<String> getincidentListValue(){
		if(jsonObj != null){
			try
			{
				ArrayList<String> incidentList = new ArrayList<String>();
				incidentList.add(getLabelObj("LBL_SELECT"));
				JSONObject incidentObj = jsonObj.getJSONObject("CMB_INCIDENT");
				Iterator keys = incidentObj.keys();

				while(keys.hasNext()) {
					String key = (String) keys.next();
					incidentList.add(incidentObj.getString(key));
				}
				return incidentList;
			}
			catch (Exception e) {
				Log.e("JSONFileReader","getincidentListValue()  "+e,e);
			}
		}
		return null;
	}
	

	public ArrayList<String> getReporterList(){
		if(jsonObj != null){
			try
			{
				ArrayList<String> reporterList = new ArrayList<String>();
				reporterList.add(getLabelObj("LBL_SELECT"));
				JSONObject reportertObj = jsonObj.getJSONObject("CMB_REPORTER");
				Iterator keys = reportertObj.keys();

				while(keys.hasNext()) {
					String key = (String) keys.next();
					reporterList.add(key);
				}
				return reporterList;
			}
			catch (Exception e) {
				Log.e("JSONFileReader","reporterList()  "+e,e);
			}
		}
		return null;
	}
	
	public ArrayList<String> getReporterListValue(){
		if(jsonObj != null){
			try
			{
				ArrayList<String> reporterList = new ArrayList<String>();
				reporterList.add(getLabelObj("LBL_SELECT"));
				JSONObject reportertObj = jsonObj.getJSONObject("CMB_REPORTER");
				Iterator keys = reportertObj.keys();

				while(keys.hasNext()) {
					String key = (String) keys.next();
					reporterList.add(reportertObj.getString(key));
				}
				return reporterList;
			}
			catch (Exception e) {
				Log.e("JSONFileReader","reporterList()  "+e,e);
			}
		}
		return null;
	}
	
}
