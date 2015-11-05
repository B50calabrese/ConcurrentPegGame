if [[ $1 == "clean" ]]; then
    echo "Removing generated files."
    rm *.class
elif [[ $1 == "run" ]]; then
    java PegGame -s $2
elif [[ -z $1 ]]; then
    echo "Building"
    javac PegGame.java
else
    echo "Unknown commands: $@"
fi
