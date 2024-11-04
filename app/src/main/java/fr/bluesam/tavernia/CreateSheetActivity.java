package fr.bluesam.tavernia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateSheetActivity extends AppCompatActivity {

    private String username;
    EditText characterName, characterClass;
    Button createSheetButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_sheet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        characterName = findViewById(R.id.sheet_creation_name);
        characterClass = findViewById(R.id.sheet_creation_class);
        createSheetButton = findViewById(R.id.sheet_creation_button);

        createSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance("https://projet-android-dnd-default-rtdb.europe-west1.firebasedatabase.app/");
                reference = database.getReference("users");

                String name = characterName.getText().toString();
                String cClass = characterClass.getText().toString();
                username = getIntent().getStringExtra("username");

                if (name.isEmpty() || cClass.isEmpty()) {
                    Toast.makeText(CreateSheetActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                SheetHelperClass sheetHelperClass = new SheetHelperClass(name, cClass);
                String characterId = reference.push().getKey();
                assert characterId != null;

                reference.child(username).child("sheets").child(characterId).setValue(sheetHelperClass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateSheetActivity.this, "Character saved successfully !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateSheetActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CreateSheetActivity.this, "Failed to save character", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}