package ru.mirea.chirka.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    private static final String TAG = "MyLooper";

    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        this.mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d(TAG, "Thread started, preparing Looper...");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                String ageStr = data.getString("age");
                String occupation = data.getString("occupation");

                int age = Integer.parseInt(ageStr);
                Log.d(TAG, "Получены данные: возраст=" + age + ", род=" + occupation);

                try {
                    Thread.sleep(age * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String result = String.format(
                        "Через %d сек: вы %s (возраст %d лет)",
                        age, occupation, age
                );

                Message reply = new Message();
                Bundle out = new Bundle();
                out.putString("result", result);
                reply.setData(out);
                mainHandler.sendMessage(reply);
            }
        };

        Looper.loop();
    }
}
