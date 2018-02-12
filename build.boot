(set-env!
 :dependencies '[[org.clojure/core.logic "0.8.11"]])

(require '[clojure.core.logic :as l])
(require '[clojure.string :as string])

(defn parse-input
  [input]
  (->> (string/split (string/trim input) #"\n")
       (map string/trim)
       (map #(string/split % #""))))

(defn print-output
  [output]
  (->> output
       (map #(string/join "" %))
       (string/join "\n")
       println)
  (println))

(defn unify-starting-state
  [logic-vars cells letters]
  (if (seq logic-vars)
    (let [logic-var (first logic-vars)
          cell (first cells)]
      (l/all
       (if (some #{cell} letters)
         (l/== logic-var cell)
         (l/membero logic-var letters))
       (unify-starting-state (next logic-vars) (next cells) letters)))
    l/succeed))

(l/defne not-membero [item coll]
  ([_ []])
  ([_ [?first . ?rest]]
    (l/!= item ?first)
    (not-membero item ?rest)))

(l/defne linear-contiguouso
  [coll]
  ([[_]])
  ([[cell . ?rest]]
   (l/fresh [next-cell]
     (l/firsto ?rest next-cell)
     (l/conde
      [(l/== cell next-cell)]
      [(l/!= cell next-cell) (not-membero cell ?rest)])
     (linear-contiguouso ?rest))))

(l/defne vertical-pairs-contiguouso
  [top-row bottom-row rows]
  ([[] [] _])
  ([[top . ?rest-top] [bottom . ?rest-bottom] _]
   (l/conde
    [(l/== top bottom)]
    [(l/!= top bottom)
     (not-membero top (flatten rows))])
   (vertical-pairs-contiguouso ?rest-top ?rest-bottom rows)))

(l/defne linear-pairs-contiguouso
  [row-pairs rows]
  ([[] _])
  ([[[top-row bottom-row] . ?rest-row-pairs] _]
   (vertical-pairs-contiguouso top-row bottom-row (next rows))
   (linear-pairs-contiguouso ?rest-row-pairs (next rows))))

(defn contiguouso
  [rows]
  (l/all
   (l/everyg linear-contiguouso rows)
   (let [row-pairs (partition 2 1 rows)]
     (linear-pairs-contiguouso row-pairs rows))))

(defn atomic-rectangularo
  [[northwest northeast southwest southeast]]
  (l/conde
   [(l/== northwest southeast)
    (l/== northwest northeast)
    (l/== northwest southwest)]
   [(l/!= northwest southeast)
    (l/!= northeast southwest)]))

(defn rectangularo
  [rows]
  (let [squares (mapcat (fn [[top-row bottom-row]]
                          (let [top-pairs (partition 2 1 top-row)
                                bottom-pairs (partition 2 1 bottom-row)]
                            (map concat top-pairs bottom-pairs)))
                        (partition 2 1 rows))]
    (l/everyg atomic-rectangularo squares)))

(defn alpha-cake-data
  [starting-state]
  (let [input-cells (flatten starting-state)
        letters (remove #(= % "?") input-cells)
        cells (repeatedly (count input-cells) l/lvar)
        rows (partition (count (first starting-state)) cells)]
    (l/run* [solution]
      (l/== solution rows)
      (unify-starting-state cells input-cells letters)
      (rectangularo rows)
      (contiguouso rows))))

(defn alpha-cake
  [input]
  (let [starting-state (parse-input input)
        results (alpha-cake-data starting-state)]
    (run! #(print-output %) results)
    (count results)))

(def sample1
  "G??
   ?C?
   ??J")

(def sample2
  "CODE
   ????
   ?JAM")
