package br.unis.edu.si.atividade4;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nome, email;
    TextView resultadoNome, resultadoEmail, resultadoSexo, resultadoCNH;
    RadioGroup sexo, save;
    Button cadastrar;
    CheckBox CNH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nome = findViewById(R.id.edtNome);
        email = findViewById(R.id.edtEmail);

        CNH = findViewById(R.id.cbCNH);
        sexo = findViewById(R.id.rbSexo);

        resultadoCNH  = findViewById(R.id.txtResultadoCnh);
        resultadoEmail = findViewById(R.id.txtResultadoEmail);
        resultadoNome = findViewById(R.id.txtResultadoNome);
        resultadoSexo = findViewById(R.id.txtResultadoSexo);

        cadastrar = (Button) findViewById(R.id.btnCadastrar);
        cadastrar.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
        lerArquivoInterno();
        lerArquivoExterno();
    }

    @Override
    public void onClick(View v) {

        if(getSave().equals("Externo")){
            salvarArquivoExterno(
                    this.nome.getText().toString(),
                    this.email.getText().toString(),
                    getCNH(),
                    getSexo()
            );
        } else {
            salvarArquivoInterno(
                    this.nome.getText().toString(),
                    this.email.getText().toString(),
                    getCNH(),
                    getSexo()
            );
        }

        finish();
        startActivity(getIntent());

    }

    public String getCNH() {

        String cnh = "";

        if (CNH.isChecked()) {
            cnh += CNH.getText().toString();
        } else {
            cnh += "Não!";
        }
        return cnh;
    }

    private String getSexo(){
        String sSexo = " ";
        if(sexo.getCheckedRadioButtonId() != -1){
            RadioButton sexoSelecionado = (RadioButton) findViewById(sexo.getCheckedRadioButtonId());
            sSexo += sexoSelecionado.getText().toString();
        }

        return sSexo;
    }

    private String getSave(){

        String sSave = "";
        save = findViewById(R.id.rbSalvar);
        RadioButton rbSave = findViewById(save.getCheckedRadioButtonId());

        if(rbSave.getText().toString().equals("Interno")) {
            sSave += "Interno";
        } else {
            sSave += "Externo";
        }

        return sSave;
    }


    public void salvarArquivoInterno(String nome, String email, String cnh, String sexo) {
        String user = "";
        user += "nome="+ nome;
        user += "\n";
        user += "email="+ email;
        user += "\n";
        user += "sexo="+ sexo;
        user += "\n";
        user += "cnh="+ cnh;
        user += "\n";


        try {
            FileOutputStream fos = openFileOutput("user.txt", MODE_PRIVATE);
            fos.write(user.getBytes());
            fos.close();
            Toast.makeText(this,"Usuário cadastrado com sucesso no arquivo interno!",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("Error File: ", e.getMessage());
        }

    }

    public void lerArquivoInterno() {
        String nome = "";
        String email = "";
        String sexo = "";
        String cnh = "";

        try {
            File dir = getFilesDir();
            File file = new File(dir+"/user.txt");

            if (file.exists()) {
                FileInputStream fis = openFileInput("user.txt");
                byte[] buffer = new byte[(int)file.length()];

                while(fis.read(buffer) != -1) {
                    String texto = new String(buffer);

                    if (texto.indexOf("nome") != -1) {
                        int indice = texto.indexOf("=");
                        int indiceFinal = texto.indexOf("\n");
                        nome = texto.substring(indice+1, indiceFinal);
                        texto = texto.substring(indiceFinal+1);
                    }
                    if (texto.indexOf("email") != -1) {
                        int indice = texto.indexOf("=");
                        int indiceFinal = texto.indexOf("\n");
                        email = texto.substring(indice+1);
                    }
                    if (texto.indexOf("cnh") != -1) {
                        int indice = texto.indexOf("=");
                        int indiceFinal = texto.indexOf("\n");
                        cnh = texto.substring(indice+1);
                    }
                    if (texto.indexOf("sexo") != -1) {
                        int indice = texto.indexOf("=");
                        int indiceFinal = texto.indexOf("\n");
                        sexo = texto.substring(indice+1, indiceFinal);
                    }
                }

                this.resultadoNome.setText(nome);
                this.resultadoEmail.setText(email);
                this.resultadoCNH.setText(cnh);
                this.resultadoSexo.setText(sexo);
            }
        } catch (Exception e) {
            Log.e("Error File: ", e.getMessage());
        }
    }

    public void salvarArquivoExterno(String nome, String email, String cnh, String sexo) {
        String user = "";
        user += "nome="+ nome;
        user += "\n";
        user += "email="+ email;
        user += "\n";
        user += "sexo="+ sexo;
        user += "\n";
        user += "cnh="+ cnh;
        user += "\n";

        try {
            String estado = Environment.getExternalStorageState();
            if (estado.equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = openFileOutput("ExternalUser.txt", MODE_PRIVATE);
                fos.write(user.getBytes());
                fos.close();
                Toast.makeText(this,"Usuário cadastrado com sucesso no arquivo externo",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Error File: ", e.getMessage());
        }
    }

    public void lerArquivoExterno() {
        String nome = "";
        String email = "";
        String sexo = "";
        String cnh = "";

        try {
            String estado = Environment.getExternalStorageState();

            if (estado.equals(Environment.MEDIA_MOUNTED)) {
                File dir = getExternalFilesDir(null);
                File file = new File(dir + "/ExternalUser.txt");

                if (file.exists()) {
                    FileInputStream fis = openFileInput("ExternalUser.txt");
                    byte[] buffer = new byte[(int) file.length()];

                    while (fis.read(buffer) != -1) {
                        String texto = new String(buffer);

                        if (texto.indexOf("nome") != -1) {
                            int indice = texto.indexOf("=");
                            int indiceFinal = texto.indexOf("\n");
                            nome = texto.substring(indice + 1, indiceFinal);
                            texto = texto.substring(indiceFinal + 1);
                        }
                        if (texto.indexOf("email") != -1) {
                            int indice = texto.indexOf("=");
                            int indiceFinal = texto.indexOf("\n");
                            email = texto.substring(indice + 1);
                        }
                        if (texto.indexOf("cnh") != -1) {
                            int indice = texto.indexOf("=");
                            int indiceFinal = texto.indexOf("\n");
                            cnh = texto.substring(indice + 1);
                        }
                        if (texto.indexOf("sexo") != -1) {
                            int indice = texto.indexOf("=");
                            int indiceFinal = texto.indexOf("\n");
                            sexo = texto.substring(indice + 1, indiceFinal);
                        }
                    }

                    this.resultadoNome.setText(nome);
                    this.resultadoEmail.setText(email);
                    this.resultadoCNH.setText(cnh);
                    this.resultadoSexo.setText(sexo);
                }
            }
        } catch (Exception e) {
            Log.e("Error File: ", e.getMessage());
        }
    }

}
