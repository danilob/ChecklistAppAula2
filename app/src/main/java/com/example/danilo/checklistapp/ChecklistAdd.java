package com.example.danilo.checklistapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danilo.checklistapp.database.DatabaseHelper;
import com.example.danilo.checklistapp.model.Checklist;
import com.example.danilo.checklistapp.model.ChecklistDAO;

public class ChecklistAdd extends AppCompatActivity {

    private SQLiteDatabase mConection;
    private DatabaseHelper mDataHelper;

    private ChecklistDAO mChecklistDAO;
    private Checklist mChecklist;

    private EditText mEditTextDescription;
    private CheckBox mCheckBoxActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditTextDescription = (EditText) findViewById(R.id.editTextDescription);
        mCheckBoxActive = (CheckBox) findViewById(R.id.checkBoxActive);

        createConection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu. menu_add_checklist,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itemOK:
                confirm();
                //Toast. makeText(this,"Ok Selecionado", Toast. LENGTH_SHORT).show();
                break;
            case R.id.itemCancelar:
                finish();
                //Toast. makeText(this,"Cancelar Selecionado", Toast. LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createConection(){
        try{
            mDataHelper = new DatabaseHelper(this.getApplicationContext());
            mConection = mDataHelper.getWritableDatabase();
            mChecklistDAO = new ChecklistDAO(mConection);
            //Toast.makeText(this,"Conexão criada com sucesso",Toast.LENGTH_SHORT).show();
            //Snackbar.make(mLayoutMain, R.string.sucess_conection,Snackbar.LENGTH_LONG).show();

        }catch(SQLException e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }


    }

    private boolean validateForm(){
        boolean res;
        String desc = mEditTextDescription.getText().toString();
        boolean active = mCheckBoxActive.isChecked();
        mChecklist = new Checklist();
        mChecklist.setDescription(desc);
        mChecklist.setActive(active);
        if (res = desc.isEmpty()){
            mEditTextDescription.requestFocus();
            AlertDialog.Builder diag = new AlertDialog.Builder(this);
            diag.setTitle(R.string.campo_vazio);
            diag.setMessage(R.string.descricao_invalido);
            diag.setNeutralButton("Ok",null);
            diag.show();
            return res;
        }
        return false;
    }

    private void confirm(){
        if (validateForm() == false){
            try {
                mChecklistDAO.insert(mChecklist);
                finish();
            }catch (SQLException e){
                Toast. makeText(this,e.toString(),Toast. LENGTH_SHORT).show();
            }
        }
    }

}
