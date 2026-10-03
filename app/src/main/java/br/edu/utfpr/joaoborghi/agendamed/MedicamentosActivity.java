package br.edu.utfpr.joaoborghi.agendamed;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicamentosActivity extends AppCompatActivity {

    private ListView listViewMedicamentos;
    private List<Medicamento> listaMedicamentos;
    private MedicamentoAdapter medicamentoAdapter;

    private int posicaoSelecionada = -1;

    private ActionMode actionMode;

    private View viewSelecionada;
    private Drawable backgroundDrawable;

    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.medicamentos_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int idMenuItem = item.getItemId();

            if(idMenuItem==R.id.menuItemEditar){
                editarMedicamento();
                return true;
            } else if (idMenuItem==R.id.menuItemExcluir){
                excluirMedicamento();
                mode.finish();
                return true;
            }else {
                return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelecionada != null) viewSelecionada.setBackground(backgroundDrawable);

            actionMode = null;
            viewSelecionada = null;
            backgroundDrawable = null;
            listViewMedicamentos.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);

        setTitle(getString(R.string.controle_de_medicamentos));

        listViewMedicamentos = findViewById(R.id.listViewMedicamentos);

        listViewMedicamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                posicaoSelecionada = i;
                editarMedicamento();
            }
        });

        listViewMedicamentos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(actionMode!=null) return false;

                posicaoSelecionada = i;

                viewSelecionada = view;
                backgroundDrawable = view.getBackground();

                view.setBackgroundColor(ContextCompat.getColor(MedicamentosActivity.this, R.color.cor_item_selecionado));

                listViewMedicamentos.setEnabled(false);

                actionMode = startSupportActionMode(callback);

                return true;
            }
        });

        popularListaMedicamentos();

        registerForContextMenu(listViewMedicamentos);
    }

    private void popularListaMedicamentos(){
        listaMedicamentos = new ArrayList<>();

        medicamentoAdapter = new MedicamentoAdapter(this, listaMedicamentos);

        listViewMedicamentos.setAdapter(medicamentoAdapter);
    }

    public void abrirSobre(){
        Intent intentAbertura = new Intent(this, SobreActivity.class);

        startActivity(intentAbertura);
    }

    ActivityResultLauncher<Intent> launcherNovoMedicamento = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == MedicamentosActivity.RESULT_OK){
                        Intent intent = o.getData();

                        Bundle bundle = intent.getExtras();

                        if(bundle!=null){
                            String nome = bundle.getString(MedicamentoActivity.KEY_NOME);
                            String tipo = bundle.getString(MedicamentoActivity.KEY_TIPO);
                            int via = bundle.getInt(MedicamentoActivity.KEY_VIA);
                            boolean continuo = bundle.getBoolean(MedicamentoActivity.KEY_CONTINUO);

                            Medicamento medicamento = new Medicamento(nome, TipoMedicamento.valueOf(tipo), via, continuo);

                            listaMedicamentos.add(medicamento);

                            Collections.sort(listaMedicamentos, Medicamento.oredenacaoCrescente);

                            medicamentoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

    public void abrirNovoMedicamento(){
        Intent intentAbertura = new Intent(this, MedicamentoActivity.class);

        intentAbertura.putExtra(MedicamentoActivity.KEY_MODO, MedicamentoActivity.MODO_NOVO);

        launcherNovoMedicamento.launch(intentAbertura);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medicamentos_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if(idMenuItem == R.id.menuItemAdicionar){
            abrirNovoMedicamento();
            return true;
        }else if(idMenuItem == R.id.menuItemSobre){
            abrirSobre();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    ActivityResultLauncher<Intent> launcherEditarMedicamento = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == MedicamentosActivity.RESULT_OK){
                        Intent intent = o.getData();

                        Bundle bundle = intent.getExtras();

                        if(bundle!=null){
                            String nome = bundle.getString(MedicamentoActivity.KEY_NOME);
                            String tipo = bundle.getString(MedicamentoActivity.KEY_TIPO);
                            int via = bundle.getInt(MedicamentoActivity.KEY_VIA);
                            boolean continuo = bundle.getBoolean(MedicamentoActivity.KEY_CONTINUO);

                            Medicamento medicamento = listaMedicamentos.get(posicaoSelecionada);
                            medicamento.setNome(nome);
                            medicamento.setTipo(TipoMedicamento.valueOf(tipo));
                            medicamento.setVia(via);
                            medicamento.setUsoContinuo(continuo);

                            Collections.sort(listaMedicamentos, Medicamento.oredenacaoCrescente);

                            medicamentoAdapter.notifyDataSetChanged();
                        }
                    }
                    posicaoSelecionada = -1;

                    if(actionMode!=null){
                        actionMode.finish();
                    }
                }
            });
    private void editarMedicamento(){
        Medicamento medicamento = listaMedicamentos.get(posicaoSelecionada);

        Intent intentAbertura = new Intent(this, MedicamentoActivity.class);

        intentAbertura.putExtra(MedicamentoActivity.KEY_MODO, MedicamentoActivity.MODO_EDITAR);

        intentAbertura.putExtra(MedicamentoActivity.KEY_NOME, medicamento.getNome());
        intentAbertura.putExtra(MedicamentoActivity.KEY_TIPO, medicamento.getTipo().toString());
        intentAbertura.putExtra(MedicamentoActivity.KEY_VIA, medicamento.getVia());
        intentAbertura.putExtra(MedicamentoActivity.KEY_CONTINUO, medicamento.isUsoContinuo());

        launcherEditarMedicamento.launch(intentAbertura);
    }

    private void excluirMedicamento(){
        listaMedicamentos.remove(posicaoSelecionada);
        medicamentoAdapter.notifyDataSetChanged();
    }
}