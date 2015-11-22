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


	    //�뒪�뵾�뱶瑜� �겢由��븯硫�
	    ImageView img=(ImageView) findViewById(R.id.speed);
	    img.setOnTouchListener(btnTouchListener);
	    findViewById(R.id.speed).setOnClickListener(
				new Button.OnClickListener() {
	    			public void onClick(View v) {
	                    Intent intent = new Intent(startscreen.this, SpeedometerActivity.class);

                        startActivity(intent);

	    			}
				});




	    //////////////////////////////////////////////////吏��룄蹂닿린瑜� �겢由��븯硫�
		ImageView img2=(ImageView) findViewById(R.id.map2);
		img.setOnTouchListener(btnTouchListener);
	    findViewById(R.id.map2).setOnClickListener(
				new Button.OnClickListener() {
	    			public void onClick(View v) {
	                    Intent intent = new Intent(startscreen.this, MainActivity.class);

                        startActivity(intent);

	    			}
				});

	}

	private OnTouchListener btnTouchListener = new OnTouchListener(){
		public boolean onTouch (View v, MotionEvent event){
			ImageView view =(ImageView)v;
			if(event.getAction()==MotionEvent.ACTION_DOWN){

				view.setColorFilter(0xaa111111, Mode.SRC_OVER);
			}else if (event.getAction()==MotionEvent.ACTION_UP){
				view.setColorFilter(0x00000000, Mode.SRC_OVER);
			}
			return false;
			}
		};

	}

