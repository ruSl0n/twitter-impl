(ns app.containers.root.views
	(:require [reagent.core :as r]
			  [re-frame.core :refer [subscribe dispatch]]
			  [secretary.core :as secretary
			   :include-macros    true]

			  [app.containers.root.style :as css]
			  [app.containers.news-feed.views :refer [news-feed-container]]
			  [app.containers.details.views :refer [details-container]]
			  [app.components.menu-list :refer [menu-list-component]]
			  [app.containers.root.db]
			  [app.containers.root.events]
			  [app.containers.root.subs]
			  [app.containers.pop-up.views :refer [pop-up-container]]
			  [app.components.spinner :refer [spiner-component]]))

(def menu-list [{:href "/logout" :title "Logout"}])

(def nav-list [{:href "/" :title "My profile" :name "root"}
			   {:href "/news" :title "News" :name "news"}
			   {:href "/search" :title "Search" :name "search"}])

(defn root-header-nav-comp [links]
	(let [route-name @(subscribe [:current-route-name])]
		[:div.root-header-nav
		 (css/root-header-nav)
		 [:ul
		  (for [link links]
			  [:li
			   {:key   (link :href)
				:class (when (= route-name (:name link)) "is-active")}
			   [:a {:href (link :href)}
				(link :title)]])]]))



(defn root-container [page]
	(dispatch [:get-user-data])
	(fn []
		(let [not-ready? (subscribe [:loading-user?])
			  pop-up-sub @(subscribe [:pop-up-sub])]
			(if-not @not-ready?
				(spiner-component)
				[:div.root
				 (css/root)
				 [:header.root-header
				  (css/root-header)
				  [:div
				   {:class "root-header--info"}
				   [:section.root-header--item [:p "News feed"]]
				   [:section.root-header--item
					[menu-list-component menu-list]]]
				  (root-header-nav-comp nav-list)]
				 [:main {:class "root-content"}
				  [@page]
				  (when
					  (not= (:comp pop-up-sub) nil)
					  (pop-up-container
					   (:comp pop-up-sub)
					   (:title pop-up-sub)))]]))))


