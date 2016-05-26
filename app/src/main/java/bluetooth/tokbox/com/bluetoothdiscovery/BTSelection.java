package bluetooth.tokbox.com.bluetoothdiscovery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BTSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btselection);
    }

    public void onWidgetClick(View view) {
        switch (view.getId()) {
            case R.id.button_sco: {
                Intent launch = new Intent(this, Sco.class);
                startActivity(launch);
                break;
            }
            case R.id.button_bluetooth: {
                Intent launch = new Intent(this, Bluetooth.class);
                startActivity(launch);
                break;
            }
        }
    }
}
