package se.ju.group8.depot;

/**
 * Created by hama17zp on 2017-12-12.
 *
 */



public class MyDate{
    private int YY;
    private int MM;
    private int DD;

    MyDate(){}

    MyDate(int MM, int DD, int YY){
        this.MM = MM;
        this.DD = DD;
        this.YY = YY;
    }

    public int getYY() {
        return YY;
    }

    public void setYY(int YY) {
        this.YY = YY;
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