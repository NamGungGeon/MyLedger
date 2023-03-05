package site.cpsp.myledger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.cpsp.myledger.adapters.PersonListAdapter;
import site.cpsp.myledger.data.FileLedgerDataManager;
import site.cpsp.myledger.data.LedgerDataManager;
import site.cpsp.myledger.utils.AdmobUtil;
import site.cpsp.myledger.utils.LedgerUtil;
import site.cpsp.myledger.utils.PermissionUtils;
import site.cpsp.myledger.utils.SimpleDialogUtil;

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

    @BindView(R.id.total_ledgerAddBtn)
    View addBtn;
    @BindView(R.id.total_ad)
    AdView adView;

    private int requestCode;
    private LedgerDataManager ledgerManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalsummary);
        ButterKnife.bind(this);

        if(!PermissionUtils.getInst(this).isAllowPermission()){
            requestCode= PermissionUtils.getInst(this).requestPermission();
            setState(false);
        }else{
            setState(true);
            initUI();
        }

    }

    private void setState(boolean enable){
        int state= enable? View.VISIBLE : View.GONE;

        personList.setVisibility(state);
        iStatus.setVisibility(state);
        wBond.setVisibility(state);
        tBond.setVisibility(state);
        wDebt.setVisibility(state);
        tDebt.setVisibility(state);
        wEmpty.setVisibility(state);
        tEmpty.setVisibility(state);
        addBtn.setVisibility(state);
    }

    private void initUI(){
        AdmobUtil.getInst(this, null).loadBannerAd(adView);

        initLedgerManager();
        if(ledgerManager!= null){
            initPersonList();
            initSummary();
        }else{
            setState(false);
        }
    }
    private void initLedgerManager(){
        try {
            ledgerManager= FileLedgerDataManager.getInst(getApplicationContext());
        } catch (Exception e) {
            setState(false);
            e.printStackTrace();
            Toast.makeText(this, "파일 정보를 읽어올 수 없습니다\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();

            SimpleDialogUtil dialog= new SimpleDialogUtil();
            dialog.setValue("장부 파일을 읽어올 수 없습니다.\n장부 파일이나 앱이 손상되었을 수 있습니다.\n이 문제가 계속되면 앱을 재설치하세요."
                    ,"닫기", null, ()->{
                        dialog.dismiss();
                        finish();
                    }, null);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "");
        }
    }
    private void initSummary(){
        wDebt.setVisibility(View.VISIBLE);
        wEmpty.setVisibility(View.VISIBLE);
        wBond.setVisibility(View.VISIBLE);

        int subtract= ledgerManager.getTotalBond()- ledgerManager.getTotalDebt();
        if(subtract!=0){
            tDebt.setText(LedgerUtil.priceDivider(ledgerManager.getTotalDebt())+ " 원");
            tBond.setText(LedgerUtil.priceDivider(ledgerManager.getTotalBond())+ " 원");
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
        personList.setAdapter(new PersonListAdapter(ledgerManager, this));

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
            setState(false);
            Toast.makeText(this, "파일 권한이 없으면 장부를 사용할 수 없습니다", Toast.LENGTH_SHORT).show();
        }else{
            setState(true);
            initUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initUI();
    }


    private long lastTime= 0;
    @Override
    public void onBackPressed() {
        long currentTime= System.currentTimeMillis();
        if(currentTime- lastTime< 2000){
            finish();
        }else{
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 앱을 종료합니다", Toast.LENGTH_SHORT).show();
            lastTime= currentTime;
        }
    }
}
