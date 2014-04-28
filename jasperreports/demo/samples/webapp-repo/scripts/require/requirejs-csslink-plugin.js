/**
 * requirejs plugin to put css link tags inside the head element
 */
define({
    load: function (name, require, onload, config) {
        var link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = require.toUrl(name);

        // FIXME: probably webfonts could be better identified
        if (link.href.indexOf('font') != 0) {
            link.className = 'jrWebFont';
        }

        document.getElementsByTagName("head")[0].appendChild(link);

        onload();
        return;
    }
});