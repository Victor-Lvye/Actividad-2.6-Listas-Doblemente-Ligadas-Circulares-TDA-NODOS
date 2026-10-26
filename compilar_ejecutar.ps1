# ==============================================================
# Script PowerShell: compilar y ejecutar el proyecto
# Lista Circular — TDA con Records Java + Swing
# ==============================================================

$SRC   = "$PSScriptRoot\src"
$OUT   = "$PSScriptRoot\out"

# Crear directorio de salida si no existe
if (!(Test-Path $OUT)) {
    New-Item -ItemType Directory -Path $OUT | Out-Null
}

Write-Host "==> Compilando..." -ForegroundColor Cyan

javac -d $OUT `
    "$SRC\modelo\Proceso.java" `
    "$SRC\tda\Nodo.java" `
    "$SRC\tda\ListaCircular.java" `
    "$SRC\vista\VentanaPrincipal.java"

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR de compilacion." -ForegroundColor Red
    exit 1
}

Write-Host "==> Compilacion exitosa. Ejecutando..." -ForegroundColor Green
java -cp $OUT vista.VentanaPrincipal
