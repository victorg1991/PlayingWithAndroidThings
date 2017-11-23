package com.victor.catapult;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import com.google.android.things.contrib.driver.pwmservo.Servo;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;
import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 *
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity implements HTTPServer.OnCatapultActionsListener {

	private Servo servo;
	private HTTPServer httpServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PeripheralManagerService service = new PeripheralManagerService();

		try {
			servo = new Servo("PWM1");

			servo.setAngleRange(-90, 90);
			servo.setPulseDurationRange(1, 2.2);

			httpServer = new HTTPServer(this);
			httpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			servo.close();
			httpServer.closeAllConnections();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			servo = null;
		}
	}

	@Override
	public void onFire() {
		try {
			servo.setAngle(90);
			servo.setEnabled(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRecharge() {
		try {
			servo.setAngle(-10);
			servo.setEnabled(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
