
(ns server-sent-events.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as http]
            [server-sent-events.service :as service]))

(def service
  (::http/service-fn (http/create-servlet service/service)))

(deftest leibniz-reductions-first-term-not-nulltest
  (is (not= (take 1 leibniz-reductions) '()))
)

(deftest leibniz-reductions-first-term-test
  (is (= (take 1 leibniz-reductions) '(4.0)))
)

(deftest leibniz-reductions-first-ten-terms-test
  (is (= (take 10 leibniz-reductions)
    '(
      4.0
      2.666666666666667
      3.466666666666667
      2.8952380952380956
      3.3396825396825403
      2.9760461760461765
      3.2837384837384844
      3.017071817071818
      3.2523659347188767
      3.0418396189294032
      )
    )
  )
)

(deftest leibniz-node-first-term
  (is (= (leibniz-node 0) 4.0))
)

(deftest leibniz-node-second-term
  (is (= (leibniz-node 0) (/ -4.0 3)))
)

(deftest euler-reductions-first-term-test
  (is
    (= (take 1 euler-reductions) '(2.449489742783178))
  )
)

(deftest euler-reductions-first-ten-terms-test
  (is
    (= (take 10 euler-reductions)
      '(
        2.449489742783178
        2.7386127875258306
        2.857738033247041
        2.92261298612503
        2.9633877010385707
        2.991376494748418
        3.011773947846214
        3.027297856657843
        3.0395075895610533
        3.04936163598207
      )
    )
  )
)

(deftest euler-node-first-term-not-mapped
  (is (not= (euler-node 0) 2.449489742783178))
)

(deftest euler-node-first-term
  (is (= (euler-node 0) 6.0))
)

(deftest service-boots-up-test
  (is (.contains
       (:body (response-for service :get "/test-response"))
       "Server Sent Service - Test response")))
