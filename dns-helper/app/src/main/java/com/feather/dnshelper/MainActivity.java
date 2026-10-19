package com.feather.dnshelper;

import android.app.Activity;
import android.os.Bundle;
import android.content.ComponentName;
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
        note.setText("Opens More connection settings, where you can tap Private DNS.");
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
        // Samsung One UI: open the More connection settings page shown in the user's screenshot.
        String[] classes = {
            "com.samsung.android.settings.connection.MoreConnectionSettings",
            "com.android.settings.Settings$MoreConnectionSettingsActivity",
            "com.android.settings.Settings$NetworkDashboardActivity"
        };
        for (String className : classes) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", className));
                startActivity(intent);
                return;
            } catch (Exception ignored) { }
        }

        // Fallback to Connections so the user can continue manually.
        try {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "Tap Connections → More connection settings → Private DNS.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            try {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                Toast.makeText(this, "Tap Connections → More connection settings → Private DNS.", Toast.LENGTH_LONG).show();
            } catch (Exception ignored) {
                Toast.makeText(this, "Open Settings → Connections → More connection settings → Private DNS.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
