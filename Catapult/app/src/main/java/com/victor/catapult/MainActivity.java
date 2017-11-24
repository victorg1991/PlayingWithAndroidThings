package com.victor.catapult;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.things.contrib.driver.pwmservo.Servo;
import java.io.IOException;

public class MainActivity extends Activity implements HTTPServer.OnCatapultActionsListener {

	private static final String APP = "APP";
	private Servo servo;
	private HTTPServer httpServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			servo = new Servo("PWM1");

			servo.setAngleRange(-90, 90);
			servo.setPulseDurationRange(1, 2.2);

			httpServer = new HTTPServer(this);
			httpServer.start();
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			servo.close();
			httpServer.closeAllConnections();
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		} finally {
			servo = null;
		}
	}

	@Override
	public void onFire() {
		try {
			servo.setAngle(90);
			servo.setEnabled(true);
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		}
	}

	@Override
	public void onRecharge() {
		try {
			servo.setAngle(-10);
			servo.setEnabled(true);
		} catch (IOException e) {
			Log.e(APP, e.getMessage(), e);
		}
	}
}
