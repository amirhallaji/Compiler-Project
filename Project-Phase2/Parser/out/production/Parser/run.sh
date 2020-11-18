#!/bin/bash
mkdir -p out
mkdir -p report
cd ./tests
prefix="t" ;
dirlist=(`ls ${prefix}*.in`) ;
OUTPUT_DIRECTORY="out/"
TEST_DIRECTORY="tests/"
REPORT_DIRECTORY="report/"
NUMBER_OF_PASSED=0
NUMBER_OF_FAILED=0
cd ../
for filelist in ${dirlist[*]}
do
    filename=`echo $filelist | cut -d'.' -f1`;
    output_filename="$filename.out"
    report_filename="$filename.report.txt"
    echo "Running Test $filename -------------------------------------"
    javac -javaagent:/snap/intellij-idea-community/257/lib/idea_rt.jar=33855:/snap/intellij-idea-community/257/bin -Dfile.encoding=UTF-8 -classpath /home/hamed/Documents/University/Term5/Compiler/Assignments/Project/Final/Project-Phase2/Parser/out/production/Parser:/home/hamed/Documents/University/Term5/Compiler/Assignments/Project/Final/Project-Phase2/Parser/src/java-cup-11b-runtime.jar main.java
    if [ $? -eq 1 ]; then
        echo "Compile Error"
    else
        echo "Code Compiled Successfully"
        /home/hamed/.jdks/openjdk-14.0.2/bin/java  -classpath /home/hamed/Documents/University/Term5/Compiler/Assignments/Project/Final/Project-Phase2/Parser/out/production/Parser:/home/hamed/Documents/University/Term5/Compiler/Assignments/Project/Final/Project-Phase2/Parser/src/java-cup-11b-runtime.jar main -i $filelist -o $output_filename 
        if [ $? -eq 0 ]; then
            echo "Code Executed Successfuly!"
            if command -v python3; then
                python3 comp.py -a "$OUTPUT_DIRECTORY$output_filename" -b "$TEST_DIRECTORY$output_filename" -o "$REPORT_DIRECTORY$report_filename"
            else
                python comp.py -a "$OUTPUT_DIRECTORY$output_filename" -b "$TEST_DIRECTORY$output_filename" -o "$REPORT_DIRECTORY$report_filename"
            fi
            if [[ $? = 0 ]]; then
                ((NUMBER_OF_PASSED++))
                echo "++++ test passed"
            else
                ((NUMBER_OF_FAILED++))
                echo "---- test failed !"
            echo
            fi 
        else
            echo "Code Did Not Execute Successfuly!"
            ((NUMBER_OF_FAILED++))
        fi
    fi


done

echo "Passed : $NUMBER_OF_PASSED"
echo "Failed : $NUMBER_OF_FAILED"

