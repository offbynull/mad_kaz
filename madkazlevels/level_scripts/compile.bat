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

echo Compiling practice levels...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar prac_free.txt C:\code\trunk\cydMadKaz\src\prac_free.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar prac_climb.txt C:\code\trunk\cydMadKaz\src\prac_climb.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar prac_drop.txt C:\code\trunk\cydMadKaz\src\prac_drop.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed

echo Compiling UI resources...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar csm_res.txt C:\code\trunk\cydMadKaz\src\csm_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar net_res.txt C:\code\trunk\cydMadKaz\src\net_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar browse_res.txt C:\code\trunk\cydMadKaz\src\browse_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar splash_res.txt C:\code\trunk\cydMadKaz\src\splash_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar setup_res.txt C:\code\trunk\cydMadKaz\src\setup_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar mm_res.txt C:\code\trunk\cydMadKaz\src\mm_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar gp_res.txt C:\code\trunk\cydMadKaz\src\gp_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar sc_res.txt C:\code\trunk\cydMadKaz\src\sc_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar ls_res.txt C:\code\trunk\cydMadKaz\src\ls_res.script>lastout.txt 2>&1
if %errorlevel% NEQ 0 goto failed
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar p2ph_res.txt C:\code\trunk\cydMadKaz\src\p2ph_res.script>lastout.txt 2>&1
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
