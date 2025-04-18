package ru.mirea.chirkans.simplefragmentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private Button btnFirst, btnSecond;
    private boolean isLandscape = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLandscape = getResources().getConfiguration().orientation ==
                android.content.res.Configuration.ORIENTATION_LANDSCAPE;

        if (!isLandscape) {
            btnFirst = findViewById(R.id.btnFirstFragment);
            btnSecond = findViewById(R.id.btnSecondFragment);

            btnFirst.setOnClickListener(v -> loadFragment(new FirstFragment()));
            btnSecond.setOnClickListener(v -> loadFragment(new SecondFragment()));

            loadFragment(new FirstFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        if (isLandscape) return;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
}
