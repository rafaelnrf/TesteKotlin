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
import android.content.Context.DOWNLOAD_SERVICE
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.view.View


internal class TabInvestimentosActivity : AppCompatActivity() {

    private val url       = "https://floating-mountain-50292.herokuapp.com/fund.json"
    internal var sumarios = HashMap<String,String>()
    internal var moreInfo = HashMap<String,HashMap<String,String>>()
    internal var infos    = HashMap<String,String>()
    internal var downInfo = HashMap<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_investimentos)

        GetFormulario().execute()
    }

    fun startDownload(url:String) {

        /*
        val mManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val mRqRequest = DownloadManager.Request(
                Uri.parse("http://androidtrainningcenter.blogspot.in/2012/11/android-webview-loading-custom-html-and.html"))
        mRqRequest.setDescription("This is Test File")
        //  mRqRequest.setDestinationUri(Uri.parse("give your local path"));
        val idDownLoad = mManager.enqueue(mRqRequest)
        */
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
                    val js_screen            = jsonObj.getJSONObject("screen");

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

                    val js_downInfo         = js_screen.getJSONArray("downInfo");
                    for (i in 0..(js_downInfo.length() - 1)) {
                        val info            = js_downInfo.getJSONObject(i);
                        downInfo.set(info.get("name").toString(),info.get("data").toString());
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

        @SuppressLint("ResourceType")
        override fun onPostExecute(result: String?)  {
            super.onPostExecute(result)

            val tv_title               = findViewById<TextView>(R.id.title)
            tv_title.text              = sumarios.get("title").toString();

            val tv_fundName            = findViewById<TextView>(R.id.fundName)
            tv_fundName.text           = sumarios.get("fundName").toString();

            val tv_whatIs              = findViewById<TextView>(R.id.whatIs)
            tv_whatIs.text             = sumarios.get("whatIs").toString();

            val tv_definition          = findViewById<TextView>(R.id.definition)
            tv_definition.text         = sumarios.get("definition").toString();

            val tv_riskTitle           = findViewById<TextView>(R.id.riskTitle)
            tv_riskTitle.text          = sumarios.get("riskTitle").toString();

            val tv_infoTitle           = findViewById<TextView>(R.id.infoTitle)
            tv_infoTitle.text          = sumarios.get("infoTitle").toString();


            val tv_fundoMonth          = findViewById<TextView>(R.id.col_fundo_month)
            tv_fundoMonth.text         = moreInfo.get("month")!!.get("fundo").toString()+"%";

            val tv_cdiMonth            = findViewById<TextView>(R.id.col_cdi_month)
            tv_cdiMonth.text           = moreInfo.get("month")!!.get("cdi").toString()+"%";

            val tv_fundoYear           = findViewById<TextView>(R.id.col_fundo_year)
            tv_fundoYear.text          = moreInfo.get("year")!!.get("fundo").toString()+"%";

            val tv_cdiYear             = findViewById<TextView>(R.id.col_cdi_year)
            tv_cdiYear.text            = moreInfo.get("year")!!.get("cdi").toString()+"%";

            val tv_fundo12Months       = findViewById<TextView>(R.id.col_fundo_12month)
            tv_fundo12Months.text      = moreInfo.get("12months")!!.get("fundo").toString()+"%";

            val tv_cdi12Months         = findViewById<TextView>(R.id.col_cdi_12month)
            tv_cdi12Months.text        = moreInfo.get("12months")!!.get("cdi").toString()+"%";

            val tv_ta_valor            = findViewById<TextView>(R.id.col_ta_valor)
            tv_ta_valor.text           = infos.get("Taxa de administração").toString();

            val tv_ai_valor            = findViewById<TextView>(R.id.col_ai_valor)
            tv_ai_valor.text           = infos.get("Aplicação inicial").toString();

            val tv_mv_valor            = findViewById<TextView>(R.id.col_mv_valor)
            tv_mv_valor.text           = infos.get("Movimentação mínima").toString();

            val tv_sm_valor            = findViewById<TextView>(R.id.col_sm_valor)
            tv_sm_valor.text           = infos.get("Saldo mínimo").toString();

            val tv_rvb_valor           = findViewById<TextView>(R.id.col_rvb_valor)
            tv_rvb_valor.text          = infos.get("Resgate (valor bruto)").toString();

            val tv_cvb_valor           = findViewById<TextView>(R.id.col_cvb_valor)
            tv_cvb_valor.text          = infos.get("Cota (valor bruto)").toString();

            val tv_pvb_valor           = findViewById<TextView>(R.id.col_pvb_valor)
            tv_pvb_valor.text          = infos.get("Pagamento (valor bruto)").toString();



            val tv_essentials_valor    = findViewById<TextView>(R.id.col_essentials_valor)
            tv_essentials_valor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_down, 0, 0, 0);
            tv_essentials_valor.setOnClickListener {
                startDownload(downInfo.get("Essentials").toString());
            }

            val tv_desempenho_valor    = findViewById<TextView>(R.id.col_desempenho_valor)
            tv_desempenho_valor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_down, 0, 0, 0);
            tv_desempenho_valor.setOnClickListener {
                startDownload(downInfo.get("Desempenho").toString());
            }

            val tv_complementares_valor = findViewById<TextView>(R.id.col_complementares_valor)
            tv_complementares_valor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_down, 0, 0, 0);
            tv_complementares_valor.setOnClickListener {
                startDownload(downInfo.get("Complementares").toString());
            }

            val tv_regulamento_valor    = findViewById<TextView>(R.id.col_regulamento_valor)
            tv_regulamento_valor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_down, 0, 0, 0);
            tv_regulamento_valor.setOnClickListener {
                startDownload(downInfo.get("Regulamento").toString());
            }

            val tv_adesao_valor    = findViewById<TextView>(R.id.col_adesao_valor)
            tv_adesao_valor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_down, 0, 0, 0);
            tv_adesao_valor.setOnClickListener {
                startDownload(downInfo.get("Adesão").toString());
            }

        }

    }





}