(ns tictactoe.view
  (:use hiccup.form
        [hiccup.def :only [defhtml]]
        [hiccup.element :only [link-to]]
        [hiccup.page :only [html5 include-css]])
  (:require [tictactoe.model :as model]))

(defhtml layout [& content]
  (html5
   [:head
    [:title "The Captain's Mistress"]
    (include-css "/css/tictactoe.css")]
   [:body [:div#holder [:div#title [:h1]] [:div#wrapper content]]
    
    ]))

(defn cell-html [rownum colnum cell with-submit?] 
  [:td 
   [:input {:name (str "b" rownum colnum) 
            :value (str cell)
            :type (if with-submit? 
                    "submit" 
                    "button")}]])
  
(defn row-html [rownum row with-submit?]
  [:tr (map-indexed (fn [colnum cell]
                      (cell-html rownum colnum cell with-submit?))
                    row)])
  
(defn board-html [board with-submit?]
  (form-to [:post "/"]
           [:table 
            (map-indexed (fn [rownum row]
                           (row-html rownum row with-submit?)) 
                         board)]))

(defn play-screen []
  (layout
    [:div 
     [:p "Captain " (model/get-player) ", it is your time to shine!"]
     (board-html (model/get-board) true)]))

(defn winner-screen [winner]
  (layout
    [:div 
   [:p "The winner is: " (model/other-player)]
   (board-html (model/get-board) false)
   (link-to "/" "Reset")]))

(defn draw-screen []
  (layout
    [:div
     [:p "It's a draw!"]
     (board-html (model/get-board) false)
     (link-to "/" "Reset")]))
  
