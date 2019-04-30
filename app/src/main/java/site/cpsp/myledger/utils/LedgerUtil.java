package site.cpsp.myledger.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import site.cpsp.myledger.data.LedgerData;
import site.cpsp.myledger.utils.TimeUtils;

/**
 * Created by WINDOWS7 on 2019-04-29.
 */

public class LedgerUtil {
    private LedgerUtil(){}

    public static List<String> sortByName(List<String> names){
        Collections.sort(names, (s, t1) -> -s.compareTo(t1));
        return names;
    }
    public static String priceDivider(int p){
        String price= String.valueOf(Math.abs(p));
        String result= "";
        int cnt= 1;
        for(int i= price.length()-1; i>=0; i--){
            result+= price.charAt(i);
            if(i!= 0 && cnt%3== 0){
                result+= ",";
            }
            cnt++;
        }
        result= new StringBuilder(result).reverse().toString();
        return result;
    }
    public static List<LedgerData> sortByTime(List<LedgerData> ledgers){
        Collections.sort(ledgers, (ledgerData, t1) -> -TimeUtils.getDiff(ledgerData.getTime(), t1.getTime()));
        return ledgers;
    }
}
