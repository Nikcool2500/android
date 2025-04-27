package ru.mirea.chirka.viewbinding;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.mirea.chirka.viewbinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.songTitle.setText("Turbo Killer");
        binding.artistName.setText("Carpenter Brut");

        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    binding.playButton.setText("Play");
                    Toast.makeText(MainActivity.this, "Paused", Toast.LENGTH_SHORT).show();
                } else {
                    binding.playButton.setText("Pause");
                    Toast.makeText(MainActivity.this, "Playing", Toast.LENGTH_SHORT).show();
                }
                isPlaying = !isPlaying;
            }
        });

        binding.prevButton.setOnClickListener(v ->
                Toast.makeText(this, "Previous track", Toast.LENGTH_SHORT).show());

        binding.nextButton.setOnClickListener(v ->
                Toast.makeText(this, "Next track", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}