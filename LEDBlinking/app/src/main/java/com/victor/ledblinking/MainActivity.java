package com.victor.ledblinking;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import java.io.IOException;

public class MainActivity extends Activity {

	private Gpio led;
	private Handler handler = new Handler();
	private static final String LED_ADDRESS = "BCM6";
	public static final String APP = "APP";
	private ButtonInputDriver driver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			PeripheralManagerService service = new PeripheralManagerService();
			led = service.openGpio(LED_ADDRESS);
			led.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		}

		try {
			this.driver = new ButtonInputDriver(
                "BCM21",
                Button.LogicState.PRESSED_WHEN_LOW,
                KeyEvent.KEYCODE_SPACE
            );

			driver.register();
		} catch (IOException e) {
			e.printStackTrace();
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

		if (driver != null) {
			driver.unregister();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyUp(keyCode, event);

		try {
			this.led.setValue(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		super.onKeyUp(keyCode, event);

		try {
			this.led.setValue(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
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
