package com.kaudesign.speedometer;

import java.util.Formatter;

import java.util.Locale;

import com.kaudesign.speedometer.R;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CSpeedometer extends Activity implements IBaseGpsListener
{
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.speedometer_main);

    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

    this.updateSpeed(null);

    CheckBox chkUseMetricUnits = (CheckBox)this.findViewById(R.id.chkUseMetricUnits);
    chkUseMetricUnits.setOnCheckedChangeListener(new OnCheckedChangeListener()
    
    {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        CSpeedometer.this.updateSpeed(null);
      }
    });
  }

  /*public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.speedometer_main, menu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.menuItemQuit:
        this.finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }*/

  public void finish()
  {
    super.finish();
    System.exit(0);
  }

  public void updateSpeed(CLocation location)
  {
    float nCurrentSpeed = 0;

    if( location!=null )
    {
      location.setUseMetricUnits(this.useMetricUnits());
      nCurrentSpeed = location.getSpeed();
    }

    Formatter fmt = new Formatter(new StringBuilder());

    fmt.format(Locale.KOREA, "%5.1f", nCurrentSpeed);
    String strCurrentSpeed = fmt.toString();
    strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

    String strUnits = "kilometer/hour";
    if (this.useMetricUnits())
    {
      strUnits = "meters/second";
    }

    TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
    txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
  }

  public boolean useMetricUnits()
  {
    CheckBox chkUseMetricUnits = (CheckBox)this.findViewById(R.id.chkUseMetricUnits);
    return chkUseMetricUnits.isChecked();
  }

  public void onLocationChanged(Location location)
  {
    if (location != null)
    {
      CLocation myLocation = new CLocation(location, this.useMetricUnits());
      this.updateSpeed(myLocation);
      
      if(location != null) {
    	  
      }
    }
  }

  public void onProviderDisabled(String provider)
  {
    // TODO: do something one day?
  }

  public void onProviderEnabled(String provider)
  {
    // TODO: do something one day?
  }

  public void onStatusChanged(String provider, int status, Bundle extras)
  {
    // TODO: do something one day?

  }

  public void onGpsStatusChanged(int event)
  {
    // TODO: do something one day?
  }
}