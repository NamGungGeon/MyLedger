package site.cpsp.myledger;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.cpsp.myledger.adapters.PersonListAdapter;
import site.cpsp.myledger.data.FileLedgerDataManager;
import site.cpsp.myledger.data.LedgerDataManager;
import site.cpsp.myledger.data.LedgerFactory;
import site.cpsp.myledger.utils.PermissionUtils;
import site.cpsp.myledger.utils.ViewUtils;

public class TotalSummaryActivity extends AppCompatActivity {

    @BindView(R.id.total_personList)
    RecyclerView personList;

    @BindView(R.id.total_status)
    ImageView iStatus;

    @BindView(R.id.total_wBond)
    ViewGroup wBond;
    @BindView(R.id.total_tBond)
    TextView tBond;
    @BindView(R.id.total_wDebt)
    ViewGroup wDebt;
    @BindView(R.id.total_tDebt)
    TextView tDebt;
    @BindView(R.id.total_wEmpty)
    ViewGroup wEmpty;
    @BindView(R.id.total_tEmpry)
    TextView tEmpty;

    private int requestCode;
    private LedgerDataManager ledgerManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalsummary);
        ButterKnife.bind(this);

        if(!PermissionUtils.getInst(this).isAllowPermission()){
            requestCode= PermissionUtils.getInst(this).requestPermission();
        }else{
            initUI();
        }

    }


    private void initUI(){
        initLedgerManager();
        if(ledgerManager!= null){
            initPersonList();
            initSummary();
        }
    }
    private void initLedgerManager(){
        try {
            ledgerManager= FileLedgerDataManager.getInst(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "파일 정보를 읽어올 수 없습니다\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void initSummary(){
        wDebt.setVisibility(View.VISIBLE);
        wEmpty.setVisibility(View.VISIBLE);
        wBond.setVisibility(View.VISIBLE);

        int subtract= ledgerManager.getTotalBond()- ledgerManager.getTotalDebt();
        if(subtract!=0){
            tDebt.setText(LedgerFactory.priceDivider(ledgerManager.getTotalDebt())+ " 원");
            tBond.setText(LedgerFactory.priceDivider(ledgerManager.getTotalBond())+ " 원");
            wEmpty.setVisibility(View.GONE);
            if(subtract>0){
                iStatus.setBackground(getDrawable(R.drawable.happyface));
            }else{
                iStatus.setBackground(getDrawable(R.drawable.sadface));
            }
        }else{
            wDebt.setVisibility(View.GONE);
            wBond.setVisibility(View.GONE);
            iStatus.setBackground(getDrawable(R.drawable.normalface));
        }
    }

    private void initPersonList(){
        personList.setLayoutManager(new LinearLayoutManager(this));
        personList.setAdapter(new PersonListAdapter(ledgerManager, getApplicationContext()));

    }

    @OnClick(R.id.total_ledgerAddBtn)
    void openAddLedgerDataActivity(){
        if(PermissionUtils.getInst(this).isAllowPermission()){
            startActivityForResult(new Intent(this, AddLedgerDataActivity.class), 1);
        }else{
            requestCode= PermissionUtils.getInst(this).requestPermission();
            Toast.makeText(this, "이 기능을 사용하려면 파일 권한이 필요합니다", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.total_settingBtn)
    void openSettingActivity(){
        startActivityForResult(new Intent(this, SettingActivity.class), 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]){
        if(!PermissionUtils.getInst(this).isAllowPermission()){
            Toast.makeText(this, "파일 권한이 없으면 장부를 사용할 수 없습니다", Toast.LENGTH_SHORT).show();
            initUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initUI();
    }
}
