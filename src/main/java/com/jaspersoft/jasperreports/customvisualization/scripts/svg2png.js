var webpage = require('webpage')
        , URL, FILENAME, W, H;
;

if (phantom.args.length < 2 || phantom.args.length > 5) {
    console.log('Usage: svg2png source.svg output.png [width||?] [height||?]');
    phantom.exit();
}
else {
    URL = phantom.args[0];
    FILENAME = phantom.args[1];
    W = Number(phantom.args[2]);
    H = Number(phantom.args[3]);
    if (isNaN(W))
        W = undefined;
    if (isNaN(H))
        H = undefined;
    peek(URL, grabSize);
}

function peek(url, fn) {
    var page = webpage.create();
    page.onConsoleMessage = function(msg) {
        console.log(msg);
    };
    page.open(url, function(status) {
        if (status !== 'success')
            console.log('Unable to load ' + url);
        else
            fn(page);
    });
}

function grabSize(page) {
    page.onConsoleMessage = function(msg) {
        var d = JSON.parse(msg);
        if (!W && !H) {
            W = d.w;
            H = d.h;
        }
        if (W && !H) {
            H = W * d.h / d.w;
        }
        if (!W && H) {
            W = H * d.w / d.h;
        }
        screenshot(URL, FILENAME, W, H);
    };

    page.evaluate(function() {
        var svg = document.documentElement
                , w = svg.getAttribute('width')
                , h = svg.getAttribute('height');
        
        console.log(JSON.stringify({w: Number(w), h: Number(h)}));
    });
}

function screenshot(url, filename, w, h) {
    var page = webpage.create();
    page.viewportSize = {width: w, height: h};
    page.open(url, function(status) {
        if (status !== 'success') {
            console.log('Unable to load ' + url);
            phantom.exit();
        }
        else {
            window.setTimeout(function() {
                page.zoomFactor = 4.0;
                page.render(filename);
                console.log("SCRIPT_SUCCESS");
                phantom.exit();
            }, 200);
        }
    });
}
