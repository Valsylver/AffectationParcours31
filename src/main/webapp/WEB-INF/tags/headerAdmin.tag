<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="run" required="true" type="java.lang.Boolean"%>

<nav class="navbar navbar-inverse">
	<div class="navbar-inner">
		<a href="${pageContext.request.contextPath}/admin/" class="brand">Affectation 3A </a>
		<div class="container pull-right">
			<ul class="nav">
				<c:choose>
					<c:when test="${run}">
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/students">Administration</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="${pageContext.request.contextPath}/admin/config/exclude">Elèves (liste)</a></li>
						<li><a href="${pageContext.request.contextPath}/admin/config/exclude-from-file">Elèves (fichier xls)</a></li>
					</c:otherwise>
				</c:choose>
				<li><a href="${pageContext.request.contextPath}/logout">Deconnexion</a></li>
			</ul>
		</div>
	</div>
</nav>