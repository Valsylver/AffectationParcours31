<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filières 3ème année Centrale Marseille</title>
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-combobox.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<br />
		<div class="row">
			<div class="span12">
				<div class="alert alert-success">
					<h4>Soumission</h4>
					Votre soumission a bien été prise en compte.
				</div>
				<c:if test="${fn:length(notFilledIcNumber) > 0}">
					<div class="alert alert-block">
						<h4>Parcours d'approfondissement</h4>
						${notFilledIc}
					</div>
				</c:if>
				<c:if test="${fn:length(notFilledJsNumber) > 0}">
					<div class="alert alert-block">
						<h4>Filières métier</h4>
						${notFilledJs}
					</div>
				</c:if>
				<c:if test="${(!hasFilledLetterIc) || ((!hasFilledLetterJs) || (!hasFilledResume))}">
					<div class="alert alert-block">
						<h4>Documents</h4>
						<c:if test="${!hasFilledResume}">
		            			Vous n'avez pas déposé votre CV. 
		            		</c:if>
						<c:if test="${!hasFilledLetterIc}">
		            			Vous n'avez pas déposé votre lettre de motivation pour le parcours d'approfondissement. 
		            			<br />
						</c:if>
						<c:if test="${!hasFilledLetterJs}">
		            			Vous n'avez pas déposé votre lettre de motivation pour la filière métier. 
		            			<br />
						</c:if>
					</div>
				</c:if>
				<div class="alert alert-info">
					<h4>Récapitulatif de vos choix</h4>
					<table class="table">
						<thead>
							<tr>
								<th></th>
								<th>Parcours d'approfondissement</th>
								<th>Filières métier</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="index" begin="0" end="4">
								<tr>
									<td>${index+1}.</td>
									<td><c:choose>
											<c:when test="${icChoices[index] != null}">
										${icChoices[index].stringForForm}
									</c:when>
											<c:otherwise>
										----------------------
									</c:otherwise>
										</c:choose></td>
									<td><c:choose>
											<c:when test="${jsChoices[index] != null}">
										${jsChoices[index].stringForForm}
									</c:when>
											<c:otherwise>
										----------------------
									</c:otherwise>
										</c:choose></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="alert alert-info">
					<h4>Modification</h4>
					Vous pouvez modifier vos choix et vos documents jusqu'au ${dateEnd}<br /> <a class="btn btn-primary pull-right" href="/student/add">Modifier</a> <br /> <br /></div>
					
			</div>
		</div>
		<div class="row">
			<div class="span2 offset10">
				<div class="pull right">
					<a href="/logout" class="btn btn-danger"><i class="icon-white icon-off"></i> Deconnexion</a>
				</div>
			</div>
		</div>
		<br />
		<br />
	</div>


</body>
</html>