@echo off
REM ============================================================
REM Script Batch: compilar y ejecutar el proyecto
REM Lista Circular - TDA con Records Java + Swing
REM ============================================================

set SRC=%~dp0src
set OUT=%~dp0out

if not exist "%OUT%" mkdir "%OUT%"

echo =^> Compilando...
javac -d "%OUT%" ^
    "%SRC%\modelo\Proceso.java" ^
    "%SRC%\tda\Nodo.java" ^
    "%SRC%\tda\ListaCircular.java" ^
    "%SRC%\vista\VentanaPrincipal.java"

if errorlevel 1 (
    echo ERROR de compilacion.
    pause
    exit /b 1
)

echo =^> Compilacion exitosa. Ejecutando...
java -cp "%OUT%" vista.VentanaPrincipal
pause
