package com.example.saferoute.services;

import com.example.saferoute.models.User;
import com.google.firebase.database.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    // Register New User or Admin
    public CompletableFuture<String> registerUser(User user) {
        CompletableFuture<String> future = new CompletableFuture<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Check for duplication in both admin and user nodes
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean usernameExists = false;

                // Check admin
                if (snapshot.child("admin").exists()) {
                    for (DataSnapshot adminSnap : snapshot.child("admin").getChildren()) {
                        User existingAdmin = adminSnap.getValue(User.class);
                        if (existingAdmin != null && existingAdmin.getUsername().equalsIgnoreCase(user.getUsername())) {
                            usernameExists = true;
                            break;
                        }
                    }
                }

                // Check users
                if (!usernameExists && snapshot.child("users").exists()) {
                    for (DataSnapshot userSnap : snapshot.child("users").getChildren()) {
                        User existingUser = userSnap.getValue(User.class);
                        if (existingUser != null && existingUser.getUsername().equalsIgnoreCase(user.getUsername())) {
                            usernameExists = true;
                            break;
                        }
                    }
                }

                if (usernameExists) {
                    future.complete("Username already exists!");
                } else {
                    user.setId(UUID.randomUUID().toString());
                    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

                    if (user.getUsername().equalsIgnoreCase("admin")) {
                        user.setRole("admin");
                        databaseReference.child("admin").child(user.getId()).setValueAsync(user);
                        future.complete("Admin registered successfully!");
                    } else {
                        user.setRole("user");
                        databaseReference.child("users").child(user.getId()).setValueAsync(user);
                        future.complete("User registered successfully!");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.complete("Error during registration!");
            }
        });

        return future;
    }

    // Login User
    public CompletableFuture<String> login(String username, String password) {
        CompletableFuture<String> future = new CompletableFuture<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Check Admin Node
        databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot adminSnapshot) {
                for (DataSnapshot snapshot : adminSnapshot.getChildren()) {
                    User admin = snapshot.getValue(User.class);
                    if (admin != null && admin.getUsername().equals(username) && BCrypt.checkpw(password, admin.getPassword())) {
                        future.complete("Admin Login Success");
                        return;
                    }
                }

                // If not found in admin, check users
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot userSnapshot) {
                        for (DataSnapshot snapshot : userSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null && user.getUsername().equals(username) && BCrypt.checkpw(password, user.getPassword())) {
                                future.complete("User Login Success");
                                return;
                            }
                        }
                        future.complete("Login Failed");
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.complete("Login Failed");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.complete("Login Failed");
            }
        });

        return future;
    }
}
