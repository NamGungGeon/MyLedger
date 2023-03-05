package site.cpsp.myledger.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import site.cpsp.myledger.DetailLedgerActivity;
import site.cpsp.myledger.R;
import site.cpsp.myledger.data.LedgerDataManager;
import site.cpsp.myledger.utils.LedgerUtil;
import site.cpsp.myledger.utils.SimpleDialogUtil;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.VHolder> {
    private LedgerDataManager ledgerManager;
    private AppCompatActivity context;
    private List<String> names;


    public PersonListAdapter(LedgerDataManager ledgerDataManager, AppCompatActivity context) {
        this.context= context;
        this.ledgerManager= ledgerDataManager;
        this.names= ledgerDataManager.getNames();
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_people, viewGroup, false);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int i) {

        String tName= names.get(i);
        TextView name= holder.name;
        name.setText(tName);

        TextView debt= holder.debt;
        TextView bond= holder.bond;
        TextView empty= holder.empty;
        int subtract= ledgerManager.getPersonTotalBond(tName)- ledgerManager.getPersonTotalDebt(tName);
        if(subtract>0){
            bond.setText("에게 "+ LedgerUtil.priceDivider(Math.abs(subtract))+ "원을 받아야 합니다");
            debt.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            holder.itemView.setBackground(context.getDrawable(R.drawable.leftbordersafe));
        }else if(subtract<0){
            debt.setText("에게 "+ LedgerUtil.priceDivider(Math.abs(subtract))+ "원을 갚아야 합니다");
            bond.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            holder.itemView.setBackground(context.getDrawable(R.drawable.leftborderwarning));
        }else{
            bond.setVisibility(View.GONE);
            debt.setVisibility(View.GONE);
            empty.setText("깔ㅡ끔합니다!");
            holder.itemView.setBackground(context.getDrawable(R.drawable.leftbordernormal));
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent= new Intent(context, DetailLedgerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("tName", tName);
            context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(view -> {
            SimpleDialogUtil dialog= new SimpleDialogUtil();
            dialog.setValue(tName+ "의 장부를 전부 삭제하시겠습니까?", "예", "아니오",
                    ()->{
                        ledgerManager.removeData(tName, (isSuccess -> {
                            if(isSuccess){
                                Toast.makeText(context, tName+ "의 장부 정보가 전부 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                context.recreate();
                            }else{
                                Toast.makeText(context, "삭제할 수 없습니다. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                            }
                        }));
                    }, null);
            dialog.show(context.getSupportFragmentManager(), "");
            return false;
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class VHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView debt;
        public TextView bond;
        public TextView empty;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.pList_name);
            this.debt = itemView.findViewById(R.id.pList_totalDebut);
            this.bond = itemView.findViewById(R.id.pList_totalBond);
            this.empty = itemView.findViewById(R.id.pList_empty);
        }


    }

}
