function removeResume() {
	removeDocument("resume");
	var onclick = 'onclick="$(' + "'input[id=resume]').click();";
	var ihtml = '<input id="resume" name="resume" type="file" style="display: none">';
	ihtml += '<div class="input-append">';
	ihtml += '<input id="resumeInput" ';
	ihtml += onclick + '"';
	ihtml += ' type="text" style="min-width: 300px"><a class="btn" ';
	ihtml += onclick;
	ihtml += '">';
	ihtml += "Parcourir...</a></div><br />";
	document.getElementById('divResume').innerHTML = ihtml;
	$('input[id=resume]').change(function() {
		$('#resumeInput').val($(this).val());
	});
};

function removeLetterIc() {
	removeDocument("letterIc");
	var onclick = 'onclick="$(' + "'input[id=letterIc]').click();";
	var ihtml = '<input id="letterIc" name="letterIc" type="file" style="display: none">';
	ihtml += '<div class="input-append">';
	ihtml += '<input id="letterIcInput" ';
	ihtml += onclick + '"';
	ihtml += ' type="text" style="min-width: 300px"> <a class="btn" ';
	ihtml += onclick;
	ihtml += '">';
	ihtml += "Parcourir...</a></div><br />";
	document.getElementById('divLetterIc').innerHTML = ihtml;
	$('input[id=letterIc]').change(function() {
		$('#letterIcInput').val($(this).val());
	});
};

function removeLetterJs() {
	removeDocument("letterJs");
	var onclick = 'onclick="$(' + "'input[id=letterJs]').click();";
	var ihtml = '<input id="letterJs" name="letterJs" type="file" style="display: none">';
	ihtml += '<div class="input-append">';
	ihtml += '<input id="letterJsInput" ';
	ihtml += onclick + '"';
	ihtml += ' type="text" style="min-width: 300px"> <a class="btn" ';
	ihtml += onclick;
	ihtml += '">';
	ihtml += "Parcourir...</a></div><br />";
	document.getElementById('divLetterJs').innerHTML = ihtml;
	$('input[id=letterJs]').change(function() {
		$('#letterJsInput').val($(this).val());
	});
};

function removeDocument(type) {
	var path = document.getElementById("path").innerHTML;
$.ajax({
						url : path + "/student/remove-document",
				data : "type=" + type,
				success : function(sucM) {
					var suc;
					if (sucM == "true") {
						suc = true;
					} else {
						suc = false;
					}
					if (type == "resume") {
						var alert = document.getElementById("alertResume");
						if (suc) {
							alert.innerHTML = '<div class="alert alert-info">Votre <b>CV</b> a bien été <b>supprimé</b>.</div>';
						} else {
							alert.innerHTML = '<div class="alert alert-error">Une erreur est survenue lors de la suppression de votre CV.</div>';
						}
					}
					if (type == "letterIc") {
						var alert = document.getElementById("alertLetterIc");
						if (suc) {
							alert.innerHTML = '<div class="alert alert-info">Votre <b>lettre de motivation</b> pour votre choix de <b>parcours d\'approfondissement</b> a bien été <b>supprimée</b>.</div>';
						} else {
							alert.innerHTML = '<div class="alert alert-error">Une erreur est survenue lors la suppression de votre lettre de motivation pour votre choix de parcours d\'approfondissement.</div>';
						}
					}
					if (type == "letterJs") {
						var alert = document.getElementById("alertLetterJs");
						if (suc) {
							alert.innerHTML = '<div class="alert alert-info">Votre <b>lettre de motivation</b> pour votre choix de <b>filière métier</b> a bien été <b>supprimée</b>.</div>';
						} else {
							alert.innerHTML = '<div class="alert alert-error">Une erreur est survenue lors la suppression de votre lettre de motivation pour votre choix de filière métier.</div>';
						}
					}
				}
			});
}