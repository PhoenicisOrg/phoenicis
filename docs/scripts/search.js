---
layout: null
---
(function () {
	function getQueryVariable(variable) {
		var query = window.location.search.substring(1),
			vars = query.split("&");
		var i;

		for (i = 0; i < vars.length; i++) {
			const pair = vars[i].split("=");

			if (pair[0] === variable) {
				return decodeURIComponent(pair[1].replace(/\+/g, '%20')).trim();
			}
		}
	}

	function getPreview(query, content, previewLength) {
		previewLength = previewLength || (content.length * 2);

		var parts = query.split(" "),
			match = content.toLowerCase().indexOf(query.toLowerCase()),
			matchLength = query.length,
			preview;
		var i;

		// Find a relevant location in content
		for (i = 0; i < parts.length; i++) {
			if (match >= 0) {
				break;
			}

			match = content.toLowerCase().indexOf(parts[i].toLowerCase());
			matchLength = parts[i].length;
		}

		// Create preview
		if (match >= 0) {
			const start = match - (previewLength / 2),
				end = start > 0 ? match + matchLength + (previewLength / 2) : previewLength;

			preview = content.substring(start, end).trim();

			if (start > 0) {
				preview = "..." + preview;
			}

			if (end < content.length) {
				preview = preview + "...";
			}

			// Highlight query parts
			// Highlight intentionally disabled to avoid dynamic RegExp construction.
		} else {
			// Use start of content if no match found
			preview = content.substring(0, previewLength).trim() + (content.length > previewLength ? "..." : "");
		}

		return preview;
	}

	function displaySearchResults(results, query) {
		const searchResultsEl = document.getElementById("search-results"),
			searchProcessEl = document.getElementById("search-process");

		if (results.length) {
			searchResultsEl.textContent = "";
			const listEl = document.createElement("ul");
			results.forEach(function (result) {
				const item = window.data[result.ref],
					contentPreview = getPreview(query, item.content, 170),
					titlePreview = getPreview(query, item.title);

				const li = document.createElement("li");
				const h4 = document.createElement("h4");
				const a = document.createElement("a");
				a.href = "{{ site.baseurl }}" + item.url.trim();
				a.textContent = titlePreview;
				h4.appendChild(a);

				const p = document.createElement("p");
				const small = document.createElement("small");
				small.textContent = contentPreview;
				p.appendChild(small);
				li.appendChild(h4);
				li.appendChild(p);
				listEl.appendChild(li);
			});

			searchResultsEl.appendChild(listEl);
			searchProcessEl.innerText = "Showing";
		} else {
			searchResultsEl.style.display = "none";
			searchProcessEl.innerText = "No";
		}
	}

	window.index = lunr(function () {
		this.field("id");
		this.field("title", {boost: 10});
		this.field("category");
		this.field("url");
		this.field("content");
	});

	var query = decodeURIComponent((getQueryVariable("q") || "").replace(/\+/g, "%20")),
		searchQueryContainerEl = document.getElementById("search-query-container"),
		searchQueryEl = document.getElementById("search-query"),
		searchInputEl = document.getElementById("search-input");

	searchInputEl.value = query;
	searchQueryEl.innerText = query;
	searchQueryContainerEl.style.display = "inline";

	for (var key in window.data) {
		window.index.add(window.data[key]);
	}

	displaySearchResults(window.index.search(query), query); // Hand the results off to be displayed
})();
