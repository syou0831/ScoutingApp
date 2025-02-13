package jp.ac.jec.cm0117.scoutapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SaimokuJsonHelper {

    private static final String TAG = "SaimokuJsonHelper";

    public static ArrayList<SaimokuDataItem> parseJson(String strJson) {
        ArrayList<SaimokuDataItem> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(strJson);
            for (int i = 0; i < array.length(); i++) {
                JSONObject entry = array.getJSONObject(i);
                list.add(parseToItem(entry));
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSON parsing error: " + e.getMessage(), e);
        }
        Log.d(TAG, "Parsed List: " + list.size() + "件");
        return list;
    }

    private static SaimokuDataItem parseToItem(JSONObject json) throws JSONException {
        SaimokuDataItem saimoku = new SaimokuDataItem();
        Log.d(TAG, "parseToItem: データを解析中");

        saimoku.setKaikyuID(json.optString("KaikyuID"));
        saimoku.setKaikyuTheme(json.optString("KaikyuTheme"));
        saimoku.setFirstID(json.optString("FirstID"));
        saimoku.setSecondID(json.optString("SecondID"));
        saimoku.setThirdID(json.optString("ThirdID"));
        saimoku.setGenreText(json.optString("GenreText"));
        saimoku.setDaimokuText(json.optString("DaimokuText"));
        saimoku.setCompletedDate(json.optString("CompletedDate"));
        saimoku.setSyouninsyaName(json.optString("SyouninsyaName"));

        return saimoku;
    }
}
