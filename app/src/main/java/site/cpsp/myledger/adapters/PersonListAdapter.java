package site.cpsp.myledger.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import site.cpsp.myledger.DetailLedgerActivity;
import site.cpsp.myledger.R;
import site.cpsp.myledger.data.LedgerDataManager;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.VHolder> {
    private LedgerDataManager ledgerManager;
    private Context context;
    private LayoutInflater inflater;
    private List<String> names;


    public PersonListAdapter(LedgerDataManager ledgerDataManager, Context context) {
        this.context= context;
        this.ledgerManager= ledgerDataManager;
        this.names= ledgerDataManager.getNames();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            bond.setText("에게 "+ String.valueOf(Math.abs(subtract))+ "원을 받아야 합니다");
            debt.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            holder.itemView.setBackground(context.getDrawable(R.drawable.leftbordersafe));
        }else if(subtract<0){
            debt.setText("에게 "+ String.valueOf(Math.abs(subtract))+ "원을 갚아야 합니다");
            bond.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            holder.itemView.setBackground(context.getDrawable(R.drawable.leftborderwarning));
        }else{
            bond.setVisibility(View.GONE);
            debt.setVisibility(View.GONE);
            holder.itemView.setBackground(context.getDrawable(R.drawable.leftbordernormal));
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent= new Intent(context, DetailLedgerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("tName", tName);
            context.startActivity(intent);
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
