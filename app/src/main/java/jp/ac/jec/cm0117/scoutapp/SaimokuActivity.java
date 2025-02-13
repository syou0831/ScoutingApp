package jp.ac.jec.cm0117.scoutapp;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SaimokuActivity extends AppCompatActivity {

    private static final String TAG = "SaimokuDataActivity";

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

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http");
        uriBuilder.encodedAuthority("23cm0117.main.jp");
        uriBuilder.path("scouting/JSON/SaimokuJSON.php");

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
                    case "見習い":

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

    private RowModelAdapter adapter;
    class RowModelAdapter extends ArrayAdapter<Saimoku_Item> {
        RowModelAdapter(Context context){
            super(context, R.layout.activity_saimoku_box_item);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final Saimoku_Item item = getItem(position);
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.activity_saimoku_item,null);
            }
            if(item != null){
                ((TextView) findViewById(R.id.SecondID)).setText("AA");
            }
            return convertView;
        }
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
//                        updateUI(ary.get(0)); // 最初のデータを適用
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

//    private void updateUI(Saimoku_Item person) {
//        // 各 TextView にデータをセット
//        ((TextView) findViewById(R.id.GenreText)).setText(person.getSex());
//    }
}