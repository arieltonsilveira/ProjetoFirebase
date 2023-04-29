package com.arieltonback.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FormProdutos extends AppCompatActivity {

    private EditText edit_nome_produto, edit_valor_custo, edit_valor_venda;
    private Button bt_cadastrar;
    String[] mensagens = {"preencha todos os campos", "cadastro realizado com sucesso", "erro inesperado ao salvar produto"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produtos);
        IniciarComponentes();

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome_produto = edit_nome_produto.getText().toString();
                String valor_custo = edit_valor_custo.getText().toString();
                String valor_venda = edit_valor_venda.getText().toString();

                if (nome_produto.isEmpty() || valor_custo.isEmpty() || valor_venda.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    cadastrarProduto(view);
                }
            }
        });
    }

    private void cadastrarProduto(View view) {
        String nome_produto = edit_nome_produto.getText().toString();
        String valor_custo = edit_valor_custo.getText().toString();
        String valor_venda = edit_valor_venda.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> produto = new HashMap<>();
        produto.put("nome_produto", nome_produto);
        produto.put("valor_custo", valor_custo);
        produto.put("valor_venda", valor_venda);

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        DocumentReference documentReference = db.collection("Produtos").document(uuidAsString);
        documentReference.set(produto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
                Log.d("db", "sucesso ao salvar os dados");
                limparCampos();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackbar = Snackbar.make(view, mensagens[2], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
                Log.d("db_error", "Erro ao salvar os dados");
            }
        });
    }

    private void limparCampos() {
        edit_nome_produto.setText("");
        edit_valor_custo.setText("");
        edit_valor_venda.setText("");
    }

    private void IniciarComponentes() {
        edit_nome_produto = findViewById(R.id.edit_produto_nome);
        edit_valor_custo = findViewById(R.id.edit_produto_valor_custo);
        edit_valor_venda = findViewById(R.id.edit_produto_valor_venda);
        bt_cadastrar = findViewById(R.id.bt_cadastrar_produto);
    }
}