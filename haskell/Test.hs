import Control.Parallel
import Control.Parallel.Strategies


--evalList :: Strategy a -> Strategy [a]
--evalList strat [] = return []
--evalList strat (x:xs) = do
--    x' <- strat x
--    xs' <- evalList strat xs
--    return (x':xs')[

-------------------------------------------------------------
-- Used to create a keyword using that will apply a strategy.
-------------------------------------------------------------

addFunc :: Int -> Int
addFunc x = x + 2

func :: [Int] -> [Int]
func [] = []
func a =
    map addFunc a `using` parList rseq

main = print (func [1,2,3])
