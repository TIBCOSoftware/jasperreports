requirejs.config({
    paths: {
        "async": "scripts/require/async",
        "text": "scripts/require/text",
        "csslink": "scripts/require/requirejs-csslink-plugin",
        "jquery": "scripts/jquery/core/jquery-3.3.1.min",
        "jquery-ui": "scripts/jquery/ui",
        "jquery-ui/widgets/timepicker": "scripts/jquery/ui/jquery-ui-timepicker-addon-1.6.3.min",
        "jr.ReportExecution": "scripts/hyperlinkHandlers/jr.ReportExecution",
        "jr.Reference": "scripts/hyperlinkHandlers/jr.Reference"
    },
    map: {
        "jquery-ui/widgets/timepicker": {
            "jquery-ui": "jquery-ui/widgets/datepicker"
        }
    }
});