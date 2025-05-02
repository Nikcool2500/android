package ru.mirea.chirkans.mireaproject.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.chirkans.mireaproject.R;

public class CollageFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private ImageView[] collageImages = new ImageView[4];
    private int currentImageIndex = 0;
    private Uri[] imageUris = new Uri[4];
    private boolean isWork = false;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collage, container, false);

        collageImages[0] = view.findViewById(R.id.collage_image1);
        collageImages[1] = view.findViewById(R.id.collage_image2);
        collageImages[2] = view.findViewById(R.id.collage_image3);
        collageImages[3] = view.findViewById(R.id.collage_image4);

        Button addPhotoButton = view.findViewById(R.id.add_photo_button);
        addPhotoButton.setOnClickListener(v -> takePhoto());

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        collageImages[currentImageIndex].setImageURI(imageUris[currentImageIndex]);
                        currentImageIndex++;
                        if (currentImageIndex >= 4) {
                            Toast.makeText(getContext(), "Коллаж заполнен", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        checkPermissions();

        return view;
    }

    private void takePhoto() {
        if (currentImageIndex >= 4) {
            Toast.makeText(getContext(), "Коллаж заполнен", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isWork) {
            Toast.makeText(getContext(), "Разрешения не предоставлены", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File photoFile = createImageFile();
            String authorities = requireContext().getPackageName() + ".fileprovider";
            imageUris[currentImageIndex] = FileProvider.getUriForFile(
                    requireContext(),
                    authorities,
                    photoFile
            );

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUris[currentImageIndex]);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraLauncher.launch(cameraIntent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions() {
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA);

        isWork = cameraPermissionStatus == PackageManager.PERMISSION_GRANTED;

        if (!isWork) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isWork = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isWork = false;
                break;
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}