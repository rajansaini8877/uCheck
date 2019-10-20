package com.myappcompany.rajan.ucheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Record extends AppCompatActivity {

    ArrayList<ImageData> mList;
    private Map<String, String> val;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        final String data2 = getIntent().getExtras().getString("AADHAAR","000000000000");
        mList = new ArrayList<>();
        mListView = (ListView)findViewById(R.id.listview);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try {
                                    if (document.getId().equals(data2)) {
                                        val = (Map) document.get("record");
                                        // Get keys and values
                                        for (Map.Entry<String, String> entry : val.entrySet()) {
                                            String k = entry.getKey();
                                            String v = entry.getValue();
                                            mList.add(new ImageData(v));
                                            //System.out.println("Key: " + k + ", Value: " + v);
                                        }

                                        break;
                                    }
                                }
                                catch(Exception e) {
                                    Toast.makeText(Record.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            Toast.makeText(Record.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        DetailsAdapter adapter = new DetailsAdapter(Record.this, mList);
        mListView.setAdapter(adapter);

    }
}
