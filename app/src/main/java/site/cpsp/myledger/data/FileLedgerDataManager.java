package site.cpsp.myledger.data;

import android.content.Context;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileLedgerDataManager implements LedgerDataManager {
    private File repository;
    private final String fileName= "ledgerData.txt";
    private static FileLedgerDataManager inst;
    private Context context;

    private FileLedgerDataManager(Context context) throws Exception{
        this.context= context;
        repository= new File(context.getFilesDir(), fileName);
        if(!repository.exists()){
            repository.createNewFile();
        }
        readFromFile();
    }
    public static FileLedgerDataManager getInst(Context context) throws Exception{
        if(inst== null)
            inst= new FileLedgerDataManager(context);

        return inst;
    }

    private void readFromFile() throws Exception{
        //flush ledgerList
        for(LedgerData data: ledgerList){
            ledgerList.remove(data);
        }
        //new ledgerList is empty

        ObjectInputStream iStream= null;

        try{
            iStream= new ObjectInputStream(new FileInputStream(repository));
            LedgerData readObj;
            while((readObj= (LedgerData)iStream.readObject())!= null){
                ledgerList.add(readObj);
            }

        }catch (EOFException e){
            e.printStackTrace();
        }
        if(iStream!= null) iStream.close();
    }

    private boolean saveToFile(){
        ObjectOutputStream oStream= null;
        try {
            oStream= new ObjectOutputStream(new FileOutputStream(repository));
            for(LedgerData data: ledgerList) {
                oStream.writeObject(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "파일을 열 수 없습니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(oStream!= null){
            try {
                oStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void flushData(Callback callback) {
        try {
            boolean result= repository.delete();
            if(!result)
                callback.call(false);
            readFromFile();
        } catch (Exception e) {
            callback.call(false);
            e.printStackTrace();
        }
        callback.call(true);
    }

    @Override
    public int getTotalBond() {
        int bonds= 0;
        List<String> names= getNames();
        for(String name: names){
            bonds+= getPersonTotalBond(name)- getPersonTotalDebt(name)>0 ? getPersonTotalBond(name): 0;
        }
        return bonds;
    }

    @Override
    public int getTotalDebt() {
        int debts= 0;
        List<String> names= getNames();
        for(String name: names){
            debts+= getPersonTotalDebt(name)- getPersonTotalBond(name)>0 ? getPersonTotalDebt(name): 0;
        }
        return debts;
    }

    @Override
    public int getPersonTotalBond(String name) {
        int total= 0;
        for(LedgerData data: ledgerList){
            if(data.isBond() && data.getName().equals(name))
                total+= data.getPrice();
        }
        return total;
    }

    @Override
    public int getPersonTotalDebt(String name) {
        int total= 0;
        for(LedgerData data: ledgerList){
            if(!data.isBond() && data.getName().equals(name))
                total+= data.getPrice();
        }
        return total;
    }

    @Override
    public void addData(LedgerData data, Callback callback) {
        //wait!
        ledgerList.add(data);
        boolean success= saveToFile();

        if(callback!= null) callback.call(success);

        if(!success){
            ledgerList.remove(data);
        }
    }

    @Override
    public List<String> getNames() {
        List<String> names= new ArrayList<>();
        for(LedgerData data: ledgerList){
            boolean isExist= false;
            for(String name: names){
                if(name.equals(data.getName())){
                    isExist= true;
                    break;
                }
            }
            if(!isExist){
                names.add(data.getName());
            }
        }
        return names;
    }

    @Override
    public List<LedgerData> getPersonDataList(String name) {
        List<LedgerData> list= new ArrayList<>();
        for(LedgerData data: ledgerList){
            if(data.getName().equals(name))
                list.add(new LedgerData(data.getName(), data.getTime(), data.getDescription(), data.getPrice(), data.isBond()));
        }
        return list;
    }
}
