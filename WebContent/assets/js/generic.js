function direcionarPaginaErro(cod_error, name_error, description_error) {
	window.location = 'pages/errors/error.html?ce=' + cod_error + '&ne='
			+ name_error + '&de=' + description_error;
}

function limpaUrl() {
	urlpg = $(location).attr('href');
	console.log(urlpg);
	urllimpa = urlpg.split("pages/")[0]
	window.history.replaceState(null, null, urllimpa);
}

function getQueryString() {
	return decodeURIComponent(window.location.search.slice(1)).split('&')
			.reduce(function _reduce(/* Object */a, /* String */b) {
				b = b.split('=');
				a[b[0]] = b[1];
				return a;
			}, {});
}