(ns mastermind.core-test
  (:require [clojure.test :refer :all]
            [mastermind.core :refer :all]))

(deftest grade-guess-test
  (testing "grade-guess fail."
    (is (= (grade-guess [0 1 2] [0 0 0]) {:bulls 1 :cows 0}))
    (is (= (grade-guess [0 1 2] [0 1 0]) {:bulls 2 :cows 0}))
    (is (= (grade-guess [0 1 2] [0 1 1]) {:bulls 2 :cows 0}))
    (is (= (grade-guess [0 1 2] [0 1 2]) {:bulls 3 :cows 0}))
    (is (= (grade-guess [0 1 2] [0 2 1]) {:bulls 1 :cows 2}))
    (is (= (grade-guess [1 0 1] [1 1 1]) {:bulls 2 :cows 0}))
    (is (= (grade-guess [1 0 1] [0 1 2]) {:bulls 0 :cows 2}))
    (is (= (grade-guess [0 0] [0 0]) {:bulls 2 :cows 0}))
    (is (= (grade-guess [0 1] [0 0]) {:bulls 1 :cows 0}))
    (is (= (grade-guess [1 0] [0 0]) {:bulls 1 :cows 0}))
    (is (= (grade-guess [1 1] [0 0]) {:bulls 0 :cows 0}))
    ))

(deftest guess-space-test
  (testing "guess-space fail."
    (is (= (guess-space 0 3) #{[]}))
    (is (= (guess-space 1 3) #{[0] [1] [2]}))
    (is (= (guess-space 2 3) #{[0 0] [0 1] [0 2] [1 0] [1 1] [1 2] [2 0] [2 1] [2 2]}))
    (is (= (guess-space 3 2) #{[0 0 0] [0 0 1] [0 1 0] [0 1 1] [1 0 0] [1 0 1] [1 1 0] [1 1 1]}))
    ))

(deftest min-removed-test
  (testing "min-removed fail."
    (is (= (min-removed [0 0] (guess-space 2 2)) 2))
    (is (= (min-removed [0 0 0] (guess-space 3 3)) 15))
    (is (= (min-removed [0 0 0] (guess-space 3 2)) 5))
    (is (= (min-removed [2 0 1] #{[0 1 2] [1 2 0]}) 0))
    (is (= (min-removed [2 0 1] #{[0 1 2] [1 2 0] [2 0 1]}) 1))
    ))

(deftest best-guess-test
  (testing "best-guess fail."
    (is (#{[0 0] [0 1]} (best-guess #{[0 0] [0 1]} (guess-space 2 3))))
    ))
