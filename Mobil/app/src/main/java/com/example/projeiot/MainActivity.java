package com.example.projeiot;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    TextView SicaklikVeri, NemVeri, IsikVeri, ToprakVeri, EsikDegertxtw,ManuelBilgi;
    String sicaklikDeger, nemDeger, isikDeger, toprakDeger;
    SeekBar EsikBar;
    Button veriGetir;
    public int esikDeger;
    public int barSonKonum;
    public int baslangicKontrol=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        SicaklikVeri = findViewById(R.id.sicaklikVeri);
        NemVeri = findViewById(R.id.nemVeri);
        IsikVeri = findViewById(R.id.isikVeri);
        ToprakVeri = findViewById(R.id.toprakVeri);
        EsikDegertxtw = findViewById(R.id.esikDeger);
        EsikBar = findViewById(R.id.esikBar);
        veriGetir = findViewById(R.id.verigetir);
        ManuelBilgi=findViewById(R.id.manuelBilgi);



        ApiGetBarSonDurum();//Eşik Değerini aldığımız barın en son nasıl bırakıldıysa öyle başlamasını istediğimiz için get ile son bar konumunu çektik ver barı o konumdan başlatıyoruz
        //barın son konumunu thingSpeakden alıp başlangıç konumunu o konum olarak ayarlar
        //
        ApiGetSensorler(); //program açıldığında ekranın boş gelmemesi için verilerin apiden gelmesini sağlıyorum
        final Handler handler = new Handler(); //saniye cinsinden veri güncelleme kısmı
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                ApiGetSensorler();
                handler.postDelayed(this, 4000); //4 saniyede bir verileri güncelle
            }
        };
        handler.postDelayed(refresh, 4000);




        if (barSonKonum == 0) {
            EsikDegertxtw.setText("Toprağın Nem Oranı %20'nin Altına Düştüğünde Sulama yap");
        }
        if (barSonKonum == 1) {
            EsikDegertxtw.setText("Toprağın Nem Oranı %40'ın Altına Düştüğünde Sulama yap");
        }
        if (barSonKonum == 2) {
            EsikDegertxtw.setText("Toprağın Nem Oranı %60'ın Altına Düştüğünde Sulama yap");
        }
        veriGetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veriGetir.setEnabled(false);
                ApiPostManuelSulama(1);//butona bastığımızda manuel sulamanın istendiği bilgisi 1 olarak thingspeak'e gider
                ButonGeriSayim();       //ilk geri sayım 32 saniyede bir thingspeake veri gönderebildiğimiz için butona basıldığında değeri 1 olarak kaydeder ve 32 saniye bekleyip değeri 0 a çeker bunu yapmamızın amacı arduino üzerinden 1 değerini okuyup sulamayı başlatmamız için
                ButonGeriSayim2();      //ikinci sayım 60 saniye çünkü ilk önce değeri 1 yapıp 32 saniye bekleyip 0 yaptığımız için bir 15 saniye daha thingspeake eklemek için beklemeye ihtiyacımız var
                Toast.makeText(MainActivity.this, "Manuel Sulama Başlatılıyor", Toast.LENGTH_SHORT).show();
            }
        });

            EsikBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int esikKonum, boolean b) {
                    if(baslangicKontrol==1) { //program ilk açıldığında veritabanından esik değeri hangi konumda bırakıldıysa onu çekiyoruz fakat
                                             //apiden son konum gelip bar konumu değiştirilmek istendiğinde onchange fonksiyonuna girip içierisinde yazdığım
                                             //15 saniyelik bekleme süresine giriyor bu yüzden başlangıçta apiden gelen response okey olduğunda içerisindeki kodları yap dedik

                        if (esikKonum == 0) {
                            EsikDegertxtw.setText("Toprağın Nem Oranı %20'nin Altına Düştüğünde Sulama yap");
                            esikDeger = 0;
                            ApiPostEsik();
                            EsikBar.setEnabled(false); //serverda 15 saniye sınırı olduğu için bar'ı kullanılamaz yapıyorum
                            BarGeriSayim(); //15 saniyeden geriye saydırarak ekrana yazdırıyoruz
                            PostDelay(); // bar'ı 15 saniye sonra aktif etmek için 15 saniye sayaç koyup aktif ediyorum.
                        }
                        if (esikKonum == 1) {
                            EsikDegertxtw.setText("Toprağın Nem Oranı %40'ın Altına Düştüğünde Sulama yap");
                            esikDeger = 1;
                            ApiPostEsik();
                            EsikBar.setEnabled(false);
                            BarGeriSayim(); //15 saniyeden geriye saydırarak ekrana yazdırıyoruz
                            PostDelay();
                        }
                        if (esikKonum == 2) {
                            EsikDegertxtw.setText("Toprağın Nem Oranı %60'ın Altına Düştüğünde Sulama yap");
                            esikDeger = 2;
                            ApiPostEsik();
                            EsikBar.setEnabled(false);
                            BarGeriSayim(); //15 saniyeden geriye saydırarak ekrana yazdırıyoruz
                            PostDelay();
                        }
                    }

                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


    }
    public void ButonGeriSayim(){
        new CountDownTimer(32000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                ApiPostManuelSulama(0);   //geri sayım bittiğinde durumu 0 a çek

            }

        }.start();
    }
    public void ButonGeriSayim2(){
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                ManuelBilgi.setText("Yeniden Sulayabilmek İçin Kalan Süre: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                veriGetir.setEnabled(true);
                ManuelBilgi.setText("Bitki Toprağın Nem Oranına Göre Zaten Sulanıyor Fakat Kendin Manuel Sulamak İstersen Aşağıdaki Butona Basarak Sulayabilirsin.");

            }

        }.start();
    }
    public void BarGeriSayim(){  //Bar disable olduktan sonra enable olmasına kaç saniye kaldığını kullanıcıya göstermek için 15 den 0 a sayan bir sayaç oluşturuyorum
        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                EsikDegertxtw.setText("Değeri Yeniden Değiştirebilmek İçin Kalan Süre: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if(esikDeger==0){EsikDegertxtw.setText("Toprağın Nem Oranı %20'ın Altına Düştüğünde Sulama yap");}
                if(esikDeger==1){EsikDegertxtw.setText("Toprağın Nem Oranı %40'ın Altına Düştüğünde Sulama yap");}
                if(esikDeger==2){EsikDegertxtw.setText("Toprağın Nem Oranı %60'ın Altına Düştüğünde Sulama yap");}
            }

        }.start();
    }
    public void PostDelay(){
    Handler handlerPostDelay = new Handler();
            handlerPostDelay.postDelayed(new Runnable() {
        public void run () {
            EsikBar.setEnabled(true);
        }
    },15000);   //15 saniye sonra barı kullanılabilir yap
    }

    public void ApiPostEsik(){ //Kullanıcı eşik değerini değiştirdiğinde thingspeak apisine kaydedilir ve  arduino kodu üzerinden değer değiştirilir
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://api.thingspeak.com/update?api_key=00EY7F73TLOSQUEZ&"; //ThingSpeak POST Api URL
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("field1", esikDeger);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequestPost = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Desteklenmeyen kodlama %s kullanımı %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequestPost);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void ApiPostManuelSulama(int durumManuel){ //Kullanıcı eşik değerini değiştirdiğinde thingspeak apisine kaydedilir ve  arduino kodu üzerinden değer değiştirilir
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    String URL = "https://api.thingspeak.com/update?api_key=768HM3F6KLGNI3VT&"; //ThingSpeak POST Api URL
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("field1", durumManuel);//manuel sulama tuşuna basıldığında ThingSpeak'e durum değişkeninin değeri olarak kaydet


                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequestPost = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) { //işlem gerçekleşirse içerisini yapar

                        }
                    }, new Response.ErrorListener() { //işlem hata verirse içerisini yapar
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Desteklenmeyen kodlama %s kullanımı %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequestPost);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "hataya düştüm", Toast.LENGTH_SHORT).show();
                }
    }


    public void ApiGetSensorler(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String urlget = "https://api.thingspeak.com/channels/1686086/feeds/last.json?api_key=HL13V1HW21GLPJZO&field=0";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlget,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsnOBJ = new JSONObject(response); //String olarak gelen json verileri obje türüne çeviriyoruz
                            sicaklikDeger=jsnOBJ.getString("field1");//obje içerisinden field1' de bulunan sicaklik değerini alıyorum
                            SicaklikVeri.setText(sicaklikDeger.substring(0,4)+"°C");

                            nemDeger=jsnOBJ.getString("field2");//obje içerisinden field2' de bulunan nem değerini alıyorum
                            NemVeri.setText("%"+nemDeger.substring(0,4));

                            toprakDeger=jsnOBJ.getString("field3");//obje içerisinden field3' de bulunan toprak nem değerini alıyorum
                            ToprakVeri.setText("%"+toprakDeger.substring(0,4));

                            isikDeger=jsnOBJ.getString("field4");//obje içerisinden field4' de bulunan ışık değerini alıyorum
                            IsikVeri.setText("%"+isikDeger.substring(0,4));

                        } catch (Throwable t) {
                            Toast.makeText(MainActivity.this, "Json string json objeye dönusturulemedi", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Hata", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void ApiGetBarSonDurum(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String urlget = "https://api.thingspeak.com/channels/1688783/feeds/last.json?api_key=FSYK2KYPP0W054QO&field=0";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlget,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsnOBJ = new JSONObject(response);
                            barSonKonum=Integer.parseInt(jsnOBJ.getString("field1")); //barın son konumunu çektik
                            EsikBar.setProgress(barSonKonum); //başladığında hangi konumda olduğu
                            baslangicKontrol=1;


                        } catch (Throwable t) {
                            Toast.makeText(MainActivity.this, "Json string json objeye dönusturulemedi", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Hata", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}





