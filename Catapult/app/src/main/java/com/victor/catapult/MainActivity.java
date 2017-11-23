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
public class MainActivity extends Activity {

	private Pwm pwm;
	private Servo servo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PeripheralManagerService service = new PeripheralManagerService();

		try {
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
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			pwm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			pwm = null;
		}
	}
}
