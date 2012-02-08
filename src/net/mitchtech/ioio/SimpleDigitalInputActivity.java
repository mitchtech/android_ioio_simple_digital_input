package net.mitchtech.ioio;

import ioio.lib.api.DigitalInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import net.mitchtech.ioio.simpledigitalinput.R;
import android.os.Bundle;
import android.widget.TextView;

public class SimpleDigitalInputActivity extends AbstractIOIOActivity {
	private final int BUTTON1_PIN = 34;
	private final int BUTTON2_PIN = 35;
	
	private TextView mBtn1TextView;
	private TextView mBtn2TextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mBtn1TextView = (TextView) findViewById(R.id.btn1TextView);
		mBtn2TextView = (TextView) findViewById(R.id.btn2TextView);
	}

	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		private DigitalInput mButton1;
		private DigitalInput mButton2;

		@Override
		public void setup() throws ConnectionLostException {
			try {
				mButton1 = ioio_.openDigitalInput(BUTTON1_PIN, DigitalInput.Spec.Mode.PULL_UP);
				mButton2 = ioio_.openDigitalInput(BUTTON2_PIN, DigitalInput.Spec.Mode.PULL_UP);
			} catch (ConnectionLostException e) {
				throw e;
			}
		}

		@Override
		public void loop() throws ConnectionLostException {
			try {
				String button1txt;
				String button2txt;
				final boolean reading1 = mButton1.read();
				final boolean reading2 = mButton2.read();

				if (!reading1) {
					button1txt = getString(R.string.button1) + " active!";
				} else {
					button1txt = getString(R.string.button1);
				}
				if (!reading2) {
					button2txt = getString(R.string.button2) + " active!";
				} else {
					button2txt = getString(R.string.button2);
				}

				setText(button1txt, button2txt);
				sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
			} catch (ConnectionLostException e) {
				throw e;
			}
		}
	}

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}

	private void setText(final String str1, final String str2) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mBtn1TextView.setText(str1);
				mBtn2TextView.setText(str2);
			}
		});
	}
}