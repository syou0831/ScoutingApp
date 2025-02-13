package jp.ac.jec.cm0117.scoutapp;

import android.util.Log;

public class PersonDataItem {
    private String TAG = "TAG";
    private String Name;
    private String NameFurigana;
    private String Birthday;
    private String Address;
    private String Tel;
    private String Sex;
    private String Prefecture;
    private String Area;
    private String City;
    private String GroupNum;

    public void setName(String name) {
        this.Name = name;
        Log.d(TAG, "setName: "+name);
    }

    public String getName() {
        return Name;
    }

    public void setNameFurigana(String nameFurigana) {
        this.NameFurigana = nameFurigana;
        Log.d(TAG, "setNameFurigana: "+nameFurigana);
    }

    public String getNameFurigana() {
        return NameFurigana;
    }

    public void setBirthday(String birthday) {
        this.Birthday = birthday;
        Log.d(TAG, "setBirthday: "+birthday);
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setAddress(String address) {
        this.Address = address;
        Log.d(TAG, "setAddress: "+address);
    }

    public String getAddress() {
        return Address;
    }

    public void setSex(String sex) {
        this.Sex = sex;
        Log.d(TAG, "setSex: "+sex);
    }

    public String getSex() {
        return Sex;
    }

    public void setTel(String tel) {
        this.Tel = tel;
        Log.d(TAG, "setTel: "+tel);
    }

    public String getTel() {
        return Tel;
    }

    public void setPrefecture(String prefecture) {
        this.Prefecture = prefecture;
        Log.d(TAG, "setPrefecture: "+prefecture);
    }

    public String getPrefecture() {
        return Prefecture;
    }

    public void setArea(String area) {
        this.Area = area;
        Log.d(TAG, "setArea: "+area);
    }

    public String getArea() {
        return Area;
    }

    public void setCity(String city) {
        this.City = city;
        Log.d(TAG, "setCity: "+city);
    }

    public String getCity() {
        return City;
    }

    public void setGroupNum(String groupNum) {
        this.GroupNum = groupNum;
        Log.d(TAG, "setGroupNum: "+groupNum);
    }

    public String getGroupNum() {
        return GroupNum;
    }
}
