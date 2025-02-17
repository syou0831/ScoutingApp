package jp.ac.jec.cm0117.scoutapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SaimokuActivity extends AppCompatActivity {

    private static final String TAG = "SaimokuDataActivity";

    int PersonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saimoku);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PersonID = getIntent().getIntExtra("PersonID", 0);
        Log.d(TAG, "onCreate:" + PersonID);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SaimokuActivity.this, menuActivity.class);
                intent.putExtra("PersonID", PersonID);
                startActivity(intent);
            }
        };

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http");
        uriBuilder.encodedAuthority("23cm0117.main.jp");
        uriBuilder.path("scouting/JSON/SaimokuJSON.php");
        uriBuilder.appendQueryParameter("PID", String.valueOf(PersonID));

        loadSvData(uriBuilder);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //アイテムが選択されたとき
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                findViewById(R.id.syokyu).setVisibility(View.GONE);
                findViewById(R.id.nikyu).setVisibility(View.GONE);
                findViewById(R.id.itikyu).setVisibility(View.GONE);
                findViewById(R.id.kiku).setVisibility(View.GONE);
                findViewById(R.id.hayabusa).setVisibility(View.GONE);
                findViewById(R.id.fuji).setVisibility(View.GONE);

                switch (item){
                    case "全件表示":
                        findViewById(R.id.syokyu).setVisibility(View.VISIBLE);
                        findViewById(R.id.nikyu).setVisibility(View.VISIBLE);
                        findViewById(R.id.itikyu).setVisibility(View.VISIBLE);
                        findViewById(R.id.kiku).setVisibility(View.VISIBLE);
                        findViewById(R.id.kiku).setVisibility(View.VISIBLE);
                        findViewById(R.id.hayabusa).setVisibility(View.VISIBLE);
                        findViewById(R.id.fuji).setVisibility(View.VISIBLE);

                        break;

                    case "初級":
                        findViewById(R.id.syokyu).setVisibility(View.VISIBLE);
                        break;

                    case "二級":
                        findViewById(R.id.nikyu).setVisibility(View.VISIBLE);
                        break;

                    case "一級":
                        findViewById(R.id.itikyu).setVisibility(View.VISIBLE);
                        break;

                    case "菊":
                        findViewById(R.id.kiku).setVisibility(View.VISIBLE);
                        break;

                    case "隼":
                        findViewById(R.id.hayabusa).setVisibility(View.VISIBLE);
                        break;

                    case "富士":
                        findViewById(R.id.fuji).setVisibility(View.VISIBLE);
                        break;
                }
            }
            //アイテムが選択されなかったとき
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //処理なし
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

                final ArrayList<SaimokuDataItem> ary = SaimokuJsonHelper.parseJson(resString);
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


    private  void SaimokuSet( int KID, int firstID, int secondID, int thirdID){
        Log.d(TAG, "SaimokuSet : PPPPPPPPPP" + PersonID);
        Log.d(TAG, "SaimokuSet : KKKKKKKKKK" + KID);
        Log.d(TAG, "SaimokuSet : FFFFFFFFFF" + firstID);
        Log.d(TAG, "SaimokuSet : SSSSSSSSSS" + secondID);
        Log.d(TAG, "SaimokuSet : TTTTTTTTTT" + thirdID);
        Intent intent = new Intent(SaimokuActivity.this, SaimokuUpdateActivity.class);
        intent.putExtra("PID",PersonID);
        intent.putExtra("KID", KID);
        intent.putExtra("FirstID", firstID);
        intent.putExtra("SecondID", secondID);
        intent.putExtra("ThirdID", thirdID);
        startActivity(intent);
    }


    private void updateUI(ArrayList<SaimokuDataItem> items) {
        Log.d(TAG, "UI更新");
        // 各 TextView にデータをセット
        ArrayList<String> DateArray = new ArrayList<>();
        ArrayList<String> NameArray = new ArrayList<>();

        for (SaimokuDataItem item : items){
            DateArray.add(item.getCompletedDate());
            NameArray.add(item.getSyouninsyaName());
        }

        ((TextView) findViewById(R.id.SyouninsyaName2_1_1_1)).setText(NameArray.get(0));
        ((TextView) findViewById(R.id.SyouninsyaName2_1_2_1)).setText(NameArray.get(1));
        ((TextView) findViewById(R.id.SyouninsyaName2_1_3_1)).setText(NameArray.get(2));
        ((TextView) findViewById(R.id.SyouninsyaName2_1_4_1)).setText(NameArray.get(3));
        ((TextView) findViewById(R.id.SyouninsyaName2_1_5_1)).setText(NameArray.get(4));
        ((TextView) findViewById(R.id.SyouninsyaName2_1_6_1)).setText(NameArray.get(5));
        ((TextView) findViewById(R.id.SyouninsyaName2_2_1_1)).setText(NameArray.get(6));
        ((TextView) findViewById(R.id.SyouninsyaName2_3_1_1)).setText(NameArray.get(7));
        ((TextView) findViewById(R.id.SyouninsyaName2_3_2_1)).setText(NameArray.get(8));
        ((TextView) findViewById(R.id.SyouninsyaName2_3_3_1)).setText(NameArray.get(9));
        ((TextView) findViewById(R.id.SyouninsyaName2_4_1_1)).setText(NameArray.get(10));
        ((TextView) findViewById(R.id.SyouninsyaName2_5_1_1)).setText(NameArray.get(11));
        ((TextView) findViewById(R.id.SyouninsyaName2_6_1_1)).setText(NameArray.get(12));
        ((TextView) findViewById(R.id.SyouninsyaName3_1_1_1)).setText(NameArray.get(13));
        ((TextView) findViewById(R.id.SyouninsyaName3_1_2_1)).setText(NameArray.get(14));
        ((TextView) findViewById(R.id.SyouninsyaName3_1_3_1)).setText(NameArray.get(15));
        ((TextView) findViewById(R.id.SyouninsyaName3_2_1_1)).setText(NameArray.get(16));
        ((TextView) findViewById(R.id.SyouninsyaName3_2_2_1)).setText(NameArray.get(17));
        ((TextView) findViewById(R.id.SyouninsyaName3_2_2_2)).setText(NameArray.get(18));
        ((TextView) findViewById(R.id.SyouninsyaName3_2_3_1)).setText(NameArray.get(19));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_1_1)).setText(NameArray.get(20));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_1_2)).setText(NameArray.get(21));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_1_3)).setText(NameArray.get(22));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_1_4)).setText(NameArray.get(23));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_1_5)).setText(NameArray.get(24));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_1_6)).setText(NameArray.get(25));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_2_1)).setText(NameArray.get(26));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_2_2)).setText(NameArray.get(27));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_2_3)).setText(NameArray.get(28));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_3_1)).setText(NameArray.get(29));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_4_1)).setText(NameArray.get(30));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_4_2)).setText(NameArray.get(31));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_5_1)).setText(NameArray.get(32));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_5_2)).setText(NameArray.get(33));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_5_3)).setText(NameArray.get(34));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_6_1)).setText(NameArray.get(35));
        ((TextView) findViewById(R.id.SyouninsyaName3_3_6_2)).setText(NameArray.get(36));
        ((TextView) findViewById(R.id.SyouninsyaName3_4_1_1)).setText(NameArray.get(37));
        ((TextView) findViewById(R.id.SyouninsyaName3_5_1_1)).setText(NameArray.get(38));
        ((TextView) findViewById(R.id.SyouninsyaName3_6_1_1)).setText(NameArray.get(39));
        ((TextView) findViewById(R.id.SyouninsyaName4_1_1_1)).setText(NameArray.get(40));
        ((TextView) findViewById(R.id.SyouninsyaName4_1_2_1)).setText(NameArray.get(41));
        ((TextView) findViewById(R.id.SyouninsyaName4_1_3_1)).setText(NameArray.get(42));
        ((TextView) findViewById(R.id.SyouninsyaName4_1_4_1)).setText(NameArray.get(43));
        ((TextView) findViewById(R.id.SyouninsyaName4_2_1_1)).setText(NameArray.get(44));
        ((TextView) findViewById(R.id.SyouninsyaName4_2_2_1)).setText(NameArray.get(45));
        ((TextView) findViewById(R.id.SyouninsyaName4_2_3_1)).setText(NameArray.get(46));
        ((TextView) findViewById(R.id.SyouninsyaName4_2_3_2)).setText(NameArray.get(47));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_1_1)).setText(NameArray.get(48));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_1_2)).setText(NameArray.get(49));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_1_3)).setText(NameArray.get(50));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_1_4)).setText(NameArray.get(51));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_1_5)).setText(NameArray.get(52));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_1_6)).setText(NameArray.get(53));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_2_1)).setText(NameArray.get(54));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_2_2)).setText(NameArray.get(55));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_2_3)).setText(NameArray.get(56));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_3_1)).setText(NameArray.get(57));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_4_1)).setText(NameArray.get(58));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_4_2)).setText(NameArray.get(59));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_4_3)).setText(NameArray.get(60));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_5_1)).setText(NameArray.get(61));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_5_2)).setText(NameArray.get(62));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_5_3)).setText(NameArray.get(63));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_6_1)).setText(NameArray.get(64));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_6_2)).setText(NameArray.get(65));
        ((TextView) findViewById(R.id.SyouninsyaName4_3_7_1)).setText(NameArray.get(66));
        ((TextView) findViewById(R.id.SyouninsyaName4_4_1_1)).setText(NameArray.get(67));
        ((TextView) findViewById(R.id.SyouninsyaName4_4_2_1)).setText(NameArray.get(68));
        ((TextView) findViewById(R.id.SyouninsyaName4_5_1_1)).setText(NameArray.get(69));
        ((TextView) findViewById(R.id.SyouninsyaName4_6_1_1)).setText(NameArray.get(70));
        ((TextView) findViewById(R.id.SyouninsyaName5_1_1_1)).setText(NameArray.get(71));
        ((TextView) findViewById(R.id.SyouninsyaName5_1_2_1)).setText(NameArray.get(72));
        ((TextView) findViewById(R.id.SyouninsyaName5_1_3_1)).setText(NameArray.get(73));
        ((TextView) findViewById(R.id.SyouninsyaName5_1_4_1)).setText(NameArray.get(74));
        ((TextView) findViewById(R.id.SyouninsyaName5_2_1_1)).setText(NameArray.get(75));
        ((TextView) findViewById(R.id.SyouninsyaName5_2_2_1)).setText(NameArray.get(76));
        ((TextView) findViewById(R.id.SyouninsyaName5_2_2_2)).setText(NameArray.get(77));
        ((TextView) findViewById(R.id.SyouninsyaName5_3_1_1)).setText(NameArray.get(78));
        ((TextView) findViewById(R.id.SyouninsyaName5_3_2_1)).setText(NameArray.get(79));
        ((TextView) findViewById(R.id.SyouninsyaName5_4_1_1)).setText(NameArray.get(80));
        ((TextView) findViewById(R.id.SyouninsyaName5_5_1_1)).setText(NameArray.get(81));
        ((TextView) findViewById(R.id.SyouninsyaName5_6_1_1)).setText(NameArray.get(82));
        ((TextView) findViewById(R.id.SyouninsyaName6_1_1_1)).setText(NameArray.get(83));
        ((TextView) findViewById(R.id.SyouninsyaName6_2_1_1)).setText(NameArray.get(84));
        ((TextView) findViewById(R.id.SyouninsyaName6_2_2_1)).setText(NameArray.get(85));
        ((TextView) findViewById(R.id.SyouninsyaName6_2_3_1)).setText(NameArray.get(86));
        ((TextView) findViewById(R.id.SyouninsyaName6_3_1_1)).setText(NameArray.get(87));
        ((TextView) findViewById(R.id.SyouninsyaName6_4_1_1)).setText(NameArray.get(88));
        ((TextView) findViewById(R.id.SyouninsyaName6_4_2_1)).setText(NameArray.get(89));
        ((TextView) findViewById(R.id.SyouninsyaName6_5_1_1)).setText(NameArray.get(90));
        ((TextView) findViewById(R.id.SyouninsyaName6_5_2_1)).setText(NameArray.get(91));
        ((TextView) findViewById(R.id.SyouninsyaName6_6_1_1)).setText(NameArray.get(92));
        ((TextView) findViewById(R.id.SyouninsyaName7_1_1_1)).setText(NameArray.get(93));
        ((TextView) findViewById(R.id.SyouninsyaName7_1_2_1)).setText(NameArray.get(94));
        ((TextView) findViewById(R.id.SyouninsyaName7_2_1_1)).setText(NameArray.get(95));
        ((TextView) findViewById(R.id.SyouninsyaName7_2_2_1)).setText(NameArray.get(96));
        ((TextView) findViewById(R.id.SyouninsyaName7_3_1_1)).setText(NameArray.get(97));
        ((TextView) findViewById(R.id.SyouninsyaName7_4_1_1)).setText(NameArray.get(98));
        ((TextView) findViewById(R.id.SyouninsyaName7_4_2_1)).setText(NameArray.get(99));
        ((TextView) findViewById(R.id.SyouninsyaName7_4_3_1)).setText(NameArray.get(100));
        ((TextView) findViewById(R.id.SyouninsyaName7_5_1_1)).setText(NameArray.get(101));
        ((TextView) findViewById(R.id.SyouninsyaName7_6_1_1)).setText(NameArray.get(102));

        ((TextView) findViewById(R.id.CompletedDate2_1_1_1)).setText(DateArray.get(0));
        findViewById(R.id.CompletedDate2_1_1_1).setOnClickListener(v -> {
            SaimokuSet(2, 1, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_1_2_1)).setText(DateArray.get(1));
        findViewById(R.id.CompletedDate2_1_2_1).setOnClickListener(v -> {
            SaimokuSet(2, 1, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_1_3_1)).setText(DateArray.get(2));
        findViewById(R.id.CompletedDate2_1_3_1).setOnClickListener(v -> {
            SaimokuSet(2, 1, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_1_4_1)).setText(DateArray.get(3));
        findViewById(R.id.CompletedDate2_1_4_1).setOnClickListener(v -> {
            SaimokuSet(2, 1, 4, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_1_5_1)).setText(DateArray.get(4));
        findViewById(R.id.CompletedDate2_1_5_1).setOnClickListener(v -> {
            SaimokuSet(2, 1, 5, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_1_6_1)).setText(DateArray.get(5));
        findViewById(R.id.CompletedDate2_1_6_1).setOnClickListener(v -> {
            SaimokuSet(2, 1, 6, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_2_1_1)).setText(DateArray.get(6));
        findViewById(R.id.CompletedDate2_2_1_1).setOnClickListener(v -> {
            SaimokuSet(2, 2, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_3_1_1)).setText(DateArray.get(7));
        findViewById(R.id.CompletedDate2_3_1_1).setOnClickListener(v -> {
            SaimokuSet(2, 3, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_3_2_1)).setText(DateArray.get(8));
        findViewById(R.id.CompletedDate2_3_2_1).setOnClickListener(v -> {
            SaimokuSet(2, 3, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_3_3_1)).setText(DateArray.get(9));
        findViewById(R.id.CompletedDate2_3_3_1).setOnClickListener(v -> {
            SaimokuSet(2, 3, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_4_1_1)).setText(DateArray.get(10));
        findViewById(R.id.CompletedDate2_4_1_1).setOnClickListener(v -> {
            SaimokuSet(2, 4, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_5_1_1)).setText(DateArray.get(11));
        findViewById(R.id.CompletedDate2_5_1_1).setOnClickListener(v -> {
            SaimokuSet(2, 5, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate2_6_1_1)).setText(DateArray.get(12));
        findViewById(R.id.CompletedDate2_6_1_1).setOnClickListener(v -> {
            SaimokuSet(2, 6, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_1_1_1)).setText(DateArray.get(13));
        findViewById(R.id.CompletedDate3_1_1_1).setOnClickListener(v -> {
            SaimokuSet(3, 1, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_1_2_1)).setText(DateArray.get(14));
        findViewById(R.id.CompletedDate3_1_2_1).setOnClickListener(v -> {
            SaimokuSet(3, 1, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_1_3_1)).setText(DateArray.get(15));
        findViewById(R.id.CompletedDate3_1_3_1).setOnClickListener(v -> {
            SaimokuSet(3, 1, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_2_1_1)).setText(DateArray.get(16));
        findViewById(R.id.CompletedDate3_2_1_1).setOnClickListener(v -> {
            SaimokuSet(3, 2, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_2_2_1)).setText(DateArray.get(17));
        findViewById(R.id.CompletedDate3_2_2_1).setOnClickListener(v -> {
            SaimokuSet(3, 2, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_2_2_2)).setText(DateArray.get(18));
        findViewById(R.id.CompletedDate3_2_2_2).setOnClickListener(v -> {
            SaimokuSet(3, 2, 2, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate3_2_3_1)).setText(DateArray.get(19));
        findViewById(R.id.CompletedDate3_2_3_1).setOnClickListener(v -> {
            SaimokuSet(3, 2, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_1_1)).setText(DateArray.get(20));
        findViewById(R.id.CompletedDate3_3_1_1).setOnClickListener(v -> {
            SaimokuSet(3, 3, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_1_2)).setText(DateArray.get(21));
        findViewById(R.id.CompletedDate3_3_1_2).setOnClickListener(v -> {
            SaimokuSet(3, 3, 1, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_1_3)).setText(DateArray.get(22));
        findViewById(R.id.CompletedDate3_3_1_3).setOnClickListener(v -> {
            SaimokuSet(3, 3, 1, 3);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_1_4)).setText(DateArray.get(23));
        findViewById(R.id.CompletedDate3_3_1_4).setOnClickListener(v -> {
            SaimokuSet(3, 3, 1, 4);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_1_5)).setText(DateArray.get(24));
        findViewById(R.id.CompletedDate3_3_1_5).setOnClickListener(v -> {
            SaimokuSet(3, 3, 1, 5);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_1_6)).setText(DateArray.get(25));
        findViewById(R.id.CompletedDate3_3_1_6).setOnClickListener(v -> {
            SaimokuSet(3, 3, 1, 6);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_2_1)).setText(DateArray.get(26));
        findViewById(R.id.CompletedDate3_3_2_1).setOnClickListener(v -> {
            SaimokuSet(3, 3, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_2_2)).setText(DateArray.get(27));
        findViewById(R.id.CompletedDate3_3_2_2).setOnClickListener(v -> {
            SaimokuSet(3, 3, 2, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_2_3)).setText(DateArray.get(28));
        findViewById(R.id.CompletedDate3_3_2_3).setOnClickListener(v -> {
            SaimokuSet(3, 3, 2, 3);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_3_1)).setText(DateArray.get(29));
        findViewById(R.id.CompletedDate3_3_3_1).setOnClickListener(v -> {
            SaimokuSet(3, 3, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_4_1)).setText(DateArray.get(30));
        findViewById(R.id.CompletedDate3_3_4_1).setOnClickListener(v -> {
            SaimokuSet(3, 3, 4, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_4_2)).setText(DateArray.get(31));
        findViewById(R.id.CompletedDate3_3_4_2).setOnClickListener(v -> {
            SaimokuSet(3, 3, 4, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_5_1)).setText(DateArray.get(32));
        findViewById(R.id.CompletedDate3_3_5_1).setOnClickListener(v -> {
            SaimokuSet(3, 3, 5, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_5_2)).setText(DateArray.get(33));
        findViewById(R.id.CompletedDate3_3_5_2).setOnClickListener(v -> {
            SaimokuSet(3, 3, 5, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_5_3)).setText(DateArray.get(34));
        findViewById(R.id.CompletedDate3_3_5_3).setOnClickListener(v -> {
            SaimokuSet(3, 3, 5, 3);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_6_1)).setText(DateArray.get(35));
        findViewById(R.id.CompletedDate3_3_6_1).setOnClickListener(v -> {
            SaimokuSet(3, 3, 6, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_3_6_2)).setText(DateArray.get(36));
        findViewById(R.id.CompletedDate3_3_6_2).setOnClickListener(v -> {
            SaimokuSet(3, 3, 6, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate3_4_1_1)).setText(DateArray.get(37));
        findViewById(R.id.CompletedDate3_4_1_1).setOnClickListener(v -> {
            SaimokuSet(3, 4, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_5_1_1)).setText(DateArray.get(38));
        findViewById(R.id.CompletedDate3_5_1_1).setOnClickListener(v -> {
            SaimokuSet(3, 4, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate3_6_1_1)).setText(DateArray.get(39));
        findViewById(R.id.CompletedDate3_6_1_1).setOnClickListener(v -> {
            SaimokuSet(3, 6, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_1_1_1)).setText(DateArray.get(40));
        findViewById(R.id.CompletedDate4_1_1_1).setOnClickListener(v -> {
            SaimokuSet(4, 1, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_1_2_1)).setText(DateArray.get(41));
        findViewById(R.id.CompletedDate4_1_2_1).setOnClickListener(v -> {
            SaimokuSet(4, 1, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_1_3_1)).setText(DateArray.get(42));
        findViewById(R.id.CompletedDate4_1_3_1).setOnClickListener(v -> {
            SaimokuSet(4, 1, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_1_4_1)).setText(DateArray.get(43));
        findViewById(R.id.CompletedDate2_1_1_1).setOnClickListener(v -> {
            SaimokuSet(4, 1, 4, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_2_1_1)).setText(DateArray.get(44));
        findViewById(R.id.CompletedDate4_2_1_1).setOnClickListener(v -> {
            SaimokuSet(4, 2, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_2_2_1)).setText(DateArray.get(45));
        findViewById(R.id.CompletedDate4_2_2_1).setOnClickListener(v -> {
            SaimokuSet(4, 2, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_2_3_1)).setText(DateArray.get(46));
        findViewById(R.id.CompletedDate4_2_3_1).setOnClickListener(v -> {
            SaimokuSet(4, 2, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_2_3_2)).setText(DateArray.get(47));
        findViewById(R.id.CompletedDate4_2_3_2).setOnClickListener(v -> {
            SaimokuSet(4, 2, 3, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_1_1)).setText(DateArray.get(48));
        findViewById(R.id.CompletedDate4_3_1_1).setOnClickListener(v -> {
            SaimokuSet(4, 3, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_1_2)).setText(DateArray.get(49));
        findViewById(R.id.CompletedDate4_3_1_2).setOnClickListener(v -> {
            SaimokuSet(4, 3, 1, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_1_3)).setText(DateArray.get(50));
        findViewById(R.id.CompletedDate4_3_1_3).setOnClickListener(v -> {
            SaimokuSet(4, 3, 1, 3);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_1_4)).setText(DateArray.get(51));
        findViewById(R.id.CompletedDate4_3_1_4).setOnClickListener(v -> {
            SaimokuSet(4, 3, 1, 4);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_1_5)).setText(DateArray.get(52));
        findViewById(R.id.CompletedDate4_3_1_5).setOnClickListener(v -> {
            SaimokuSet(4, 3, 1, 5);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_1_6)).setText(DateArray.get(53));
        findViewById(R.id.CompletedDate4_3_1_6).setOnClickListener(v -> {
            SaimokuSet(4, 3, 1, 6);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_2_1)).setText(DateArray.get(54));
        findViewById(R.id.CompletedDate4_3_2_1).setOnClickListener(v -> {
            SaimokuSet(4, 3, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_2_2)).setText(DateArray.get(55));
        findViewById(R.id.CompletedDate2_1_1_1).setOnClickListener(v -> {
            Log.d(TAG, "updateUI: なんでここがうごくの？");
            SaimokuSet(4, 3, 2, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_2_3)).setText(DateArray.get(56));
        findViewById(R.id.CompletedDate4_3_2_3).setOnClickListener(v -> {
            SaimokuSet(4, 3, 2, 3);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_3_1)).setText(DateArray.get(57));
        findViewById(R.id.CompletedDate4_3_3_1).setOnClickListener(v -> {
            SaimokuSet(4, 3, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_4_1)).setText(DateArray.get(58));
        findViewById(R.id.CompletedDate4_3_4_1).setOnClickListener(v -> {
            SaimokuSet(4, 3, 4, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_4_2)).setText(DateArray.get(59));
        findViewById(R.id.CompletedDate4_3_4_2).setOnClickListener(v -> {
            SaimokuSet(4, 3, 4, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_4_3)).setText(DateArray.get(60));
        findViewById(R.id.CompletedDate4_3_4_3).setOnClickListener(v -> {
            SaimokuSet(4, 3, 4, 3);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_5_1)).setText(DateArray.get(61));
        findViewById(R.id.CompletedDate4_3_5_1).setOnClickListener(v -> {
            SaimokuSet(4, 3, 5, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_5_2)).setText(DateArray.get(62));
        findViewById(R.id.CompletedDate4_3_5_2).setOnClickListener(v -> {
            SaimokuSet(4, 3, 5, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_5_3)).setText(DateArray.get(63));
        findViewById(R.id.CompletedDate4_3_5_3).setOnClickListener(v -> {
            SaimokuSet(4, 3, 5, 3);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_6_1)).setText(DateArray.get(64));
        findViewById(R.id.CompletedDate4_3_6_1).setOnClickListener(v -> {
            SaimokuSet(4, 3, 6, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_6_2)).setText(DateArray.get(65));
        findViewById(R.id.CompletedDate4_3_6_2).setOnClickListener(v -> {
            SaimokuSet(4, 3, 6, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate4_3_7_1)).setText(DateArray.get(66));
        findViewById(R.id.CompletedDate4_3_7_1).setOnClickListener(v -> {
            SaimokuSet(4, 3, 7, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_4_1_1)).setText(DateArray.get(67));
        findViewById(R.id.CompletedDate4_4_1_1).setOnClickListener(v -> {
            SaimokuSet(4, 4, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_4_2_1)).setText(DateArray.get(68));
        findViewById(R.id.CompletedDate4_4_2_1).setOnClickListener(v -> {
            SaimokuSet(4, 4, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_5_1_1)).setText(DateArray.get(69));
        findViewById(R.id.CompletedDate4_5_1_1).setOnClickListener(v -> {
            SaimokuSet(4, 5, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate4_6_1_1)).setText(DateArray.get(70));
        findViewById(R.id.CompletedDate4_6_1_1).setOnClickListener(v -> {
            SaimokuSet(4, 6, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_1_1_1)).setText(DateArray.get(71));
        findViewById(R.id.CompletedDate5_1_1_1).setOnClickListener(v -> {
            SaimokuSet(5, 1, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_1_2_1)).setText(DateArray.get(72));
        findViewById(R.id.CompletedDate5_1_2_1).setOnClickListener(v -> {
            SaimokuSet(5, 1, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_1_3_1)).setText(DateArray.get(73));
        findViewById(R.id.CompletedDate5_1_3_1).setOnClickListener(v -> {
            SaimokuSet(5, 1, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_1_4_1)).setText(DateArray.get(74));
        findViewById(R.id.CompletedDate5_1_4_1).setOnClickListener(v -> {
            SaimokuSet(5, 1, 4, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_2_1_1)).setText(DateArray.get(75));
        findViewById(R.id.CompletedDate5_2_1_1).setOnClickListener(v -> {
            SaimokuSet(5, 2, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_2_2_1)).setText(DateArray.get(76));
        findViewById(R.id.CompletedDate5_2_2_1).setOnClickListener(v -> {
            SaimokuSet(5, 2, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_2_2_2)).setText(DateArray.get(77));
        findViewById(R.id.CompletedDate5_2_2_2).setOnClickListener(v -> {
            SaimokuSet(5, 2, 2, 2);
        });
        ((TextView) findViewById(R.id.CompletedDate5_3_1_1)).setText(DateArray.get(78));
        findViewById(R.id.CompletedDate5_3_1_1).setOnClickListener(v -> {
            SaimokuSet(5, 3, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_3_2_1)).setText(DateArray.get(79));
        findViewById(R.id.CompletedDate5_3_2_1).setOnClickListener(v -> {
            SaimokuSet(5, 3, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_4_1_1)).setText(DateArray.get(80));
        findViewById(R.id.CompletedDate5_4_1_1).setOnClickListener(v -> {
            SaimokuSet(5, 4, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_5_1_1)).setText(DateArray.get(81));
        findViewById(R.id.CompletedDate5_5_1_1).setOnClickListener(v -> {
            SaimokuSet(5, 5, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate5_6_1_1)).setText(DateArray.get(82));
        findViewById(R.id.CompletedDate5_6_1_1).setOnClickListener(v -> {
            SaimokuSet(5, 6, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_1_1_1)).setText(DateArray.get(83));
        findViewById(R.id.CompletedDate6_1_1_1).setOnClickListener(v -> {
            SaimokuSet(6, 1, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_2_1_1)).setText(DateArray.get(84));
        findViewById(R.id.CompletedDate6_2_1_1).setOnClickListener(v -> {
            SaimokuSet(6, 2, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_2_2_1)).setText(DateArray.get(85));
        findViewById(R.id.CompletedDate6_2_2_1).setOnClickListener(v -> {
            SaimokuSet(6, 2, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_2_3_1)).setText(DateArray.get(86));
        findViewById(R.id.CompletedDate6_2_3_1).setOnClickListener(v -> {
            SaimokuSet(6, 2, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_3_1_1)).setText(DateArray.get(87));
        findViewById(R.id.CompletedDate6_3_1_1).setOnClickListener(v -> {
            SaimokuSet(6, 3, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_4_1_1)).setText(DateArray.get(88));
        findViewById(R.id.CompletedDate6_4_1_1).setOnClickListener(v -> {
            SaimokuSet(6, 4, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_4_2_1)).setText(DateArray.get(89));
        findViewById(R.id.CompletedDate6_4_2_1).setOnClickListener(v -> {
            SaimokuSet(6, 4, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_5_1_1)).setText(DateArray.get(90));
        findViewById(R.id.CompletedDate6_5_1_1).setOnClickListener(v -> {
            SaimokuSet(6, 5, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_5_2_1)).setText(DateArray.get(91));
        findViewById(R.id.CompletedDate6_5_2_1).setOnClickListener(v -> {
            SaimokuSet(6, 5, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate6_6_1_1)).setText(DateArray.get(92));
        findViewById(R.id.CompletedDate6_6_1_1).setOnClickListener(v -> {
            SaimokuSet(6, 6, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_1_1_1)).setText(DateArray.get(93));
        findViewById(R.id.CompletedDate7_1_1_1).setOnClickListener(v -> {
            SaimokuSet(7, 1, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_1_2_1)).setText(DateArray.get(94));
        findViewById(R.id.CompletedDate7_1_2_1).setOnClickListener(v -> {
            SaimokuSet(7, 1, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_2_1_1)).setText(DateArray.get(95));
        findViewById(R.id.CompletedDate7_2_1_1).setOnClickListener(v -> {
            SaimokuSet(7, 2, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_2_2_1)).setText(DateArray.get(96));
        findViewById(R.id.CompletedDate7_2_2_1).setOnClickListener(v -> {
            SaimokuSet(7, 2, 2, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_3_1_1)).setText(DateArray.get(97));
        findViewById(R.id.CompletedDate7_3_1_1).setOnClickListener(v -> {
            SaimokuSet(7, 3, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_4_1_1)).setText(DateArray.get(98));
        findViewById(R.id.CompletedDate7_4_1_1).setOnClickListener(v -> {
            SaimokuSet(7, 4, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_4_2_1)).setText(DateArray.get(99));
        findViewById(R.id.CompletedDate7_4_1_1).setOnClickListener(v -> {
            SaimokuSet(7, 4, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_4_3_1)).setText(DateArray.get(100));
        findViewById(R.id.CompletedDate7_4_3_1).setOnClickListener(v -> {
            SaimokuSet(7, 4, 3, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_5_1_1)).setText(DateArray.get(101));
        findViewById(R.id.CompletedDate7_5_1_1).setOnClickListener(v -> {
            SaimokuSet(7, 5, 1, 1);
        });
        ((TextView) findViewById(R.id.CompletedDate7_6_1_1)).setText(DateArray.get(102));
        findViewById(R.id.CompletedDate7_6_1_1).setOnClickListener(v -> {
            SaimokuSet(7, 6, 1, 1);
        });
    }
}