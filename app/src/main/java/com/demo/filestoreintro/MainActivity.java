package com.demo.filestoreintro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private EditText enterTitle;
    private EditText enterThought;
    private Button saveButton, showButton, updateTitle, deleteButton;
    private TextView recTitle;
//    private TextView recThought;

    // Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";

    // Connect to Firestoe
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

//    private DocumentReference journalRef = db.document("Journal/First Thought");

    private DocumentReference journalRef = db.collection("Journal")
            .document("First Thought");

    private CollectionReference collectionReference = db.collection("Journal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterTitle = findViewById(R.id.edit_text_title);
        enterThought = findViewById(R.id.edit_text_thoughts);
        recTitle = findViewById(R.id.rec_title);
//        recThought = findViewById(R.id.rec_thought);
        saveButton = findViewById(R.id.save_button);
        showButton = findViewById(R.id.show_data);
        updateTitle = findViewById(R.id.update_data);
        deleteButton = findViewById(R.id.delete_button);

        updateTitle.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getThougths();
//                journalRef.get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                if (documentSnapshot.exists()) {
//
//                                    Journal journal = documentSnapshot.toObject(Journal.class);
////                                    String title = documentSnapshot.getString(KEY_TITLE);
////                                    String thought = documentSnapshot.getString(KEY_THOUGHT);
//
//                                    if(journal != null) {
//                                        recTitle.setText(journal.getTitle());
//                                        recThought.setText(journal.getThought());
//                                    }
//
//                                } else {
//                                    Toast.makeText(MainActivity.this, "No Data Exists", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure : " + e.toString());
//                            }
//                        });
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addThought();
//                String title = enterTitle.getText().toString().trim();
//                String thought = enterThought.getText().toString().trim();
//
//                Journal journal = new Journal(title, thought);

//                Map<String, Object> data = new HashMap<String, Object>();
//                data.put(KEY_TITLE, title);
//                data.put(KEY_THOUGHT, thought);

//                journalRef.set(journal)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure: " + e.toString());
//                            }
//                        });
            }
        });
    }

    private void addThought() {

        String title = enterTitle.getText().toString().trim();
        String thought = enterThought.getText().toString().trim();

        Journal journal = new Journal(title, thought);

        collectionReference.add(journal);
    }

    private void getThougths() {

        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {


                            Journal journal = snapshot.toObject(Journal.class);
                            data += "Title: " + journal.getTitle() + " \n "
                                    + "Thought: " + journal.getThought() + "\n\n";

                        }

                        recTitle.setText(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "onEvent: " + e.toString());
                    return;
                }

                String data = "";
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {


                        Journal journal = snapshot.toObject(Journal.class);
                        data += "Title: " + journal.getTitle() + " \n "
                                + "Thought: " + journal.getThought() + "\n\n";

                    }

                    recTitle.setText(data);
                }
            }
        });

//        journalRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
//                                @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//
//                }
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//
//                    String data = "";
//                    Journal journal = documentSnapshot.toObject(Journal.class);
//                    data += "Title: " + journal.getTitle() + " \n "
//                            + "Thought: " + journal.getThought() + "\n\n";
//                    recTitle.setText(data);
//                } else {
//                    recTitle.setText("");
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_data:
                // call update
                updateMyTitle();
                break;

            case R.id.delete_button:
                // call delete
                deleteAll();
                break;
        }
    }

    private void deleteMyTitle() {

//        Map<String, Object> data = new HashMap<>();
//        data.put(KEY_THOUGHT, FieldValue.delete());
//        journalRef.update(data);
        journalRef.update(KEY_THOUGHT, FieldValue.delete());
    }

    private void deleteAll() {
        journalRef.delete();
    }

    private void updateMyTitle() {

        String title = enterTitle.getText().toString().trim();
        String thought = enterThought.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_TITLE, title);
        data.put(KEY_THOUGHT, thought);

        journalRef.update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Updated!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
