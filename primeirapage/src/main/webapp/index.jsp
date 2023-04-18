<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

<title>Insert title here</title>

<style>
		body {
			margin: 0;
			padding: 0;
			background: url("https://cdn.discordapp.com/attachments/1088577939211292765/1088659527555092531/n0xzerazada_modern_background_image_theme_blue_with_some_detail_73a8ede5-71f9-4832-94d0-1c1f6209531c.png") no-repeat center center fixed;
			background-size: cover;
			font-family: sans-serif;
		}
		
		.container {
			width: 400px;
			margin: 0 auto;
			margin-top: 150px;
			background:rgba(0,0,0,0.4);
			color: white;
		    backdrop-filter: blur(6px);
			padding: 30px;
			border-radius: 8px;
			box-shadow: 0 0 20px rgba(0,0,0,0.3);
			display: flex;
			flex-direction: column;
			align-items: center;
		}
		
		.container h2 {
			font-size: 2rem;
			font-weight: bold;
			margin-bottom: 30px;
			text-align: center;
			color: #FFF;
			text-transform: uppercase;
			letter-spacing: 1px;
		}
		
		input[type="text"], input[type="password"] {
			width: 100%;
			padding: 12px 20px;
			margin: 10px 0;
			display: inline-block;
			border: none;
			border-radius: 5px;
			border-bottom: 2px solid #eee;
			background-color: #f8f9fa;
			font-size: 1rem;
			color: #495057;
			transition: border-bottom-color 0.2s ease-in-out;
		}
		
		input[type="text"]:focus, input[type="password"]:focus {
			border-bottom-color: #007bff;
			outline: none;
		}
		
		.button {
			background-color: #007bff;
			color: #fff;
			padding: 12px 20px;
			margin-top: 30px;
			border: none;
			border-radius: 5px;
			cursor: pointer;
			width: 100%;
			font-size: 1.1rem;
			font-weight: bold;
			text-transform: uppercase;
			letter-spacing: 1px;
			transition: background-color 0.2s ease-in-out;
		}
		
		.button:hover {
			background-color: #0069d9;
		}
		
		.container p {
			font-size: 0.9rem;
			text-align: center;
			margin-top: 20px;
			color: #FFF;
		}
		
		.container a {
			color: #007bff;
			text-decoration: none;
			transition: color 0.2s ease-in-out;
		}
		
		.container a:hover {
			color: #0069d9;
		}
		
		.msg {
			color: red;
			top:50px;
		}
		
	</style>
</head>
<body>
	<div class="container">
		<h2>Login</h2>
		<form action="<%= request.getContextPath() %>/ServletLogin" method="post">
			<label for="username">Nome de Usuário:</label>
			<input type="text" id="username" name="username" required="required">
			
			<label for="password">Senha:</label>
			<input type="password" id="password" name="password" required="required">
			
			<input class="button" type="submit" value="Acessar">
		</form>
		<p>Não tem uma conta? <a href="#">Cadastre-se</a></p>
		<h5 class="msg">${msg}</h5>
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

</body>
</html>