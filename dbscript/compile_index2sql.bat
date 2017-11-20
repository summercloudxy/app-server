@echo off
set username=jenkins01
set password=jenkins01
set ip=192.168.5.28
set warDir=/xg_smartfactory2/component/db
echo open %ip%>ftp.txt  
echo user %username% %password% >>ftp.txt  
echo cd %warDir% >>ftp.txt  
for /f "delims=" %%i in ('dir /b /s /a-d *.index') do (
echo %%i
	for /f "delims=" %%j in (%%i) do (
	echo %%j
	echo get %%j >>ftp.txt  
	)
)
echo bye >>ftp.txt  
ftp -n -s:ftp.txt