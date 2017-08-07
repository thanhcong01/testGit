
			function showVertical() {

				if ($('#outpanel').css('display') == 'block') {
					$("#outpanel").css("display", "none");
				} else {
					$("#outpanel").css("display", "block");
					$("#outpanel").css("z-index", "9999999");
				}
			}

			$(function() {
				$(".AVSearch").click(function() {
					$(".oplAdvanceSearch").toggleClass("showAVS");
					$(".oplHandSearch").toggleClass("hideTS");
				});
			});
