package com.mirea.nabiulingb.mireaproject.ui.work;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import com.mirea.nabiulingb.mireaproject.R;

public class WorkFragment extends Fragment {

    private TextView textStatus;
    private TextView textResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_work, container, false);

        textStatus = root.findViewById(R.id.textStatus);
        textResult = root.findViewById(R.id.textResult);
        View buttonStartWork = root.findViewById(R.id.buttonStartWork);

        buttonStartWork.setOnClickListener(v -> startBackgroundWork());

        return root;
    }

    private void startBackgroundWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager workManager = WorkManager.getInstance(requireContext());
        workManager.enqueue(workRequest);

        workManager.getWorkInfoByIdLiveData(workRequest.getId())
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            textStatus.setText("Статус: " + workInfo.getState().name());
                            if (workInfo.getState().isFinished()) {
                                textResult.setText("Результат: Данные отправлены");
                            }
                        }
                    }
                });
    }
}
