package com.feather.dnshelper;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        int pad = (int)(24 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);
        TextView title = new TextView(this);
        title.setText("Feather DNS Helper");
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);
        TextView note = new TextView(this);
        note.setText("Tap below to open Private DNS settings. If your phone does not support the direct screen, general Settings will open.");
        note.setTextSize(16);
        note.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(-1, -2);
        np.setMargins(0, pad, 0, pad);
        layout.addView(note, np);
        Button button = new Button(this);
        button.setText("Open Private DNS Settings");
        button.setOnClickListener(v -> openDns());
        layout.addView(button);
        setContentView(layout);
        if (getIntent() != null && "featherdns".equals(getIntent().getScheme())) openDns();
    }
    private void openDns() {
        try {
            Intent intent = new Intent("android.settings.PRIVATE_DNS_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            try { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
            catch (Exception ignored) { Toast.makeText(this, "Please open Settings manually and search for Private DNS.", Toast.LENGTH_LONG).show(); }
        }
    }
}
