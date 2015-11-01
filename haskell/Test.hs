import Control.Parallel

fib :: Int -> Int
fib 0 = 1
fib 1 = 1
fib n =
    par f (f + e)
    where
    f = fib (n - 1)
    e = fib (n - 2)

main = print (fib 40)
