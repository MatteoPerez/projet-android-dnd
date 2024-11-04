package fr.bluesam.tavernia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SheetRecyclerView extends RecyclerView.Adapter<SheetRecyclerView.CharacterViewHolder> {

    private final List<SheetHelperClass> characterList;

    public SheetRecyclerView(List<SheetHelperClass> characterList) {
        this.characterList = characterList;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_sheet_item, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        SheetHelperClass character = characterList.get(position);
        holder.characterName.setText(character.getName());
        holder.characterClass.setText(character.getcClass());
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }
    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        TextView characterName, characterClass;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            characterName = itemView.findViewById(R.id.character_name);
            characterClass = itemView.findViewById(R.id.character_class);
        }
    }
}
