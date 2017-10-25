function adjust_footer(){
	$('.push').height(0);
	$('.content-inner').css('height', 'auto');
	$('.side-navbar').css('height', 'auto');
	$('.content-inner').height($(document).height() - $('.header').height());
	var footerHeight = $('.content-inner').height() - $('.push').position().top;
	if(footerHeight < 0)
	{
		$('.content-inner').height($('.push').position().top);
		footerHeight = 0;
	}
	$('.push').height(footerHeight);
}	


$(document).ready(function(){
	adjust_footer();
});

$(window).resize(function() {
		adjust_footer();
});