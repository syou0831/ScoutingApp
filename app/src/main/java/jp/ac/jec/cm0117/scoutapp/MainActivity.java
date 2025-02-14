package jp.ac.jec.cm0117.scoutapp;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.security.identity.CredentialDataResult;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Boolean response = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editID = findViewById(R.id.loginID);
        EditText editPassword = findViewById(R.id.loginPassword);

        findViewById(R.id.loginSubmitButton).setOnClickListener(v -> {

            if(!isEmpty(editID.getText().toString()) && !isEmpty(editPassword.getText().toString())){
                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.scheme("http");
                uriBuilder.encodedAuthority("23cm0117.main.jp");
                uriBuilder.path("scouting/JSON/LoginJSON.php");
                uriBuilder.appendQueryParameter("PID", editID.getText().toString());
                uriBuilder.appendQueryParameter("PASS", editPassword.getText().toString());

                loadSvData(uriBuilder);

                Log.d("TAG", "URL: " + uriBuilder);

                if(response) {
                    Log.d("TAG", "ログイン ");
                    Intent intent = new Intent(MainActivity.this, menuActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void loadSvData(Uri.Builder uriBuilder) {
        Log.d(TAG, "loadSvData: " + uriBuilder.toString());
        final Request request = new Request.Builder().url(uriBuilder.toString()).build();
        final OkHttpClient client = new OkHttpClient.Builder().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resString = response.body().string();
                Log.d(TAG, "ABCABConResponse: " + resString);

                final ArrayList<ResponseItem> ary = ResponseJsonHelper.parseJson(resString);
                Log.d(TAG, "onResponse: 取得したデータ = " + ary);

                // UI スレッドで更新する
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    if (ary != null && !ary.isEmpty()) {
                        updateUI(ary); // 最初のデータを適用
                        Log.d(TAG, "AAAAAAAAAAonResponse: " + ary.get(0));
                    } else {
                        Log.d(TAG, "データが空です");
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: データ取得に失敗しました", e);
            }
        });
    }

    private void updateUI(ArrayList<ResponseItem> ary) {
        if(ary.get(0).getResponse()) {
            Log.d("TAG", "ログイン ");
            Intent intent = new Intent(MainActivity.this, menuActivity.class);
            intent.putExtra("PersonID", ary.get(0).getPersonID());
            startActivity(intent);
        }
    }
}