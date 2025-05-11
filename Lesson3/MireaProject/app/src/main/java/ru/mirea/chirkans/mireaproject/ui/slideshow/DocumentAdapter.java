package ru.mirea.chirkans.mireaproject.ui.slideshow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import ru.mirea.chirkans.mireaproject.ui.Document;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentHolder> {

    private final List<Document> documents;
    private final OnDocumentSelectListener selectionListener;

    public DocumentAdapter(List<Document> documents, OnDocumentSelectListener listener) {
        this.documents = documents;
        this.selectionListener = listener;
    }

    @NonNull
    @Override
    public DocumentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new DocumentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentHolder holder, int position) {
        Document document = documents.get(position);
        holder.documentTitle.setText(document.getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (selectionListener != null) {
                selectionListener.onDocumentSelected(document.getFile());
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    static class DocumentHolder extends RecyclerView.ViewHolder {
        TextView documentTitle;

        public DocumentHolder(@NonNull View itemView) {
            super(itemView);
            documentTitle = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnDocumentSelectListener {
        void onDocumentSelected(File document);
    }
}