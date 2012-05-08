package info.liuqy.adc.tomatoclock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.PendingIntent;
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
    private final int ONGOING_NOTIFICATION = 1;
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
        Log.d("Tomato", "onCreate");
        Notification notification = new Notification(R.drawable.tomato, getText(R.string.app_name),
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, TomatoClockActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, getText(R.string.app_name),
                getText(R.string.app_name), pendingIntent);
        startForeground(ONGOING_NOTIFICATION, notification);
    }
    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		msgr = (Messenger)extras.get(UPDATE_MSGR);
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timeElapsed = 0;
		Log.d("Tomato", "onStartCommand");
		pollForUpdates();

		return super.onStartCommand(intent, flags, startId);
	}

    private void pollForUpdates() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeElapsed++;
                Log.d("Tomato", "pollForUpdates | timeElapsed = " + timeElapsed);
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
        stopForeground(true);
        Log.i(TAG, "TimerUpdateService stopped.");
    }

}
