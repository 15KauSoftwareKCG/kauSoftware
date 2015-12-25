package com.example.tmaptest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class startscreen extends Activity {



	public static Activity startActivity;

	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);


		startActivity = startscreen.this;

		startActivity(new Intent(this,splash.class));
		setContentView(R.layout.nmenu);


	    
	    findViewById(R.id.speed).setOnClickListener(
				new Button.OnClickListener() {
	    			public void onClick(View v) {
	                    Intent intent = new Intent(startscreen.this, SpeedometerActivity.class);

                        startActivity(intent);

	    			}
				});




	  findViewById(R.id.map2).setOnClickListener(
				new Button.OnClickListener() {
	    			public void onClick(View v) {
	                    Intent intent = new Intent(startscreen.this, MainActivity.class);

                        startActivity(intent);

	    			}
				});

	}

	}

