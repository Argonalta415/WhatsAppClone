package com.argo.whatsapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFirebase {

    public static String getIdentificadorUsuario()

    {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        String email = usuario.getCurrentUser ().getEmail ();
        String identificadorUsuario = Base64Custon.codificarBase64 (email);

        return identificadorUsuario;
    }
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        return usuario.getCurrentUser ();

    }
}
