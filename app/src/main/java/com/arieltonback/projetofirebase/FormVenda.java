package com.arieltonback.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FormVenda extends AppCompatActivity {

    private Spinner select_produto;
    private EditText quantidade_produto;
    private Button bt_cadastrar;
    List<Produto> lProdutos;

    String[] mensagens = {"preencha todos os campos corretamente", "cadastro realizado com sucesso",
            "erro inesperado ao salvar venda"};

    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_venda);
        getSupportActionBar().hide();
        iniciarComponentes();
        buscarProdutos();

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarVenda(view);
            }
        });
    }

    private void buscarProdutos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Produtos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Produto> produtos = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Produto produto = new Produto();
                                produto.id = document.getId();
                                produto.nome = (String) document.getData().get("nome_produto");
                                produto.valor_custo = (String) document.getData().get("valor_custo");
                                produto.valor_venda = (String) document.getData().get("valor_venda");
                                produtos.add(produto);
                            }
                            setListProdutos(produtos);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setListProdutos(List<Produto> produtos) {
        lProdutos = produtos;
        List<String> lista = new ArrayList<>();
        for (Produto produto: produtos) {
            Log.d(TAG, "PRODUTO: " + produto.nome);
            lista.add(produto.nome);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, lista);
        select_produto.setAdapter(adapter);
    }

    private void cadastrarVenda(View view) {

        if (quantidade_produto.getText().toString().isEmpty() || Integer.parseInt(quantidade_produto.getText().toString()) <= 0) {
            AlertSnackBar alerta = new  AlertSnackBar();
            alerta.alertaSnackBarUsuario(view, mensagens[0], false);
            return;
        }

        int indice_produto = select_produto.getSelectedItemPosition();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> venda = new HashMap<>();
        venda.put("nome_produto", lProdutos.get(indice_produto).nome);
        venda.put("valor_custo", lProdutos.get(indice_produto).valor_custo);
        venda.put("valor_venda", lProdutos.get(indice_produto).valor_venda);
        venda.put("quantidade", quantidade_produto.getText().toString());
        venda.put("vendedor_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        venda.put("data_venda", dateFormat.format(date));

        UUID uuid = UUID.randomUUID();
        DocumentReference documentReference = db.collection("vendas").document(uuid.toString());
        documentReference.set(venda).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                AlertSnackBar alerta = new  AlertSnackBar();
                alerta.alertaSnackBarUsuario(view, mensagens[1], true);
                quantidade_produto.setText("1");
                Log.d("db", "sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertSnackBar alerta = new  AlertSnackBar();
                alerta.alertaSnackBarUsuario(view, mensagens[2], false);
                Log.d("db_error", "Erro ao salvar os dados");
            }
        });
    }

    private void iniciarComponentes() {
        select_produto = findViewById(R.id.select_produto);
        quantidade_produto = findViewById(R.id.quantidade_produto);
        bt_cadastrar = findViewById(R.id.bt_cadastrar_venda);
    }
}