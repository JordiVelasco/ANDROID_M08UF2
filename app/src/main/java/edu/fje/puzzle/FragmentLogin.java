package edu.fje.puzzle;

import android.content.Context;
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

public class FragmentLogin extends Fragment {

    Button bLogin, bRegister;
    EditText eUser, ePass;
    CallbackFragment callbackFragment;
    String userName, password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    MainActivity ma = new MainActivity();

    @Override
    public void onAttach(Context context) {
         sharedPreferences = context.getSharedPreferences("usersFile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);
        eUser = view.findViewById(R.id.eUsuario);
        ePass = view.findViewById(R.id.eContra);

        bLogin = view.findViewById(R.id.bLogin);
        bRegister = view.findViewById(R.id.bRegister);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             userName =  eUser.getText().toString();
             password = ePass.getText().toString();
             String uName, uPass;
             uName = sharedPreferences.getString("userName", null);
             uPass = sharedPreferences.getString("pass", null);
             if(userName.equals(uName) && password.equals(uPass)){
                 Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();
                 //ma.startFragment();
             }else{
                 Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
             }
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callbackFragment!=null){
                    callbackFragment.changeFragment();
                }
            }
        });
        return view;
    }

    public void setCallbackFragment(CallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
}
