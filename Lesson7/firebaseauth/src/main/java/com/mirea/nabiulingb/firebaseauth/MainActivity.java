package com.mirea.nabiulingb.firebaseauth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.mirea.nabiulingb.firebaseauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.titleText.setText(R.string.emailpassword_title_text);
        binding.detailTextView.setText(R.string.desc_emailpassword);

        mAuth = FirebaseAuth.getInstance();

        binding.emailPasswordButtons.setVisibility(View.VISIBLE);
        binding.signedInButtons.setVisibility(View.GONE);

        binding.emailCreateAccountButton.setOnClickListener(v -> {
            String email = binding.fieldEmail.getText().toString();
            String password = binding.fieldPassword.getText().toString();
            createAccount(email, password);
        });

        binding.emailSignInButton.setOnClickListener(v -> {
            String email = binding.fieldEmail.getText().toString();
            String password = binding.fieldPassword.getText().toString();
            signIn(email, password);
        });

        binding.signOutButton.setOnClickListener(v -> signOut());
        binding.verifyEmailButton.setOnClickListener(v -> sendEmailVerification());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        sendEmailVerification();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.auth_failed,
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) return;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.error_sign_in_failed,
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        binding.verifyEmailButton.setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(this, task -> {
                        binding.verifyEmailButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Failed to send verification email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = binding.fieldEmail.getText().toString();
        if (email.isEmpty()) {
            binding.fieldEmail.setError("Required");
            valid = false;
        } else {
            binding.fieldEmail.setError(null);
        }

        String password = binding.fieldPassword.getText().toString();
        if (password.isEmpty()) {
            binding.fieldPassword.setError("Required");
            valid = false;
        } else {
            binding.fieldPassword.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in
            binding.statusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            binding.detailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            binding.emailPasswordButtons.setVisibility(View.GONE);
            binding.emailPasswordFields.setVisibility(View.GONE);
            binding.signedInButtons.setVisibility(View.VISIBLE);

            binding.verifyEmailButton.setEnabled(!user.isEmailVerified());
        } else {
            binding.statusTextView.setText(R.string.signed_out);
            binding.detailTextView.setText(R.string.desc_emailpassword);

            binding.emailPasswordButtons.setVisibility(View.VISIBLE);
            binding.emailPasswordFields.setVisibility(View.VISIBLE);
            binding.signedInButtons.setVisibility(View.GONE);
        }
    }
}