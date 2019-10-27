(ns mastermind.core
  (:require [clojure.set :refer :all])
  (:gen-class))

(defn grade-guess [guess answer]
  (let [bulls (count (filter identity (map = guess answer)))
        hits (for [letter (set answer)] (min (count (filter #(= letter %) guess))
                                             (count (filter #(= letter %) answer))))
        cows (- (apply + hits) bulls)]
    {:bulls bulls
     :cows cows}))

(defn guess-space
  "Returns all possible guesses of a length within an alphabet of a given number of letters."
  ([length letters] (guess-space length letters #{[]}))
  ([length letters prefaces]
   (if (= 0 length)
     prefaces
     (let [new-prefaces (set (for [letter (range letters)
                                   preface prefaces]
                               (conj preface letter)))]
       (recur (dec length) letters new-prefaces)))))

(defn min-removed [guess guesses-remaining]
  "Given a list of remaining guesses, how many minimum answers are removed by a grade of this guess."
  ; TODO should work for game where cows are omitted
  ;
  ; get a num removed for each possible grade
  ; return the minimum
  (let [possible-grades (filter #(<= (+ (:bulls %) (:cows %)) (count guess))
                                (for [bulls (range (inc (count guess)))
                                      cows (range (inc (count guess)))]
                                  {:bulls bulls :cows cows})) ; way too broad, work on this
        remains (for [grade possible-grades]
                  (filter #(= (grade-guess % guess) grade) guesses-remaining))
        remove-counts (map #(- (count guesses-remaining) (count %)) remains)
        ;dummy (println (str "remove-counts for guess=" guess " " (into [] remove-counts)))
        ]
    (apply min remove-counts)))

(defn best-guess [guesses-remaining space]
  "Given a list of remaining guesses and the guess space, find the best possible next guess."
  ; should also accept a guess space that has been pre-filtered (say, if we know there are no repeats)
  (let [scored (for [guess space] {:guess guess :score (min-removed guess guesses-remaining)})]
    (:guess (apply max-key :score scored))))


(defn play-mastermind [guesses-remaining space]
  "The computer guesses, player scores."
  (let [guess (best-guess guesses-remaining space)
        prompt (println (str "Computer guesses:  " guess ".\n"
                             "How many bulls?"))
        bulls (read-string (read-line))
        prompt (println (str "How many cows?"))
        cows (read-string (read-line))
        grade {:bulls bulls :cows cows}
        filtered (if (< cows 0)
                   ; no cows given strongly suboptimal because best guess chosen assuming cows will be given
                   (apply concat (for [x (range (count (first space)))] (filter #(= {:bulls bulls :cows x} (grade-guess guess %)) guesses-remaining)))
                   (filter #(= grade (grade-guess guess %)) guesses-remaining))
        prompt (println (str (count filtered) " possibilities remain."))
        prompt (println (str (into [] filtered)))]
    (if (not= 1 (count filtered))
      (recur filtered space))))


(defn -main
  "Get the size of the space and play."
  [& args]
  (let [space (guess-space (read-string (first args)) (read-string (nth args 1)))]
    (play-mastermind space space)))
