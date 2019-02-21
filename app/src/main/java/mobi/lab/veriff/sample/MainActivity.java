package mobi.lab.veriff.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.MessageDigest;
import java.util.Date;

import mobi.lab.veriff.data.ColorSchema;
import mobi.lab.veriff.data.Veriff;
import mobi.lab.veriff.network.AcceptHeaderInterceptor;
import mobi.lab.veriff.sample.data.TokenPayload;
import mobi.lab.veriff.sample.data.TokenResponse;
import mobi.lab.veriff.sample.loging.Log;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static mobi.lab.veriff.sample.BuildConfig.API_SECRET;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_VERIFF = 8000;
    private static final int REQUEST_SUCCESSFULL = 201;
    private static Log log = Log.getInstance("Retrofit");
    private static final String BASE_URL = "https://stagingapi.veriff.me";
    private static final String URL_STAGING = "https://staging.veriff.me/v1/";

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TokenService tokenService = createRetrofit().create(TokenService.class);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = findViewById(R.id.launch_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pay = "{\"verification\":{\"document\":{\"number\":\"B01234567\",\"type\":\"ID_CARD\",\"country\":\"EE\"},\"additionalData\":{\"placeOfResidence\":\"Tartu\",\"citizenship\":\"EE\"},\"timestamp\":\"2018-12-12T11:02:05.261Z\",\"lang\":\"et\",\"features\":[\"selfid\"],\"person\":{\"firstName\":\"Tundmatu\",\"idNumber\":\"38508260269\",\"lastName\":\"Toomas\"}}}";

                TokenPayload load = GSON.fromJson(pay, TokenPayload.class);

                String toBeHashed =  GSON.toJson(load) + API_SECRET;
                String signature = sha256(toBeHashed);

                tokenService.getToken(signature, load).enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        if (response.isSuccessful() && response.code() == REQUEST_SUCCESSFULL && response.body() != null) {
                            launchVeriffLibrary(response.body());
                        }
                        else {
                            Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Something went wrong, please contact development team", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString().toUpperCase();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void launchVeriffLibrary(@NonNull TokenResponse body) {
        final String sessionToken = body.getVerification().getSessionToken();
        //enable logging for the library
        Veriff.setLoggingImplementation(Log.getInstance(MainActivity.class));
        Veriff.Builder veriffSDK = new Veriff.Builder(URL_STAGING, sessionToken);
        // If this not specified, then reverts to the default colors
        final ColorSchema schema = new ColorSchema.Builder()
                .setControlsColor(ContextCompat.getColor(this, R.color.colorLibrary))
                .build();
        veriffSDK.setCustomColorSchema(schema);
        // If not specified, then default gradient is used
        veriffSDK.setBackgroundImage(R.drawable.city_tartu);

        veriffSDK.launch(MainActivity.this, REQUEST_VERIFF);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(GSON))
                    .build();

        return retrofit;
    }

    private interface TokenService {

        @Headers({
                "X-AUTH-CLIENT:24d887f4-ad3e-43da-bc98-c8099ad6f430",
                "CONTENT-TYPE:application/json"
        })
        @POST("/v1/sessions")
        Call<TokenResponse> getToken(@Header("X-SIGNATURE") String signature, @Body TokenPayload payload);
    }

}
