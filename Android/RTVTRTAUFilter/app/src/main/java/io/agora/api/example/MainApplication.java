package io.agora.api.example;

import android.app.Application;

import java.lang.annotation.Annotation;
import java.util.Set;

import io.agora.api.example.utils.ClassUtils;

public class MainApplication extends Application {

//    private GlobalSettings globalSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        initExamples();
    }

    private void initExamples() {
        try {
            Set<String> packageName = ClassUtils.getFileNameByPackageName(this, "io.agora.api.example.examples");
            for (String name : packageName) {
                Class<?> aClass = Class.forName(name);
                Annotation[] declaredAnnotations = aClass.getAnnotations();
                for (Annotation annotation : declaredAnnotations) {

                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    public GlobalSettings getGlobalSettings() {
//        if(globalSettings == null){
//            globalSettings = new GlobalSettings();
//        }
//        return globalSettings;
//    }
}
