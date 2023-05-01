package com.arieltonback.projetofirebase;

public class Venda {
    Produto produto;
    int quantidade;
    String data_venda;

    @Override public String toString() {
        return "Produto: " + produto.nome + "\n Valor: " + produto.valor_venda + "\n Quantidade: " + quantidade + "\n DATA: " + data_venda;
    }
}
