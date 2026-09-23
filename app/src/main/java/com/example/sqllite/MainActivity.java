package com.example.sqllite;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtNome, edtEmail;
    Button btnSalvar;
    ListView listViewUsuarios;
    BancoHelper databaseHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> listaUsuarios;
    ArrayList<Integer> listaIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtNome = findViewById(R.id.editNome);
        edtEmail = findViewById(R.id.editEmail);
        btnSalvar = findViewById(R.id.btnSalvar);
        listViewUsuarios = findViewById(R.id.listViewUsuarios);

        databaseHelper = new BancoHelper(this);

        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseHelper.inserirUsuario(nome, email);
            edtNome.setText("");
            edtEmail.setText("");
            carregarUsuarios();
        });

        carregarUsuarios();
    }

    private void carregarUsuarios() {
        Cursor cursor = databaseHelper.listarUsuarios();
        listaUsuarios = new ArrayList<>();
        listaIds = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String nome = cursor.getString(1);
                    String email = cursor.getString(2);
                    listaUsuarios.add(id + " - " + nome + " - " + email);
                    listaIds.add(id);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaUsuarios);
        listViewUsuarios.setAdapter(adapter);
    }
}
