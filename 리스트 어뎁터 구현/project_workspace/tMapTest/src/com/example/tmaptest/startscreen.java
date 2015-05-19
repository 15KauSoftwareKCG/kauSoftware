package com.example.tmaptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startscreen extends Activity {


	protected void onCreate(Bundle savedInstanceState)
	{
	
		super.onCreate(savedInstanceState);

	    setContentView(R.layout.menu);
	    
	    
	    //////////////////////////////////////////////////지도보기를 눌리면 이쪽으로 이동
		findViewById(R.id.map).setOnClickListener(
				new Button.OnClickListener() {
	    			public void onClick(View v) {
	                    Intent intent = new Intent(startscreen.this, MainActivity.class);

                        startActivity(intent);

	    			}
				});
	
	}
}
