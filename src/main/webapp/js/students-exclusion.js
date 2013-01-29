function removeStudent(id) {
	var resume = document.getElementById("resume");
	var promo = document.getElementById("promo");
	var exclusion = document.getElementById("exclusion");
	var number = parseInt(id.substring(5, id.length));

	var li = promo.getElementsByTagName('li')[number];
	var fullName = li.childNodes[1].textContent;
	var login = li.childNodes[0].title;
	promo.removeChild(li);

	var liPromoElements = promo.getElementsByTagName("a");
	for ( var childIndex = 0; childIndex < liPromoElements.length; childIndex++) {
		liPromoElements[childIndex].id = "promo" + childIndex;
	}

	var newI = document.createElement("i");
	newI.className = "icon-plus-sign";
	var newA = document.createElement("a");
	newA.title = login;
	//newA.href ="javascript:addStudent(this.id);";
	//newA.onclick = "addStudent(this.id);";
	newA.appendChild(newI);
	var newLi = document.createElement("li");
	newLi.appendChild(newA);
	newLi.appendChild(document
			.createTextNode(fullName));

	if (fullName.split(" ").length > 1){
		var firstName = fullName.split(" ")[0];
		var name = fullName.split(" ")[1];
	}
	else{
		var name = fullName;
	}

	var exclusionElements = exclusion.getElementsByTagName("li");	
	if (exclusionElements.length > 1){
		var foundPosition = false;
		var position = 0;
		var fullName1 = exclusionElements[0].childNodes[1].textContent;
		var fullName2 = exclusionElements[exclusionElements.length - 1].childNodes[1].textContent;
		if (compareNames(fullName, fullName1) == 1){
			foundPosition = true;
			position = 0;
		}
		else{
			if ((compareNames(fullName2, fullName) == 1) || (compareNames(fullName2, fullName) == 0)){
				foundPosition = true;
				position = exclusionElements.length;
			}
		}
		
		var childIndex = 0;
		while ((!foundPosition) && (childIndex < exclusionElements.length-1)){
			var fullName1 = exclusionElements[childIndex].childNodes[1].textContent;
			var fullName2 = exclusionElements[childIndex+1].childNodes[1].textContent;
			if ((compareNames(fullName1, fullName) == 1) && (compareNames(fullName, fullName2) == 1)){
				position = childIndex+1;
				foundPosition = true;
			}
			else{
				if (compareNames(fullName, fullName1) == 0){
					foundPosition = true;
					position = childIndex;
				}
			}
			childIndex += 1;
		}
		
		if (position == exclusionElements.length){
			exclusion.appendChild(newLi);
		}
		else{
			exclusion.insertBefore(newLi, exclusionElements[position]);
		}
	}
	else{
		if (exclusionElements.length == 1){
			var fullName1 = exclusionElements[0].childNodes[1].textContent;
			if ((compareNames(fullName1, fullName) == 1) || (compareNames(fullName, fullName1) == 0)){
				exclusion.appendChild(newLi);
			}
			else{
				exclusion.insertBefore(newLi, exclusionElements[0]);
				var resume = document.getElementById("resume");
			}
		}
		else{
			exclusion.appendChild(newLi);
		}
	}
	
	var liExclusionElements = exclusion.getElementsByTagName("a");
	for ( var childIndex = 0; childIndex < liExclusionElements.length; childIndex++) {
		liExclusionElements[childIndex].id = "exclusion" + childIndex;
		liExclusionElements[childIndex].href = "javascript:addStudent(" + "exclusion" + childIndex + ");";
	}
	
};

function compareNames(fullName1, fullName2) {
	if (fullName1.split(" ").length > 1){
		var name1 = fullName1.split(" ")[2];
	}
	else{
		var name1 = fullName1;
	}
	if (fullName2.split(" ").length > 1){
		var name2 = fullName2.split(" ")[2];
	}
	else{
		var name2 = fullName2;
	}
	if (name1 < name2){
		return 1;
	}
	else{
		if (name1 == name2){
			var firstName1 = fullName1.split(" ")[1];
			var firstName2 = fullName2.split(" ")[1];
			if (firstName1 < firstName2){
				return 1;
			}
			else{
				if (firstName1 == firstName2){
					return 0;
				}
				else{
					return -1;
				}
			}
		}
		else{
			return -1;
		}
	}
};

function addStudent(id) {
	var promo = document.getElementById("promo");
	var exclusion = document.getElementById("exclusion");
	var id2 = id.toString();
	var resume = document.getElementById("resume");
	resume.innerHTML = id2;
	var number = parseInt(id2.substring(9, id2.length));

	var li = exclusion.getElementsByTagName('li')[number];
	var fullName = li.childNodes[1].textContent;
	var login = li.childNodes[0].title;
	exclusion.removeChild(li);

	var liExclusionElements = exclusion.getElementsByTagName("a");
	for ( var childIndex = 0; childIndex < liExclusionElements.length; childIndex++) {
		liExclusionElements[childIndex].id = "exclusion" + childIndex;
	}

	var newI = document.createElement("i");
	newI.className = "icon-minus-sign";
	var newA = document.createElement("a");
	newA.title = login;
	newA.href ="javascript:void(0);";
	newA.onclick = "removeStudent(this.id);";
	newA.appendChild(newI);
	var newLi = document.createElement("li");
	newLi.appendChild(newA);
	newLi.appendChild(document
			.createTextNode(fullName));

	if (fullName.split(" ").length > 1){
		var firstName = fullName.split(" ")[0];
		var name = fullName.split(" ")[1];
	}
	else{
		var name = fullName;
	}

	var promoElements = promo.getElementsByTagName("li");	
	if (promoElements.length > 1){
		var foundPosition = false;
		var position = 0;
		var fullName1 = promoElements[0].childNodes[1].textContent;
		var fullName2 = promoElements[promoElements.length - 1].childNodes[1].textContent;
		if (compareNames(fullName, fullName1) == 1){
			foundPosition = true;
			position = 0;
		}
		else{
			if ((compareNames(fullName2, fullName) == 1) || (compareNames(fullName2, fullName) == 0)){
				foundPosition = true;
				position = promoElements.length;
			}
		}
		
		var childIndex = 0;
		while ((!foundPosition) && (childIndex < promoElements.length-1)){
			var fullName1 = promoElements[childIndex].childNodes[1].textContent;
			var fullName2 = promoElements[childIndex+1].childNodes[1].textContent;
			if ((compareNames(fullName1, fullName) == 1) && (compareNames(fullName, fullName2) == 1)){
				position = childIndex+1;
				foundPosition = true;
			}
			else{
				if (compareNames(fullName, fullName1) == 0){
					foundPosition = true;
					position = childIndex;
				}
			}
			childIndex += 1;
		}
		
		if (position == promoElements.length){
			promo.appendChild(newLi);
		}
		else{
			promo.insertBefore(newLi, promoElements[position])
		}
	}
	else{
		if (promoElements.length == 1){
			var fullName1 = promoElements[0].childNodes[1].textContent;
			if ((compareNames(fullName1, fullName) == 1) || (compareNames(fullName, fullName1) == 0)){
				promo.appendChild(newLi);
			}
			else{
				promo.insertBefore(newLi, promoElements[0]);
				var resume = document.getElementById("resume");
			}
		}
		else{
			promo.appendChild(newLi);
		}
	}
	
	var liPromoElements = promo.getElementsByTagName("a");
	for ( var childIndex = 0; childIndex < liPromoElements.length; childIndex++) {
		liPromoElements[childIndex].id = "promo" + childIndex;
	}
};
