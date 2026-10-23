# Runs Odyssey locally using the values in .env
# Usage:  .\run-local.ps1
Get-Content "$PSScriptRoot\.env" | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith('#')) {
        $i = $line.IndexOf('=')
        if ($i -gt 0) {
            [Environment]::SetEnvironmentVariable($line.Substring(0, $i).Trim(), $line.Substring($i + 1).Trim(), 'Process')
        }
    }
}
$jar = "$PSScriptRoot\target\odyssey-1.0.0.jar"
if (-not (Test-Path $jar)) { Write-Host "Building jar first..."; & "$PSScriptRoot\mvnw.cmd" -q -DskipTests package }
java -jar $jar
