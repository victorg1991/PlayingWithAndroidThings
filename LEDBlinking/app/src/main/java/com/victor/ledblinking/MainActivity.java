package com.victor.ledblinking;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import java.io.IOException;

public class MainActivity extends Activity {

	private Gpio led;
	private Handler handler = new Handler();
	private static final String LED_ADDRESS = "BCM6";
	public static final String APP = "APP";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			PeripheralManagerService service = new PeripheralManagerService();
			led = service.openGpio(LED_ADDRESS);
			led.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

			handler.post(blinkingRunnable);
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		handler.removeCallbacks(blinkingRunnable);

		if (led == null) {
			return;
		}

		try {
			led.close();
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		} finally {
			led = null;
		}
	}

	private Runnable blinkingRunnable = new Runnable() {
		@Override
		public void run() {
			if (led == null) {
				return;
			}

			try {
				led.setValue(!led.getValue());

				handler.postDelayed(blinkingRunnable, 1000);
			} catch (IOException e) {
				Log.e(APP, e.getMessage(), e);
			}
		}
	};
}
