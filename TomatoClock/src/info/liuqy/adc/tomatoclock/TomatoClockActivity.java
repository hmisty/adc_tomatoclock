package info.liuqy.adc.tomatoclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TomatoClockActivity extends Activity {
	static final int VIBRATE_ACTION = 0;
    public static final long TWENTYFIVE_MIN = 5*1000; //only for class demo. real: 25*60*1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void startTimer(View v) {
        Intent i = new Intent(this, TomatoVibrator.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                this.getApplicationContext(), VIBRATE_ACTION, i, 0);
        AlarmManager am = (AlarmManager)this.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TWENTYFIVE_MIN, pi);
        Toast.makeText(this, "TomatoTimer started!", Toast.LENGTH_SHORT).show();      
    }
}