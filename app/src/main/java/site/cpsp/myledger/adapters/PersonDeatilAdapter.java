package site.cpsp.myledger.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import site.cpsp.myledger.R;
import site.cpsp.myledger.data.LedgerData;
import site.cpsp.myledger.data.LedgerDataManager;
import site.cpsp.myledger.data.LedgerFactory;

public class PersonDeatilAdapter extends RecyclerView.Adapter<PersonDeatilAdapter.Holder>{

    private LedgerDataManager ledgerDataManager;
    private Context context;
    private String tName;
    private List<LedgerData> ledgers;

    public PersonDeatilAdapter(LedgerDataManager ledgerDataManager, Context context, String tName) {
        this.ledgerDataManager = ledgerDataManager;
        this.context = context;
        this.tName = tName;
        ledgers= ledgerDataManager.getPersonDataList(tName);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_timeline, viewGroup, false);
        return new PersonDeatilAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        TextView desc= holder.desc;
        TextView status= holder.status;
        TextView result= holder.result;
        TextView time= holder.time;

        LedgerData currentData= ledgers.get(i);
        desc.setText(currentData.getDescription().equals("")? "?": currentData.getDescription());
        time.setText(currentData.getTime());
        if(currentData.isBond()){
            status.setText("내가 "+ LedgerFactory.priceDivider(currentData.getPrice())+ "원을 빌려주었(갚았)습니다");
            status.setTextColor(context.getResources().getColor(R.color.safe));
            holder.itemView.setBackground(context.getDrawable(R.drawable.leftbordersafe));
        }else{
            status.setText("내가 "+ LedgerFactory.priceDivider(currentData.getPrice())+ "원을 빌렸(돌려받았)습니다");
            status.setTextColor(context.getResources().getColor(R.color.warning));
            holder.itemView.setBackground(context.getDrawable(R.drawable.leftborderwarning));
        }
        result.setText("");
    }

    @Override
    public int getItemCount() {
        return ledgers.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        public TextView desc;
        public TextView status;
        public TextView result;
        public TextView time;
        public Holder(@NonNull View itemView) {
            super(itemView);
            desc= itemView.findViewById(R.id.tList_desc);
            status= itemView.findViewById(R.id.tList_status);
            result= itemView.findViewById(R.id.tList_result);
            time= itemView.findViewById(R.id.tList_time);
        }
    }
}
