param([switch]$Run)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force -Path 'build/classes','build/test-classes' | Out-Null
    $mainSources = @(Get-ChildItem -LiteralPath 'src/main/java' -Recurse -Filter '*.java' | ForEach-Object FullName)
    & javac --release 17 -encoding UTF-8 -d build/classes @mainSources
    if ($LASTEXITCODE -ne 0) { throw 'Error de compilación.' }
    $testSources = @(Get-ChildItem -LiteralPath 'src/test/java' -Recurse -Filter '*.java' | ForEach-Object FullName)
    & javac --release 17 -encoding UTF-8 -cp build/classes -d build/test-classes @testSources
    if ($LASTEXITCODE -ne 0) { throw 'Error al compilar las pruebas.' }
    & java '-Djava.awt.headless=true' -cp 'build/classes;build/test-classes' reservas.Pruebas
    if ($LASTEXITCODE -ne 0) { throw 'Las pruebas fallaron.' }
    & jar --create --file build/sistema-reservas.jar --main-class reservas.Main -C build/classes .
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo generar el JAR.' }
    Write-Output 'JAR generado: build/sistema-reservas.jar'
    if ($Run) { & java -jar build/sistema-reservas.jar }
} finally { Pop-Location }
