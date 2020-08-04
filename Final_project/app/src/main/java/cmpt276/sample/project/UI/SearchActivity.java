package cmpt276.sample.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cmpt276.sample.project.R;

public class SearchActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_SEARCH = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpSearch();
    }

    private void setUpSearch(){
        Button button = findViewById(R.id.buttonSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}