function inverseValidation(login){
	$.ajax({
		url: "/affectation-3A/responsable/inverse-validation",
		data: "login="+login,
		success: function(validated){
			if (validated == "true"){
				$('#info').html("L'élève dont le login est <b>" + login + "</b> est désormais <b>accepté.</b>");				
			}
			else{
				$('#info').html("L'élève dont le login est <b>" + login + "</b> n'est désormais <b>plus accepté.</b>");
			}
		}
	});
}