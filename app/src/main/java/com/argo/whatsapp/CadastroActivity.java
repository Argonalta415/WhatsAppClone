package com.argo.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CadastroActivity extends AppCompatActivity {
    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_cadastro);

        campoNome = findViewById (R.id.editNome);
        campoEmail = findViewById (R.id.editLoginEmail);
        campoSenha = findViewById (R.id.editLoginSenha);
    }
    public void cadastrarUsuario(Usuario usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        autenticacao.createUserWithEmailAndPassword (
                usuario.getEmail (), usuario.getSenha ()

        ).addOnCompleteListener (this, new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful ()){
                    Toast.makeText (CadastroActivity.this,
                            "Sucesso ao cadastrar usuario!",
                            Toast.LENGTH_SHORT).show ();
                    finish ();

                    try {
                        String identificadorUsuario = Base64Custon.codificarBase64 (usuario.getEmail ());
                        usuario.setId (identificadorUsuario);
                        usuario.salvar ();


                    }catch (Exception e){
                        e.printStackTrace ();

                    }

                }else {
                    String excecao = "";
                    try {
                        throw task.getException ();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e ){
                        excecao = "Digite um email valido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Usuario ja cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuario:" + e.getMessage ();
                        e.printStackTrace ();
                    }
                    Toast.makeText (CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show ();
                }

            }
        });

    }
    public void validarCadastroUsuario(View view){
        //recuperando texto dos campos
        String textoNome = campoNome.getText ().toString ();
        String textoEmail = campoEmail.getText ().toString ();
        String textoSenha = campoSenha.getText ().toString ();


        if (!textoNome.isEmpty ()){
            if (!textoNome.isEmpty ()){
                if (!textoNome.isEmpty ()){
                    Usuario usuario = new Usuario ();
                    usuario.setNome (textoNome);
                    usuario.setEmail (textoEmail);
                    usuario.setSenha (textoSenha);

                    cadastrarUsuario (usuario);

                }else{
                    Toast.makeText (CadastroActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_SHORT).show ();


                }

            }else{
                Toast.makeText (CadastroActivity.this,
                        "Preencha o E-mail!",
                        Toast.LENGTH_SHORT).show ();

            }
            


        }else {
            Toast.makeText (CadastroActivity.this,
                    "Preencha o nome!",
                    Toast.LENGTH_SHORT).show ();

        }
    }

}