#!/bin/bash
# ==============================================================================
# Script de Ejecución de la Suite de Pruebas Reproducible - MicroRol_Inventario
# ==============================================================================
set -e

echo "[+] Compilando código fuente y suite de pruebas en bin/..."
mkdir -p bin
javac -d bin src/microrol/modelo/*.java src/microrol/gestion/*.java src/microrol/principal/*.java src/microrol/test/*.java

echo "[+] Ejecutando Suite de Pruebas Automatizadas..."
java -cp bin microrol.test.TestSuite
