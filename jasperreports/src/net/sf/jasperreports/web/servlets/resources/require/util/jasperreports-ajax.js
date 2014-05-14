define(["jquery"], function($) {
	return {
		load: function(url, params) {
			return $.ajax(url, params);
		}
    };
});
