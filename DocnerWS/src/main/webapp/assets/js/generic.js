function limpaUrl(rootPage) {
	urlpg = $(location).attr('href');
	if (rootPage) {
		split = urlpg.split(new RegExp("/"));
		urllimpa = "http://" + split[2].split("/")[0];
	} else {
		urllimpa = urlpg.split("pages/")[0]
	}
	window.history.replaceState(null, null, urllimpa);
}

function getQueryString(isLogin) {
	return decodeURIComponent(window.location.search.slice(1)).split('&')
			.reduce(function _reduce(/* Object */a, /* String */b) {
				b = b.split('=');
				if (isLogin) {
					if (b[0] == "error") {
						b[1] = "usuario ou senha invalidos!";
					}
				}
				a[b[0]] = b[1];
				return a;
			}, {});
}