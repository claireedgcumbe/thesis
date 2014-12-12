#!/bin/sh

for ((i=1; i<=5; i++))
do
ssh node-$i.AccessControl-1.HEPTOX.emulab.net "exit"
done
