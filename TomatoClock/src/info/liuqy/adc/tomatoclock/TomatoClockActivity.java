package info.liuqy.adc.tomatoclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TomatoClockActivity extends Activity {
	static final int VIBRATE_ACTION = 0;
    public static final long TWENTYFIVE_MIN = 5*1000; //only for class demo. real: 25*60*1000;
    Intent service = null;
    Button clockBtn = null;
	private TimerUpdateService srv = null;
	private ServiceConnection conn = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			srv = ((TimerUpdateService.MyBinder)binder).getService();
		}
		public void onServiceDisconnected(ComponentName className) {
			srv = null;
		}
	};
	
	private Handler updateTimerHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            clockBtn = (Button)TomatoClockActivity.this.findViewById(R.id.start_timer);
            //int secs = (Integer)msg.obj;
            setTomatoView();
        }
    };
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        service = new Intent(this, TimerUpdateService.class);
        Messenger message = new Messenger(updateTimerHandler);
        service.putExtra(TimerUpdateService.UPDATE_MSGR, message);
    }
    @Override
    protected void onResume()
    {
        bindService(service, conn, Context.BIND_AUTO_CREATE);
        startService(service);
        
        if(clockBtn != null)
        {
            setTomatoView();
        }
        
        super.onResume();
    }
    @Override
    protected void onPause()
    {
        unbindService(conn);
        super.onPause();
    }
    
    private void setTomatoView()
    {
        long secs = srv.getTimeElapsed();
        int min = (int) secs/60;
        int sec = (int) secs - min*60;
        clockBtn.setText(String.format("%1$02d:%2$02d", min, sec));
    }

    public void startTimer(View v) {  
        Intent i = new Intent(this, TomatoVibrator.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                this.getApplicationContext(), VIBRATE_ACTION, i, 0);
        AlarmManager am = (AlarmManager)this.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TWENTYFIVE_MIN, pi);
        Toast.makeText(this, "TomatoTimer started!", Toast.LENGTH_SHORT).show();
        
		startService(service);
		srv.restart();
    }
}