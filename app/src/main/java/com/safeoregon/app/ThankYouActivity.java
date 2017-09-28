package com.safeoregon.app;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.etech.util.Header;

public class ThankYouActivity extends HeaderActivity implements OnClickListener {

	Button thankYou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thankyou);

		setHeader();

		thankYou = (Button) findViewById(R.id.btnThank);
		thankYou.setOnClickListener(this);
	}

	private void setHeader() {
		try {

			super.setTitle(getString(R.string.title_thank_u));
			Header header = (Header) findViewById(R.id.header1);
			header.setLeftBtnImage(R.drawable.ic_back_arrow);
			header.hideRightBtn();
			header.setLeftBtnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnThank:

			this.finish();
			break;

		}
	}
	
}
