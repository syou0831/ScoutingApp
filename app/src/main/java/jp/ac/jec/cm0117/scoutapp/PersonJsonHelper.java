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
        person.setStateDate(json.getString("StateDate"));
        person.setStateField(json.getString("StateField"));
        person.setInBVS(json.getString("inBVS"));
        person.setInCS(json.getString("inCS"));
        person.setInBS(json.getString("inBS"));
        person.setInVS(json.getString("inVS"));
        person.setInRS(json.getString("inRS"));
        person.setMinarai(json.getString("Minarai"));
        person.setBasic(json.getString("Basic"));
        person.setSecond(json.getString("Second"));
        person.setFirst(json.getString("First"));
        person.setKiku(json.getString("Kiku"));
        person.setHayabusa(json.getString("Hayabusa"));
        person.setFuji(json.getString("Fuji"));
        person.setSinkouSyourei(json.getString("SinkouSyourei"));
        person.setSyukyou(json.getString("Syukyou"));
        person.setSyukyouName(json.getString("SyukyouName"));


        return person;
    }


}
