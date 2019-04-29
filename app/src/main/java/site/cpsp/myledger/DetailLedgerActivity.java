package site.cpsp.myledger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.cpsp.myledger.adapters.PersonDeatilAdapter;
import site.cpsp.myledger.data.FileLedgerDataManager;
import site.cpsp.myledger.data.LedgerDataManager;
import site.cpsp.myledger.data.LedgerFactory;

public class DetailLedgerActivity extends AppCompatActivity {
    @BindView(R.id.detail_name)
    TextView tName;
    @BindView(R.id.detail_summary)
    TextView tSummary;
    @BindView(R.id.detail_list)
    RecyclerView timeLine;
    @BindView(R.id.detail_status)
    ImageView iStatus;


    private String targetName;

    private final int IGNORE_RESULT= 10002;
    private final int CHECK_RESULT= 10004;

    private LedgerDataManager ledgerDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailledger);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        loadDataFromIntent();
        if(!readyLedgerManager()){
            Toast.makeText(this, "파일을 열 수 없습니다", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initUI();
    }
    private void loadDataFromIntent(){
        targetName= getIntent().getStringExtra("tName");
    }
    private boolean readyLedgerManager(){
        try {
            ledgerDataManager= FileLedgerDataManager.getInst(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private void initUI(){
        tName.setText(targetName+ " 상세보기");
        timeLine.setLayoutManager(new LinearLayoutManager(this));
        timeLine.setAdapter(new PersonDeatilAdapter(ledgerDataManager, this, targetName));

        int subtract= ledgerDataManager.getPersonTotalBond(targetName)- ledgerDataManager.getPersonTotalDebt(targetName);
        if(subtract>0){
            tSummary.setText("내가 "+ LedgerFactory.priceDivider(subtract)+ "원을 받아야 합니다");
            tSummary.setTextColor(getResources().getColor(R.color.safe));
            iStatus.setBackground(getDrawable(R.drawable.happyface));
        }else if(subtract<0){
            tSummary.setText("내가 "+ LedgerFactory.priceDivider(subtract)+ "원을 갚아야 합니다");
            tSummary.setTextColor(getResources().getColor(R.color.warning));
            iStatus.setBackground(getDrawable(R.drawable.sadface));
        }else{
            tSummary.setText("깔ㅡ끔합니다!");
            tSummary.setTextColor(getResources().getColor(R.color.fontbasic));
            iStatus.setBackground(getDrawable(R.drawable.normalface));
        }
    }

    @OnClick(R.id.detail_ledgerAddBtn)
    void openLedgerDataAdd(){
        Intent intent= new Intent(this, AddLedgerDataActivity.class);
        intent.putExtra("tName", targetName);
        startActivityForResult(intent, CHECK_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CHECK_RESULT) init();
    }
}
