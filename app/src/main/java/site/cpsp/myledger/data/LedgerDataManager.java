package site.cpsp.myledger.data;

import java.util.ArrayList;
import java.util.List;

public interface LedgerDataManager {
    interface Callback{
        void call(boolean isSuccess);
    }
    List<LedgerData> ledgerList = new ArrayList<>();

    void removeAllData(Callback callback);
    int getTotalBond();
    int getTotalDebt();
    int getPersonTotalBond(String name);
    int getPersonTotalDebt(String name);
    void addData(LedgerData data, Callback callback);
    List<String> getNames();
    List<LedgerData> getPersonDataList(String name);
}
