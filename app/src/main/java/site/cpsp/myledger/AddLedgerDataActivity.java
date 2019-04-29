package site.cpsp.myledger;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.cpsp.myledger.data.FileLedgerDataManager;
import site.cpsp.myledger.data.LedgerData;
import site.cpsp.myledger.data.LedgerDataManager;
import site.cpsp.myledger.utils.SimpleDialogUtil;

public class AddLedgerDataActivity extends AppCompatActivity {

    @BindView(R.id.adder_name)
    EditText eName;
    @BindView(R.id.adder_description)
    EditText eDesc;
    @BindView(R.id.adder_type)
    CheckBox cType;
    @BindView(R.id.adder_time)
    EditText eTime;
    @BindView(R.id.adder_price)
    EditText ePrice;

    private String tName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addledgerdata);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        getDataFromIntent();
        initUI();
    }
    private void initUI(){
        cType.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                cType.setHint("내가 받을 돈입니다");
            }else{
                cType.setHint("내가 갚을 돈입니다");
            }
        });
        if(tName!= null && !tName.equals("")){
            eName.setText(tName);
            eName.setEnabled(false);
        }
    }

    private void getDataFromIntent(){
        tName= getIntent().getStringExtra("tName");
    }


    private LedgerData buildLedgerData(){
        String name= eName.getText().toString();
        String desc= eDesc.getText().toString();
        String time= eTime.getText().toString();
        int price= Integer.valueOf(ePrice.getText().toString().equals("")? "0": ePrice.getText().toString());
        boolean isBond= cType.isChecked();

        if(name.equals("")){
            Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return null;
        }

        return new LedgerData(name, time, desc, price, isBond);
    }

    @OnClick(R.id.adder_submitBtn)
    void submit(){
        LedgerData build= buildLedgerData();
        ProgressDialog pDialog= SimpleDialogUtil.showProgressDialog(this, "저장하는 중입니다", "잠시만 기다려 주세요");
        if(build!= null){
            try {
                FileLedgerDataManager.getInst(this).addData(build, (isSuccess -> {
                    pDialog.dismiss();
                    if(isSuccess){
                        Toast.makeText(this, "성공적으로 저장되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(this, "저장할 수 없습니다. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                    }
                }));
            } catch (Exception e) {
                pDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(this, "파일을 열 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        SimpleDialogUtil dialog= new SimpleDialogUtil();
        dialog.setValue("작성한 내용이 사라집니다.\n정말 이 창을 닫으시겠습니까?", "닫기", "취소", ()->{
            super.onBackPressed();
        }, null);
        dialog.show(getSupportFragmentManager(), "");
    }
}