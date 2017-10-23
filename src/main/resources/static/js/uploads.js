function fetchUploads() {

    (function ($) {
        var tempScrollTop = $(window).scrollTop();
        $(".uploads").remove();
        var url = '/api/status';

        $.ajax(
            {
                url: url,
                async: false,

                success: function (json) {
                    $.each(json, function () {
                        var oneGB = 1024 * 1024 * 1024;
                        var oneMB = 1024 * 1024;
                        var oneKB = 1024;

                        var speed;
                        var completed;
                        var remaining = this.totalSize - this.uploadedSize;

                        if (this.speed > oneGB)
                            speed = (this.speed / oneGB).toFixed(1) + ' GBps';
                        else if (this.speed > oneMB)
                            speed = (this.speed / oneMB).toFixed(1) + ' MBps';
                        else if (this.speed > oneKB)
                            speed = (this.speed / oneKB).toFixed(1) + ' KBps';
                        else
                            speed = this.speed + " Bps";

                        if (this.uploadedSize > oneGB)
                            completed = (this.uploadedSize / oneGB).toFixed(1) + ' GB';
                        else if (this.uploadedSize > oneMB)
                            completed = (this.uploadedSize / oneMB).toFixed(1) + ' MB';
                        else if (this.uploadedSize > oneKB)
                            completed = (this.uploadedSize / oneKB).toFixed(1) + ' KB';
                        else
                            completed = this.uploadedSize + " B";

                        if (remaining > oneGB)
                            remaining = (remaining / oneGB).toFixed(1) + ' GB';
                        else if (remaining > oneMB)
                            remaining = (remaining / oneMB).toFixed(1) + ' MB';
                        else if (remaining > oneKB)
                            remaining = (remaining / oneKB).toFixed(1) + ' KB';
                        else
                            remaining = remaining + " B";

                        var completedPercentage = (this.uploadedSize / this.totalSize * 100).toFixed(1);

                        var background = '';
                        var upload;

                        if(this.uploadStatus == 'failed')
                        {
                        	background = 'bg-danger';
                        	upload = '<section class="dashboard-counts no-padding-bottom uploads">' +
                            '<div class="container-fluid">' +
                            '<div class="row bg-white has-shadow upload-info">' +
                            '<div class="col-xl-12"><span>Name: ' + this.fileName + '</span></div>' +
                            '<div class="col-xl-3 col-sm-6"><span> Status: ' + this.uploadStatus + ' </span></div>' +
                            '<div class="col-xl-9 col-sm-6"><span> Message: ' + this.errorMessage + ' </span></div>' +
                            '<div class="col-xl-12 col-sm-6">' +
                            '<div class="progress">' +
                            '<div class="progress-bar ' + background + '" role="progressbar" style="width:' + completedPercentage + '%;" aria-valuenow="' + completedPercentage + '" aria-valuemin="0" aria-valuemax="100">' + completedPercentage + '%</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</section>';
                        }
                        else
                        {
                            if (completedPercentage == 100)
                                background = 'bg-success';

                        	upload = '<section class="dashboard-counts no-padding-bottom uploads">' +
                            '<div class="container-fluid">' +
                            '<div class="row bg-white has-shadow upload-info">' +
                            '<div class="col-xl-12"><span>Name: ' + this.fileName + '</span></div>' +
                            '<div class="col-xl-3 col-sm-6"><span> Status: ' + this.uploadStatus + ' </span></div>' +
                            '<div class="col-xl-3 col-sm-6"><span> Speed: ' + speed + ' </span></div>' +
                            '<div class="col-xl-3 col-sm-6"><span> Completed: ' + completed + ' </span></div>' +
                            '<div class="col-xl-3 col-sm-6"><span> Remaining: ' + remaining + '</span></div>' +
                            '<div class="col-xl-12 col-sm-6">' +
                            '<div class="progress">' +
                            '<div class="progress-bar ' + background + '" role="progressbar" style="width:' + completedPercentage + '%;" aria-valuenow="' + completedPercentage + '" aria-valuemin="0" aria-valuemax="100">' + completedPercentage + '%</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</section>';


                        }	
                        
                        $('.push').before(upload);
                    });
                },
                error: function (xhr) {
                    alert("An error occured: " + xhr.status + " " + xhr.statusText);
                }
            });
        $(window).scrollTop(tempScrollTop);
    })(jQuery);

}


jQuery(document).ready(function ($) {
    $(function () {
        setInterval(function () {
            fetchUploads();
        }, 1000);
    });
});