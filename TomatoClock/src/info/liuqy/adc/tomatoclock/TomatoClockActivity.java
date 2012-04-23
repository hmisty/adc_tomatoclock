package info.liuqy.adc.tomatoclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TomatoClockActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void startTimer(View v) {
        Intent service = new Intent(TomatoClockActivity.this, TomatoTimerService.class);
        TomatoClockActivity.this.startService(service);
    }
}