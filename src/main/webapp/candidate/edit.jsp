<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>

    <script>
        function validate() {

            if ($('#name').val() === '') {
                alert($('#name').attr('title'));
            }
            return false;
        }

        $(document).ready(function() {
            $.ajax({
                type: 'GET',
                crossdomain: true,
                url: 'http://localhost:8080/dreamjob/city',
                dataType: 'json'
            }).done(function(data) {
                console.log(data);
                for (let i = 0; i < data.length; i++) {
                    $('#idCity').append('<option value=' + data[i].id + '>' + data[i].name + '</option>');
                }
            }).fail(function(err){
                alert(err);
            });
        });
    </script>
    <title>Работа мечты</title>
</head>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "", 0);
    if (id != null) {
        candidate = PsqlStore.instOf().canFindById(Integer.parseInt(id));
    }
%>
<body>
<div class="container pt-3">
    <div class="row">
        <div class="row">
            <ul class="nav">

            </ul>
        </div>
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новая вакансия.
                <% } else { %>
                Редактирование вакансии.
                <% } %>
            </div>
            <div class="card-body">
                    <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post">
                        <div class="form-group">
                            <label for="name">Имя</label>
                            <input type="text" class="form-control" id="name"  value="<%=candidate.getName()%>" title="введите  имя">
                        </div>
                        < <div class="form-group">
                        <label>Город</label>
                        <select id="city">
                            <option value="<%=candidate.getId()%>"></option>
                        </select>
                    </div>
                        <button type="submit" class="btn btn-primary" onclick="return validate();">Сохранить</button>
                    </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>