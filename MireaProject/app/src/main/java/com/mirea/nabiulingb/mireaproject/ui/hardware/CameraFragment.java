package com.mirea.nabiulingb.mireaproject.ui.hardware;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mirea.nabiulingb.mireaproject.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment {

    private ImageView imageViewPhoto;
    private EditText editTextCaption;
    private Button buttonSave;
    private TextView textViewSavedNote;

    private static final String TAG = "Фото-фрагмент";
    private static final int REQUEST_CODE_PERMISSIONS = 100;

    private Uri imageUri;
    private ActivityResultLauncher<String[]> permissionRequestLauncher;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public CameraFragment() {
        // Пустой конструктор
    }

    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionRequestLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean cameraGranted = result.getOrDefault(CAMERA, false);
                    Boolean storageGranted = result.getOrDefault(WRITE_EXTERNAL_STORAGE, false);

                    if (cameraGranted != null && cameraGranted) {
                        Log.d(TAG, "Доступ к камере разрешен");
                        takePhoto();
                    } else {
                        Log.w(TAG, "Доступ к камере запрещен");
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "Результат съемки: " + result.getResultCode());
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.d(TAG, "Фото сохранено: " + imageUri);
                            imageViewPhoto.setImageURI(imageUri);
                            buttonSave.setEnabled(true);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        imageViewPhoto = root.findViewById(R.id.imageViewNotePhoto);
        editTextCaption = root.findViewById(R.id.editTextNoteCaption);
        buttonSave = root.findViewById(R.id.buttonSaveNote);
        textViewSavedNote = root.findViewById(R.id.textViewSavedNote);

        buttonSave.setEnabled(false);

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndTakePhoto();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        return root;
    }

    private void checkPermissionsAndTakePhoto() {
        boolean cameraPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storagePermissionGranted = ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (cameraPermissionGranted && storagePermissionGranted) {
            Log.d(TAG, "Все необходимые разрешения получены");
            takePhoto();
        } else {
            Log.d(TAG, "Запрос разрешений у пользователя");
            permissionRequestLauncher.launch(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE});
        }
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            File photoFile = createImageFile();
            String authorities = requireContext().getPackageName() + ".fileprovider";
            imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
            Log.d(TAG, "Создан URI для фото: " + imageUri);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cameraActivityResultLauncher.launch(cameraIntent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка при создании файла для фото", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "ФОТО_" + timeStamp + "_";

        File storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }

    private void saveNote() {
        String caption = editTextCaption.getText().toString().trim();

        if (imageUri == null) {
            Toast.makeText(requireContext(), "Пожалуйста, сначала сделайте фото", Toast.LENGTH_SHORT).show();
            return;
        }
        if (caption.isEmpty()) {
            Toast.makeText(requireContext(), "Пожалуйста, введите текст заметки", Toast.LENGTH_SHORT).show();
            return;
        }

        String savedNoteText = "Фото: " + imageUri.getLastPathSegment() + "\nТекст: " + caption;
        textViewSavedNote.setText("Последняя запись:\n" + savedNoteText);

        Toast.makeText(requireContext(), "Ваша заметка сохранена", Toast.LENGTH_SHORT).show();

        imageUri = null;
        imageViewPhoto.setImageResource(android.R.drawable.ic_menu_camera);
        editTextCaption.setText("");
        buttonSave.setEnabled(false);
    }
}