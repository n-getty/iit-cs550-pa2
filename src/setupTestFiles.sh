#!/bin/bash
#
# The first argument is the number of hosts (10)
#  the second argument will be the overlap factor (ie the percentage of host)
#  the third argrument is the number of files
#  the fourth argument is the number of consecutive downloads
#
# Check for correct usage
if [ "$#" -ne 4 ]; then
    echo "Illegal number of parameters, correct usage"
    echo "Arg1: number of hosts"
    echo "Arg2: overlap factor (number out of (Arg1) of locations for each file to be replicated"
    echo "Arg3: number of files to generate"
    echo "Arg4: number of consecutive downloaders (for input files) [add 1]"
    exit
fi
#
# clean the old stuff
rm -r tests/*
rm ../topologies/topo/*
#
# create directories and initialize input files
for n in `seq 1 $1`; do
    mkdir tests/test$(($n))
    touch ../topologies/topo/input_$(($n)).txt
done
#
# create random files
for n in `seq 1 $3`; do
    #
    # generates 1k * file number size random file
    dd if=/dev/urandom of=tests/test1/file_$(printf %03d "$n").bin bs=1024 count=$(printf "$n")
    #
    # add overlap factor to random other folders (and possibly delete from master)
    # compute $2 permuations and sort in reverse order
    files=($(shuf -i 1-10 -n $2 | sort -rn))
    #
    for file in "${files[@]}"; do
	if [ $file -ne 1 ]; then
	    # copy to new location
	    cp tests/test1/file_$(printf %03d "$n").bin tests/test$(printf "$file")/file_$(printf %03d "$n").bin
	else
	    # remove from origional
	    rm tests/test1/file_$(printf %03d "$n").bin
	fi
    done
    for p in `seq 2 $4`; do
	echo file_$(printf %03d "$n").bin >> ../topologies/topo/input_$(printf %d "$p").txt   
    done
done
for p in `seq 2 $4`; do
	echo exi >> ../topologies/topo/input_$(printf %d "$p").txt   
    done
exit
