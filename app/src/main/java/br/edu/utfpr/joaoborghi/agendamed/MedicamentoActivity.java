package br.edu.utfpr.joaoborghi.agendamed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MedicamentoActivity extends AppCompatActivity {

    public static final String KEY_NOME = "KEY_NOME";
    public static final String KEY_TIPO = "KEY_TIPO";
    public static final String KEY_VIA = "KEY_VIA";
    public static final String KEY_CONTINUO = "KEY_CONTINUO";
    private EditText editTextNome;
    private RadioGroup radioGroupTipo;
    private Spinner spinnerVia;
    private CheckBox checkBoxContinuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medicamento);

        editTextNome = findViewById(R.id.editTextNome);
        radioGroupTipo = findViewById(R.id.radioGroupTipo);
        spinnerVia = findViewById(R.id.spinnerVia);
        checkBoxContinuo = findViewById(R.id.checkBoxContinuo);
    }

    public void limpar(View view){
        editTextNome.setText(null);
        radioGroupTipo.clearCheck();
        spinnerVia.setSelection(0);
        checkBoxContinuo.setChecked(false);

        editTextNome.requestFocus();

        Toast.makeText(this, R.string.entradas_apagadas, Toast.LENGTH_SHORT).show();
    }

    public void salvar(View view){
        String nome = editTextNome.getText().toString();

        if(nome==null || nome.trim().isEmpty()){
            Toast.makeText(this, R.string.nome_em_branco, Toast.LENGTH_LONG).show();
            editTextNome.requestFocus();
            return;
        }

        int tipoId = radioGroupTipo.getCheckedRadioButtonId();
        TipoMedicamento tipoMedicamento;

        if(tipoId == R.id.radioButtonCapsula){
            tipoMedicamento = TipoMedicamento.Capsula;
        } else if (tipoId == R.id.radioButtonComprimido) {
            tipoMedicamento = TipoMedicamento.Comprimido;
        } else if (tipoId == R.id.radioButtonLiquido){
            tipoMedicamento = TipoMedicamento.Liquido;
        } else{
            Toast.makeText(this, getString(R.string.selecione_o_tipo), Toast.LENGTH_LONG).show();
            return;
        }

        int via = spinnerVia.getSelectedItemPosition();

        if(via== AdapterView.INVALID_POSITION){
            Toast.makeText(this, R.string.erro_nos_valores_do_spinner, Toast.LENGTH_LONG).show();
            return;
        }

        if(!verificar(tipoMedicamento, via)){
            Toast.makeText(this, R.string.via_incompativel_com_o_tipo_selecionado, Toast.LENGTH_LONG).show();
            return;
        }

        boolean continuo = checkBoxContinuo.isChecked();

        Intent intentResposta = new Intent();

        intentResposta.putExtra(KEY_NOME, nome);
        //intentResposta.putExtra(KEY_TIPO, tipoMedicamento.ordinal()); // representação em int
        intentResposta.putExtra(KEY_TIPO, tipoMedicamento.toString());
        intentResposta.putExtra(KEY_VIA, via);
        intentResposta.putExtra(KEY_CONTINUO, continuo);

        setResult(MedicamentoActivity.RESULT_OK, intentResposta);

        finish();
    }

    private boolean verificar(TipoMedicamento tipo, int via){
        if(tipo==TipoMedicamento.Capsula){
            return via==0;
        } else if(tipo==TipoMedicamento.Comprimido){
            return via==0 || via==1;
        } else if(tipo==TipoMedicamento.Liquido){
            return via>=0 && via<=3;
        } else return false;
    }
}