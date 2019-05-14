package com.amith.schoollabapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amith.schoollabapp.Model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBoxActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        Toolbar toolbar = findViewById((R.id.toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatBoxActivity.this, NavigationActivity.class).setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP));
            }
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);//error point
                    username.setText(user.getUsername());
                    //username.setText("Amith");

                try {
                    if(user.getImageURL().equals("default")){
                        //profile_image.setImageResource(R.mipmap.ic_launcher);
                        profile_image.setImageResource(R.drawable.pp_icon);
                    } else {

                        // change here(14 th video)
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                    }

                } catch (Exception e) {
                    System.out.println("pic : " + e);
                    Toast.makeText(ChatBoxActivity.this, "pic toast : "+e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        final TabLayout tabLayout = findViewById(R.id.tab_layout);
//        final ViewPager viewPager = findViewById(R.id.view_pager);
//
//        //remove loacation of here line in 2o th video
//
//        //and add these
//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager()); //move to here
//                int unread = 0;
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Chat chat = snapshot.getValue(Chat.class);
//                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()) {//count unread messages
//                        unread++;
//                    }
//
//
//                }
//                if (unread == 0) {
//                    viewPagerAdapter.addFragment(new ChatFragment(), "Chats");
//                } else {
//                    viewPagerAdapter.addFragment(new ChatFragment(), "("+unread+") Chats");
//                }
//
//                viewPagerAdapter.addFragment(new UsersFragment(), "Users");
//                //viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");
//
//                viewPager.setAdapter(viewPagerAdapter);
//
//                tabLayout.setupWithViewPager(viewPager);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}