package br.edu.utfpr.joaoborghi.agendamed;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MedicamentosActivity extends AppCompatActivity {

    private ListView listViewMedicamentos;
    private List<Medicamento> listaMedicamentos;
    private MedicamentoAdapter medicamentoAdapter;

    private int posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);

        setTitle(getString(R.string.controle_de_medicamentos));

        listViewMedicamentos = findViewById(R.id.listViewMedicamentos);

        listViewMedicamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editarMedicamento(i);
            }
        });

        popularListaMedicamentos();

        registerForContextMenu(listViewMedicamentos);
    }

    private void popularListaMedicamentos(){
        /*String[] medicamentosNome = getResources().getStringArray(R.array.medicamentos_nome);
        int[] medicamentosTipo = getResources().getIntArray(R.array.medicamentos_tipo);
        int[] medicamentosVia = getResources().getIntArray(R.array.medicamentos_via);
        int[] medicamentosUsoContinuo = getResources().getIntArray(R.array.medicamentos_uso_continuo);*/

        listaMedicamentos = new ArrayList<>();

        /*for(int i = 0; i<medicamentosNome.length; i++){
            listaMedicamentos.add(new Medicamento(
                    medicamentosNome[i],
                    TipoMedicamento.values()[medicamentosTipo[i]],
                    medicamentosVia[i],
                    medicamentosUsoContinuo[i] == 1
            ));
        }*/

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

                            medicamentoAdapter.notifyDataSetChanged();
                        }
                    }
                    posicaoSelecionada = -1;
                }
            });
    private void editarMedicamento(int posicao){
        posicaoSelecionada = posicao;

        Medicamento medicamento = listaMedicamentos.get(posicaoSelecionada);

        Intent intentAbertura = new Intent(this, MedicamentoActivity.class);

        intentAbertura.putExtra(MedicamentoActivity.KEY_MODO, MedicamentoActivity.MODO_EDITAR);

        intentAbertura.putExtra(MedicamentoActivity.KEY_NOME, medicamento.getNome());
        intentAbertura.putExtra(MedicamentoActivity.KEY_TIPO, medicamento.getTipo().toString());
        intentAbertura.putExtra(MedicamentoActivity.KEY_VIA, medicamento.getVia());
        intentAbertura.putExtra(MedicamentoActivity.KEY_CONTINUO, medicamento.isUsoContinuo());

        launcherEditarMedicamento.launch(intentAbertura);
    }

    private void excluirMedicamento(int posicao){
        listaMedicamentos.remove(posicao);
        medicamentoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.medicamentos_item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int idMenuItem = item.getItemId();

        if(idMenuItem==R.id.menuItemEditar){
            editarMedicamento(info.position);
            return true;
        } else if (idMenuItem==R.id.menuItemExcluir){
            excluirMedicamento(info.position);
            return true;
        }else {
            return super.onContextItemSelected(item);
        }
    }
}