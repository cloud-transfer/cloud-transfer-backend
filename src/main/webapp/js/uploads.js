
(function($) {
var url = 'http://cooktv.sndimg.com/webcook/sandbox/perf/topics.json';

$.ajax({
   type: 'GET',
    url: url,
    async: false,
    jsonpCallback: 'callback',
    contentType: "application/json",
    dataType: 'jsonp',
    success: function(json) {
		console.log(json.topics);
		$.each(json.topics, function(key,value){
		
			var upload =  '<!-- Upload Section !-->' +
			'<section class="dashboard-counts no-padding-bottom">' +
            '<div class="container-fluid">' +
              '<div class="row bg-white has-shadow upload-info">' +
                '<div class="col-xl-12"><span>Name: ' + this.name + '</span></div>' +
                '<div class="col-xl-3 col-sm-6"><span> Status: Uploading </span></div>' +
                '<div class="col-xl-3 col-sm-6"><span> Speed: 10 MBps </span></div>' +
                '<div class="col-xl-3 col-sm-6"><span> Completed: 120 MB  </span></div>' +
				'<div class="col-xl-3 col-sm-6"><span> Remaining: 480 MB </span></div>' + 
				'<div class="col-xl-12 col-sm-6">' +
					'<div class="progress">' +
						'<div class="progress-bar" role="progressbar" style="width: 25%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">25%</div>' +
					'</div>' +
                '</div>' +
              '</div>' + 
            '</div>' +
          '</section>';
		  
			
			$('.push').before(upload);
                        $(window).resize();
		});
	},
    error: function(e) {
       console.log(e.message);
	   window.location.replace("http://stackoverflow.com");
    }
});

})(jQuery);
