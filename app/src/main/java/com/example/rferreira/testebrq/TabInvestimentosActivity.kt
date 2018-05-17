package com.example.rferreira.testebrq

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.rferreira.testebrq.lib.HttpHandler
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

internal class TabInvestimentosActivity : AppCompatActivity() {

    private val url       = "https://floating-mountain-50292.herokuapp.com/fund.json"
    internal var sumarios = HashMap<String,String>()
    internal var moreInfo = HashMap<String,HashMap<String,String>>()
    internal var infos    = HashMap<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_investimentos)

        GetFormulario().execute()
    }


    private inner class GetFormulario : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): String?  {
            val sh = HttpHandler()

            val jsonStr                      = sh.makeServiceCall(url)
            Log.e("Teste Screen",jsonStr);
            if (jsonStr != null) {
                try {
                    Log.e("Teste 1","teste1");
                    val jsonObj              = JSONObject(jsonStr)
                    Log.e("Teste 3","teste2");
                    val js_screen            = jsonObj.getJSONObject("screen");
                    Log.e("Teste Screen",js_screen.toString());

                    sumarios.set("title",js_screen.get("title").toString());
                    sumarios.set("fundName",js_screen.get("fundName").toString());
                    sumarios.set("whatIs",js_screen.get("whatIs").toString());
                    sumarios.set("definition",js_screen.get("definition").toString());
                    sumarios.set("riskTitle",js_screen.get("riskTitle").toString());
                    sumarios.set("risk",js_screen.get("risk").toString());
                    sumarios.set("infoTitle",js_screen.get("infoTitle").toString());


                    val js_moreInfo          = js_screen.getJSONObject("moreInfo");
                    val js_moreInfo_Month    = js_moreInfo.getJSONObject("month");
                    var mapMonth             = HashMap<String, String>();
                    mapMonth.set("fundo",js_moreInfo_Month.get("fund").toString())
                    mapMonth.set("cdi",js_moreInfo_Month.get("CDI").toString())
                    moreInfo.set("month",mapMonth);

                    val js_moreInfo_Year    = js_moreInfo.getJSONObject("year");
                    var mapYear             = HashMap<String, String>();
                    mapYear.set("fundo",js_moreInfo_Year.get("fund").toString())
                    mapYear.set("cdi",js_moreInfo_Year.get("CDI").toString())
                    moreInfo.set("year",mapYear);

                    val js_moreInfo_12Month = js_moreInfo.getJSONObject("12months");
                    var map12Month          = HashMap<String, String>();
                    map12Month.set("fundo",js_moreInfo_12Month.get("fund").toString())
                    map12Month.set("cdi",js_moreInfo_12Month.get("CDI").toString())
                    moreInfo.set("12months",map12Month);

                    val js_infos            = js_screen.getJSONArray("info");
                    for (i in 0..(js_infos.length() - 1)) {
                        val info            = js_infos.getJSONObject(i);
                        infos.set(info.get("name").toString(),info.get("data").toString());
                    }
                    /*
                    for (i in 0..(js_screen.length() - 1)) {
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
                        sumarios.add(map);
                    }
                    */

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

        @SuppressLint("ResourceType")
        override fun onPostExecute(result: String?)  {
            super.onPostExecute(result)

            val tv_title          = findViewById<TextView>(R.id.title)
            tv_title.text         = sumarios.get("title").toString();

            val tv_fundName       = findViewById<TextView>(R.id.fundName)
            tv_fundName.text      = sumarios.get("fundName").toString();

            val tv_whatIs         = findViewById<TextView>(R.id.whatIs)
            tv_whatIs.text        = sumarios.get("whatIs").toString();

            val tv_definition     = findViewById<TextView>(R.id.definition)
            tv_definition.text    = sumarios.get("definition").toString();

            val tv_riskTitle      = findViewById<TextView>(R.id.riskTitle)
            tv_riskTitle.text     = sumarios.get("riskTitle").toString();

            val tv_infoTitle      = findViewById<TextView>(R.id.infoTitle)
            tv_infoTitle.text     = sumarios.get("infoTitle").toString();


            val tv_fundoMonth     = findViewById<TextView>(R.id.col_fundo_month)
            tv_fundoMonth.text    = moreInfo.get("month")!!.get("fundo").toString();

            val tv_cdiMonth       = findViewById<TextView>(R.id.col_cdi_month)
            tv_cdiMonth.text      = moreInfo.get("month")!!.get("cdi").toString();

            val tv_fundoYear      = findViewById<TextView>(R.id.col_fundo_year)
            tv_fundoYear.text     = moreInfo.get("year")!!.get("fundo").toString();

            val tv_cdiYear        = findViewById<TextView>(R.id.col_cdi_year)
            tv_cdiYear.text       = moreInfo.get("year")!!.get("cdi").toString();

            val tv_fundo12Months  = findViewById<TextView>(R.id.col_fundo_12month)
            tv_fundo12Months.text = moreInfo.get("12months")!!.get("fundo").toString();

            val tv_cdi12Months    = findViewById<TextView>(R.id.col_cdi_12month)
            tv_cdi12Months.text   = moreInfo.get("12months")!!.get("cdi").toString();

            val tv_ta_valor       = findViewById<TextView>(R.id.col_ta_valor)
            tv_ta_valor.text      = infos.get("Taxa de administração").toString();

            val tv_ai_valor       = findViewById<TextView>(R.id.col_ai_valor)
            tv_ai_valor.text      = infos.get("Aplicação inicial").toString();

            val tv_mv_valor       = findViewById<TextView>(R.id.col_mv_valor)
            tv_mv_valor.text      = infos.get("Movimentação mínima").toString();

            val tv_sm_valor       = findViewById<TextView>(R.id.col_sm_valor)
            tv_sm_valor.text      = infos.get("Saldo mínimo").toString();

            val tv_rvb_valor      = findViewById<TextView>(R.id.col_rvb_valor)
            tv_rvb_valor.text     = infos.get("Resgate (valor bruto)").toString();

            val tv_cvb_valor      = findViewById<TextView>(R.id.col_cvb_valor)
            tv_cvb_valor.text     = infos.get("Cota (valor bruto)").toString();

            val tv_pvb_valor      = findViewById<TextView>(R.id.col_pvb_valor)
            tv_pvb_valor.text     = infos.get("Pagamento (valor bruto)").toString();



        }

    }


}