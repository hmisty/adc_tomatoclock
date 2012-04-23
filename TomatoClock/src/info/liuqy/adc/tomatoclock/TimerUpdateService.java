package info.liuqy.adc.tomatoclock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class TimerUpdateService extends Service {
    private final String TAG = this.getClass().getSimpleName();
    private Timer timer;
    private static final long UPDATE_INTERVAL = 1000; //1 sec
    private long timeElapsed = 0;

    public static final String UPDATE_MSGR = "info.liuqy.adc.tomatoclock.update_msgr";
    private Messenger msgr;
    
    public long getTimeElapsed() {
		return timeElapsed;
	}

	/*FIXME really bad practice
     * see here: http://www.ozdroid.com/#!BLOG/2010/12/19/How_to_make_a_local_Service_and_bind_to_it_in_Android
     */
    private final IBinder mBinder = new MyBinder();
    public class MyBinder extends Binder {
        TimerUpdateService getService() {
            return TimerUpdateService.this;
        }
    }

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

    @Override
    public void onCreate() {
        super.onCreate();
    }
    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		msgr = (Messenger)extras.get(UPDATE_MSGR);

		timer = new Timer();
		timeElapsed = 0;
		pollForUpdates();

		return super.onStartCommand(intent, flags, startId);
	}

    private void pollForUpdates() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeElapsed++;
                
				if (timeElapsed * 1000 >= TomatoClockActivity.TWENTYFIVE_MIN) {
					this.cancel();
					TimerUpdateService.this.stopSelf();
				}
				
				//update UI to show the timer
				Message msg = Message.obtain();
				msg.obj = timeElapsed;
				try {
					msgr.send(msg);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            }
        }, 0, UPDATE_INTERVAL);
        Log.i(TAG, "TimerUpdateService started.");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        Log.i(TAG, "TimerUpdateService stopped.");
    }

}
