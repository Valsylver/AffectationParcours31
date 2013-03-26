<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/admins">Administrateurs</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/students">Elèves</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/export">Export</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail1">Mail 1 <c:choose><c:when test="${mail1Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail2">Mail 2 <c:choose><c:when test="${mail2Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></a></li>
					<li class="active"><a href="${pageContext.request.contextPath}/admin/run/settings/spontaneous-mail">Mail Spontané</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/process">Processus</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/specializations">Spécialisations</a></li>
				</ul>
			</div>

			<div class="span6">
				<form:form action="${pageContext.request.contextPath}/admin/run/settings/process-spontaneous-mail" method="post" commandName="simpleMail" class="well form-horizontal span6">
					<c:if test='${not empty flashMessage }'>
						<div class="alert alert-success">
							${flashMessage}
						</div>
					</c:if>
					
					<label for="addressees">
						Destinataires
					</label>
					<form:select path="addressee" class="span4">
						<option>Tous les élèves concernés par le processus</option>
						<option>Elèves n'ayant pas terminé leur candidature</option>
					</form:select>
					
					<br />
					<br />
					<label for="object">Objet <form:errors path="object">
							<br />
							<font color="red">L'objet ne doit pas être vide.</font>
						</form:errors>
					</label>
					<form:input id="object" path="object" class="span4" />
					
					<br />
					<br />
					<label for="message">Message <form:errors path="message">
							<br />
							<font color="red">Le message ne doit pas être vide.</font>
						</form:errors>
					</label>
					<form:textarea id="message" path="message" class="span6" />

					<br />
					<br />

					<button class="btn btn-primary pull-right" name="commit" type="submit">
						<i class="icon-white icon-envelope"></i>  Envoyer
					</button>

				</form:form>

			</div>
		</div>
	</div>
</body>
</html>
