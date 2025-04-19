@echo off
set "OUTPUT=project-structure.txt"

if exist %OUTPUT% del %OUTPUT%

tree /F /A > %OUTPUT%

start notepad %OUTPUT%
