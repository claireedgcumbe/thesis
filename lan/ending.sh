#!/bin/sh

Max=5

killall java &

echo "killing java process on node1"

for ((i=2;i<=${Max};i++))
do
ssh node-$i.AccessControl-1.HEPTOX.emulab.net "killall java; exit" &
echo "killing java process on node$i"
done

