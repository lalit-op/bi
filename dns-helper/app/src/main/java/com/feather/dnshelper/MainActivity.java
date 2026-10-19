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
        note.setText("Tap below to open Private DNS. If your phone blocks the direct screen, use Connections → More connection settings → Private DNS.");
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
        // The AOSP Settings manifest uses this exact action string.
        try {
            Intent intent = new Intent("com.android.settings.PRIVATE_DNS_SETTINGS");
            intent.setPackage("com.android.settings");
            startActivity(intent);
            return;
        } catch (Exception ignored) { }

        // Try the corresponding AOSP component name.
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                "com.android.settings",
                "com.android.settings.Settings$PrivateDnsSettingsActivity"
            ));
            startActivity(intent);
            return;
        } catch (Exception ignored) { }

        // Samsung may not expose the Private DNS activity to third-party apps.
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                "com.android.settings",
                "com.android.settings.Settings$MoreConnectionSettingsActivity"
            ));
            startActivity(intent);
            return;
        } catch (Exception ignored) { }

        try {
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            Toast.makeText(this, "Tap More connection settings → Private DNS.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
            Toast.makeText(this, "Tap Connections → More connection settings → Private DNS.", Toast.LENGTH_LONG).show();
        }
    }
}
