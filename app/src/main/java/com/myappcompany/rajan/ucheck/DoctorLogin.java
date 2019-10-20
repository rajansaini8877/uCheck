package com.myappcompany.rajan.ucheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DoctorLogin extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private String val1;

    private FirebaseAuth mAuth;
    private QueryDocumentSnapshot temp_doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(DoctorLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(DoctorLogin.this, "Success!",
                                            Toast.LENGTH_SHORT).show();
                                    final Intent intent = new Intent(DoctorLogin.this, DoctorHome.class);

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("doctor")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                            try {
                                                                if (document.getId().equals(username.getText().toString())) {
                                                                    temp_doc = document;
                                                                    val1 = document.getString("name");
                                                                    intent.putExtra("NAME", val1);
                                                                    startActivity(intent);
                                                                    break;
                                                                }
                                                            }
                                                            catch(Exception e) {
                                                                Toast.makeText(DoctorLogin.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    } else {
                                                        Toast.makeText(DoctorLogin.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(DoctorLogin.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

    }
}
