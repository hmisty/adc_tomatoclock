package info.liuqy.adc.tomatoclock;

import android.app.IntentService;
import android.content.Intent;

public class TomatoTimerService extends IntentService {
    public static final String DURATION = "duration";
    public static final long TWENTYFIVE_MIN = 5*1000; //only for class demo. real: 25*60*1000;
    public static final String NAME = "TomatoTimerService";

	public TomatoTimerService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
