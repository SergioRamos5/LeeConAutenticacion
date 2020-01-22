package com.example.leeconautenticacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Button btCrear, btIniciar, btAnonimus;
    EditText user, pasword;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener !=null)
            mAuth.removeAuthStateListener(mAuthListener);
        mAuth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        user = findViewById(R.id.etUser);
        pasword = findViewById(R.id.etPass);
        btCrear = findViewById(R.id.btCrear);
        btIniciar = findViewById(R.id.btIniciar);
        btAnonimus = findViewById(R.id.btAnonimo);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    Toast.makeText(MainActivity.this, user.getEmail()+" LOGUEADO", Toast.LENGTH_LONG ).show();
                    mAuth = firebaseAuth;
                }
                else
                    Toast.makeText(MainActivity.this, "Usuario NULO", Toast.LENGTH_LONG ).show();
            }
        };

        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(user.getText().toString(), pasword.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Authentication failed:" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                                // iniciarAplicacion(task.getResult().getUser().getEmail().split("@")[0]);
                            }
                        });
            }
        });

        //////// Iniciar sesión con anónimo
        btAnonimus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInAnonymously().addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                         //iniciarAplicacion((task.getResult().getUser()==null?"anonimous":task.getResult().getUser().getEmail().split("@")[0]));
                    }
                });}
        });

        ////// Crear usuario nueveo y iniciar sesión
        btCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(user.getText().toString(), pasword.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                                    //iniciarAplicacion(task.getResult().getUser().getEmail().split("@")[0]);
                                } else  Toast.makeText(MainActivity.this, "Problemas al crear usuario" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        });
            }});

    }
}
