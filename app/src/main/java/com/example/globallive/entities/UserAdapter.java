package com.example.globallive.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallive.R;
import com.example.globallive.tabs.UserListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> _users;
    private UserListFragment activity;
    private OnUserListener mOnUserListener;


    public UserAdapter(UserListFragment activity, ArrayList<User> users, OnUserListener onUserListener){
        this.activity = activity;
        this._users = users;
        this.mOnUserListener = onUserListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_user, viewGroup, false);
        return new ViewHolder(view, mOnUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = _users.get(position);
        holder.getUserFirstName().setText(user.getFirstName());
        holder.getUserLastName().setText(user.getLastName());
        holder.getUserEmail().setText(user.getEmail());
        holder.getSwitchIsBanned().setChecked(user.getIsBanned());

        String imgUrl = "";
        //Si notre événément a une image perso on va la chercher sur l'API avec le bon chemin
        //Sinon dans le dossier drawable l'image par défaut
        if(user.isUserImg()){
            imgUrl = holder.itemView.getContext().getString(R.string.api_url) + "/userId"+user.getId()+"/userImg.jpg";
            Picasso.with(holder.getUserImage().getContext()).cancelRequest(holder.getUserImage());
            Picasso.with(holder.getUserImage().getContext()).load(imgUrl).into(holder.getUserImage());
        }else{
            //Modifier sur le genre
            if(user.getGenre_id() == 2){
                Picasso.with(holder.getUserImage().getContext()).load(R.drawable.man).into(holder.getUserImage());
            }else{
                Picasso.with(holder.getUserImage().getContext()).load(R.drawable.woman).into(holder.getUserImage());
            }
        }
        holder.getEditionButton().setVisibility(View.VISIBLE);
        holder.getTrashButton().setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return _users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView userFirstName;
        private final TextView userLastName;
        private final TextView userEmail;
        private final CircleImageView userImage;
        private final FloatingActionButton editionButton;
        private final FloatingActionButton trashButton;
        private final SwitchMaterial switchIsBanned;

        OnUserListener mOnUserListener;

        public ViewHolder(View view, OnUserListener OnUserListener) {
            super(view);
            //Le click sur un élément de la liste
            mOnUserListener = OnUserListener;
            view.setOnClickListener(this);
            // Define click listener for the ViewHolder's View
            userFirstName = (TextView) view.findViewById(R.id.textViewUserFirstName);
            userLastName = (TextView) view.findViewById(R.id.textViewUserLastName);
            userEmail = (TextView) view.findViewById(R.id.textViewUserEmail);
            userImage = (CircleImageView) view.findViewById(R.id.imageViewUser);
            editionButton = (FloatingActionButton) view.findViewById(R.id.layoutUsertBtnEdit);
            trashButton = (FloatingActionButton) view.findViewById(R.id.layoutUserBtnTrash);
            switchIsBanned = (SwitchMaterial) view.findViewById(R.id.textViewUserSwitchIsBanned);

            editionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnUserListener.onUserEditClick(getAdapterPosition());
                }
            });
            trashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnUserListener.onUserDeleteClick(getAdapterPosition());
                }
            });
            switchIsBanned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mOnUserListener.onBanUserClick(getAdapterPosition(), isChecked);
                }
            });

        }
        public TextView getUserFirstName() {
            return userFirstName;
        }
        public TextView getUserLastName() {
            return userLastName;
        }
        public TextView getUserEmail() {
            return userEmail;
        }
        public CircleImageView getUserImage() {
            return userImage;
        }
        public FloatingActionButton getEditionButton() {
            return editionButton;
        }
        public FloatingActionButton getTrashButton() {
            return trashButton;
        }
        public SwitchMaterial getSwitchIsBanned() {
            return switchIsBanned;
        }

        @Override
        public void onClick(View view) {
            mOnUserListener.onUserClick(getAdapterPosition());
        }


    }

    public interface OnUserListener {
        void onUserClick(int position);
        void onUserEditClick(int position);
        void onUserDeleteClick(int position);
        void onBanUserClick(int position, boolean isBanned);
    }
}
