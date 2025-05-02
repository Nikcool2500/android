package ru.mirea.chirkans.mireaproject.ui;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

import ru.mirea.chirkans.mireaproject.R;

public class VoiceNotesFragment extends Fragment {
    private Button recordButton, playButton;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private boolean isRecording = false;
    private boolean isPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice_notes, container, false);

        recordButton = view.findViewById(R.id.record_button);
        playButton = view.findViewById(R.id.play_button);
        playButton.setEnabled(false);

        audioFilePath = getActivity().getExternalCacheDir().getAbsolutePath() + "/audiotest.3gp";

        recordButton.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
                recordButton.setText("Начать запись");
                playButton.setEnabled(true);
            } else {
                startRecording();
                recordButton.setText("Остановить запись");
                playButton.setEnabled(false);
            }
            isRecording = !isRecording;
        });

        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopPlaying();
                playButton.setText("Воспроизвести");
                recordButton.setEnabled(true);
            } else {
                startPlaying();
                playButton.setText("Остановить");
                recordButton.setEnabled(false);
            }
            isPlaying = !isPlaying;
        });

        return view;
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audioFilePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(getContext(), "Запись начата", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(getContext(), "Запись остановлена", Toast.LENGTH_SHORT).show();
        }
    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(getContext(), "Воспроизведение начато", Toast.LENGTH_SHORT).show();

            mediaPlayer.setOnCompletionListener(mp -> {
                stopPlaying();
                playButton.setText("Воспроизвести");
                recordButton.setEnabled(true);
                isPlaying = false;
            });
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(getContext(), "Воспроизведение остановлено", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
        if (isPlaying) {
            stopPlaying();
        }
    }
}