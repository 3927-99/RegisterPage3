package com.example.registerpage3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(){
    //为了把位置信息带出来，只能放外面了。。。。。
    //val a3:TextView = findViewById(R.id.a3)//appear
    //接收地址变量
    var latitude:Double = 0.0
    var longitude:Double = 0.0
    var address:String = "默认地址"
    //声明AMapLocationClient类对象
    var mLocationClient:AMapLocationClient? = null;
//声明定位回调监听器
//    var mLocationListener:AMapLocationListener  = object:AMapLocationListener{
//    override fun onLocationChanged(amapLocation: AMapLocation?) {
//        if (amapLocation != null) {
//            if (amapLocation.getErrorCode() == 0) {
//                //可在其中解析amapLocation获取相应内容。
//                latitude = amapLocation.getLatitude()//获取纬度
//                longitude = amapLocation.getLongitude()//获取经度
//                address = amapLocation.getAddress()//地址
//            }else {
//                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError","location Error, ErrCode:"
//                        + amapLocation.getErrorCode() + ", errInfo:"
//                        + amapLocation.getErrorInfo())
//                //Toast.makeText(this, "地理信息获取失败，请检查网络后重试", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//};


    /**
     * onCreate函数
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //1.签到界面实时显示时间
        val mDate:TextView = findViewById(R.id.mDate)
        setDate(mDate)
        //2.设置当前时间信息
        val mTime:TextView = findViewById(R.id.mTime)
        setTime(mTime)
        //3.从Bobm获取地址，并上传到服务器上
        val mLocation:TextView = findViewById(R.id.a3)

        nailLocation(mLocation)
         //没有成功获取地理位置信息给提示
//        if(nailLocation(mLocation) != 1){
//            Toast.makeText(this, "地理信息获取失败，请检查网络后重试", Toast.LENGTH_SHORT).show()
//        }

        //4.建立并show体温选择弹窗
        val mRegisterImg:ImageView = findViewById(R.id.mRegisterImg)//hide
        val mRegisterTag:TextView = findViewById(R.id.mRegisterTag)//hide
        val mSuccessImg:ImageView = findViewById(R.id.mSuccessImg)//appear
        val a1:TextView = findViewById(R.id.a1)//appear
        val a2:TextView = findViewById(R.id.a2)//appear
        val a3:TextView = findViewById(R.id.a3)//appear
        val a4:TextView = findViewById(R.id.a4)//appear
        setTime(a4)//a4设置时间
        mRegisterImg.setOnClickListener {
            //弹窗出现
            buildTemperatureWindow(this, R.layout.temperature)
            //信息更换
            mRegisterImg.visibility = View.INVISIBLE
            mRegisterTag.visibility = View.INVISIBLE
            mSuccessImg.visibility = View.VISIBLE
            a1.visibility = View.VISIBLE
            a2.visibility = View.VISIBLE
            a3.visibility = View.VISIBLE
            a4.visibility = View.VISIBLE



        }




    }




    //1.将对应文本设置为当前日期
    fun setDate(xTextView: TextView){
        val xDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = xDateFormat.format(Date())
        xTextView.setText(date)
    }
    //2.将对应文本设置为当前时间
    fun setTime(xTextView: TextView){
        val xTimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val time = xTimeFormat.format(Date())
        xTextView.setText(time)
    }


    //3.从高德地图获取地址，显示到textView控件中，并上传到服务器上
    fun nailLocation(xTextView: TextView):Int{
        //临时接收变量
        var latitude:Double = 0.0
        var longitude:Double = 0.0
        var address:String = "默认地址"
        //声明AMapLocationClient类对象
        var mLocationClient: AMapLocationClient?
        //声明定位回调监听器
        var mLocationListener:AMapLocationListener  = object:AMapLocationListener{
            override fun onLocationChanged(amapLocation: AMapLocation?) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        latitude = amapLocation.getLatitude()//获取纬度
                        longitude = amapLocation.getLongitude()//获取经度
                        address = amapLocation.getAddress()//地址
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo())
                        //Toast.makeText(this, "地理信息获取失败，请检查网络后重试", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = AMapLocationClient(applicationContext)
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)
        //声明AMapLocationClientOption对象
        var mLocationOption: AMapLocationClientOption? = null
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        val option = AMapLocationClientOption()
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option)
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation()
            mLocationClient.startLocation()
        }
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //如果成功

        object:AMapLocationListener{
            override fun onLocationChanged(amapLocation: AMapLocation?) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        latitude = amapLocation.getLatitude()//获取纬度
                        longitude = amapLocation.getLongitude()//获取经度
                        address = amapLocation.getAddress()//地址
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo())
                        //Toast.makeText(this, "地理信息获取失败，请检查网络后重试", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        xTextView.setText(address+"「经度："+latitude.toString()+"纬度："+longitude.toString()+"」")
        return 1
    }
    //4.建立弹窗(可以任意修改)
    fun buildTemperatureWindow(context: Context, layoutResID: Int){
        val myView: View =
            LayoutInflater.from(context).inflate(layoutResID, null)
        val alertDialog: AlertDialog? = context?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("立即签到",
                    DialogInterface.OnClickListener { dialog, id ->
                        //根据温度信息进行操作
                    })
                setNegativeButton("取消",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            }
            //创建alertDialog
            builder.create()
        }
        alertDialog?.setView(myView)
        alertDialog?.show()
    }

//    override fun onLocationChanged(amapLocation: AMapLocation?) {
//        if (amapLocation != null) {
//            if (amapLocation.getErrorCode() == 0) {
//                //可在其中解析amapLocation获取相应内容。
//                latitude = amapLocation.getLatitude()//获取纬度
//                longitude = amapLocation.getLongitude()//获取经度
//                address = amapLocation.getAddress()//地址
//            }else {
//                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError","location Error, ErrCode:"
//                        + amapLocation.getErrorCode() + ", errInfo:"
//                        + amapLocation.getErrorInfo())
//                Toast.makeText(this, "地理信息获取失败，请检查网络后重试", Toast.LENGTH_SHORT).show()
//            }
//        }
//        a3.setText(address+"「经度："+latitude.toString()+"纬度："+longitude.toString()+"」")
//    }


}