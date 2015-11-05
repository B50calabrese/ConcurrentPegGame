export GOPATH=$(pwd)

if [[ $1 == "clean" ]]; then
    echo "Removing generated files."
    go clean
elif [[ $1 == "run" ]]; then
    go run src/PegGame.go -s $2
else
    echo "Unknown commands: $@"
fi
