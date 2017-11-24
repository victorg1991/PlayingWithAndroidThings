package com.victor.catapult;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;
import java.io.IOException;

public class MainActivity extends Activity {

	private Pwm pwm;
	private static final String APP = "APP";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			PeripheralManagerService service = new PeripheralManagerService();

			pwm = service.openPwm("PWM1");

			pwm.setPwmFrequencyHz(50.0);

			//0
			pwm.setPwmDutyCycle(7.5);
			pwm.setEnabled(true);

			SystemClock.sleep(1000);

			//-90
			pwm.setPwmDutyCycle(3.0);

			SystemClock.sleep(1000);

			//90
			pwm.setPwmDutyCycle(11);
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			pwm.close();
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		} finally {
			pwm = null;
		}
	}
}
