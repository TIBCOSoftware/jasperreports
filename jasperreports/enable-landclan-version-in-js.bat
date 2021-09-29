@echo off
rem This script will need to be run with Administrator privileges if your TIBCO Jaspersoft Studio install location is
rem under Administrator control on your system (which is probably true if you installed it to Program Files).

rem This script will alter your local installation of TIBCO Jaspersoft Studio. Please ensure you read these
rem instructions first so that you know what has changed.

rem This script takes the custom LandClan jar and overwrites the local official jar that is used by Jaspersoft Studio
rem so that JS runs with the custom LandClan jar.

rem The install location on my local machine of the relevant jar file is:
rem C:\Program Files\TIBCO\Jaspersoft Studio-6.17.0\configuration\org.eclipse.osgi\68\0\.cp\lib\jasperreports-6.17.0.jar

rem If we do not already have a backup of the official build, make one now.
set jarDir="C:\Program Files\TIBCO\Jaspersoft Studio-6.17.0\configuration\org.eclipse.osgi\68\0\.cp\lib\"
set jarFile="jasperreports-6.17.0.jar"
set backupFile="%jarFile%.official"
echo Checking for backup copy of official jar...
if exist %jarDir%%backupFile% (
    echo Official jar has already been backed-up.
) else (
    echo Creating backup copy of official jar...
    call copy %jarDir%%jarFile% %jarDir%%backupFile%
)

echo Copying the custom LandClan version of Jasper Reports to the Jaspersoft Studio library...
call copy ".\dist\jasperreports-6.17.0-landclan.jar" %jarDir%%jarFile%
echo Finished.