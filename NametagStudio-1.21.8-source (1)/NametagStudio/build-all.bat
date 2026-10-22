@echo off
set G=gradle
if exist gradlew.bat set G=call gradlew.bat
if not exist dist mkdir dist
%G% clean build -Pmc=1.21.8 || exit /b 1
copy /Y build\libs\nametagstudio-1.0.0.jar dist\
echo Build finished. Check the dist folder.
