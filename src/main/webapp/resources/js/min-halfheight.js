$(document).ready(
				function() {
					var vh = $(window).height() - 1;
					var mvh = vh / 2;
					var coutTollbar = $('.halfHeight').find('.toolbar').length;

					if ($('.halfHeight').find('.toolbar').length > 0) {
						coutTollbar--;
						mvh -= 19 * coutTollbar;
					}
					var mtbH = mvh - 98;
					var mtreetbH = mvh - 98;
					var mtreeH = mvh - 98;
					var mlistbH = mvh - 98;

					/* TABLE */

					if ($('.halfHeight').find('.ui-datatable-scrollable-body').length > 0) {
						if ($('.halfHeight').find('.ui-paginator-bottom').length > 0) {
							mtbH -= 28;
						}
						if ($('.halfHeight').find(
								'.ui-datatable-scrollable-header-box').length > 0) {
							mtbH -= 32;
						}
						$('.halfHeight').find('.ui-datatable-scrollable-body')
								.css({
									"height" : +mtbH + "px"
								});
					}
					/* TREE TABLE */
					if ($('.haflHeight').find('.ui-treetable-scrollable-body').length > 0) {
						$('.halfHeight').find('.ui-treetable-scrollable-body')
								.css({
									"height" : +mtreetbH + "px"
								});
					}
					/* TREE */

					if ($('.halfHeight').find('.ui-tree').length > 0) {
						$('.halfHeight').find('.ui-tree').css({
							"height" : +mtreeH + "px"
						});
					}
					/* SELECT LIST BOX */

					if ($('.halfHeight')
							.find('.ui-selectlistbox-listcontainer').length > 0) {
						$(".halfHeight")
								.find(".ui-selectlistbox-listcontainer").css({
									"height" : +mlistbH + "px"
								});
					}
				});

$(window).resize(
				function() {
					var vh = $(window).height() - 1;
					var mvh = vh / 2;
					var coutTollbar = $('.halfHeight').find('.toolbar').length;

					if ($('.halfHeight').find('.toolbar').length > 0) {
						coutTollbar--;
						mvh -= 19 * coutTollbar;
					}
					var mtbH = mvh - 98;
					var mtreetbH = mvh - 98;
					var mtreeH = mvh - 98;
					var mlistbH = mvh - 98;

					/* TABLE */

					if ($('.halfHeight').find('.ui-datatable-scrollable-body').length > 0) {
						if ($('.halfHeight').find('.ui-paginator-bottom').length > 0) {
							mtbH -= 28;
						}
						if ($('.halfHeight').find(
								'.ui-datatable-scrollable-header-box').length > 0) {
							mtbH -= 32;
						}
						$('.halfHeight').find('.ui-datatable-scrollable-body')
								.css({
									"height" : +mtbH + "px"
								});
					}
					/* TREE TABLE */
					if ($('.haflHeight').find('.ui-treetable-scrollable-body').length > 0) {
						$('.halfHeight').find('.ui-treetable-scrollable-body')
								.css({
									"height" : +mtreetbH + "px"
								});
					}
					/* TREE */

					if ($('.halfHeight').find('.ui-tree').length > 0) {
						$('.halfHeight').find('.ui-tree').css({
							"height" : +mtreeH + "px"
						});
					}
					/* SELECT LIST BOX */

					if ($('.halfHeight')
							.find('.ui-selectlistbox-listcontainer').length > 0) {
						$(".halfHeight")
								.find(".ui-selectlistbox-listcontainer").css({
									"height" : +mlistbH + "px"
								});
					}
				});
