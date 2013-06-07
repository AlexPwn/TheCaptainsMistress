;;http://www.java-online.ch/gamegrid/gamegridEnglish/index.php?inhalt_links=navigation.inc.php&inhalt_mitte=gittergames/fourInARow.inc.php
(ns tictactoe.model
  (:require [noir.session :as session]))

(def empty-board [[\- \- \- \- \- \- \- \-]
                  [\- \- \- \- \- \- \- \-]
                  [\- \- \- \- \- \- \- \-]
                  [\- \- \- \- \- \- \- \-]
                  [\- \- \- \- \- \- \- \-]])

(def init-state {:board empty-board :player \X})

(defn reset-game! []
  (do (println "Reset-game called")
  (session/put! :game-state init-state)))

(defn get-board []
  (:board (session/get :game-state)))

(defn get-board-cell 
  ([row col]
    (get-board-cell (get-board) row col))
  ([board row col]
      (get-in board [row col])))

(defn get-player []
  (:player (session/get :game-state)))

(defn other-player 
  ([] (other-player (get-player)))
  ([player] (if (= player \X) \O \X))) 


(defn check-columns [board player]
  (let [[e1 e2 e3 e4 e5 e6 e7 e8] board] 
    (or (and (= e1 player) (= e2 player) (= e3 player) (= e4 player) )
             (and (= e2 player) (= e3 player) (= e4 player) (= e5 player) )
             (and (= e3 player) (= e4 player) (= e5 player) (= e6 player) )
             (and (= e4 player) (= e5 player) (= e6 player) (= e7 player) )
             (and (= e5 player) (= e6 player) (= e7 player) (= e8 player) ))
  )
)
;;(check-columns ["X""X""X""O""X""X""X""X"] "X")
(defn check-nested-columns [board player]  
  (or (check-columns (get-in board [0]) player)
       (check-columns (get-in board [1]) player)
       (check-columns (get-in board [2]) player)
       (check-columns (get-in board [3]) player)
       (check-columns (get-in board [4]) player))
  )
(check-nested-columns [["X""X""X""O""X""O""X""X"] ["X""X""O""X""X""O""X""X"]] "X")

(defn winner-in-rows? [board player]
  (do (println "Winner-in-rows? called " "\nBoard: " board "\nPlayer: " player)
  (boolean (check-nested-columns board player))))


;(defn winner-in-rows? [board player]
;  (do (println "Winner-in-rows? called " "\nBoard: " board "\nPlayer: " player)
;  (boolean (some (fn [row] (every? (fn [x] (= x player)) row)) board))))

(defn transposed-board [board]
  (vec (apply map vector board)))

(defn winner-in-cols? [board player]
  (winner-in-rows? (transposed-board board) player))

(defn winner-in-diagonals? [board player]
  (let [diag-coords [[[0 0] [1 1] [2 2]]
                     [[0 2] [1 1] [2 0]]]]
    (boolean (some (fn [coords] 
                     (every? (fn [coord] 
                               (= player (apply get-board-cell board coord))) 
                             coords))
                   diag-coords))))

(defn winner?
  "checks if there is a winner. when called with no args, checks for player X and player O.
returns the character for the winning player, nil if there is no winner"
  ([] (winner? (get-board)))
  ([board]
    (boolean (or (winner? board \X)
                 (winner? board \O))))
  ([board player]
    (if (or (winner-in-rows? board player)
            (winner-in-cols? board player)
            (winner-in-diagonals? board player))
      player)))

(defn full-board?
  ([] (full-board? (get-board)))
  ([board] (let [all-cells (apply concat board)]
             (not-any? #(= % \-) all-cells))))

(defn new-state [row col old-state]
  (if (and (= (get-board-cell (:board old-state) row col) \-)
           (not (winner? (:board old-state))))
    {:board (assoc-in (:board old-state) [row col] (:player old-state))
     :player (other-player (:player old-state))}
    old-state))

(defn play! [row col]
   ;;(do (set-coordinates row col)
  (session/swap! (fn [session-map]
                   (assoc session-map :game-state 
                          (new-state row col (:game-state session-map))))))