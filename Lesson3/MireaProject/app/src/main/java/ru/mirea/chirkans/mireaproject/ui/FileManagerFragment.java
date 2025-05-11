package ru.mirea.chirkans.mireaproject.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.chirkans.mireaproject.R;
import ru.mirea.chirkans.mireaproject.ui.slideshow.DocumentAdapter;

public class FileManagerFragment extends Fragment {

    private RecyclerView documentsList;
    private TextView documentPreviewText, previewTitle;
    private ScrollView previewContainer;
    private Button formatToggleBtn;
    private FloatingActionButton createDocumentBtn;
    private DocumentAdapter documentAdapter;
    private List<Document> documents = new ArrayList<>();
    private File currentDocument;
    private boolean isXmlType = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);

        documentsList = rootView.findViewById(R.id.documentsRecycler);
        documentPreviewText = rootView.findViewById(R.id.documentContent);
        previewTitle = rootView.findViewById(R.id.previewHeader);
        previewContainer = rootView.findViewById(R.id.previewScroll);
        formatToggleBtn = rootView.findViewById(R.id.toggleFormatBtn);
        createDocumentBtn = rootView.findViewById(R.id.createDocumentBtn);

        initDocumentList();
        loadDocuments();

        createDocumentBtn.setOnClickListener(v -> showDocumentCreationDialog());

        formatToggleBtn.setOnClickListener(v -> {
            if (currentDocument != null) {
                if (isXmlType) {
                    transformToTextFormat(currentDocument);
                } else {
                    transformToXmlFormat(currentDocument);
                }
            }
        });

        return rootView;
    }

    private void initDocumentList() {
        documentAdapter = new DocumentAdapter(documents, doc -> {
            currentDocument = doc;
            isXmlType = doc.getName().endsWith(".xml");
            formatToggleBtn.setText(isXmlType ? "В обычный текст" : "В XML формат");
            formatToggleBtn.setVisibility(View.VISIBLE);
            previewTitle.setVisibility(View.VISIBLE);
            previewContainer.setVisibility(View.VISIBLE);

            try {
                String content = getDocumentContent(doc);
                documentPreviewText.setText(content);
            } catch (IOException e) {
                showMessage("Не удалось прочитать документ");
            }
        });

        documentsList.setLayoutManager(new LinearLayoutManager(getContext()));
        documentsList.setAdapter(documentAdapter);
    }

    private void loadDocuments() {
        documents.clear();
        File appDocumentsDir = requireContext().getFilesDir();
        File[] files = appDocumentsDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".txt") || file.getName().endsWith(".xml")) {
                    documents.add(new Document(file.getName(), file));
                }
            }
        }

        documentAdapter.notifyDataSetChanged();
    }

    private void showDocumentCreationDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Новый документ");

        View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_document, null);
        EditText documentNameInput = dialogLayout.findViewById(R.id.documentNameInput);
        EditText documentContentInput = dialogLayout.findViewById(R.id.documentContentInput);
        SwitchMaterial formatSelector = dialogLayout.findViewById(R.id.formatSelector);

        dialogBuilder.setView(dialogLayout);
        dialogBuilder.setPositiveButton("Создать", (dialog, which) -> {
            String docName = documentNameInput.getText().toString();
            String docContent = documentContentInput.getText().toString();
            boolean useXml = formatSelector.isChecked();

            if (!docName.isEmpty() && !docContent.isEmpty()) {
                makeNewDocument(docName, docContent, useXml);
            } else {
                showMessage("Заполните все поля");
            }
        });
        dialogBuilder.setNegativeButton("Отменить", null);
        dialogBuilder.show();
    }

    private void makeNewDocument(String name, String content, boolean xmlFormat) {
        try {
            String fullName = xmlFormat ? name + ".xml" : name + ".txt";
            File newDoc = new File(requireContext().getFilesDir(), fullName);

            if (xmlFormat) {
                content = formatAsXml(content);
            }

            FileOutputStream outputStream = new FileOutputStream(newDoc);
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            loadDocuments();
            showMessage("Документ создан");
        } catch (IOException e) {
            showMessage("Ошибка при создании");
        }
    }

    private void transformToXmlFormat(File document) {
        try {
            String content = getDocumentContent(document);
            String xmlContent = formatAsXml(content);

            File xmlDoc = new File(requireContext().getFilesDir(),
                    document.getName().replace(".txt", ".xml"));
            FileOutputStream outputStream = new FileOutputStream(xmlDoc);
            outputStream.write(xmlContent.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            document.delete();
            loadDocuments();
            selectDocument(xmlDoc);
            showMessage("Конвертировано в XML");
        } catch (IOException e) {
            showMessage("Ошибка конвертации");
        }
    }

    private void transformToTextFormat(File document) {
        try {
            String xmlContent = getDocumentContent(document);
            String textContent = extractFromXml(xmlContent);

            File textDoc = new File(requireContext().getFilesDir(),
                    document.getName().replace(".xml", ".txt"));
            FileOutputStream outputStream = new FileOutputStream(textDoc);
            outputStream.write(textContent.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            document.delete();
            loadDocuments();
            selectDocument(textDoc);
            showMessage("Конвертировано в текст");
        } catch (IOException e) {
            showMessage("Ошибка конвертации");
        }
    }

    private String formatAsXml(String content) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<document>\n" +
                "    <data>" + content.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;") + "</data>\n" +
                "</document>";
    }

    private String extractFromXml(String xmlContent) {
        try {
            int start = xmlContent.indexOf("<data>") + 6;
            int end = xmlContent.indexOf("</data>");
            if (start >= 0 && end > start) {
                String textData = xmlContent.substring(start, end);
                return textData.replace("&lt;", "<")
                        .replace("&gt;", ">")
                        .replace("&amp;", "&");
            }
            return xmlContent;
        } catch (Exception e) {
            return xmlContent;
        }
    }

    private void selectDocument(File document) {
        currentDocument = document;
        isXmlType = document.getName().endsWith(".xml");
        formatToggleBtn.setText(isXmlType ? "В обычный текст" : "В XML формат");

        try {
            String content = getDocumentContent(document);
            documentPreviewText.setText(content);
        } catch (IOException e) {
            showMessage("Не удалось прочитать документ");
        }
    }

    private String getDocumentContent(File document) throws IOException {
        FileInputStream inputStream = new FileInputStream(document);
        byte[] contentBytes = new byte[(int) document.length()];
        inputStream.read(contentBytes);
        inputStream.close();
        return new String(contentBytes, StandardCharsets.UTF_8);
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}