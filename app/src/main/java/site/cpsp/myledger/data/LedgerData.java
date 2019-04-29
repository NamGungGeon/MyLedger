package site.cpsp.myledger.data;

import java.io.Serializable;

public class LedgerData implements Serializable{
    public String name;
    public String time;
    public String description;
    public int price;

    //isBond== true 채권
    //isBond== false 채무
    boolean isBond;

    public LedgerData() {
    }

    public LedgerData(String name, String time, String description, int price, boolean isBond) {
        this.name = name;
        this.time = time;
        this.description = description;
        this.price = price;
        this.isBond = isBond;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public boolean isBond() {
        return isBond;
    }

}

