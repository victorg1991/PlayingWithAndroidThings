package com.victor.catapult;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.things.contrib.driver.pwmservo.Servo;
import java.io.IOException;

public class MainActivity extends Activity {

	private Servo servo;
	private static final String APP = "APP";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			servo = new Servo("PWM1");

			servo.setAngleRange(-90, 90);
			servo.setPulseDurationRange(1, 2);
			servo.setAngle(0);

			servo.setEnabled(true);

			SystemClock.sleep(1000);

			servo.setAngle(-90);

			SystemClock.sleep(1000);

			servo.setAngle(90);
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			servo.close();
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		} finally {
			servo = null;
		}
	}
}
