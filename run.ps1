
$SrcDir = "${PSScriptRoot}\src"
$BuildDir = "${PSScriptRoot}\build"

Push-Location $PSScriptRoot

if (!(Test-Path -Path $BuildDir)) {
    New-Item -ItemType Directory -Path $BuildDir
}

javac.exe -d $BuildDir $SrcDir\*.java
if ($LASTEXITCODE -ne 0) {
    throw "Compile failed."
}

$Program = $args[0]
if (!($Program)) {
    throw "Empty program argument!"
}

java.exe -cp $BuildDir $Program