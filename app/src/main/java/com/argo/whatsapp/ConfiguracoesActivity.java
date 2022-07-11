package com.argo.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {
    
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private ImageButton imageButtonCamera, imageButtonGaleria;
    private static int SELECAO_CAMERA = 100;
    private static int SELECAO_GALERIA = 200;
    private CircleImageView circleImageViewPerfil;
    private StorageReference storageReference;
    private String identificadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_configuracoes);

        //Configuraçoes iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorege ();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario ();


        //Validando permissoes
        Permissao.validarPermissoes (permissoesNecessarias, this, 1);

        //botao camera e galeria
        imageButtonCamera = findViewById (R.id.imageButtonCamera);
        imageButtonGaleria = findViewById (R.id.imageButtonGaleria);
        circleImageViewPerfil = findViewById (R.id.circleImageViewFotoPerfil);



        Toolbar toolbar = findViewById (R.id.toolbarPrincipal);
        toolbar.setTitle ("Configurações");
        setSupportActionBar (toolbar);

        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);

        //Recuoerar dados do usuario
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual ();


        //evento de click nos botoes camera e galeria
        imageButtonCamera.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity (getPackageManager () )!= null){

                }
                startActivityForResult (i,SELECAO_CAMERA);

            }
        });
        imageButtonGaleria.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult (i, SELECAO_GALERIA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap imagem =null;
            try {
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras ().get ("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData ();
                        imagem = MediaStore.Images.Media.getBitmap (getContentResolver (), localImagemSelecionada);
                        break;

                }
                if ( imagem != null){
                    circleImageViewPerfil.setImageBitmap (imagem);
                    //recuperando dados da imagem no firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream ();
                    imagem.compress (Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem  = baos.toByteArray ();



                    //salvando no firebase
                    final StorageReference imagemRef = storageReference
                            .child ("imagens")
                            .child ("perfil")

                            .child (identificadorUsuario+".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes (dadosImagem);
                    uploadTask.addOnFailureListener (new OnFailureListener () {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText (ConfiguracoesActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show ();

                        }
                    }).addOnSuccessListener (new OnSuccessListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText (ConfiguracoesActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show ();

                            imagemRef.getDownloadUrl ().addOnCompleteListener (new OnCompleteListener<Uri> () {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult ();
                                    autualizaFotoUsuario( url );
                                }
                            });

                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace ();
            }
        }
    }
    public void autualizaFotoUsuario(Uri url){
            

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao ();

            }

        }
    }
    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle ("Permissões Nagadas");
        builder.setMessage ("Para utilizar esse app é necessario aceitar as permissões");
        builder.setCancelable (false);
        builder.setPositiveButton ("Confirmar", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish ();
            }
        });
        AlertDialog dialog = builder.create ();
        dialog.show ();

    }
}