package info.liuqy.adc.tomatoclock;

import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TimerUpdateService extends Service {
    private final String TAG = this.getClass().getSimpleName();
    private Timer timer = new Timer();
    private static final long UPDATE_INTERVAL = 1000; //1 sec
    private long timeElapsed = 0;

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
        pollForUpdates();
    }
    
    private void pollForUpdates() {
    	//TODO start the timer
        Log.i(TAG, "TimerUpdateService started.");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO stop the timer
        Log.i(TAG, "TimerUpdateService stopped.");
    }

}
