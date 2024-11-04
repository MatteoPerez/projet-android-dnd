package fr.bluesam.tavernia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SheetRecyclerView extends RecyclerView.Adapter<SheetRecyclerView.ViewHolder> {

    private Context context;
    private List<SheetHelperClass> characterSheets;
    private List<String> characterSheetIds;  // List of Firebase IDs
    private String username;

    public SheetRecyclerView(Context context, List<SheetHelperClass> characterSheets, List<String> characterSheetIds, String username) {
        this.context = context;
        this.characterSheets = characterSheets;
        this.characterSheetIds = characterSheetIds;
        this.username = username;
    }

    public void deleteCharacterSheet(int position) {
        if (position >= 0 && position < characterSheetIds.size()) {
            String sheetId = characterSheetIds.get(position);
            DatabaseReference reference = FirebaseDatabase.getInstance("https://projet-android-dnd-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(username).child("sheets").child(sheetId);
            // Suppression dans Firebase avec gestion des erreurs
            reference.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    characterSheets.remove(position);
                    characterSheetIds.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Character deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete character", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Log.e("SheetRecyclerView", "Firebase deletion failed: ", e));
        } else {
            Log.e("SheetRecyclerView", "Invalid position for deletion: " + position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_sheet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SheetHelperClass sheet = characterSheets.get(position);
        holder.characterName.setText(sheet.getName());
        holder.characterClass.setText(sheet.getcClass());

        // Confirmation de suppression
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Character")
                    .setMessage("Are you sure you want to delete this character?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteCharacterSheet(position))
                    .setNegativeButton("No", null)
                    .show();
        });

        // Action d'édition
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditSheetActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("characterId", characterSheetIds.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return characterSheets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView characterName, characterClass;
        ImageButton deleteButton;
        Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            characterName = itemView.findViewById(R.id.character_name);
            characterClass = itemView.findViewById(R.id.character_class);
            deleteButton = itemView.findViewById(R.id.delete_sheet_button);
            editButton = itemView.findViewById(R.id.edit_sheet_button);
        }
    }
}
