package site.cpsp.myledger;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.cpsp.myledger.data.FileLedgerDataManager;
import site.cpsp.myledger.utils.AdmobUtil;
import site.cpsp.myledger.utils.SimpleDialogUtil;

public class SettingActivity extends AppCompatActivity {
  
  @BindView(R.id.setting_ad)
  AdView adView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    ButterKnife.bind(this);
    init();
  }
  
  private void init() {
    initUI();
  }
  
  private void initUI() {
    AdmobUtil.getInst(this, null).loadBannerAd(adView);
  }
  
  @OnClick(R.id.setting_resetAllBtn)
  void resetAllData() {
    SimpleDialogUtil dialog = new SimpleDialogUtil();
    dialog.setValue("정말 모든 데이터를 삭제하시겠습니까?\n이 작업을 수행하면 장부 데이터가 전부 삭제됩니다", "예", "아니오", () -> {
      ProgressDialog progressDialog = SimpleDialogUtil.showProgressDialog(this, "데이터를 삭제하는 중입니다", "잠시만 기다려 주세요");
      try {
        FileLedgerDataManager.getInst(getApplicationContext()).removeAllData((isSuccess -> {
          if (isSuccess) {
            dialog.dismiss();
            Toast.makeText(this, "모든 데이터가 삭제되었습니다", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(this, "데이터 삭제에 실패했습니다.\n이 문제가 계속되면 앱을 재설치하세요.", Toast.LENGTH_SHORT).show();
          }
        }));
      } catch (Exception e) {
        e.printStackTrace();
      }
      progressDialog.dismiss();
    }, null);
    dialog.show(getSupportFragmentManager(), "");
  }
}
