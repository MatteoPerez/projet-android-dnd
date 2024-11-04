package fr.bluesam.tavernia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String username;
    private Button createSheetButton;
    private DatabaseReference reference;
    private RecyclerView characterRecyclerView;
    private SheetRecyclerView characterAdapter;
    private List<SheetHelperClass> characterList;
    private List<String> characterSheetIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");

        createSheetButton = findViewById(R.id.create_sheet_button);
        createSheetButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateSheetActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        characterRecyclerView = findViewById(R.id.character_recycler_view);
        characterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        characterList = new ArrayList<>();
        characterSheetIds = new ArrayList<>();

        characterAdapter = new SheetRecyclerView(this, characterList, characterSheetIds, username);
        characterRecyclerView.setAdapter(characterAdapter);

        reference = FirebaseDatabase.getInstance("https://projet-android-dnd-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(username).child("sheets");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                characterList.clear();
                characterSheetIds.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SheetHelperClass character = dataSnapshot.getValue(SheetHelperClass.class);
                    characterList.add(character);
                    characterSheetIds.add(dataSnapshot.getKey());
                }
                characterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Failed to load characters", error.toException());
            }
        });
    }
}
