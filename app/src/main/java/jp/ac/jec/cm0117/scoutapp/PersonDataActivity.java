package jp.ac.jec.cm0117.scoutapp;

import static jp.ac.jec.cm0117.scoutapp.R.*;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonDataActivity extends AppCompatActivity {

    private static final String TAG = "PersonDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int PersonID = getIntent().getIntExtra("PersonID", 0);

        // サーバーの URL を作成
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http");
        uriBuilder.encodedAuthority("23cm0117.main.jp");
        uriBuilder.path("scouting/JSON/PersonDataJSON.php");
        uriBuilder.appendQueryParameter("PID", String.valueOf(PersonID));

        // データを読み込む
        loadSvData(uriBuilder);
    }

    private void loadSvData(Uri.Builder uriBuilder) {
        Log.d(TAG, "loadSvData: " + uriBuilder.toString());
        final Request request = new Request.Builder().url(uriBuilder.toString()).build();
        final OkHttpClient client = new OkHttpClient.Builder().build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resString = response.body().string();
                Log.d(TAG, "onResponse: " + resString);

                final ArrayList<PersonDataItem> ary = PersonJsonHelper.parseJson(resString);
                Log.d(TAG, "onResponse: 取得したデータ = " + ary);

                // UI スレッドで更新する
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    if (ary != null && !ary.isEmpty()) {
                        updateUI(ary.get(0)); // 最初のデータを適用
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

    private void updateUI(PersonDataItem person) {

        // 各 TextView にデータをセット
        ((TextView) findViewById(R.id.Text_Syozoku)).setText(person.getPrefecture() + "連盟" + person.getArea() + "地区" + person.getCity() + "第" + person.getGroupNum() + "団");
        ((TextView) findViewById(R.id.Text_Name)).setText(person.getName());
        ((TextView) findViewById(R.id.Text_Furigana)).setText(person.getNameFurigana());
        ((TextView) findViewById(R.id.Text_Birthday)).setText(person.getBirthday());
        ((TextView) findViewById(R.id.Text_Address)).setText(person.getAddress());
        ((TextView) findViewById(R.id.Text_Tel)).setText(person.getTel());
        ((TextView) findViewById(R.id.Text_Sex)).setText(person.getSex());
        ((TextView) findViewById(R.id.Text_StateDate)).setText(person.getStateDate());
        ((TextView) findViewById(R.id.Text_StateField)).setText(person.getStateField());
        ((TextView) findViewById(R.id.Text_inBVS)).setText(person.getInBVS());
        ((TextView) findViewById(R.id.Text_inCS)).setText(person.getInCS());
        ((TextView) findViewById(R.id.Text_inBS)).setText(person.getInBS());
        ((TextView) findViewById(R.id.Text_inVS)).setText(person.getInVS());
        ((TextView) findViewById(R.id.Text_inRS)).setText(person.getInRS());
        ((TextView) findViewById(R.id.Text_Minarai)).setText(person.getMinarai());
        ((TextView) findViewById(R.id.Text_Basic)).setText(person.getBasic());
        ((TextView) findViewById(R.id.Text_Second)).setText(person.getSecond());
        ((TextView) findViewById(R.id.Text_First)).setText(person.getFirst());
        ((TextView) findViewById(R.id.Text_Kiku)).setText(person.getKiku());
        ((TextView) findViewById(R.id.Text_Hayabusa)).setText(person.getHayabusa());
        ((TextView) findViewById(R.id.Text_Fuji)).setText(person.getFuji());
        ((TextView) findViewById(R.id.Text_SinkouSyourei)).setText(person.getSinkouSyourei());
        ((TextView) findViewById(R.id.Text_Syukyou)).setText(person.getSyukyou());
        ((TextView) findViewById(R.id.Text_SyukyouName)).setText(person.getSyukyouName());
    }
}