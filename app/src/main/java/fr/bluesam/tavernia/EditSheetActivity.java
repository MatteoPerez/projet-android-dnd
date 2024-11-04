package fr.bluesam.tavernia;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSheetActivity extends AppCompatActivity {

    private EditText characterName, characterClass, characterMaxHP, characterHP, characterStrength, characterDexterity, characterConstitution, characterIntelligence, characterWisdom, characterCharisma, characterInventory;
    private Button saveButton;
    private String username, characterId;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sheet);

        // Configurer la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Afficher le bouton de retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back);
        }

        characterName = findViewById(R.id.sheet_edit_name);
        characterClass = findViewById(R.id.sheet_edit_class);
        characterMaxHP = findViewById(R.id.sheet_edit_max_hp);
        characterHP = findViewById(R.id.sheet_edit_hp);
        characterStrength = findViewById(R.id.sheet_edit_strength);
        characterDexterity = findViewById(R.id.sheet_edit_dexterity);
        characterConstitution = findViewById(R.id.sheet_edit_constitution);
        characterIntelligence = findViewById(R.id.sheet_edit_intelligence);
        characterWisdom = findViewById(R.id.sheet_edit_wisdom);
        characterCharisma = findViewById(R.id.sheet_edit_charisma);
        characterInventory = findViewById(R.id.sheet_edit_inventory);
        saveButton = findViewById(R.id.finish_editing_button);

        username = getIntent().getStringExtra("username");
        characterId = getIntent().getStringExtra("characterId");

        reference = FirebaseDatabase.getInstance("https://projet-android-dnd-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(username).child("sheets").child(characterId);

        reference.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                SheetHelperClass sheet = snapshot.getValue(SheetHelperClass.class);
                if (sheet != null) {
                    characterName.setText(sheet.getName());
                    characterClass.setText(sheet.getcClass());
                }
            }
        });

        saveButton.setOnClickListener(v -> {
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

            if (name.isEmpty() || cClass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            reference.setValue(new SheetHelperClass(name, cClass, maxHP, HP, strength, dexterity, constitution, intelligence, wisdom, charisma, inventory)).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Character updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update character", Toast.LENGTH_SHORT).show();
                }
            });
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
