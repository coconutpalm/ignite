(ns meta-ex.meters
  (:use quil.core
        [overtone.core :only [on-event]]
        [overtone.helpers.ref :only [swap-returning-prev!]])
  (:require [overtone.at-at :as at-at]
            [meta-ex.client]))

(defonce meters-pool (at-at/mk-pool))

(defonce red-m (atom 0))
(defonce green-m (atom 0))
(defonce blue-m (atom 0))

(defn degrade-meters []
  (doseq [r [red-m green-m blue-m]]
    (swap! r (fn [v] (- v (* v 0.01))))))

(defonce __METER_DEGREDATION__
  (at-at/every 200
               #'degrade-meters
               meters-pool
               :initial-delay 0
               :desc "Meters degredation"))

(on-event [:vote] (fn [msg]
                    (cond
                     (= "RED" (:choice msg))   (swap! red-m inc)
                     (= "GREEN" (:choice msg)) (swap! green-m inc)
                     (= "BLUE" (:choice msg))  (swap! blue-m inc)))
          ::register-votes)

(def y-val (atom 0))
(defn draw []
  (let [m-width (/ (width) 7)]
;;    (background 0)
;;    (rotate-z (swap! y-val (fn [v] (+ v 0.1))))
    (stroke-weight 1)
    (stroke 200)

    (fill 0 255 0)
    (rect  m-width 100 m-width (* 100 @green-m))

    (fill 255 0 0)
    (rect (* 2 m-width) 100 m-width (* 100 @red-m))



    (fill 0 0 255)
    (rect (* 3 m-width) 100 m-width (* 100 @blue-m))))