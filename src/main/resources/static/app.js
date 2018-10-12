
function setCookie(name,value,days) {
	var expires = "";
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days*24*60*60*1000));
		expires = "; expires=" + date.toUTCString();
	}
	document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}
function getCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

var wordToRhyme = [];
var i = 0;

var poemRoundNumber = 0;

var firstTimeUser;

//When the page is opened
$(document).ready(function() {

	// See if its the user's first time on website, if it is, open up the 'about' tab.
	var x = getCookie('cookie');
	if (!x) {
		document.getElementById('getinfo').style.color = "white";
		document.getElementById('info-bar').innerHTML = "the poetryscript project scans from among " +
		"<a href='\https://www.gutenberg.org\'>gutenberg.org</a>'s 57,000 novels to produce randomly-generated 8-line poems that follow " + 
		"conventional sonnet standards. <b>Each sonnet is unique and will never be recreated by you or anyone else.</b>";
		$("#info-bar").slideDown("slow");
		firstTimeUser = true;
		barIsShowing = true;
		barIsShowingAbout = true;
		setCookie('cookie','cookie', 182);
	}

    //	When you click the get poem button
	$('#getPoem').on('click touchstart', function() {

		document.getElementById('getPoem').style.backgroundColor = "#eca54a";

		for (var n = 0; n < 8; n++) {
			document.getElementById('sentence' + n).innerHTML = "";
			document.getElementById('title' + n).innerHTML = "";
		}

		i = 0;
		wordToRhyme = [];
		poemRoundNumber++;

		// Trigger the spring boot controller and go to display() function with result
		$.ajax({
			url : "get-poem.html",
			data : {
				'sentence' : null,
				'poemRoundNumber' : poemRoundNumber
			},
			success : function(data) {
				addLineToTable(data);
			}
		});

	});


	var barIsShowing = false;
	var barIsShowingAbout = false;

	$('#getinfo').on('click', function() {
		document.getElementById('info-bar').innerHTML = "the poetryscript project scans from among " +
		"<a href='\https://www.gutenberg.org\'>gutenberg.org</a>'s 57,000 novels to produce randomly-generated 8-line poems that follow " + 
		"conventional sonnet standards. <b>Each sonnet is unique and will never be recreated by you or anyone else.</b>";
		if (barIsShowing) {
			if (barIsShowingAbout) {
				$("#info-bar").slideUp('slow');
				document.getElementById('getinfo').style.color = "black";
				barIsShowing = false;
				barIsShowingAbout = false;
			} else {
				document.getElementById('getinfo').style.color = "white";
				document.getElementById('getcontact').style.color = "black";
				barIsShowing = true;
				barIsShowingAbout = true;
			}
		} else {
			$("#info-bar").slideDown("slow");
			document.getElementById('getinfo').style.color = "white";
			barIsShowing = true;
			barIsShowingAbout = true;
		}
	});

	$('#getcontact').on('click', function() {
		document.getElementById('info-bar').innerHTML = "poetryscript.project@gmail.com";
		if (barIsShowing) {
			if (barIsShowingAbout) {
				document.getElementById('getcontact').style.color = "white";
				document.getElementById('getinfo').style.color = "black";
				barIsShowingAbout = false;
			} else {
				$("#info-bar").slideUp('slow');
				document.getElementById('getcontact').style.color = "black";
				barIsShowing = false;
				barIsShowingAbout = false;
			}
		} else {
			$("#info-bar").slideDown('slow');
			document.getElementById('getcontact').style.color = "white";
			barIsShowing = true;
			barIsShowingAbout = false;
		}
	});

});


//This is triggered by response from spring boot
function addLineToTable(data) {

	document.getElementById('getPoem').style.backgroundColor = "#e5e5e5";

	var json = JSON.parse(data);

	if (json.poemRoundNumber != poemRoundNumber) {
		return false;
	}

	wordToRhyme[i] = json.sentence;

	document.getElementById('title' + i).innerHTML = json.title + ' - ' + json.author;

//	if its a first time user, the 'about' tab is open so close it now.
	if (firstTimeUser) {
		$("#info-bar").slideUp("slow");
		firstTimeUser = false;
	}

	if (i == 7) {
		document.getElementById('sentence' + i).innerHTML = json.sentence + ".";
		return false;
	} else {
		document.getElementById('sentence' + i).innerHTML = json.sentence + ',';
	}

	i++;

	if (i == 2 || i == 3 || i == 6 || i == 7) {
		$.ajax({
			url : "get-poem.html",
			data : {
				'sentence' : wordToRhyme[i-2],
				'poemRoundNumber' : poemRoundNumber
			},
			success : function(data) {
				addLineToTable(data);
			}
		});
	} else {
		$.ajax({
			url : "get-poem.html",
			data : {
				'sentence' : null,
				'poemRoundNumber' : poemRoundNumber
			},
			success : function(data) {
				addLineToTable(data);
			}
		});
	}

}
	