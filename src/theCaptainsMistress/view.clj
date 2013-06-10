(ns theCaptainsMistress.view
  (:use hiccup.form
        [hiccup.def :only [defhtml]]
        [hiccup.element :only [link-to]]
        [hiccup.page :only [html5 include-css include-js]])
  (:require [theCaptainsMistress.model :as model]))


(defhtml layout [& content]
  (html5
   [:head
    [:title "The Captain's Mistress"]
    (include-css "/css/tictactoe.css")
    (include-js "/js/lib.js")]
   [:body [:div#holder [:div#title [:h1]] [:div#placeholders] [:div#wrapper content]]
    
    ]))

(defn cell-html [rownum colnum cell with-submit?] 
  [:td { :class (str "col" colnum) }
   [:input {:name (str "b" rownum colnum) 
            :value ""
            :class (if (= (str cell) "-") (str "token") (str "token" (str cell)))
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
     [:p "Captain " (if (= (str (model/get-player)) "O") (str "RED") (str "YELLOW")) ", it is your time to shine!" ]
     (board-html (model/get-board) true)))

(defn winner-screen [winner]
  (layout
    [:div 
   [:p "The winner is: " (if (= (str (model/other-player)) "O") (str "RED") (str "YELLOW"))]
   (board-html (model/get-board) false)
   (link-to "/" "Reset")]))

(defn draw-screen []
  (layout
    [:div
     [:p "It's a draw!"]
     (board-html (model/get-board) false)
     (link-to "/" "Reset")]))