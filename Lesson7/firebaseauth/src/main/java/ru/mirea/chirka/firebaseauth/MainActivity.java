package ru.mirea.chirka.firebaseauth;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import ru.mirea.chirka.firebaseauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseAuth";
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();


        binding.createAccountButton.setOnClickListener(v -> createAccount(
                binding.emailField.getText().toString(),
                binding.passwordField.getText().toString()
        ));

        binding.signInButton.setOnClickListener(v -> signIn(
                binding.emailField.getText().toString(),
                binding.passwordField.getText().toString()
        ));

        binding.signOutButton.setOnClickListener(v -> signOut());
        binding.verifyEmailButton.setOnClickListener(v -> sendEmailVerification());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = binding.emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.emailField.setError("Required.");
            valid = false;
        } else {
            binding.emailField.setError(null);
        }

        String password = binding.passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            binding.passwordField.setError("Required.");
            valid = false;
        } else {
            binding.passwordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.statusTextView.setText(getString(
                    R.string.emailpassword_status_fmt,
                    user.getEmail(),
                    user.isEmailVerified()
            ));

            binding.detailTextView.setText(getString(
                    R.string.firebase_status_fmt,
                    user.getUid()
            ));

            binding.emailPasswordButtons.setVisibility(View.GONE);
            binding.emailPasswordFields.setVisibility(View.GONE);
            binding.signedInButtons.setVisibility(View.VISIBLE);
            binding.verifyEmailButton.setEnabled(!user.isEmailVerified());
        } else {
            binding.statusTextView.setText(R.string.signed_out);
            binding.detailTextView.setText("");
            binding.emailPasswordButtons.setVisibility(View.VISIBLE);
            binding.emailPasswordFields.setVisibility(View.VISIBLE);
            binding.signedInButtons.setVisibility(View.GONE);
        }
    }
}