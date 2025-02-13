package jp.ac.jec.cm0117.scoutapp;

public class SaimokuDataItem {
    private String KaikyuID;
    private String KaikyuTheme;
    private String FirstID;
    private String SecondID;
    private String ThirdID;
    private String GenreText;
    private String DaimokuText;
    private String CompletedDate;
    private String SyouninsyaName;

    public void setKaikyuID(String kaikyuID) {
        this.KaikyuID = kaikyuID;
    }

    public String getKaikyuID() {
        return KaikyuID;
    }

    public void setKaikyuTheme(String kaikyuTheme) {
        this.KaikyuTheme = kaikyuTheme;
    }

    public String getKaikyuTheme() {
        return KaikyuTheme;
    }

    public void setFirstID(String firstID) {
        this.FirstID = firstID;
    }

    public String getFirstID() {
        return FirstID;
    }

    public void setSecondID(String secondID) {
        this.SecondID = secondID;
    }

    public String getSecondID() {
        return SecondID;
    }

    public void setThirdID(String thirdID) {
        this.ThirdID = thirdID;
    }

    public String getThirdID() {
        return ThirdID;
    }

    public void setGenreText(String genreText) {
        this.GenreText = genreText;
    }

    public String getGenreText() {
        return GenreText;
    }

    public void setDaimokuText(String daimokuText) {
        this.DaimokuText = daimokuText;
    }

    public String getDaimokuText() {
        return DaimokuText;
    }

    public void setCompletedDate(String completedDate) {
        this.CompletedDate = completedDate;
    }

    public String getCompletedDate() {
        return CompletedDate;
    }

    public void setSyouninsyaName(String syouninsyaName) {
        this.SyouninsyaName = syouninsyaName;
    }

    public String getSyouninsyaName() {
        return SyouninsyaName;
    }
}
