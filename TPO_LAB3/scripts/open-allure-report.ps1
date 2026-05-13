$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $PSScriptRoot
$reportDir = Join-Path $repoRoot "target\site\allure-maven-plugin"
$index = Join-Path $reportDir "index.html"
$port = 8088

if (-not (Test-Path $index)) {
    throw "Allure report not found. Run: mvn clean test allure:report"
}

$connection = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
if (-not $connection) {
    Start-Process -WindowStyle Hidden -FilePath powershell -ArgumentList @(
        "-NoProfile",
        "-ExecutionPolicy",
        "Bypass",
        "-Command",
        "cd '$reportDir'; python -m http.server $port --bind 127.0.0.1"
    )
    Start-Sleep -Seconds 1
}

Start-Process "http://127.0.0.1:$port/index.html"
