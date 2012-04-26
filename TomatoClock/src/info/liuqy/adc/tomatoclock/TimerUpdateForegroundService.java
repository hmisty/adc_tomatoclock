package info.liuqy.adc.tomatoclock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
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

public class TimerUpdateForegroundService extends Service {

	private NotificationManager mNotificationManager;
	public static final String UPDATE_MSGR = "info.liuqy.adc.tomatoclock.update_msgr";
    private final String TAG = this.getClass().getSimpleName();
    private Timer timer;
    private static final long UPDATE_INTERVAL = 1000; //1 sec
    private long timeElapsed = 0;
    private Messenger msgr;
    
    public long getTimeElapsed() {
		return timeElapsed;
	}
    private final IBinder mBinder = new MyBinder();
    public class MyBinder extends Binder {
    	TimerUpdateForegroundService getService() {
            return TimerUpdateForegroundService.this;
        }
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.tomato,
				"Foreground Service Started.", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, TomatoClockActivity.class), 0);
		notification.setLatestEventInfo(this, "Foreground Service",
				"Foreground Service Started.", contentIntent);

		startForeground(1, notification);
		mNotificationManager.notify(1, notification);
		
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mNotificationManager.cancel(1);
		stopForeground(false);
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
					TimerUpdateForegroundService.this.stopSelf();
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
}
