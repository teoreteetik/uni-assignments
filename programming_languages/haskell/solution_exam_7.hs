-- 1.
main =
  let
    tulemus = splitAt 189 (concat (replicate 100 [True, False, False, False]))
  in
  putStrLn ("Nõutud listipaar on " ++ show tulemus ++ ".")

-- 2.
edastaUjukoma :: IO Double
edastaUjukoma =
  let
    onArv x = x `elem` ['0'..'9']
    parsi (x : xs)
      | onArv x = read [x]
    parsi (x1 : x2 : xs)
      | x1 == '.' && onArv x2 = read ("0." ++ [x2])
    parsi _ = 0
  in
  do
    rida <- getLine
    return (parsi rida)

-- 3.
lonka :: (Num a) => a -> [a]
lonka n =
  let
    kumbTehe kasLiita = if kasLiita then (+1) else (*2)
    abi jooksev kasLiita =
      jooksev : abi ((kumbTehe kasLiita) jooksev) (not kasLiita)
  in
  abi n True

-- 4.
posTulList :: (Num a, Ord a) => (b -> c -> a) -> [b] -> [c] -> [a]
posTulList f list1 list2 =
  [tulemus | x1 <- list1, x2 <- list2, let tulemus = f x1 x2, tulemus >= 0]

-- 5.
data KahendKolmend a =
    Vahetipp (KahendKolmend a) (KahendKolmend a)
  | Leht (a, a, a)

korrutis :: (Num a) => KahendKolmend a -> a
korrutis puu =
  case puu of
    Vahetipp vasak parem -> korrutis vasak * korrutis parem
    Leht (a, b, c) -> a * b * c
