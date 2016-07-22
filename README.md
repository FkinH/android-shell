# android-shell
Android shell command library. 

## Usage

```
FakeShellKeeper shell = FakeShellKeeper.getInstance();
shell.exec(String[] commands);  //executing commands and geting out put in log
shell.close();  //close shell
shell.restart();  //restart shell, a replacement for `ctrl+c`
```
