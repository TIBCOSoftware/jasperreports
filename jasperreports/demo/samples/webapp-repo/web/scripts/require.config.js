requirejs.config({
    baseUrl: '/jasperreports/scripts',
    paths: {
        "async": "require/async",
        "text": "require/text",
        "csslink": "require/requirejs-csslink-plugin",
        "jquery": "jquery/core/jquery-3.4.1.min",
        "jquery-ui": "jquery/ui",
        "jquery-ui/widgets/timepicker": "jquery/ui/jquery-ui-timepicker-addon-1.6.3.min",
        "jr.ReportExecution": "hyperlinkHandlers/jr.ReportExecution",
        "jr.Reference": "hyperlinkHandlers/jr.Reference"
    },
    map: {
        "jquery-ui/widgets/timepicker": {
            "jquery-ui": "jquery-ui/widgets/datepicker"
        }
    }
});