@echo off

set version=1.4

echo build release: ekit
call MakeEkit.bat
echo.

echo build release: ekit spell
call MakeEkit.bat spell
echo.

echo build release: ekitapplet
call MakeEkitApplet.bat
echo.

echo build release: ekitapplet spell
call MakeEkitApplet.bat spell
echo.

set version=

echo move jars to build-Folder
move ekit*.jar builds
echo.

echo done!