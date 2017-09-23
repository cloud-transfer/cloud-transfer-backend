$(document).ready(function(){
	$(window).resize(function(){
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
                
               
		//console.log($('.content-inner').height());
		//console.log($('.push').position().top);
		//console.log(footerHeight);
		$('.push').height(footerHeight);
	});
	$(window).resize();
});
