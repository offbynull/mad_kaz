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

echo -- Compiling main files --
call compile.bat
echo -- Compiling z0 files --
cd z0_data
call z0_compile.bat
echo -- Compiling z1 files --
cd ..\z1_data
call z1_compile.bat
echo -- Compiling z2 files --
cd ..\z2_data
call z2_compile.bat
echo -- Compiling z3 files --
cd ..\z3_data
call z3_compile.bat
echo -- Compiling z4 files --
cd ..\z4_data
call z4_compile.bat
echo -- Compiling z5 files --
cd ..\z5_data
call z5_compile.bat
cd ..\
echo -- Attempting to combine level scripts --
call combinelevels.bat
