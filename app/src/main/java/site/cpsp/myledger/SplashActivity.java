package site.cpsp.myledger;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
  
  private static final long COUNTER_TIME = (long) (2.5 * 1000);
  private final String LOG_TAG = "Splash";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    
    createTimer(COUNTER_TIME);
  }
  
  
  /**
   * Create the countdown timer, which counts down to zero and show the app open ad.
   *
   * @param ms the number of milliseconds that the timer counts down from
   */
  private void createTimer(long ms) {
    
    CountDownTimer countDownTimer =
      new CountDownTimer(ms, 500) {
        @Override
        public void onTick(long millisUntilFinished) {
        }
        
        @Override
        public void onFinish() {
          Application application = getApplication();
          
          // If the application is not an instance of MyApplication, log an error message and
          // start the MainActivity without showing the app open ad.
          if (!(application instanceof MyApplication)) {
            Log.e(LOG_TAG, "Failed to cast application to MyApplication.");
            startMainActivity();
            return;
          }
          
          // Show the app open ad.
          ((MyApplication) application)
            .showAdIfAvailable(
              SplashActivity.this, () -> {
                startMainActivity();
              });
        }
      };
    countDownTimer.start();
  }
  
  /**
   * Start the MainActivity.
   */
  public void startMainActivity() {
    Intent intent = new Intent(this, TotalSummaryActivity.class);
    this.startActivity(intent);
  }
}