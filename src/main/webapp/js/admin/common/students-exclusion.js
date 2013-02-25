function removePromoStudent(id) {
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

	var newI = document.createElement("img");
	newI.setAttribute("src", "/img/plus-new.png");
	var newA = document.createElement("a");
	newA.title = login;
	newA.setAttribute("href", "javascript:addPromoStudent(this.id);");
	newA.setAttribute("onclick", "addPromoStudent(this.id);");
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
		var la = exclusionElements.length - 1;
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
	}
	
	var allExclusionInputs = document.getElementById("exclusionInputs").getElementsByTagName("input");
	for (var inputIndex = 0; inputIndex < allExclusionInputs.length; inputIndex++){
		var input = allExclusionInputs[inputIndex];
		if (input.value == ""){
			input.setAttribute("value", login);
			break;
		}
	}
	
	setNoSavedDataMessage();
	
};

function removeCesureStudent(id) {
	var cesure = document.getElementById("cesure");
	var exclusion = document.getElementById("exclusion");
	var number = parseInt(id.substring(6, id.length));

	var li = cesure.getElementsByTagName('li')[number];
	var fullName = li.childNodes[1].textContent;
	var login = li.childNodes[0].title;
	cesure.removeChild(li);

	var licesureElements = cesure.getElementsByTagName("a");
	for ( var childIndex = 0; childIndex < licesureElements.length; childIndex++) {
		licesureElements[childIndex].id = "cesure" + childIndex;
	}

	var newI = document.createElement("img");
	newI.setAttribute("src", "/img/plus-new.png");
	var newA = document.createElement("a");
	newA.title = login;
	newA.setAttribute("href", "javascript:addCesureStudent(this.id);");
	newA.setAttribute("onclick", "addCesureStudent(this.id);");
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
		var la = exclusionElements.length - 1;
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
	}
	
	var allExclusionInputs = document.getElementById("exclusionInputs").getElementsByTagName("input");
	for (var inputIndex = 0; inputIndex < allExclusionInputs.length; inputIndex++){
		var input = allExclusionInputs[inputIndex];
		if (input.value == ""){
			input.setAttribute("value", login);
			break;
		}
	}
	
	setNoSavedDataMessage();
	
};

function compareNames(fullName1, fullName2) {
	if (fullName1.split(" ").length > 1){
		var name1 = "";
		for (var i=2; i<fullName1.split(" ").length; i++){
			name1 += fullName1.split(" ")[i];
		}
	}
	else{
		var name1 = fullName1;
	}
	if (fullName2.split(" ").length > 1){
		var name2 = "";
		for (var i=2; i<fullName2.split(" ").length; i++){
			name2 += fullName2.split(" ")[i];
		}
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

function addPromoStudent(id) {
	var promo = document.getElementById("promo");
	var exclusion = document.getElementById("exclusion");
	var id2 = id.toString();
	var number = parseInt(id2.substring(9, id2.length));

	var li = exclusion.getElementsByTagName('li')[number];
	var fullName = li.childNodes[1].textContent;
	var login = li.childNodes[0].title;
	exclusion.removeChild(li);

	var liExclusionElements = exclusion.getElementsByTagName("a");
	for ( var childIndex = 0; childIndex < liExclusionElements.length; childIndex++) {
		liExclusionElements[childIndex].id = "exclusion" + childIndex;
	}

	var newI = document.createElement("img");
	newI.setAttribute("src", "/img/minus-new.png");
	var newA = document.createElement("a");
	newA.title = login;
	newA.setAttribute("href", "javascript:void(0);");
	newA.setAttribute("onclick", "removePromoStudent(this.id);");
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
			promo.insertBefore(newLi, promoElements[position]);
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
			}
		}
		else{
			promo.appendChild(newLi);
		}
	}
	
	var liPromoElements = promo.getElementsByTagName("a");
	for (var childIndex = 0; childIndex < liPromoElements.length; childIndex++) {
		liPromoElements[childIndex].id = "promo" + childIndex;
	}
	
	var allExclusionInputs = document.getElementById("exclusionInputs").getElementsByTagName("input");
	for (var inputIndex = 0; inputIndex < allExclusionInputs.length; inputIndex++){
		var input = allExclusionInputs[inputIndex];
		if (input.value == login){
			input.setAttribute("value", "");
			break;
		}
	}
	
	setNoSavedDataMessage();
};

function addCesureStudent(id) {
	var cesure = document.getElementById("cesure");
	var exclusion = document.getElementById("exclusion");
	var id2 = id.toString();
	var number = parseInt(id2.substring(9, id2.length));

	var li = exclusion.getElementsByTagName('li')[number];
	var fullName = li.childNodes[1].textContent;
	var login = li.childNodes[0].title;
	exclusion.removeChild(li);

	var liExclusionElements = exclusion.getElementsByTagName("a");
	for ( var childIndex = 0; childIndex < liExclusionElements.length; childIndex++) {
		liExclusionElements[childIndex].id = "exclusion" + childIndex;
	}

	var newI = document.createElement("img");
	newI.setAttribute("src", "/img/minus-new.png");
	var newA = document.createElement("a");
	newA.title = login;
	newA.setAttribute("href", "javascript:void(0);");
	newA.setAttribute("onclick", "removeCesureStudent(this.id);");
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

	var cesureElements = cesure.getElementsByTagName("li");	
	if (cesureElements.length > 1){
		var foundPosition = false;
		var position = 0;
		var fullName1 = cesureElements[0].childNodes[1].textContent;
		var fullName2 = cesureElements[cesureElements.length - 1].childNodes[1].textContent;
		if (compareNames(fullName, fullName1) == 1){
			foundPosition = true;
			position = 0;
		}
		else{
			if ((compareNames(fullName2, fullName) == 1) || (compareNames(fullName2, fullName) == 0)){
				foundPosition = true;
				position = cesureElements.length;
			}
		}
		
		var childIndex = 0;
		while ((!foundPosition) && (childIndex < cesureElements.length-1)){
			var fullName1 = cesureElements[childIndex].childNodes[1].textContent;
			var fullName2 = cesureElements[childIndex+1].childNodes[1].textContent;
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
		
		if (position == cesureElements.length){
			cesure.appendChild(newLi);
		}
		else{
			cesure.insertBefore(newLi, cesureElements[position]);
		}
	}
	else{
		if (cesureElements.length == 1){
			var fullName1 = cesureElements[0].childNodes[1].textContent;
			if ((compareNames(fullName1, fullName) == 1) || (compareNames(fullName, fullName1) == 0)){
				cesure.appendChild(newLi);
			}
			else{
				cesure.insertBefore(newLi, cesureElements[0]);
			}
		}
		else{
			cesure.appendChild(newLi);
		}
	}
	
	var licesureElements = cesure.getElementsByTagName("a");
	for (var childIndex = 0; childIndex < licesureElements.length; childIndex++) {
		licesureElements[childIndex].id = "cesure" + childIndex;
	}
	
	var allExclusionInputs = document.getElementById("exclusionInputs").getElementsByTagName("input");
	for (var inputIndex = 0; inputIndex < allExclusionInputs.length; inputIndex++){
		var input = allExclusionInputs[inputIndex];
		if (input.value == login){
			input.setAttribute("value", "");
			break;
		}
	}
	
	setNoSavedDataMessage();
};

function setNoSavedDataMessage() {
	var button = document.getElementById("starNoSavedData");
	button.innerHTML = "*";
};