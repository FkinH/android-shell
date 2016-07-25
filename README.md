# android-shell
Android shell command library.

## Usage

```
OnExecuteCallback callBack = new OnExecuteCallback() {
    @Override
    public void onSuccess(String buffer) {
        //todo: read buffer
    }

    @Override
    public void onFail() {
        //todo: do sth
    }
};
FakeShellKeeper shell = FakeShellKeeper.getInstance();
shell.setCallBack(callBack) // set on execute callback
shell.exec(String[] commands);  //executing commands and geting out put in log
shell.close();  //close shell
shell.restart();  //restart shell, a replacement for `ctrl+c`
```
