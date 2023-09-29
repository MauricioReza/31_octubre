package com.amaurypm.videogamesrf.ui;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amaurypm.videogamesrf.R;

public class MyDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title));

        // Inflar el diseño personalizado para el diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
        final EditText editText = dialogView.findViewById(R.id.editText);
        final TextView errorMessage = dialogView.findViewById(R.id.errorMessage);

        // Aplicar un InputFilter para permitir solo "L" seguido de 6 números
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source.subSequence(start, end).toString());

                String inputText = builder.toString();
                if (!inputText.matches("L\\d{6}")) {
                    errorMessage.setVisibility(View.VISIBLE);
                } else {
                    errorMessage.setVisibility(View.GONE);
                }

                return null;
            }
        };

        editText.setFilters(new InputFilter[] { filter });

        builder.setView(dialogView); // Agregar el diseño personalizado al diálogo

        builder.setPositiveButton(getString(R.string.dialog_positive_button), (dialog, which) -> {
            // Manejar la entrada de texto aquí
            String userInput = editText.getText().toString();
            // Haz lo que quieras con el texto ingresado
        });

        return builder.create();
    }
}
