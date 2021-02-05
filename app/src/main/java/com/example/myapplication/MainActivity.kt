package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.net.URL

class MainActivity : AppCompatActivity() {

    var baseUrl = "http://www.gaenso.com/shop/shopbrand.html?type=Y&xcode=062&sort=&page="
    //1 페이지 부터
    var pages = 1
    //3 페이지 까지
    var maxPages = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        while (pages <= maxPages){
            var t = Thread(UrlRun(baseUrl,pages,applicationContext))

            t.start()

            t.join()

            pages++
        }
        Log.d("TTT","끝!")

    }

    class UrlRun(var baseUrl : String, var pages : Int,var context: Context) : Runnable{
        lateinit var elements: Elements
        @Synchronized
        override fun run() {
            try{
                var doc = Jsoup.connect(baseUrl+pages).get()
                elements = doc.select("div.item-cont ul li dl dt a")
                //url들을 room에 저장
                var db = Room.databaseBuilder(context,URLDataBase::class.java,"URLS").build()
                //urldatabase는 roomdatabase를 상속받습니다
                for( e in elements){
                    var url = e.absUrl("href")
                    Log.d("TTT",url)
                    try {
                        db.urlDao().insertUrls(URLS(url, "갠소"))
                        //

                    }catch(e : Exception){
                        Log.d("TTT", e.toString())
                    }
                }
            }catch (e : Exception){
                Log.d("TTT",e.toString())
            }
        }

    }


}

