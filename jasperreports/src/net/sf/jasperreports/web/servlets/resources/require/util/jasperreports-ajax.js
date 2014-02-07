define(["jquery-1.10.2"], function($) {
	return {
		load: function(url, params) {
			return $.ajax(url, params);
		}
    };
});
