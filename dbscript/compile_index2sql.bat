@echo off
set username=jenkins01
set password=jenkins01
set ip=192.168.5.28
set warDir=/xg_smartfactory2/component/db
set postfix=-dev.sql
set fileAddress=%cd%\
set ftp=ftp.txt  
set dir=dir.txt
:: 获取当前现有文件内容
echo open %ip%>%ftp%
echo user %username% %password% >>%ftp% 
echo cd %warDir% >>%ftp%
echo ls %warDir% %fileAddress%%dir% >>%ftp%
echo bye >>%ftp%
ftp -n -s:%ftp%
:: 判断文件并下载
echo open %ip%>%ftp%
echo user %username% %password% >>%ftp% 
echo cd %warDir% >>%ftp%
for /f "delims=" %%i in ('dir /b /s /a-d *.index') do (
	for /f "delims=." %%j in (%%i) do (
		findstr %%j%postfix% %dir% >nul &&(
			echo %%j%postfix% is ok
			echo get %%j%postfix% >>%ftp% 
		)||(
			echo %%j%postfix% is error
			pause
		)
	)
)
echo bye >>%ftp%
ftp -n -s:%ftp%