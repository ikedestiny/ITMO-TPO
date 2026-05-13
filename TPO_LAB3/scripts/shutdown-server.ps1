Get-CimInstance Win32_Process | ? { $_.Name -match 'python' -and $_.CommandLine -match 'http.server 8088' } | % { Stop-Process -Id $_.ProcessId -Force }
