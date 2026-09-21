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

echo Compiling space_quest...
java -jar C:\code_wrappers\cydMadKazSDK\dist\cydMadKazSDK.jar space_quest.txt .\compiled_level_scripts\space_quest.script>lastout.txt 2>&1
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
