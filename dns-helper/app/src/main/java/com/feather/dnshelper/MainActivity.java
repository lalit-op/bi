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
        note.setText("Tap below to open Private DNS settings. Some Samsung software versions do not allow apps to open this screen directly.");
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
        // Try the Android Settings activity directly first.
        try {
            Intent direct = new Intent();
            direct.setComponent(new ComponentName(
                "com.android.settings",
                "com.android.settings.Settings$PrivateDnsSettingsActivity"
            ));
            startActivity(direct);
            return;
        } catch (Exception ignored) { }

        // Try the documented-style action, scoped to Samsung/AOSP Settings.
        try {
            Intent action = new Intent("android.settings.PRIVATE_DNS_SETTINGS");
            action.setPackage("com.android.settings");
            startActivity(action);
            return;
        } catch (Exception ignored) { }

        // Last-resort navigation. Android may block direct access on some firmware.
        try {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
            Toast.makeText(this, "Open Connections → More connection settings → Private DNS.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Open Settings and search for Private DNS.", Toast.LENGTH_LONG).show();
        }
    }
}
