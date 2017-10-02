var frm = $('#uploadform');

frm.submit(function (e) {
    e.preventDefault();
    var url = $("#url").val();
    var filename = $("#filename").val();
    var dataString = 'url=' + encodeURIComponent(url) + '&filename=' + encodeURIComponent(filename);
    if (url == '') {
        alert("Please Enter Url");
    }
    else {
        $.ajax({
            type: frm.attr('method'),
            url: frm.attr('action'),
            data: frm.serialize(),
            cache: false,
            success: function () {
                frm.trigger("reset");
                $("#submit_message").remove();
                frm.append('<p id="submit_message" style="font-size: large; color: green">  <i class="fa fa-check" aria-hidden="true"></i> Successfully Submitted! </p>')
            },
            error: function (data) {
                $("#submit_message").remove();
                console.log(data.responseText);
                var json = $.parseJSON(data.responseText);
                var message = json["message"];

                if (json["statusCode"] / 100 == 5)
                    message = (message == null) ? "There is some problem at server side. Please contact developers." : message;

                frm.append('<p id="submit_message" style="font-size: large; color: red"> Error: ' + message + '</p>')
                console.log(json);
            }
        });
    }
    return false;
});
