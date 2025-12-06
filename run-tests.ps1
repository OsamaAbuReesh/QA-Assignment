# run-tests.ps1
# Downloads a temporary Maven distribution and runs `mvn test` in this project.
# Usage: Open PowerShell (may need elevated permissions to write to TEMP) and run:
# .\run-tests.ps1

param(
    [string]$MavenVersion = '3.9.4',
    [switch]$KeepDownload,
    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$MavenArgs
)

function Write-Info { param($m) Write-Host "[INFO] $m" -ForegroundColor Cyan }
function Write-ErrorMsg { param($m) Write-Host "[ERROR] $m" -ForegroundColor Red }

# If mvn already available, use it
try {
    $mvnCheck = & mvn -v 2>$null
    if ($LASTEXITCODE -eq 0) {
        $argsToPass = if ($MavenArgs -and $MavenArgs.Length -gt 0) { $MavenArgs } else { @('test') }
        Write-Info "Found system 'mvn'. Running 'mvn $($argsToPass -join ' ')'..."
        & mvn @argsToPass
        exit $LASTEXITCODE
    }
} catch {
    # continue
}

# Ensure Java exists
try {
    & java -version > $null 2>&1
    if ($LASTEXITCODE -ne 0) { throw }
} catch {
    Write-ErrorMsg "Java is not found in PATH. Install JDK 1.8+ and ensure 'java' is on PATH."
    exit 1
}

$arch = if ([Environment]::Is64BitOperatingSystem) { 'x64' } else { 'x86' }
$zipName = "apache-maven-$MavenVersion-bin.zip"
$downloadUrl = "https://archive.apache.org/dist/maven/maven-3/$MavenVersion/binaries/$zipName"
$tempDir = Join-Path $env:TEMP "maven-temp-$([System.Guid]::NewGuid().ToString('N'))"
$zipPath = Join-Path $env:TEMP $zipName

Write-Info "Will download Maven $MavenVersion to temporary folder $tempDir"

New-Item -ItemType Directory -Path $tempDir -Force | Out-Null

Write-Info "Downloading $downloadUrl..."
try {
    Invoke-WebRequest -Uri $downloadUrl -OutFile $zipPath -UseBasicParsing -ErrorAction Stop
} catch {
    Write-ErrorMsg "Failed to download Maven: $_"
    exit 1
}

Write-Info "Extracting Maven..."
try {
    Expand-Archive -Path $zipPath -DestinationPath $tempDir -Force
} catch {
    Write-ErrorMsg "Failed to extract Maven: $_"
    exit 1
}

$mavenRoot = Join-Path $tempDir "apache-maven-$MavenVersion"
$mvnCmd = Join-Path $mavenRoot 'bin\mvn.cmd'
if (-not (Test-Path $mvnCmd)) {
    Write-ErrorMsg "mvn.cmd not found after extraction"
    exit 1
}

$argsToPass = if ($MavenArgs -and $MavenArgs.Length -gt 0) { $MavenArgs } else { @('test') }
Write-Info "Running temporary Maven... (this will download dependencies)"
Push-Location -Path (Split-Path -Path $MyInvocation.MyCommand.Path -Parent)
Write-Info "Invoking: '$mvnCmd' with args: $([string]::Join(' ', $argsToPass))"
try {
    $proc = Start-Process -FilePath $mvnCmd -ArgumentList $argsToPass -Wait -NoNewWindow -PassThru
    $exitCode = $proc.ExitCode
} catch {
    Write-ErrorMsg "Failed to start Maven: $_"
    $exitCode = 1
}
Pop-Location

if (-not $KeepDownload) {
    Write-Info "Cleaning up temporary files..."
    try { Remove-Item -Recurse -Force $tempDir } catch {}
    try { Remove-Item -Force $zipPath } catch {}
} else {
    Write-Info "Kept downloaded files in $tempDir (use -KeepDownload to keep them)"
}

exit $exitCode
