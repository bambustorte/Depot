package se.ju.group8.depot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;

    private TextView uEmail;
    private TextView uPass;
    private Button signIn;
    private TextView register_text_view;

    private boolean register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_text_view = (TextView) findViewById(R.id.login_register);
        register_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(register) {
                    register_text_view.setText(R.string.login_register);
                    register = false;
                } else {
                    register_text_view.setText(R.string.login_register2);
                    register = true;
                }
            }
        });

        uEmail = (TextView) findViewById(R.id.login_email);
        uPass = (TextView) findViewById(R.id.login_pass);
        signIn = (Button) findViewById(R.id.login_button_signin);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uEmail.length()!=0 && uPass.length()!=0) {
                    if (register) {
                        registerUser(uEmail.getText().toString(), uPass.getText().toString());
//                        uEmail.setText("");
//                        uPass.setText("");
                        register = false;
                        register_text_view.setText(R.string.login_register);
                    } else {
                        if(signInUser(uEmail.getText().toString(), uPass.getText().toString())) {
                            Intent intent = new Intent(ActivityLogin.this.getApplicationContext(), ActivityMain.class);

                            intent.putExtra("userEmail", user.getEmail());
                            intent.putExtra("userID", user.getUid());

                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    public boolean registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "createUserWithEmail:success");
                            user = auth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
        return true;
    }

    public boolean signInUser(String email, String password){
        final boolean[] success = {false};
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    boolean inner;
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            success[0] = true;
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
        return success[0];
    }
}
