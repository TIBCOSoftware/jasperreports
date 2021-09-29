@echo off
rem This script will need to be run with Administrator privileges if your TIBCO Jaspersoft Studio install location is
rem under Administrator control on your system (which is probably true if you installed it to Program Files).

rem This script will alter your local installation of TIBCO Jaspersoft Studio. Please ensure you read these
rem instructions first so that you know what has changed.

rem This script takes the backup of the official Jasper Reports jar file that was created when the counterpart batch
rem script 'enable-landclan-version-in-js.bat' was run and re-instates it as the main jar file so that JS runs with the
rem official jar.

rem The install location on my local machine of the relevant jar file is:
rem C:\Program Files\TIBCO\Jaspersoft Studio-6.17.0\configuration\org.eclipse.osgi\68\0\.cp\lib\jasperreports-6.17.0.jar

rem If we do not already have a backup of the official build then it is to be assumed that the counterpart batch script
rem has not been run so there is nothing to do.
set jarDir="C:\Program Files\TIBCO\Jaspersoft Studio-6.17.0\configuration\org.eclipse.osgi\68\0\.cp\lib\"
set jarFile="jasperreports-6.17.0.jar"
set backupFile="%jarFile%.official"
echo Checking for backup copy of official jar...
if exist %jarDir%%backupFile% (
    echo Reverting to the official jar from the backup file...
    call copy %jarDir%%backupFile% %jarDir%%jarFile%
    echo Removing the backup...
    call rm %jarDir%%backupFile%
    echo Finished.
) else (
    echo Official version was already enabled (no backup file was found so this is assumed.)
)
