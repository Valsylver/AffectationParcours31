$('input[id=resume]').change(function() {
	$('#resumeInput').val($(this).val());
});

$('input[id=letterIc]').change(function() {
	$('#letterIcInput').val($(this).val());
});

$('input[id=letterJs]').change(function() {
	$('#letterJsInput').val($(this).val());
});

$(document).ready(function() {
	$("select").select2();
});

function removeResume() {
	removeDocument("resume");
	var ihtml = '<input id="resume" name="resume" type="file" style="display: none">';
	ihtml += '<div class="input-append">';
	ihtml += '<input id="resumeInput" type="text" style="min-width: 300px"><a class="btn" onclick="$(';
	ihtml += "'input[id=resume]').click();";
	ihtml += '">';
	ihtml += "Parcourir...</a></div><br />";
	document.getElementById('divResume').innerHTML = ihtml;
	$('input[id=resume]').change(function() {
		$('#resumeInput').val($(this).val());
	});
};

function removeLetterIc() {
	removeDocument("letterIc");
	var ihtml = '<input id="letterIc" name="letterIc" type="file" style="display: none">';
	ihtml += '<div class="input-append">';
	ihtml += '<input id="letterIcInput" type="text" style="min-width: 300px"> <a class="btn" onclick="$(';
	ihtml += "'input[id=letterIc]').click();";
	ihtml += '">';
	ihtml += "Parcourir...</a></div><br />";
	document.getElementById('divLetterIc').innerHTML = ihtml;
	$('input[id=letterIc]').change(function() {
		$('#letterIcInput').val($(this).val());
	});
};

function removeLetterJs() {
	removeDocument("letterJs");
	var ihtml = '<input id="letterJs" name="letterJs" type="file" style="display: none">';
	ihtml += '<div class="input-append">';
	ihtml += '<input id="letterJsInput" type="text" style="min-width: 300px"> <a class="btn" onclick="$(';
	ihtml += "'input[id=letterJs]').click();";
	ihtml += '">';
	ihtml += "Parcourir...</a></div><br />";
	document.getElementById('divLetterJs').innerHTML = ihtml;
	$('input[id=letterJs]').change(function() {
		$('#letterJsInput').val($(this).val());
	});
};

function removeDocument(type) {
	var login = document.getElementById("login").innerHTML;
	var path = document.getElementById("path").innerHTML;
	$.ajax({
				url : path + "/admin/run/main/student/remove-document",
				data : "type=" + type + "&login=" + login,
				success : function(sucM) {
					var suc;
					if (sucM == "true"){
						suc = true;
					}
					else{
						suc = false;
					}
					if (type == "resume") {
						var alert = document.getElementById("alertResume");
						if (suc) {
							alert.innerHTML = '<div class="alert alert-success">Votre CV a bien été supprimé.</div>';
						} else {
							alert.innerHTML = '<div class="alert alert-error">Une erreur est survenue lors de la suppression de votre CV.</div>';
						}
					}
					if (type == "letterIc") {
						var alert = document.getElementById("alertLetterIc");
						if (suc) {
							alert.innerHTML = '<div class="alert alert-success">Votre lettre de motivation pour votre choix de parcours d\'approfondissement a bien été supprimée.</div>';
						} else {
							alert.innerHTML = '<div class="alert alert-error">Une erreur est survenue lors la suppression de votre lettre de motivation pour votre choix de parcours d\'approfondissement.</div>';
						}
					}
					if (type == "letterJs") {
						var alert = document.getElementById("alertLetterJs");
						if (suc) {
							alert.innerHTML = '<div class="alert alert-success">Votre lettre de motivation pour votre choix de filière métier a bien été supprimée.</div>';
						} else {
							alert.innerHTML = '<div class="alert alert-error">Une erreur est survenue lors la suppression de votre lettre de motivation pour votre choix de filière métier.</div>';
						}
					}
				}
			});
}

$(document).ready(function() {
	var pa1 = document.getElementById('selectPa1');
	var pa2 = document.getElementById('selectPa2');
	var pa3 = document.getElementById('selectPa3');
	var pa4 = document.getElementById('selectPa4');
	var pa5 = document.getElementById('selectPa5');

	updatePaSelect(pa1.options[pa1.selectedIndex].text, 'selectPa1');
	updatePaSelect(pa2.options[pa2.selectedIndex].text, 'selectPa2');
	updatePaSelect(pa3.options[pa3.selectedIndex].text, 'selectPa3');
	updatePaSelect(pa4.options[pa4.selectedIndex].text, 'selectPa4');
	updatePaSelect(pa5.options[pa5.selectedIndex].text, 'selectPa5');

	var fm1 = document.getElementById('selectFm1');
	var fm2 = document.getElementById('selectFm2');
	var fm3 = document.getElementById('selectFm3');
	var fm4 = document.getElementById('selectFm4');
	var fm5 = document.getElementById('selectFm5');

	updateFmSelect(fm1.options[fm1.selectedIndex].text, 'selectFm1');
	updateFmSelect(fm2.options[fm2.selectedIndex].text, 'selectFm2');
	updateFmSelect(fm3.options[fm3.selectedIndex].text, 'selectFm3');
	updateFmSelect(fm4.options[fm4.selectedIndex].text, 'selectFm4');
	updateFmSelect(fm5.options[fm5.selectedIndex].text, 'selectFm5');

});

function updatePaSelect(value, id) {
	if (value != "------------------------- Pas de choix -------------------------") {
		var currentId = id[id.length - 1];
		var texte = "";
		for (i = 1; i < 6; i++) {
			if (i != parseInt(currentId)) {
				var select = document.getElementById("selectPa" + i);
				var index = 0;
				for (j = 0; j < select.options.length; j++) {
					if (select.options[j].text == value) {
						index = j;
					}
				}
				select.remove(index)
			}
		}

		var select = document.getElementById("selectPa" + currentId);
		var optionManquante = "lol";
		if (4 != parseInt(id[id.length - 1])) {
			var select_other = document.getElementById("selectPa4");
		} else {
			var select_other = document.getElementById("selectPa5");
		}

		var select_options = new Array();
		for (j = 0; j < select.options.length; j++) {
			select_options[j] = select.options[j].text;
		}

		var select_other_options = new Array();
		for (j = 0; j < select_other.options.length; j++) {
			select_other_options[j] = select_other.options[j].text;
		}

		var isIn = false;
		for (j = 0; j < select_options.length; j++) {
			option = select_options[j];

			isIn = false;
			for (i = 0; i < select_other_options.length; i++) {
				if (select_other_options[i] == option) {
					isIn = true;
				}
			}

			if (!isIn) {
				if ((option != value) && (option != "")) {
					optionManquante = option;
				}
			}
		}

		if (optionManquante != "lol") {
			for (i = 1; i < 6; i++) {
				if (i != parseInt(id[id.length - 1])) {
					var select_other = document.getElementById("selectPa" + i);
					var opt = document.createElement("option");
					opt.text = optionManquante;

					var where = 0;

					if (optionManquante < select_other.options[1].text) {
						where = 1;
					} else {
						if (optionManquante > select_other.options[select_other.options.length - 1].text) {
							where = null;
						} else {
							for (j = 0; j < select_other.options.length - 1; j++) {
								var option_1 = select_other.options[j].text;
								var option_2 = select_other.options[j + 1].text;
								if ((option_1 < optionManquante)
										&& (optionManquante < option_2)) {
									where = j + 1;
								}
							}
						}
					}

					document.getElementById("selectPa" + i).add(opt,
							select_other.options[where]);
				}
			}
		}
	} else {
		var texte = "mort de rire je passe la";
		var select = document.getElementById("selectPa" + id[id.length - 1]);
		var optionManquante_ = "lol";
		if (4 != parseInt(id[id.length - 1])) {
			var select_other = document.getElementById("selectPa4");
		} else {
			var select_other = document.getElementById("selectPa5");
		}

		var select_options = new Array();
		for (j = 0; j < select.options.length; j++) {
			select_options[j] = select.options[j].text;
		}

		var select_other_options = new Array();
		for (j = 0; j < select_other.options.length; j++) {
			select_other_options[j] = select_other.options[j].text;
		}

		var isIn = false;
		for (j = 0; j < select_options.length; j++) {
			option = select_options[j];

			isIn = false;
			for (i = 0; i < select_other_options.length; i++) {
				if (select_other_options[i] == option) {
					isIn = true;
				}
			}

			if (isIn) {
			} else {
				optionManquante_ = option;
			}
		}

		if (optionManquante_ != "lol") {
			for (i = 1; i < 6; i++) {
				if (i != parseInt(id[id.length - 1])) {
					var select_other = document.getElementById("selectPa" + i);
					var opt = document.createElement("option");
					opt.text = optionManquante_;

					var where = 0;

					if (optionManquante_ < select_other.options[1].text) {
						where = 1;
					} else {
						if (optionManquante_ > select_other.options[select_other.options.length - 1].text) {
							where = null;
						} else {
							for (j = 0; j < select_other.options.length - 1; j++) {
								var option_1 = select_other.options[j].text;
								var option_2 = select_other.options[j + 1].text;
								if ((option_1 < optionManquante_)
										&& (optionManquante_ < option_2)) {
									where = j + 1;
								}
							}
						}
					}
					document.getElementById("selectPa" + i).add(opt,
							select_other.options[where]);
				}
			}
		}
	}
}

function updateFmSelect(value, id) {
	if (value != "------------------------- Pas de choix -------------------------") {
		for (i = 1; i < 6; i++) {
			if (i != parseInt(id[id.length - 1])) {
				var select = document.getElementById("selectFm" + i);
				var index = 0;
				for (j = 0; j < select.options.length; j++) {
					if (select.options[j].text == value) {
						index = j;
					}
				}
				select.remove(index);
			}
		}

		var select = document.getElementById("selectFm" + id[id.length - 1]);
		var optionManquante = "optionManquante";

		if (4 != parseInt(id[id.length - 1])) {
			var select_other = document.getElementById("selectFm4");
		} else {
			var select_other = document.getElementById("selectFm5");
		}

		var select_options = new Array();
		for (j = 0; j < select.options.length; j++) {
			select_options[j] = select.options[j].text;
		}

		var select_other_options = new Array();
		for (j = 0; j < select_other.options.length; j++) {
			select_other_options[j] = select_other.options[j].text;
		}

		var isIn = false;
		for (j = 0; j < select_options.length; j++) {
			option = select_options[j];

			isIn = false;
			for (i = 0; i < select_other_options.length; i++) {
				if (select_other_options[i] == option) {
					isIn = true;
				}
			}

			if (!isIn) {
				if ((option != value) && (option != "")) {
					optionManquante = option;
				}
			}
		}

		if (optionManquante != "optionManquante") {
			for (i = 1; i < 6; i++) {
				if (i != parseInt(id[id.length - 1])) {
					var select_other = document.getElementById("selectFm" + i);
					var opt = document.createElement("option");
					opt.text = optionManquante;

					var where = 0;

					if (select_other.options.length > 1) {
						if (optionManquante < select_other.options[1].text) {
							where = 1;
						} else {
							if (optionManquante > select_other.options[select_other.options.length - 1].text) {
								where = null;
							} else {
								for (j = 0; j < select_other.options.length - 1; j++) {
									var option_1 = select_other.options[j].text;
									var option_2 = select_other.options[j + 1].text;
									if ((option_1 < optionManquante)
											&& (optionManquante < option_2)) {
										where = j + 1;
									}
								}
							}
						}
					}

					else {
						where = 1;
					}
					document.getElementById("selectFm" + i).add(opt, where);
				}
			}
		}
	}

	else {
		var select = document.getElementById("selectFm" + id[id.length - 1]);
		var optionManquante_ = "lol";
		if (4 != parseInt(id[id.length - 1])) {
			var select_other = document.getElementById("selectFm4");
		} else {
			var select_other = document.getElementById("selectFm5");
		}

		var select_options = new Array();
		for (j = 0; j < select.options.length; j++) {
			select_options[j] = select.options[j].text;
		}

		var select_other_options = new Array();
		for (j = 0; j < select_other.options.length; j++) {
			select_other_options[j] = select_other.options[j].text;
		}

		var isIn = false;
		for (j = 0; j < select_options.length; j++) {
			option = select_options[j];

			isIn = false;
			for (i = 0; i < select_other_options.length; i++) {
				if (select_other_options[i] == option) {
					isIn = true;
				}
			}

			if (!isIn) {
				optionManquante_ = option;
			}
		}

		if (optionManquante_ != "lol") {
			for (i = 1; i < 6; i++) {
				if (i != parseInt(id[id.length - 1])) {
					var select_other = document.getElementById("selectFm" + i);
					var opt = document.createElement("option");
					opt.text = optionManquante_;

					var texte = "option manquante : " + optionManquante_;
					texte += "<br />";
					texte += "premier choix non nul : "
							+ select_other.options[1].text;

					// var blocListe = document.getElementById('blocResultat');
					// blocListe.innerHTML = texte;

					var where = 0;

					if (optionManquante_ < select_other.options[1].text) {
						where = 1;
					} else {
						if (optionManquante_ > select_other.options[select_other.options.length - 1].text) {
							where = null;
						} else {
							for (j = 0; j < select_other.options.length - 1; j++) {
								var option_1 = select_other.options[j].text;
								var option_2 = select_other.options[j + 1].text;
								if ((option_1 < optionManquante_)
										&& (optionManquante_ < option_2)) {
									where = j + 1;
								}
							}
						}
					}
					document.getElementById("selectFm" + i).add(opt, where);
				}
			}
		}
	}
}