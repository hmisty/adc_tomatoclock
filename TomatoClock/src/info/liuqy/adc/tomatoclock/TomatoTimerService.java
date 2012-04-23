package info.liuqy.adc.tomatoclock;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class TomatoTimerService extends IntentService {
    public static final String DURATION = "duration";
    public static final long TWENTYFIVE_MIN = 5*1000; //only for class demo. real: 25*60*1000;
    public static final String NAME = "TomatoTimerService";

	public TomatoTimerService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(Intent i) {
        long duration = i.getLongExtra(DURATION, TWENTYFIVE_MIN);
        long endTime = System.currentTimeMillis() + duration;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "TomatoTimer started!", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        Toast.makeText(this, "TomatoTimer timed out!", Toast.LENGTH_SHORT).show();
	}

}
