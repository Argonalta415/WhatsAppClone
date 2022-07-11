package com.argo.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        campoEmail = findViewById (R.id.editLoginEmail);
        campoSenha = findViewById (R.id.editLoginSenha);
    }
    public void logarUsuario(Usuario usuario){
        autenticacao.signInWithEmailAndPassword (
                usuario.getEmail (), usuario.getSenha ()
        ).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful ()){

                   abrirTelaPrincipal ();

                }else{ String excecao = "";
                    try {
                        throw task.getException ();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Usuario não esta cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e ){
                        excecao = "Email e senha nao correspondem ao usuarop";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuario:" + e.getMessage ();
                        e.printStackTrace ();
                    }
                    Toast.makeText (LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show ();
                }



            }
        });

    }

    public void validarAutenticacao(View view){
        //Recuperar textos dos campos
        String textoEmail  = campoEmail.getText ().toString ();
        String textoSenha = campoSenha.getText ().toString ();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();

        //validar se email/senha foram digitados

            if (!textoEmail.isEmpty ()){
                if (!textoSenha.isEmpty ()){
                    Usuario usuario = new Usuario ();
                    usuario.setEmail (textoEmail);
                    usuario.setSenha (textoSenha);
                    logarUsuario (usuario);


                }else{
                    Toast.makeText (LoginActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_SHORT).show ();


                }

            }else{
                Toast.makeText (LoginActivity.this,
                        "Preencha o E-mail!",
                        Toast.LENGTH_SHORT).show ();

            }

        }

    @Override
    protected void onStart() {
        super.onStart ();
        verificarUsuarioLogado();


        }


    public void abrirTelaCadastro(View view){
        Intent intent = new Intent (LoginActivity.this, CadastroActivity.class);
        startActivity (intent);
    }
    public void abrirTelaPrincipal(){
        Intent intent = new Intent (LoginActivity.this, MainActivity.class);
        startActivity (intent);
    }
    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        //autenticacao.signOut ();
        if (autenticacao.getCurrentUser ()!= null){
            abrirTelaPrincipal ();
        }
    }
}