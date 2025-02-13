package jp.ac.jec.cm0117.scoutapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PersonJsonHelper {

    public static ArrayList<PersonDataItem> parseJson(String strJson) {
        ArrayList<PersonDataItem> list = new ArrayList<>();
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

    private static PersonDataItem parseToItem(JSONObject json) throws JSONException {
        PersonDataItem person = new PersonDataItem();
        Log.d("TAG", "parseToItem: CCCCCCCCCCC");
        person.setName(json.getString("Name"));
        person.setNameFurigana(json.getString("NameFurigana"));
        person.setBirthday(json.getString("Birthday"));
        person.setAddress(json.getString("Address"));
        person.setTel(json.getString("Tel"));
        person.setSex(json.getString("Sex"));
        person.setPrefecture(json.getString("Prefecture"));
        person.setArea(json.getString("Area"));
        person.setCity(json.getString("City"));
        person.setGroupNum(json.getString("GroupNum"));

        return person;
    }


}
