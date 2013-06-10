(ns theCaptainsMistress.alternative
;;Current X & Y coordinates of clicked button
(def currentX (atom 0))
(def currentY (atom 0))

(defn set-x [col]
  (do (println "COL: " col)
    (reset! currentX col)))
(defn set-y [row]
  (do (println "ROW: " row)
    (reset! currentY row)))

(defn set-coordinates [row col]
  (do (set-x col)
    (set-y row)
  ))

(def currentX (atom 0))

(is-legit? ["X""X""X""O""X""X""O""O"] @currentX)
(defn is-legit? [board currentx]
  (if (=(nth board (+ currentx 3))"X") "YES" "NO")
  )

(def currentX (atom 2))
(if (> @currentX 1)
  (if (=(nth board (- currentX 1))"X") "YES" "NO") 
  "ANDERS DEZE")

(and (=(nth board (- currentX 1))"X") 
     (=(nth board (- currentX 2))"X")
     (=(nth board (+ currentX 1))"X") 
     (=(nth board currentX 1)"X")) 
  

(for [x (range 0 5)] 
    (check-columns (get-in board [x]) player)))

  )