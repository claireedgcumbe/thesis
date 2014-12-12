#!/bin/sh

Max=10

for ((i=2;i<=${Max};i++))
do
ssh node-$i.AccessControl-2.HEPTOX.emulab.net "sudo reboot; exit" &
echo "reboot node$i"
done

sudo reboot
exit