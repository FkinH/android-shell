package com.fkinh.android.shell.lib;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: jinghao
 * Email: jinghao@meizu.com
 * Date: 2016-07-04
 */
public class FakeShellImpl implements FakeShell{

    public static final String INIT_COMMAND = "/system/bin/sh";

    public static final String LINE_END = "\n";

    private int mPid;

    private Process mProcess;

    private InputStream mRead;

    private InputStream mError;

    private OutputStream mWrite;

    private OnExecuteCallback mCallback;

    public FakeShellImpl(){
        init();
    }

    public void setOnExecuteCallback(OnExecuteCallback mCallback) {
        this.mCallback = mCallback;
    }

    private void init() {
        try {
            ProcessBuilder pb =  new ProcessBuilder(INIT_COMMAND).redirectErrorStream(true);
            pb.directory(new File("/"));
            Process process = pb.start();
            mProcess = process;
            mPid = getPid(process);
            mRead = process.getInputStream();
            mError = process.getErrorStream();
            mWrite = process.getOutputStream();
            mCallback = new OnExecuteCallback() {

                @Override
                public void onSuccess(String buffer) {
                    Log.e("LOG", buffer);
                }

                @Override
                public void onFail() {

                }
            };
            Log.e("LOG", "pid : " + mPid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        read();
    }

    private Thread t;

    private boolean mReading = false;

    private void read(){
        mReading = true;
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buff = new byte[4096];
                int read;
                try {
                    while (mRead != null && (read = mRead.read(buff)) > 0 && mReading) {
                        String buffStr = new String(buff, 0, read);
                        mCallback.onSuccess(buffStr);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mCallback.onFail();
                }
                Log.e("LOG", "read thread die");
            }
        });
        t.start();
    }

    @Override
    public int exec(String... args) {
        for(String arg:args){
            try {
                mWrite.write(arg.getBytes());
                mWrite.write(LINE_END.getBytes());
                mWrite.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 0;
    }

    @Override
    public void close() {
        mReading = false;
        try {
            if (mWrite != null) {
                mWrite.close();
            }
            if (mRead != null) {
                mRead.close();
            }
            if (mError != null) {
                mError.close();
            }
            if (mProcess != null) {
                mProcess.destroy();
            }
            android.os.Process.killProcess(mPid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getPid(Process process){
        String process_str = process.toString();
        int start = process_str.indexOf("=")+1;
        int end = process_str.indexOf("]");
        String pid = process_str.substring(start, end);
        return Integer.parseInt(pid);
    }
}
