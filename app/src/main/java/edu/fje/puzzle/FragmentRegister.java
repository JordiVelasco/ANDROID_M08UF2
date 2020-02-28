package edu.fje.puzzle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import java.util.HashMap;
import java.util.Map;


public class FragmentRegister extends Fragment {

    Button bLogin, bRegister;
    EditText eUser, eMail, ePass;
    CallbackFragment callbackFragment;
    String user, mail, password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth fAuth;
    DatabaseReference db;


    @Override
    public void onAttach(Context context) {
        sharedPreferences = context.getSharedPreferences("usersFile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment,container,false);
        fAuth = FirebaseAuth.getInstance();
        eUser = view.findViewById(R.id.eUsuario);
        eMail = view.findViewById(R.id.eMail);
        ePass = view.findViewById(R.id.eContra);


        bRegister = view.findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = eUser.getText().toString().trim();
                mail = eMail.getText().toString().trim();
                password = ePass.getText().toString().trim();


                editor.putString("userName", user);
                editor.putString("email", mail);
                editor.putString("pass", password);
                editor.apply();

                if(user!=null && mail!=null && password!=null){
                    registerUser();
                }
            }
        });
        return view;
    }

    private void registerUser(){
        fAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = fAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();

                    map.put("name", user);
                    map.put("email", mail);
                    map.put("password", password);

                    db.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                Toast.makeText(getContext(),"Registrado", Toast.LENGTH_SHORT).show();
                                //  startActivity(new Intent(FragmentRegister.this,MainActivity.class));
                            }
                        }
                    });


                }else{
                    Toast.makeText(getContext(),"Error de registro", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }



}