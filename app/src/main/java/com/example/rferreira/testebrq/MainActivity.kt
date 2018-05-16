package com.example.rferreira.testebrq

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import org.json.JSONException
import org.json.JSONObject
import android.os.AsyncTask
import android.support.annotation.IntegerRes
import android.util.Log
import android.view.View
import android.widget.*
import com.example.rferreira.testebrq.lib.HttpHandler
import org.json.JSONArray
import java.util.ArrayList
import java.nio.file.Files.size
import android.widget.CheckBox






class MainActivity : AppCompatActivity() {

    private val url     = "https://floating-mountain-50292.herokuapp.com/cells.json"
    internal var campos = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GetFormulario().execute()
    }

    private inner class GetFormulario : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): String?  {
            val sh = HttpHandler()

            val jsonStr                      = sh.makeServiceCall(url)
            Log.e("teste json",jsonStr)
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
            val ll_main               = findViewById<LinearLayout>(R.id.ll_main_layout)
            campos.forEach {
               val map                = it;
                redenrizaCampo(Integer.valueOf(map.get("type")),map.get("message").toString(),ll_main,map.get("topSpacing")!!.toDouble()!!.toInt(),map.get("topSpacing")!!.toBoolean());

           }


        }


    }



    fun redenrizaCampo(tipo: Int, message: String, ll_main: LinearLayout, spacing: Int, hidden: Boolean){


        val map  = HashMap<Boolean, Int>();
        map.set(false,View.VISIBLE);
        map.set(true,View.GONE);
        when(tipo){

            1->{
                val edit_text          = EditText(this@MainActivity)
                edit_text.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                edit_text.hint         = message;
                edit_text.visibility   = map.get(hidden)!!;
                (edit_text.layoutParams as LinearLayout.LayoutParams).setMargins(0, spacing, 0, 0);

                ll_main.addView(edit_text)

            }

            2->{
                val text_view          = TextView(this@MainActivity)
                text_view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                text_view.hint         = message;
                text_view.visibility   = map.get(hidden)!!;
                (text_view.layoutParams as LinearLayout.LayoutParams).setMargins(0, spacing, 0, 0);
                ll_main.addView(text_view)
            }

            3->{
                val image_view          = ImageView(this@MainActivity)
                image_view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                image_view.visibility   = map.get(hidden)!!;
                (image_view.layoutParams as LinearLayout.LayoutParams).setMargins(0, spacing, 0, 0);
                ll_main.addView(image_view)
            }

            4->{
                val cb                  = CheckBox(applicationContext)
                cb.layoutParams         = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                (cb.layoutParams as LinearLayout.LayoutParams).setMargins(0, spacing, 0, 0);
                cb.visibility           = map.get(hidden)!!;
                cb.setTextColor(Color.BLACK);
                cb.text = message
                ll_main.addView(cb)
            }

            5->{
                val bt                 = Button(this@MainActivity)
                bt.layoutParams        = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                bt.text                = message;
                bt.setOnClickListener(validaEnvioFormulario(bt))
                bt.visibility          = map.get(hidden)!!;
                (bt.layoutParams as LinearLayout.LayoutParams).setMargins(0, spacing, 0, 0);
                ll_main.addView(bt)

            }
        }


    }


    fun validaEnvioFormulario(button: Button): View.OnClickListener {
        return View.OnClickListener { button.text = "rotina de validação " }
    }

}
