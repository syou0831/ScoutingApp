package jp.ac.jec.cm0117.scoutapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResponseJsonHelper {

    public static ArrayList<ResponseItem> parseJson(String strJson) {
        ArrayList<ResponseItem> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(strJson);
            for (int i = 0; i < array.length(); i++) {
                JSONObject entry = array.getJSONObject(i);
                list.add(parseToItem(entry));
            }
        } catch (JSONException e) {
            Log.d("JsonHelper", "JSON parsing error: " + e.getMessage(), e);
        }
        Log.d("JsonHelper", "Parsed List: " + list);
        return list;
    }

    private static ResponseItem parseToItem(JSONObject json) throws JSONException {
        ResponseItem person = new ResponseItem();
        Log.d("TAG", "parseToItem: CCCCCCCCCCC");
        person.setResponse(json.getBoolean("response"));
        person.setPersonID(json.getInt("ID"));
        return person;
    }
}
