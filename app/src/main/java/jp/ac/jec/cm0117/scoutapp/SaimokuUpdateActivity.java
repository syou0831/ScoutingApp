package jp.ac.jec.cm0117.scoutapp;

import static android.text.TextUtils.isEmpty;
import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.util.Date.*;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Person;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SaimokuUpdateActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saimoku_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int PersonID = intent.getIntExtra("PID",0);
        Log.d("TAG", "onCreate: " + PersonID);
        int kID = intent.getIntExtra("KID",0);
        Log.d("TAG", "onCreate: " + kID);
        int firstID = intent.getIntExtra("FirstID",0);
        Log.d("TAG", "onCreate: " + firstID);
        int secondID = intent.getIntExtra("SecondID",0);
        Log.d("TAG", "onCreate: " + secondID);
        int thirdID = intent.getIntExtra("ThirdID",0);
        Log.d("TAG", "onCreate: " + thirdID);

        findViewById(R.id.DatePickerButton).setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    new DialogDateEvent(),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        EditText nameEdit = findViewById(R.id.NameEdit);


        Button submit = findViewById(R.id.submitButton);

        submit.setOnClickListener(v -> {

            String name = nameEdit.getText().toString();

            String date = ((TextView) findViewById(R.id.testText)).getText().toString();

            if(!isEmpty(name) && !isEmpty(date)) {

                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.scheme("http");
                uriBuilder.encodedAuthority("23cm0117.main.jp");
                uriBuilder.path("scouting/JSON/SaimokuUPDateJSON.php");
                uriBuilder.appendQueryParameter("PID", String.valueOf(PersonID));
                uriBuilder.appendQueryParameter("KaikyuID", String.valueOf(kID));
                uriBuilder.appendQueryParameter("FirstID", String.valueOf(firstID));
                uriBuilder.appendQueryParameter("SecondID", String.valueOf(secondID));
                uriBuilder.appendQueryParameter("ThirdID", String.valueOf(thirdID));
                uriBuilder.appendQueryParameter("Date", date);
                uriBuilder.appendQueryParameter("Name", name);

                Log.d("TAG", "JSON_URL: " + uriBuilder);
                uriBuilder.build();
            }
        });

    }

    private class DialogDateEvent implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            TextView editText = (TextView) findViewById(R.id.testText);
            month += 1;
            String monthText = "";
            if(month < 10){
                monthText = "0" + String.valueOf(month);
            }else{
                monthText = String.valueOf(month);
            }
            editText.setText((year + "-" + monthText + "-" + dayOfMonth));

        }
    }
}


