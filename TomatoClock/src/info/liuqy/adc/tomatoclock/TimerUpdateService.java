package info.liuqy.adc.tomatoclock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.R.string;
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

public class TimerUpdateService extends Service {
    private final String TAG = this.getClass().getSimpleName();
    private Timer timer;
    private static final long UPDATE_INTERVAL = 1000; //1 sec
    private long timeElapsed = 0;

    public static final String UPDATE_MSGR = "info.liuqy.adc.tomatoclock.update_msgr";
    private Messenger msgr;

    static final String ACTION_FOREGROUND = "info.liuqy.adc.tomatoclock.FOREGROUND";
    static final String ACTION_BACKGROUND = "info.liuqy.adc.tomatoclock.BACKGROUND";
    static final String ACTION_UPDATE_MSGR = "info.liuqy.adc.tomatoclock.UPDATEMSGR";
    private static final Class[] mStartForegroundSignature = new Class[] {
        int.class, Notification.class};
    private static final Class[] mStopForegroundSignature = new Class[] {
        boolean.class};
    
    private NotificationManager mNM;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];
    
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
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        try {
            mStartForeground = getClass().getMethod("startForeground",
                    mStartForegroundSignature);
            mStopForeground = getClass().getMethod("stopForeground",
                    mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            mStartForeground = mStopForeground = null;
        }
    }
    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleCommand(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	void handleCommand(Intent intent) {
        if (ACTION_FOREGROUND.equals(intent.getAction())) {
            // In this sample, we'll use the same text for the ticker and the expanded notification
            CharSequence text = getText(R.string.foreground_service_started);

            // Set the icon, scrolling text and timestamp
            Notification notification = new Notification(R.drawable.tomato, text,
                    System.currentTimeMillis());

            // The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, TomatoClockActivity.class), 0);

            // Set the info for the views that show in the notification panel.
            notification.setLatestEventInfo(this, getText(R.string.local_service_label),
                           text, contentIntent);
            
            startForegroundCompat(R.string.foreground_service_started, notification);
    		Bundle extras = intent.getExtras();
    		msgr = (Messenger)extras.get(UPDATE_MSGR);

    		if (timer != null) {
				timer.cancel();
			}
    		timer = new Timer();
    		timeElapsed = 0;
    		pollForUpdates();
            
        } else if (ACTION_BACKGROUND.equals(intent.getAction())) {
            stopForegroundCompat(R.string.foreground_service_started);
        } else if (ACTION_UPDATE_MSGR.equals(intent.getAction())){
        	Bundle extras = intent.getExtras();
    		msgr = (Messenger)extras.get(UPDATE_MSGR);
		}
    }

	/**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
        // If we have the new startForeground API, then use it.
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = Integer.valueOf(id);
            mStartForegroundArgs[1] = notification;
            try {
                mStartForeground.invoke(this, mStartForegroundArgs);
            } catch (InvocationTargetException e) {
                // Should not happen.
                Log.w("TomatoClock", "Unable to invoke startForeground", e);
            } catch (IllegalAccessException e) {
                // Should not happen.
                Log.w("TomatoClock", "Unable to invoke startForeground", e);
            }
            return;
        }
        
        // Fall back on the old API.
        setForeground(true);
        mNM.notify(id, notification);
    }
    
    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
	void stopForegroundCompat(int id) {
        // If we have the new stopForeground API, then use it.
        if (mStopForeground != null) {
            mStopForegroundArgs[0] = Boolean.TRUE;
            try {
                mStopForeground.invoke(this, mStopForegroundArgs);
            } catch (InvocationTargetException e) {
                // Should not happen.
                Log.w("TomatoClock", "Unable to invoke stopForeground", e);
            } catch (IllegalAccessException e) {
                // Should not happen.
                Log.w("TomatoClock", "Unable to invoke stopForeground", e);
            }
            return;
        }
	}
    
        
    private void pollForUpdates() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeElapsed++;
                
				if (timeElapsed * 1000 >= TomatoClockActivity.TWENTYFIVE_MIN) {
					this.cancel();
					TimerUpdateService.this.stopSelf();
					stopForegroundCompat(R.string.foreground_service_started);
					timer = null;
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
        stopForegroundCompat(R.string.foreground_service_started);
        Log.i(TAG, "TimerUpdateService stopped.");
    }

}
