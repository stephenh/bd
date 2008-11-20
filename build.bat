@echo off
if "%JAVA_HOME%"=="" echo Error: JAVA_HOME is not defined.
"%JAVA_HOME%/bin/java" -Xmx512m -cp "%JAVA_HOME%/lib/tools.jar;lib/build/ant-1.7.1.jar;lib/build/ant-1.7.1-launcher.jar" org.apache.tools.ant.launch.Launcher %*

