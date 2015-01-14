
--1.
main =
  let
    jupp = ['A'..'Z'] ++ ['Y', 'X'..'B']
    joru = cycle jupp
    tulemus = take (20 * length jupp) joru ++ ['A']
  in
  putStrLn ("Nõutud sõne on " ++ show tulemus ++ ".")

main2 =
  let
    pikkus = 20 * (2 * length ['A'..'Z'] - 2) + 1
    abi (x : xs)
      | x == 'Z' = x : abi ['Y', 'X'..'A']
      | x == 'A' = x : abi ['B'..'Z']
      | otherwise = x : abi xs
  in
  putStrLn ("Nõutud sõne on " ++ show (take pikkus (abi ['A'..'Z'])) ++ ".")

--2.
juhud :: [a] -> Int -> [a]
juhud _ n
  | n < 0 = error "Argument n ei tohi olla negatiivne!"
juhud [] _ = []
juhud l@(pea : xs) n =
  let
    abi ((indeks, element) : xs) =
      if (indeks == n) then
        pea : abi xs
      else
        element : abi xs
    abi _ = []
  in
  case lookup n (zip [0..] l) of
    Just a -> a : abi (zip [1..] xs)
    _ -> l

--3.
mang :: [Int] -> IO Bool
mang (x : xs) =
  do
    putStrLn (show x)
    rida <- getLine
    if length rida == x then
      mang xs
    else
      return False
mang _ =
  do
    return True

--4.
--tabel :: (Num a, Ord a) => [a] -> a -> [[a]]
--tabel l m = wtf?

--5.
data UusKahend d =
    Vahetipp (Int, d) (UusKahend d) (UusKahend d)
  | Leht (Int, Int)
  deriving (Show)

satiKorgus :: UusKahend a -> UusKahend a
satiKorgus puu =
  let
    leiaMax (Leht _) (Leht _) = 0
    leiaMax (Vahetipp (vasakKorgus, _) _ _) (Vahetipp (paremKorgus, _) _ _) = max vasakKorgus paremKorgus
    leiaMax (Vahetipp (vasakKorgus, _) _ _) _ = vasakKorgus
    leiaMax _ (Vahetipp (paremKorgus, _) _ _) = paremKorgus
  in
  case puu of
    Leht (a, b) ->
      Leht (0, 0)
    Vahetipp (a, x) vasak parem ->
      let
        uusVasak = satiKorgus vasak
        uusParem = satiKorgus parem
      in
      Vahetipp (leiaMax uusVasak uusParem + 1, x) uusVasak uusParem