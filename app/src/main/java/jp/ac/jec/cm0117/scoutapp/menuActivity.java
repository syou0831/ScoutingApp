package jp.ac.jec.cm0117.scoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int PersonID = getIntent().getIntExtra("PersonID", 0);

        findViewById(R.id.button_PersonData).setOnClickListener(v -> {
            Log.d("TAG", "個人情報表示 ");
            Intent intent = new Intent(menuActivity.this, PersonDataActivity.class);
            intent.putExtra("PersonID",PersonID);
            startActivity(intent);
        });

        findViewById(R.id.button_saimoku).setOnClickListener(v -> {
            Log.d("TAG", "細目表示 ");
            Intent intent = new Intent(menuActivity.this, SaimokuActivity.class);
            intent.putExtra("PersonID",PersonID);
            startActivity(intent);
        });
    }
}