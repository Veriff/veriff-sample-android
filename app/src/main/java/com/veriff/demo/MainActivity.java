package com.veriff.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.veriff.demo.data.TokenPayload;
import com.veriff.demo.data.TokenResponse;
import com.veriff.demo.loging.Log;
import com.veriff.demo.service.TokenService;
import com.veriff.demo.utils.GeneralUtils;

import java.net.MalformedURLException;
import java.net.URL;

import mobi.lab.veriff.network.AcceptHeaderInterceptor;
import mobi.lab.veriff.util.LangUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.veriff.demo.BuildConfig.API_SECRET;

public class MainActivity extends AppCompatActivity {
    private static Log log = Log.getInstance("Retrofit");

    private String sessionToken;
    private String baseUrl = AppStatics.getURL_STAGING();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_settings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingsActivity();
            }
        });

        Button button = findViewById(R.id.launch_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTokenRequest();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppStatics.getTOKEN_RESULT() && resultCode == RESULT_OK) {
            String contents = SettingsActivity.readExtra(data);
            QrCodeContentsParser parser = new QrCodeContentsParser(contents);
            parser.parse();
            if (!LangUtils.isStringEmpty(sessionToken)) {
                GeneralUtils.launchVeriffSDK(sessionToken, MainActivity.this, baseUrl);
            } else {
                Toast.makeText(MainActivity.this, "No token available, try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startSettingsActivity() {
        startActivityForResult(SettingsActivity.createIntent(this), AppStatics.getTOKEN_RESULT());
    }

    private void makeTokenRequest() {
        final TokenService tokenService = createRetrofit().create(TokenService.class);
        String pay = "{\"verification\":{\"document\":{\"number\":\"B01234567\",\"type\":\"ID_CARD\",\"country\":\"EE\"},\"additionalData\":{\"placeOfResidence\":\"Tartu\",\"citizenship\":\"EE\"},\"timestamp\":\"2018-12-12T11:02:05.261Z\",\"lang\":\"et\",\"features\":[\"selfid\"],\"person\":{\"firstName\":\"Tundmatu\",\"idNumber\":\"38508260269\",\"lastName\":\"Toomas\"}}}";
        TokenPayload load = AppStatics.getGSON().fromJson(pay, TokenPayload.class);
        String toBeHashed = AppStatics.getGSON().toJson(load) + API_SECRET;
        String signature = GeneralUtils.sha256(toBeHashed);

        tokenService.getToken(signature, load).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.code() == AppStatics.getREQUEST_SUCCESSFUL() && response.body() != null) {
                    GeneralUtils.launchVeriffSDK(response.body().getVerification().getSessionToken(),
                            MainActivity.this, baseUrl);
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong, please contact development team", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static Retrofit createRetrofit() {
        final HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                log.d(message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient;
        Retrofit retrofit;

        // create regular retrofit client
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AcceptHeaderInterceptor())
                .addInterceptor(logInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppStatics.getURL_STAGING())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(AppStatics.getGSON()))
                .build();

        return retrofit;
    }

    private class QrCodeContentsParser {
        String contents;
        URL url;

        QrCodeContentsParser(String contents) {
            this.contents = contents;
            this.url = createUrl(contents);
        }

        private URL createUrl(String contents) {
            try {
                return new URL(contents);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        void parse() {
            if (isUrl()) {
                // Demo interface QR codes contain host and session token
                setHostAndTokenFromUrl();
            } else {
                // Back office QR codes only contain session token
                sessionToken = contents;
            }
        }

        private boolean isUrl() {
            return url != null;
        }

        private void setHostAndTokenFromUrl() {
            baseUrl = url.getProtocol() + "://" + url.getHost() + "/";
            sessionToken = url.getPath().split("/")[2];
        }
    }

    public static void start(Activity activity) {
        Intent starter = new Intent(activity, MainActivity.class);
        activity.startActivity(starter);
    }

}
