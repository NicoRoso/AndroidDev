package com.mirea.nabiulingb.mireaproject.ui.files;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mirea.nabiulingb.mireaproject.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FilesFragment extends Fragment {

    private EditText etFileContent;
    private Button btnEncrypt, btnDecrypt, btnSaveFile, btnLoadFile;
    private static final String FILE_NAME = "encrypted_notes.txt";
    private static final String ENCRYPTION_KEY = "MIREA2024";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);

        etFileContent = view.findViewById(R.id.et_file_content);
        btnEncrypt = view.findViewById(R.id.btn_encrypt);
        btnDecrypt = view.findViewById(R.id.btn_decrypt);
        btnSaveFile = view.findViewById(R.id.btn_save_file);
        btnLoadFile = view.findViewById(R.id.btn_load_file);

        btnEncrypt.setOnClickListener(v -> encryptText());
        btnDecrypt.setOnClickListener(v -> decryptText());
        btnSaveFile.setOnClickListener(v -> saveToFile());
        btnLoadFile.setOnClickListener(v -> loadFromFile());

        view.findViewById(com.mirea.nabiulingb.mireaproject.R.id.fab_add_note).setOnClickListener(v -> showAddNoteDialog());

        return view;
    }

    private void encryptText() {
        String text = etFileContent.getText().toString();
        if (!text.isEmpty()) {
            String encrypted = simpleEncrypt(text, ENCRYPTION_KEY);
            etFileContent.setText(encrypted);
            Toast.makeText(getContext(), "Текст зашифрован", Toast.LENGTH_SHORT).show();
        }
    }

    private void decryptText() {
        String text = etFileContent.getText().toString();
        if (!text.isEmpty()) {
            String decrypted = simpleDecrypt(text, ENCRYPTION_KEY);
            etFileContent.setText(decrypted);
            Toast.makeText(getContext(), "Текст расшифрован", Toast.LENGTH_SHORT).show();
        }
    }

    private String simpleEncrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            result.append((char) (text.charAt(i) ^ key.charAt(i % key.length())));
        }
        return result.toString();
    }

    private String simpleDecrypt(String text, String key) {
        return simpleEncrypt(text, key); // XOR обратим
    }

    private void saveToFile() {
        String text = etFileContent.getText().toString();
        if (!text.isEmpty()) {
            try (FileOutputStream fos = requireActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
                fos.write(text.getBytes(StandardCharsets.UTF_8));
                Toast.makeText(getContext(), "Файл сохранен", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void loadFromFile() {
        try (FileInputStream fis = requireActivity().openFileInput(FILE_NAME)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String text = new String(bytes);
            etFileContent.setText(text);
            Toast.makeText(getContext(), "Файл загружен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Файл не найден", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Добавить новую заметку");

        final EditText input = new EditText(getContext());
        input.setHint("Введите текст заметки");
        builder.setView(input);

        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String note = input.getText().toString();
            if (!note.isEmpty()) {
                etFileContent.append(note + "\n\n");
            }
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}