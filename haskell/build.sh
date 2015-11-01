if [[ $1 == "clean" ]]; then

    echo "Removing all generated files"
    rm PegGame
    rm PegGame.hi
    rm PegGame.o
    rm Test
    rm Test.hi
    rm Test.o

elif [[ -z $1 ]]; then

    echo "Building..."
    ghc -threaded PegGame.hs
    ghc -threaded Test.hs
else
    echo "Unkown commands: $@"
fi
