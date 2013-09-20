#! /bin/sh
# 
# File:   startFly.sh
# Created on May 19, 2008, 8:18 AM
#  
# Start the fly server on the given OS and processor
#
#./bin/Linux/i386/fly &
#./bin/Linux/arm/fly &
#./bin/MacOSX/i386/fly &
#./bin/Solaris/i386/fly &
#

OS_NAME=UNKNOWN
ARCH=UNKNOWN

case `uname -s` in 
	(Darwin) OS_NAME=MacOSX;;
	(Linux)  OS_NAME=Linux;;
	(SunOS)  OS_NAME=Solaris
esac

case `uname -p` in 
	(i386) ARCH=i386;;
	(powerpc) ARCH=ppc;;
	(arm) ARCH=arm;;
	(*) ARCH=i386;;
esac

echo $OS_NAME
echo $ARCH

if [ $OS_NAME = UNKNOWN ] || [ $ARCH = UNKNOWN ];
then echo Fly starter does not recognise `uname -s` `uname -p`
else
	chmod +x ./bin/$OS_NAME/$ARCH/fly 
	./bin/$OS_NAME/$ARCH/fly $* &
fi

