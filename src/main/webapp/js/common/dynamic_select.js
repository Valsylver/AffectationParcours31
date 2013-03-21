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

function refreshPa(idChangedStr, value){
	var idChanged = parseInt(idChangedStr);
	var select = document.getElementById("selectPa" + idChangedStr);
	if (4 != idChanged) {
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

	var optionManquante = "99";
	var groupOptionManquante = "99";
	var isIn = false;
	var groups = select.getElementsByTagName("optgroup");
	for (var j = 0; j < groups.length; j++) {
		var group = groups[j];
		var options = group.getElementsByTagName("option");
		
		for (var i = 0; i < options.length; i++){
			var option = options[i].text;
			isIn = false;
			for (var k = 0; k < select_other_options.length; k++) {
				if (select_other_options[k] == option) {
					isIn = true;
				}
			}

			if (!isIn) {
				if (value != "HAHAHAHA"){
					if (option != value) {
						optionManquante = option;
						groupOptionManquante = group.label;
					}
				}
				else{
					optionManquante = option;
					groupOptionManquante = group.label;
				}
			}
		}
	}

	if (optionManquante != "99") {
		for (var i = 1; i < 6; i++) {
			if (i != idChanged){
				var select_other = document.getElementById("selectPa" + i);
				var where = 0;
				var select_groups = select_other.getElementsByTagName("optgroup");
				for (var j=0; j<select_groups.length; j++){
					var group = select_groups[j];
					if (group.label == groupOptionManquante){
						var options = group.getElementsByTagName("option");
						if (options.length > 0){
							if (optionManquante < options[0].text) {
								where = 0;
							}	
							else {
								if (optionManquante > options[options.length-1].text) {
									if (j == select_groups.length-1){
										where = null;
									}
									else{
										where = group.getElementsByTagName("option").length;
									}
								} 
								else {
									for (j = 0; j < options.length - 1; j++) {
										var option_1 = options[j].text;
										var option_2 = options[j+1].text;
										if ((option_1 < optionManquante) && (optionManquante < option_2)) {
											where = j + 1;
										}
									}
								}
							}
						}
						else{
							where = null;
						}
						if (where != null){
							group.insertBefore(new Option(optionManquante), group.getElementsByTagName("option")[where]);
						}
						else{
							group.appendChild(new Option(optionManquante));
						}
					}
				}
			}
		}
	}
}

function updatePaSelect(value, id) {
	var idChangedStr = id[id.length - 1];
	var idChanged = parseInt(idChangedStr);
	if (value != "------------------------------------- Pas de choix -------------------------------------") {
		var texte = document.getElementById("selectPa2").options.length.toString() + "\n";
		texte += document.getElementById("selectPa2").getElementsByTagName("optgroup").length.toString() + "\n";
		
		for (i = 1; i < 6; i++) {
			if (i != idChanged) {
				var select = document.getElementById("selectPa" + i);
				var index = 0;
				for (j = 0; j < select.options.length; j++) {
					if (select.options[j].text == value) {
						index = j;
					}
				}
				select.remove(index);
			}
		}
		refreshPa(idChangedStr, value);
	} else {		
		refreshPa(idChangedStr, "HAHAHAHA");
	}
}

function updateFmSelect(value, id) {
	if (value != "------------------------------------- Pas de choix -------------------------------------") {
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