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

echo Sticking together 0 levels...
java -jar "C:\code_wrappers\cydMadKazSDKIndexedCombiner\dist\cydMadKazSDKIndexedCombiner.jar" levellist0.txt "C:\code\trunk\cydMadKaz\src\levels0.dat"
if %errorlevel% NEQ 0 pause
echo Sticking together 1 levels...
java -jar "C:\code_wrappers\cydMadKazSDKIndexedCombiner\dist\cydMadKazSDKIndexedCombiner.jar" levellist1.txt "C:\code\trunk\cydMadKaz\src\levels1.dat"
if %errorlevel% NEQ 0 pause
echo Sticking together 2 levels...
java -jar "C:\code_wrappers\cydMadKazSDKIndexedCombiner\dist\cydMadKazSDKIndexedCombiner.jar" levellist2.txt "C:\code\trunk\cydMadKaz\src\levels2.dat"
if %errorlevel% NEQ 0 pause
echo Sticking together 3 levels...
java -jar "C:\code_wrappers\cydMadKazSDKIndexedCombiner\dist\cydMadKazSDKIndexedCombiner.jar" levellist3.txt "C:\code\trunk\cydMadKaz\src\levels3.dat"
if %errorlevel% NEQ 0 pause
echo Sticking together 4 levels...
java -jar "C:\code_wrappers\cydMadKazSDKIndexedCombiner\dist\cydMadKazSDKIndexedCombiner.jar" levellist4.txt "C:\code\trunk\cydMadKaz\src\levels4.dat"
if %errorlevel% NEQ 0 pause
echo Sticking together 5 levels...
java -jar "C:\code_wrappers\cydMadKazSDKIndexedCombiner\dist\cydMadKazSDKIndexedCombiner.jar" levellist5.txt "C:\code\trunk\cydMadKaz\src\levels5.dat"
if %errorlevel% NEQ 0 pause

