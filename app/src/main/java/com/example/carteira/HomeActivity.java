package com.example.carteira;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carteira.adapter.LancamentoAdapter;
import com.example.carteira.dao.LancamentoDAO;
import com.example.carteira.model.Lancamento;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private LancamentoDAO lcdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lcdao = new LancamentoDAO(this);

        ImageView ivLancamento = findViewById(R.id.ivLancamento);
        ivLancamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaLancamento = new Intent(HomeActivity.this, LancamentoActivity.class);
                startActivity(telaLancamento);
            }
        });

        ImageView ivRelatorio = findViewById(R.id.ivRelatorio);
        ivRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Relatório")
                        .setMessage(loadRelatorio());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        loadLancamentos();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadLancamentos();
    }

    private void loadLancamentos() {
        LancamentoDAO lancamentoDAO = new LancamentoDAO(this);
        List<Lancamento> lancamentos = lancamentoDAO.listarLancamentos();
        LancamentoAdapter lancamentosAdapter = new LancamentoAdapter(lancamentos, HomeActivity.this);

        ListView listViewLancamentos = findViewById(R.id.listViewLancamentos);
        listViewLancamentos.setAdapter(lancamentosAdapter);
    }

    private String loadRelatorio(){
        int totalEntradas = 0;
        int totalSaidas = 0;
        int saldoTotal = 0;

        for (Lancamento l : lcdao.listarLancamentos()){
            if (l.getTipo().equalsIgnoreCase("Entrada")){
                totalEntradas += 1;
            }
            if (l.getTipo().equalsIgnoreCase("Saída")) {
                totalSaidas += 1;
            }
            saldoTotal += l.getValor();
        }

        return "Total de Entradas: " + totalEntradas +
                "\nTotal de Saídas: " + totalSaidas +
                "\nSaldo Total: R$ " + saldoTotal;

    }


}