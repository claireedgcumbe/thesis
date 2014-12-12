#!/bin/sh

Max=5

#rm -f /users/jzhao/PDMSwithSchemaMediation/log/*.*
#rm -f /users/jzhao/PDMSwithSchemaMediation/log/timer/*.*

#cd ~/PDMSwithSchemaMediation
#java pdms.SchemaMediation 9001 0 ${Max} >~/PDMSwithSchemaMediation/log/log0.txt&
echo "job on node1"

for((i=2;i<=${Max};i++))
do
echo "ssh node-$i.PeerMediation-3.HEPTOX.emulab.net 'cd ~/PDMSwithSchemaMediation; java pdms.SchemaMediation 9001 `expr $i - 1` ${Max} >~/PDMSwithSchemaMediation/log/log`expr $i - 1`.txt'&"
echo "job on node$i"
done

