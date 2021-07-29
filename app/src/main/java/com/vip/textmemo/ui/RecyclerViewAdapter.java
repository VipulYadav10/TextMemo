package com.vip.textmemo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.vip.textmemo.R;
import com.vip.textmemo.data.DatabaseHandler;
import com.vip.textmemo.model.Memo;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Memo> memoList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private DatabaseHandler handler;

    public RecyclerViewAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memo memo = memoList.get(position);
        holder.heading.setText(memo.getHeading());
        holder.text.setText(memo.getText());
        holder.date.setText(MessageFormat.format("Date Modified: {0}", memo.getFinalDate()));
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView heading;
        public TextView text;
        public TextView date;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            heading = itemView.findViewById(R.id.heading_row_id);
            text = itemView.findViewById(R.id.text_row_id);
            date = itemView.findViewById(R.id.date_row_id);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            handler = new DatabaseHandler(context);
            int position = getAdapterPosition();
            Memo memo = memoList.get(position);
            switch(v.getId()) {
                case R.id.edit_button:
                    updateMemo(memo);
                    break;
                case R.id.delete_button:
                    deleteMemo(memo.getIndex());
                    break;
            }
        }

        private void deleteMemo(final int id) {
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.pop_confirmation, null);

            Button yesButton = view.findViewById(R.id.yes_button);
            Button noButton = view.findViewById(R.id.no_button);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.deleteMemo(id);
                    memoList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        private void updateMemo(final Memo memo) {
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            Button saveButton;
            final EditText headingEdit;
            final EditText textEdit;
            TextView titleText;

            saveButton = view.findViewById(R.id.save_popup_button);
            headingEdit = view.findViewById(R.id.heading_popup_id);
            textEdit = view.findViewById(R.id.text_popup_id);
            titleText = view.findViewById(R.id.title);

            saveButton.setText(R.string.update_text);
            titleText.setText(R.string.update_memo_text);
            headingEdit.setText(memo.getHeading());
            textEdit.setText(memo.getText());

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    memo.setHeading(headingEdit.getText().toString());
                    memo.setText(textEdit.getText().toString());

                    if(!headingEdit.getText().toString().isEmpty()
                            && !textEdit.getText().toString().isEmpty()) {
                        handler.updateMemo(memo);
                        notifyItemChanged(getAdapterPosition(), memo);
                    }
                    else {
                        Snackbar.make(v, "Fields cannot be Empty!", Snackbar.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                }
            });
        }
    }
}
