define(["jasperreports-loader", "jasperreports-report", "jquery/amd/jquery-1.7.1"], function(jr, Report, $) {
	return {
		load: function(objOptions) {
			var settings = {
					reporturl: null,
					serializedParams: "",
					
					width: 300,
					height: 400,
					containerid: 'jivecontainer',
					toolbar: true,
					async: true,
			};
			
			$.extend(settings, objOptions);
			var reportParams = $.parseJSON(settings.serializedParams);
            $.ajax(settings.reporturl, {
                type: 'POST',
                data: reportParams,
                success: function(data, textStatus, jqXHR) {
                    reportParams.url = settings.reporturl;
                    reportParams.html = data;
                    var ReportInstance = new Report(reportParams);   
                    ReportInstance.goToPage({requestedPage: 2});
                }
            }); 
		}
	};
});
