package site.cpsp.myledger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailLedgerActivity extends AppCompatActivity {
    @BindView(R.id.detail_name)
    TextView tName;
    @BindView(R.id.detail_summary)
    TextView tSummary;
    @BindView(R.id.detail_list)
    RecyclerView timeLine;


    private String targetName;

    private final int IGNORE_RESULT= 10002;
    private final int CHECK_RESULT= 10004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailledger);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        loadDataFromIntent();
        initUI();
    }
    private void loadDataFromIntent(){
        targetName= getIntent().getStringExtra("tName");
    }
    private void initUI(){
        tName.setText(targetName+ " 상세보기");
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
