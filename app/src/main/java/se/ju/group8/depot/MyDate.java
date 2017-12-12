package se.ju.group8.depot;

/**
 * Created by hama17zp on 2017-12-12.
 *
 */



public class MyDate{
    private int YYYY;
    private int MM;
    private int DD;

    MyDate(){}

    MyDate(int MM, int DD, int YYYY){
        this.MM = MM;
        this.DD = DD;
        this.YYYY = YYYY;
    }

    public int getYY() {
        return YYYY;
    }

    public void setYY(int YYYY) {
        this.YYYY = YYYY;
    }

    public int getMM() {
        return MM;
    }

    public void setMM(int MM) {
        this.MM = MM;
    }

    public int getDD() {
        return DD;
    }

    public void setDD(int DD) {
        this.DD = DD;
    }
}