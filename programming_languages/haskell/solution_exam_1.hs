import Data.Char

--1.
main =
  let
    avahemik = [32..127]
    bvahemik = drop 3 (cycle avahemik)
    tulemus = zipWith (\ a b -> (chr a, chr b)) avahemik bvahemik
  in
    putStrLn ("Nõutud list on " ++ show tulemus ++ ".")


--2.
tahejama :: IO String
tahejama =
  do
    sone <- getLine
    case sone of
      esitaht : saba ->
        return (if isAsciiUpper esitaht then sone else toUpper esitaht : saba)
      _ ->
        do
          uussone <- getLine
          return uussone

--3.
eemaldaUle :: Int -> [a] -> [a]
eemaldaUle n l =
  if n <= 0 then
    error "Argument n peab olema positiivne!"
  else
    [element | (element, indeks) <- zip l [1..], indeks `mod` n /= 0]

--4.
jarjestikusteArv :: (a -> a -> Bool) -> [a] -> Int
jarjestikusteArv p l =
  let
    abi loendur (x1 : saba@ (x2 : xs)) =
      if p x1 x2 then
        abi (loendur + 1) saba
      else
        abi loendur saba
    abi loendur _ =
      loendur
  in
    abi 0 l

--5.
data OmaList d =
    SpetsiommList [(d, d, Int)]
  | TuhiList

parisList :: OmaList Int -> [Int]
parisList list =
  case list of
    SpetsiommList list -> foldr (\(a, b, c) acc -> a : b : c : acc ) [] list
    TuhiList -> []