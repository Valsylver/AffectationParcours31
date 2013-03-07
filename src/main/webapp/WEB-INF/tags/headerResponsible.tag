<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="title" required="true" type="java.lang.String"%>

<nav class="navbar navbar-inverse">
	<div class="navbar-inner">
		<a href="${pageContext.request.contextPath}/responsable/" class="brand"> Affectation 3A, ${title} </a>
		<div class="container pull-right">
			<ul class="nav">
				<li><a href="${pageContext.request.contextPath}/logout">Deconnexion</a></li>
			</ul>
		</div>
	</div>
</nav>