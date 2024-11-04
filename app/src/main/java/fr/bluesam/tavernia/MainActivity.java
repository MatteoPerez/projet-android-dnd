package fr.bluesam.tavernia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    Button createSheetButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    private RecyclerView characterRecyclerView;
    private SheetRecyclerView characterAdapter;
    private List<SheetHelperClass> characterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtenir le nom d'utilisateur de l'intent
        username = getIntent().getStringExtra("username");

        createSheetButton = findViewById(R.id.create_sheet_button);
        createSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateSheetActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        characterRecyclerView = findViewById(R.id.character_recycler_view);
        characterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        characterList = new ArrayList<>();
        characterAdapter = new SheetRecyclerView(this, characterList, new ArrayList<>(), username);
        characterRecyclerView.setAdapter(characterAdapter);

        reference = FirebaseDatabase.getInstance("https://projet-android-dnd-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(username).child("sheets");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                characterList.clear();
                List<String> ids = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SheetHelperClass character = dataSnapshot.getValue(SheetHelperClass.class);
                    characterList.add(character);
                    ids.add(dataSnapshot.getKey());
                }
                characterAdapter = new SheetRecyclerView(MainActivity.this, characterList, ids, username);
                characterRecyclerView.setAdapter(characterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Failed to load characters", error.toException());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_topnav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

