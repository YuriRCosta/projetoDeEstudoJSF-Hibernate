<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="head.jsp"></jsp:include>

  <body>
  <!-- Pre-loader start -->
  <jsp:include page="theme-loader.jsp"></jsp:include>
  <!-- Pre-loader end -->
  <div id="pcoded" class="pcoded">
      <div class="pcoded-overlay-box"></div>
      <div class="pcoded-container navbar-wrapper">
          
          <jsp:include page="navbarfile.jsp"></jsp:include>

          <div class="pcoded-main-container">
              <div class="pcoded-wrapper">
              
                  <jsp:include page="navbarmymenu.jsp"></jsp:include>
                  
                  <div class="pcoded-content">
                      <!-- Page-header start -->
                      <jsp:include page="page-header.jsp"></jsp:include>
                      <!-- Page-header end -->
                        <div class="pcoded-inner-content" >
                            <!-- Main-body start -->
                            <div class="main-body">
                                <div class="page-wrapper">
                                    <!-- Page-body start -->
                                    <div class="page-body">
                                        <div class="row">
                                            <!-- task, page, download counter  start -->
                                            <div style="max-width: 800px; margin: 50px auto; padding: 50px; border-radius: 10px; background-color: #fff; box-shadow: 0px 0px 10px #999;">
										      <h1 style="color: #0099cc; font-size: 48px; text-align: center;">Bem vindo ao meu site de estudos</h1>
										      <p style="color: #444; font-size: 24px; line-height: 1.5; margin: 20px auto; max-width: 600px; padding: 0 20px; text-align: center;">Aqui você encontrará as paginas de estudo que criei para colocar meu conhecimento em pratica.</p>
										      <p style="color: #444; font-size: 24px; line-height: 1.5; margin: 20px auto; max-width: 600px; padding: 0 20px; text-align: center;">Navegue pelo site para testar todas os conteudos que usei para treinar!</p>
										      <p style="color: #444; font-size: 24px; line-height: 1.5; margin: 20px auto; max-width: 600px; padding: 0 20px; text-align: center;">Fique à vontade para <a href="https://github.com/YuriRCosta" style="color: #0099cc; font-size: 24px; text-decoration: none;">entrar em contato</a> caso tenha alguma sugestão ou dúvida.</p>
										    </div>
                                            <!--  project and team member end -->
                                        </div>
                                    </div>
                                    <!-- Page-body end -->
                                </div>
                                <div id="styleSelector"> </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

<jsp:include page="jsfile.jsp"></jsp:include>
</body>

</html>
