package mobi.lab.veriff.sample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Arrays;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.view.View.GONE;

public class SettingsActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int CAMERA_REQUEST = 121;
    private static final String RESULT_TOKEN = SettingsActivity.class.getSimpleName() + ".RESULT_TOKEN";

    private ZXingScannerView qrScanner;
    private boolean isScanning = false;

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    public static String readExtra(@NonNull Intent intent) {
        if (intent.hasExtra(RESULT_TOKEN)) {
            return intent.getStringExtra(RESULT_TOKEN);
        } else {
            return  null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_settings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        qrScanner = findViewById(R.id.settings_scanner);
        setScannerProperties();
        findViewById(R.id.settings_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScanning = true;
                startCamera();
            }
        });
    }

    private void startCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                return;
            }
        }
        qrScanner.setVisibility(View.VISIBLE);
        qrScanner.startCamera();
        qrScanner.setResultHandler(this);
    }

    private void stopCamera() {
        qrScanner.stopCamera();
        qrScanner.setVisibility(GONE);

    }

    private void setScannerProperties() {
        qrScanner.setFormats(Arrays.asList(BarcodeFormat.QR_CODE));
        qrScanner.setAutoFocus(true);
        qrScanner.setLaserColor(R.color.colorAccent);
        qrScanner.setMaskColor(R.color.colorAccent);
        if (Build.MANUFACTURER.equalsIgnoreCase("HUAWEI")) {
            qrScanner.setAspectTolerance(0.5f);
        }
        qrScanner.setResultHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isScanning) {
            startCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        }
    }

    @Override
    public void handleResult(Result result) {
        if (!TextUtils.isEmpty(result.getText())) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(RESULT_TOKEN, result.getText());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
