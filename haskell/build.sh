if [[ $1 == "clean" ]]; then

    echo "Removing all generated files"
    rm PegGame
    rm PegGame.hi
    rm PegGame.o
    rm Test
    rm Test.hi
    rm Test.o
elif [[ $1 == "runtest" ]]; then
    ./Test +RTS -N2 -s
elif [[ -z $1 ]]; then

    echo "Building..."
    ghc -O2 PegGame.hs -rtsopts -threaded
    ghc -O2 Test.hs -rtsopts -threaded
else
    echo "Unkown commands: $@"
fi
