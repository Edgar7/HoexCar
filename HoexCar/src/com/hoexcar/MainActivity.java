package com.hoexcar;

import com.hoexcar.navigation.GoogleMaps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*
	 * Hier werden die Buttons ausgewertet.
	 */
	public void OnClick(View view) {
		switch (view.getId()) {
			// Navigation Activity
			case R.id.bNav:
			startActivity(new Intent(this, GoogleMaps.class));
		break;
		}
	}
}
