package com.mirea.nabiulingb.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.mirea.nabiulingb.mireaproject.databinding.FragmentDataBinding;

public class DataFragment extends Fragment {
    private FragmentDataBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textTitle.setText("Кибербезопасность");
        binding.textContent.setText(
                "Кибербезопасность - это практика защиты систем, сетей и программ от цифровых атак. " +
                        "Эти атаки обычно направлены на получение доступа, изменение или уничтожение конфиденциальной информации, " +
                        "вымогательство денег у пользователей или нарушение нормальной работы бизнес-процессов.\n\n" +
                        "Основные направления:\n" +
                        "• Защита сетей\n" +
                        "• Защита приложений\n" +
                        "• Защита данных\n" +
                        "• Управление идентификацией\n" +
                        "• Облачная безопасность\n\n" +
                        "Современные угрозы включают фишинг, ransomware, вредоносное ПО, социальную инженерию и атаки типа \"человек посередине\"."
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}