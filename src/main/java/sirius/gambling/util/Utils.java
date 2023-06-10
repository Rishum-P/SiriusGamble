package sirius.gambling.util;

import java.text.DecimalFormat;

public class Utils {

    public static double doubleRoundTo2Decimals(double val){
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }
}
