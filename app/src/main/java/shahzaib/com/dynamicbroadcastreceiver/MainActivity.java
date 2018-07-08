package shahzaib.com.dynamicbroadcastreceiver;

import android.app.ApplicationErrorReport;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView batteryImage;
    BatteryStatusBroadcastReceiver batteryStatusBR = new BatteryStatusBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryImage = findViewById(R.id.batterImageView);
        setInitialBatteryState();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // register the receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryStatusBR,intentFilter);
        Toast.makeText(this, "Broadcast Receiver is Registered", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(batteryStatusBR);
        Toast.makeText(this, "Broadcast Receiver is Un-Registered", Toast.LENGTH_SHORT).show();
    }




    private void setInitialBatteryState() {
        boolean isPlugged= false;
        Intent intent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if(intent==null)
        {
            batteryImage.setImageDrawable(null);
            return;
        }

        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            isPlugged = isPlugged || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        }

        if(isPlugged)
        {
            batteryImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_power_connected));
        }
        else
        {
            batteryImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_power_not_connected));
        }
    }












    class BatteryStatusBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED) || intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                    // if power is connected
                    batteryImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_power_connected));
                } else {
                    // if power is disconnected
                    batteryImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_power_not_connected));
                }
            }
        }
    }




}
