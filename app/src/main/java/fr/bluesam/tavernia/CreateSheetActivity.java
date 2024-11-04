package fr.bluesam.tavernia;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateSheetActivity extends AppCompatActivity {

    private String username;
    private EditText characterName, characterClass, characterMaxHP, characterHP, characterStrength, characterDexterity, characterConstitution, characterIntelligence, characterWisdom, characterCharisma, characterInventory;
    private Button createSheetButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;

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

        // Initialiser les éléments de l'interface
        characterName = findViewById(R.id.sheet_creation_name);
        characterClass = findViewById(R.id.sheet_creation_class);
        characterMaxHP = findViewById(R.id.sheet_creation_max_hp);
        characterHP = findViewById(R.id.sheet_creation_hp);
        characterStrength = findViewById(R.id.sheet_creation_strength);
        characterDexterity = findViewById(R.id.sheet_creation_dexterity);
        characterConstitution = findViewById(R.id.sheet_creation_constitution);
        characterIntelligence = findViewById(R.id.sheet_creation_intelligence);
        characterWisdom = findViewById(R.id.sheet_creation_wisdom);
        characterCharisma = findViewById(R.id.sheet_creation_charisma);
        characterInventory = findViewById(R.id.sheet_creation_inventory);
        createSheetButton = findViewById(R.id.sheet_creation_button);

        // Configurer la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Afficher le bouton de retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back);
        }

        createSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCharacterSheet();
            }
        });
    }

    private void createCharacterSheet(){
        database = FirebaseDatabase.getInstance("https://projet-android-dnd-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("users");

        String name = characterName.getText().toString();
        String cClass = characterClass.getText().toString();
        String maxHP = characterMaxHP.getText().toString();
        String HP = characterHP.getText().toString();
        String strength = characterStrength.getText().toString();
        String dexterity = characterDexterity.getText().toString();
        String constitution = characterConstitution.getText().toString();
        String intelligence = characterIntelligence.getText().toString();
        String wisdom = characterWisdom.getText().toString();
        String charisma = characterCharisma.getText().toString();
        String inventory = characterInventory.getText().toString();
        username = getIntent().getStringExtra("username");

        if (name.isEmpty() || cClass.isEmpty()) {
            Toast.makeText(CreateSheetActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SheetHelperClass sheetHelperClass = new SheetHelperClass(name, cClass, maxHP, HP, strength, dexterity, constitution, intelligence, wisdom, charisma, inventory);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}