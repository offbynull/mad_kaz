@echo off

REM    Copyright 2007 CodeYield Development, Inc.  inquiries@codeyielddevelopment.com
REM
REM    This program is free software; you can redistribute it and/or modify
REM    it under the terms of the GNU General Public License as published by
REM    the Free Software Foundation; either version 3 of the License, or
REM    (at your option) any later version.
REM
REM    This program is distributed in the hope that it will be useful,
REM    but WITHOUT ANY WARRANTY; without even the implied warranty of
REM    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
REM    GNU General Public License for more details.
REM
REM    You should have received a copy of the GNU General Public License
REM    along with this program.  If not, see <http://www.gnu.org/licenses/>.

echo Compiling shared data...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar z3_shared.txt C:\code\trunk\cydMadKaz\src\z3_shared.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed

echo Compiling level 0...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar z3m0.txt C:\code\trunk\cydMadKaz\level_scripts\compiled_level_scripts\z3m0.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed

echo Compiling level 1...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar z3m1.txt C:\code\trunk\cydMadKaz\level_scripts\compiled_level_scripts\z3m1.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed

echo Compiling level 2...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar z3m2.txt C:\code\trunk\cydMadKaz\level_scripts\compiled_level_scripts\z3m2.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed

echo Compiling level 3...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar z3m3.txt C:\code\trunk\cydMadKaz\level_scripts\compiled_level_scripts\z3m3.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed

echo Compiling level 4...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar z3m4.txt C:\code\trunk\cydMadKaz\level_scripts\compiled_level_scripts\z3m4.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed

goto end




:failed

echo FAILED!
notepad lastout.txt
pause
goto end




:good
echo PASSED!

:end
