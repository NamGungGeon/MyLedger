package site.cpsp.myledger.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class PermissionUtils {
    private AppCompatActivity activity;
    private static PermissionUtils inst;
    private PermissionUtils(AppCompatActivity activity){
        this.activity= activity;
    }
    public static PermissionUtils getInst(AppCompatActivity activity){
        if(inst== null)
            inst= new PermissionUtils(activity);
        return inst;
    }

    public boolean isAllowPermission(){
        boolean read= ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        boolean write= ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return read && write;
    }
    public int requestPermission(){
        final int requestCode= 10002;

        //권한이 부여되어 있는지 확인
        if(!isAllowPermission()){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(activity.getApplicationContext(), "장부 사용을 위해서는 파일 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }else{
                ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }
        }

        return requestCode;
    }


}
