package com.rn_crash_reporter;

import android.app.Application;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new FirebaseReporterPackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
      FirebaseApp.initializeApp(this);
      Thread.setDefaultUncaughtExceptionHandler(new FirebaseExceptionHandler());
      super.onCreate();
      SoLoader.init(this, /* native exopackage */ false);
  }

  class FirebaseExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    public void uncaughtException(Thread t, Throwable e) {
      Log.e("ExceptionROB", "uncaughtException", e);
      final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
      DatabaseReference databaseReference = firebaseDatabase.getReference();
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      e.printStackTrace(printWriter);
      Log.v("ExceptionROB", stringWriter.toString());
      String[] list = stringWriter.toString().split("\\s+at");
      String time = Instant.now().toString().split("\\.")[0];
      for(String trace : list) {
        databaseReference.child("Errors").child(time).push().setValue(trace);
      }
      StringWriter stringWriterActualCause = new StringWriter();
      PrintWriter printWriterActualCause = new PrintWriter(stringWriterActualCause);
      e.getCause().getCause().printStackTrace(printWriterActualCause);
      List<String> listActualCause = Arrays.asList(stringWriterActualCause.toString().split("\\s+at"));
      for(int i = 0; i<2;i++) {
        databaseReference.child("Errors").child(time).push().setValue(listActualCause.get(i));
      }
      uncaughtExceptionHandler.uncaughtException(t, e);
    }

  }
}
