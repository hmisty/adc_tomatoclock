package info.liuqy.adc.tomatoclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class TomatoVibrator extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
        Toast.makeText(ctx, "TomatoTimer timed out!", Toast.LENGTH_SHORT).show();
        Vibrator vib = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(2000); //FIXME magic number
	}

}
