$(document).ready(function() {
					var vh = $(window).height();

					/*
					 * var coutTollbar =
					 * $('.fullHeight').find('.toolbar').length;
					 * 
					 * if ($('.fullHeight').find('.toolbar').length > 0) {
					 * coutTollbar--; mvh -= 19 * coutTollbar; }
					 */
					var tbH = vh - 300;
					var treetbH = vh - 300;
					var treeH = vh - 300;
					var listbH = vh - 300;

					/* TABLE */

					if ($('.fullHeight').find('.ui-datatable-scrollable-body').length > 0) {
						if ($('.fullHeight').find('.ui-paginator-bottom').length > 0) {
							tbH -= 28;
						}
						if ($('.fullHeight').find(
								'.ui-datatable-scrollable-header-box').length > 0) {
							tbH -= 32;
						}
						if ($('.fullHeight').find(
						'.ui-column-filter').length > 0) {
							tbH -= 22;
						}
						
						$('.fullHeight').find('.ui-datatable-scrollable-body')
								.css({
									"height" : +tbH + "px"
								});
					}
					/* TREE TABLE */
					if ($('.fullHeight').find('.ui-treetable-scrollable-body').length > 0) {
						$('.fullHeight').find('.ui-treetable-scrollable-body')
								.css({
									"height" : +treetbH + "px"
								});
					}
					/* TREE */

					if ($('.fullHeight').find('.ui-tree').length > 0) {
						treeH +=  22;
						$('.fullHeight').find('.ui-tree').css({
							"height" : +treeH + "px"
						});
					}
					/* SELECT LIST BOX */

					if ($('.fullHeight')
							.find('.ui-selectlistbox-listcontainer').length > 0) {
						$(".fullHeight")
								.find(".ui-selectlistbox-listcontainer").css({
									"height" : +listbH + "px"
								});
					}
				});

$(window).resize(function() {
					var vh = $(window).height();

					/*
					 * var coutTollbar =
					 * $('.fullHeight').find('.toolbar').length;
					 * 
					 * if ($('.fullHeight').find('.toolbar').length > 0) {
					 * coutTollbar--; mvh -= 19 * coutTollbar; }
					 */
					var tbH = vh - 196;
					var treetbH = vh - 196;
					var treeH = vh - 196;
					var mlistbH = vh - 196;

					/* TABLE */

					if ($('.fullHeight').find('.ui-datatable-scrollable-body').length > 0) {
						if ($('.fullHeight').find('.ui-paginator-bottom').length > 0) {
							tbH -= 28;
						}
						if ($('.fullHeight').find(
								'.ui-datatable-scrollable-header-box').length > 0) {
							tbH -= 32;
						}
						$('.fullHeight').find('.ui-datatable-scrollable-body')
								.css({
									"height" : +tbH + "px"
								});
					}
					/* TREE TABLE */
					if ($('.haflHeight').find('.ui-treetable-scrollable-body').length > 0) {
						$('.fullHeight').find('.ui-treetable-scrollable-body')
								.css({
									"height" : +treetbH + "px"
								});
					}
					/* TREE */

					if ($('.fullHeight').find('.ui-tree').length > 0) {
						$('.fullHeight').find('.ui-tree').css({
							"height" : +treeH + "px"
						});
					}
					/* SELECT LIST BOX */

					if ($('.fullHeight')
							.find('.ui-selectlistbox-listcontainer').length > 0) {
						$(".fullHeight")
								.find(".ui-selectlistbox-listcontainer").css({
									"height" : +listbH + "px"
								});
					}
				});
