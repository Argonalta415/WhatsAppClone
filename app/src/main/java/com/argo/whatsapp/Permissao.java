package com.argo.whatsapp;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {
    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23){
            List<String>listaPrmissoes = new ArrayList<> ();
            //Percorrer permissoes passadas, verificando uma a uma
            // se ja tem permissoes liberadas
            for (String permissao: permissoes){
               Boolean temPermissao = ContextCompat.checkSelfPermission (activity,permissao) == PackageManager.PERMISSION_GRANTED;
               if ( ! temPermissao) listaPrmissoes.add (permissao);
            }
            //caso a lista esteja vazia, nao e necessario solicitar permissao
            if (listaPrmissoes.isEmpty ()) return true;
            String[] novasPermissoes = new String[listaPrmissoes.size ()];
            listaPrmissoes.toArray (novasPermissoes);

            //Solicitando permissao
            ActivityCompat.requestPermissions (activity, novasPermissoes, requestCode );
        }
        return true;

    }
}
