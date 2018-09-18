package com.jokin.jwebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jokin.jwebview.jinterface.JwebInterface;
import com.jokin.jwebview.proxy.CallbackFilter;
import com.jokin.jwebview.proxy.Enhancer;
import com.jokin.jwebview.proxy.MethodInterceptor;
import com.jokin.jwebview.proxy.MethodProxy;
import com.jokin.jwebview.proxy.NoOp;

import java.lang.reflect.Method;

public class JWebview extends WebView{



    public JWebview(Context context) {
        this(context,null);
    }

    public JWebview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public JWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void setJsName(String name){


        Enhancer enhancer = new Enhancer(getContext());
        enhancer.setSuperclass(JwebInterface.class);
        enhancer.setCallbacks(new MethodInterceptor[]{NoOp.INSTANCE,new MethodInterceptor() {
            @Override
            public Object intercept(Object object, Object[] args, MethodProxy methodProxy) throws Exception {
                Log.e("TAG","intercept  -- before---");
                Object obj = methodProxy.invokeSuper(object, args);

                Log.e("TAG","intercept  -- after---");
                return obj;
            }
        }
        });
        enhancer.setCallbackFilter(new CallbackFilter() {
            @Override
            public int accept(Method method) {
                if (method.getName().equals("toast2"))
                    return 1;
                return 0;
            }
        });
        Object o = enhancer.create();
        addJavascriptInterface(o,name);

//        Test test = (Test) enhancer.create();
//
//        test.toast3(this);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initSettings() {
        WebSettings webSettings = getSettings();
        //允许webview对文件的操作
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        setDrawingCacheEnabled(true);
    }




}
