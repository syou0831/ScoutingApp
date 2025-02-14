package jp.ac.jec.cm0117.scoutapp;

public class ResponseItem {
    private boolean response;
    private int PersonID;

    public void setResponse(boolean response) {
        this.response = response;
    }

    public boolean getResponse() {
        return response;
    }

    public void setPersonID(int personID) {
        this.PersonID = personID;
    }

    public int getPersonID() {
        return PersonID;
    }
}
