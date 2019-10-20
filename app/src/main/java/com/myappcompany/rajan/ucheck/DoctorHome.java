package com.myappcompany.rajan.ucheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DoctorHome extends AppCompatActivity {

    private TextView name;
    private EditText aadhaar;
    private Button search;

    private String val1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        String data = getIntent().getExtras().getString("NAME","default id");

        name = (TextView)findViewById(R.id.name);
        name.setText(data);
        aadhaar = (EditText)findViewById(R.id.aadhaar);
        search = (Button)findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("user")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        try {
                                            if (document.getId().equals(aadhaar.getText().toString())) {
                                                val1 = document.getString("name");
                                                Intent intent = new Intent(DoctorHome.this, UserData.class);
                                                intent.putExtra("NAME", val1);
                                                intent.putExtra("AADHAAR", aadhaar.getText().toString());
                                                startActivity(intent);
                                                break;
                                            }
                                        }
                                        catch(Exception e) {
                                            Toast.makeText(DoctorHome.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                } else {
                                    Toast.makeText(DoctorHome.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
