package com.fkinh.android.shell.lib;

/**
 * Author: jinghao
 * Email: jinghao@meizu.com
 * Date: 2016-07-04
 */
public interface FakeShell {

    int exec(String... args);

    void close();
}
