package com.arieltonback.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.opengl.EGLExt;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaVendas extends AppCompatActivity {

    private ListView list_vendas;
    List<Venda> lVendas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vendas);
        getSupportActionBar().hide();
        getVendas();
    }

    private void getVendas() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("vendas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Venda> vendas = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Venda venda = new Venda();
                                Produto produto = new Produto();
                                produto.nome = (String) document.getData().get("nome_produto");
                                produto.valor_custo = (String) document.getData().get("valor_custo");
                                produto.valor_venda = (String) document.getData().get("valor_venda");
                                venda.data_venda = (String) document.getData().get("data_venda");
                                venda.quantidade = Integer.parseInt( (String) Objects.requireNonNull(document.getData().get("quantidade")));
                                venda.produto = produto;
                                vendas.add(venda);
                            }
                           setListVendas(vendas);
                        } else {
                            Log.d("error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setListVendas(List<Venda> vendas) {
        lVendas = vendas;
        list_vendas = findViewById(R.id.list_vendas);
        ArrayAdapter<Venda> adapter = new ArrayAdapter<Venda>(this, android.R.layout.simple_list_item_1, vendas);
        list_vendas.setAdapter(adapter);
    }
}