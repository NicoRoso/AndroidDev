package com.mirea.nabiulingb.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.mirea.nabiulingb.mireaproject.databinding.FragmentWebViewBinding;

public class WebViewFragment extends Fragment {
    private FragmentWebViewBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWebViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = binding.webView;
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.mirea.ru");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}