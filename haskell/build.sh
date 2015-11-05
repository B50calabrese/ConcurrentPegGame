if [[ $1 == "clean" ]]; then
    echo "Removing all generated files"
    rm PegGame
    rm PegGame.hi
    rm PegGame.o
elif [[ $1 == "run" ]]; then
    ./PegGame -s $2 -N2
elif [[ -z $1 ]]; then
    echo "Building..."
    ghc -O2 PegGame.hs -rtsopts -threaded
else
    echo "Unkown commands: $@"
fi
