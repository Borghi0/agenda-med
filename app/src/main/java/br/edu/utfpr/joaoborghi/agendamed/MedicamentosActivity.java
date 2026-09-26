package br.edu.utfpr.joaoborghi.agendamed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MedicamentosActivity extends AppCompatActivity {

    private ListView listViewMedicamentos;
    private List<Medicamento> listaMedicamentos;
    private MedicamentoAdapter medicamentoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);

        setTitle(getString(R.string.controle_de_medicamentos));

        listViewMedicamentos = findViewById(R.id.listViewMedicamentos);

        listViewMedicamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Medicamento medicamento = (Medicamento) listViewMedicamentos.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(), getString(R.string.medicamento_de_nome) + medicamento.getNome() + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();
            }
        });

        popularListaMedicamentos();
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

    public void abrirSobre(View view){
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
    public void abrirNovoMedicamento(View view){
        Intent intentAbertura = new Intent(this, MedicamentoActivity.class);

        launcherNovoMedicamento.launch(intentAbertura);
    }
}