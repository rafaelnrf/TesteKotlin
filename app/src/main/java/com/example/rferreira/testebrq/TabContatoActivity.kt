package com.example.rferreira.testebrq

/**
 * Created by rferreira on 16/05/2018.
 */
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.rferreira.testebrq.lib.HttpHandler
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import android.view.ViewGroup
import android.widget.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.example.rferreira.testebrq.lib.PhoneNumberFormatType
import com.example.rferreira.testebrq.lib.PhoneNumberFormatter
import com.example.rferreira.testebrq.lib.Validacao
import java.lang.ref.WeakReference


internal class TabContatoActivity : AppCompatActivity() {


    private  val url                            = "https://floating-mountain-50292.herokuapp.com/cells.json"
    internal var campos                         = ArrayList<HashMap<String, String>>()
    internal var valida_tipo                    = HashMap<View, String>()
    internal var campos_invisiveis              = ArrayList<View>()
    internal var objetos_ui                     = HashMap<String, HashMap<String, View>>();
    internal var mensagem_obrigado:TextView?    = null
    internal var mensagem_sucesso:TextView?     = null
    internal var enviar_nova_mensagem:TextView? = null
    internal var ll_main:LinearLayout?          = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)

        mensagem_obrigado                       = findViewById<TextView>(R.id.mensagem_obrigado)
        mensagem_sucesso                        = findViewById<TextView>(R.id.mensagem_sucesso)
        enviar_nova_mensagem                    = findViewById<TextView>(R.id.enviar_nova_mensagem)
        ll_main                                 = findViewById<LinearLayout>(R.id.ll_main_layout)

        GetFormulario().execute()
    }


    fun reiniciaFormularioContato(view:View) {


        ll_main!!.visibility              = View.VISIBLE
        mensagem_obrigado!!.visibility    = View.GONE
        mensagem_sucesso!!.visibility     = View.GONE
        enviar_nova_mensagem!!.visibility = View.GONE
    }


    fun alteraVisibilidadeCampo(cb:CheckBox){

        campos_invisiveis.forEach{
            if(cb.isChecked){
                it!!.visibility = View.VISIBLE
            }else{
                it!!.visibility = View.GONE
            }
        }

    }


    private inner class GetFormulario : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): String?  {
            val sh = HttpHandler()

            val jsonStr                      = sh.makeServiceCall(url)
            if (jsonStr != null) {
                try {
                    val jsonObj              = JSONObject(jsonStr)
                    val js_campos            = jsonObj.getJSONArray("cells");

                    for (i in 0..(js_campos.length() - 1)) {
                        val campo            = js_campos.getJSONObject(i);
                        val map              = HashMap<String, String>();
                        map.set("id",        campo.get("id").toString());
                        map.set("type",      campo.get("type").toString());
                        map.set("message",   campo.get("message").toString());
                        map.set("typefield", campo.get("typefield").toString());
                        map.set("hidden",    campo.get("hidden").toString());
                        map.set("topSpacing",campo.get("topSpacing").toString());
                        map.set("show",      campo.get("show").toString());
                        map.set("required",  campo.get("required").toString());
                        campos.add(map);
                    }

                } catch (e: JSONException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext,
                                "Erro Json: " + e.message,
                                Toast.LENGTH_LONG)
                                .show()
                    }

                }

            } else {
                runOnUiThread {
                    Toast.makeText(applicationContext,
                            "",
                            Toast.LENGTH_LONG)
                            .show()
                }
            }

            return null
        }

        override fun onPostExecute(result: String?)  {
            super.onPostExecute(result)
            campos.forEach {
                val map                = it;
                redenrizaCampo(Integer.valueOf(map.get("type")),map.get("typefield").toString() ,map.get("message").toString(),map.get("topSpacing")!!.toDouble()!!.toInt(),map.get("hidden").toString(),map.get("required").toString(),map.get("id").toString());

            }


        }


    }



    @SuppressLint("SetTextI18n")
    fun redenrizaCampo(tipo: Int,typeField: String, message: String, spacing: Int, hidden: String, obrigatoriedade: String, id: String){


        val mapHidden  = HashMap<String, Int>();
        mapHidden.set("false",View.VISIBLE);
        mapHidden.set("true",View.GONE);
        val sub_registro_ui            = HashMap<String, View>();

        when(tipo){

            1->{
                val edit_text          = EditText(this@TabContatoActivity)
                edit_text.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                edit_text.hint         = message;
                edit_text.visibility   = mapHidden.get(hidden)!!;
                if(hidden == "true"){ campos_invisiveis.add(edit_text)}
                (edit_text.layoutParams as LinearLayout.LayoutParams).setMargins(20, spacing, 20, 0);

                ll_main!!.addView(edit_text)
                sub_registro_ui.set(obrigatoriedade,edit_text);
                setTypeField(typeField,edit_text)
            }

            2->{
                val text_view          = TextView(this@TabContatoActivity)
                text_view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                text_view.hint         = message;
                text_view.visibility   = mapHidden.get(hidden)!!;
                if(hidden == "true"){ campos_invisiveis.add(text_view)}
                (text_view.layoutParams as LinearLayout.LayoutParams).setMargins(20, spacing, 20, 0);
                ll_main!!.addView(text_view)
                sub_registro_ui.set(obrigatoriedade,text_view);
            }

            3->{
                val image_view          = ImageView(this@TabContatoActivity)
                image_view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                image_view.visibility   = mapHidden.get(hidden)!!;
                (image_view.layoutParams as LinearLayout.LayoutParams).setMargins(20, spacing, 20, 0);
                if(hidden == "true"){ campos_invisiveis.add(image_view)}
                ll_main!!.addView(image_view)
                sub_registro_ui.set(obrigatoriedade,image_view);
            }

            4->{
                val cb                  = CheckBox(applicationContext)
                cb.layoutParams         = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                (cb.layoutParams as LinearLayout.LayoutParams).setMargins(20, spacing, 20, 0);
                cb.visibility           = mapHidden.get(hidden)!!;
                cb.setTextColor(Color.BLACK);
                cb.setBackgroundColor(Color.parseColor("#ffffff"));
                cb.text = message
                cb.setOnClickListener {
                    alteraVisibilidadeCampo(cb);
                }
                ll_main!!.addView(cb)
                sub_registro_ui.set(obrigatoriedade,cb);
            }

            5->{
                val bt                 = Button(this@TabContatoActivity)
                bt.layoutParams        = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                bt.text                = message;
                bt.setOnClickListener(validaEnvioFormulario(bt))
                bt.setTextColor(Color.WHITE)
                bt.visibility          = mapHidden.get(hidden)!!;
                (bt.layoutParams as LinearLayout.LayoutParams).setMargins(20, spacing, 20, 0);
                if(hidden == "true"){ campos_invisiveis.add(bt)}
                bt.setBackgroundResource(R.drawable.botao_enviar);
                ll_main!!.addView(bt)
                sub_registro_ui.set(obrigatoriedade,bt);

            }


        }

        objetos_ui.set(id,sub_registro_ui);
    }

    fun setTypeField(typeField: String, view: View){
        Log.e("teste valor",typeField)
        valida_tipo.set(view,typeField)
        when(typeField) {
            "telnumber" -> {
                val country = PhoneNumberFormatType.PT_BR // OR PhoneNumberFormatType.PT_BR
                val phoneFormatter = PhoneNumberFormatter(WeakReference((view as EditText)), country)
                view.addTextChangedListener(phoneFormatter)
            }
        }


    }


    fun validaEnvioFormulario(button: Button): View.OnClickListener {
        return View.OnClickListener {

            var erro = 0;
            objetos_ui.forEach{

                val map = it;
                map.value.forEach{

                    if(it.key == "true"){

                        if(it.value is EditText  && (it.value as EditText).isShown()){

                            if((it.value as EditText).text.toString().equals("")){
                                erro++;
                                Toast.makeText(applicationContext,
                                        "Preencha os campos obrigatorios",
                                        Toast.LENGTH_LONG)
                                        .show()
                            }else if((valida_tipo.get(it.value) == "telnumber" && !Validacao.isTelefone((it.value as EditText).text.toString()))){
                                erro++;
                                Toast.makeText(applicationContext,
                                        "Preencha o campo telefone corretamente",
                                        Toast.LENGTH_LONG)
                                        .show()
                            }else if((valida_tipo.get(it.value) == "3" && !Validacao.isEmail((it.value as EditText).text.toString()))){
                                erro++;
                                Toast.makeText(applicationContext,
                                        "Preencha o campo e-mail corretamente",
                                        Toast.LENGTH_LONG)
                                        .show()
                            }
                        }

                    }

                }





            }

            if(erro == 0){
                objetos_ui.forEach {
                    val map = it;
                    map.value.forEach {
                        if(it.value is EditText){
                            (it.value as EditText).setText("")
                        }

                    }
                }
                ll_main!!.visibility              = View.GONE
                mensagem_obrigado!!.visibility    = View.VISIBLE
                mensagem_sucesso!!.visibility     = View.VISIBLE
                enviar_nova_mensagem!!.visibility = View.VISIBLE

                hideKeyboard()
            }


        }
    }

    fun hideKeyboard() {
        // Check if no view has focus:
        val view = this@TabContatoActivity.getCurrentFocus()
        if (view != null) {
            val inputManager = this@TabContatoActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view!!.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}