$(document).ready(function() {
	var vw = $(window).width()-13;
	var vh = $(window).height();

	var menuWidth = $("#layout-menubar").width();
	var scrollWidth = vw - menuWidth;
	var scrollHeight = vh - 230;

	if ($('.tableSize').find('.ui-datatable').length > 0) {
		$('.tableSize').find('.ui-datatable').css({
			"width" : +scrollWidth + "px"
		});
		$('.tableSize').find('.ui-datatable').css({
			"height" : +scrollHeight + "px"
		});
	}
	/*if ($('.tableSize').find('.ui-datatable-resizable').length > 0) {
		$( ".ui-datatable" ).removeClass( "ui-datatable-resizable" )
	}*/
	
});

$(window).resize(function() {
	var vw = $(window).width()-13;
	var vh = $(window).height();

	var menuWidth = $("#layout-menubar").width();
	var scrollWidth = vw - menuWidth;
	var scrollHeight = vh - 230;

	if ($('.tableSize').find('.ui-datatable').length > 0) {
		$('.tableSize').find('.ui-datatable').css({
			"width" : +scrollWidth + "px"
		});
	/*	$('.tableSize').find('.ui-datatable').css({
			"height" : +scrollHeight + "px"
		});
	*/}
});
