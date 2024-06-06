package com.gpeter.demoparkapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class UsuarioITRestAssured {

    @Test
    public void findOneID() {

        given().log().all().
            pathParam("id", 2).
        when().
                get("http://localhost:8080/api/v1/usuarios/{id}").
        then().log().all().statusCode(200).
            body("id", is(2)).
            body("username", is("luna@email.com")).
            body("role", is("ADMIN"));
	}
    
    @Test
    public void findIdNotFound() {

        given().log().all()
            .pathParam("id", 91)
            .when()
                .get("http://localhost:8080/api/v1/usuarios/{id}")
            .then().log().all()
                .statusCode(404)
                .body("method", is("GET"))
                .body("status", is(404))
                .body("message", is("Usuário id=91 não encontrado"));
    }


    @Test
    public void findListAll() {

        given().log().all().
                when().get("http://localhost:8080/api/v1/usuarios").then().log().all()
                .statusCode(200).body("$.size()", is(9)).body("id[0]", is(1))
                .body("username[0]", is("gabriel@email.com")).body("role[0]", is("ADMIN"));
    }
    
    @Test
    public void testCreateAUser() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("username", "ronaldotolentine@gmail.com.br");
        jsonAsMap.put("password", "123456");
        

        given().log().all().
            contentType(ContentType.JSON).
            body(jsonAsMap).
        when().log().all().
            post("http://localhost:8080/api/v1/usuarios").
        then().log().all()
            .statusCode(201).
            body("username", is(jsonAsMap.get("username"))).
            body("username", is("marcosdeandrade@email.com.br")).
            body("role",is("CLIENTE"));    
	}
    @Test
    public void testCreateAUserExistent() {

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("username", "marcosdeandrade@email.com.br");
        jsonAsMap.put("password", "123456");


        given().log().all().
            contentType(ContentType.JSON).
            body(jsonAsMap).
        when().log().all().
            post("http://localhost:8080/api/v1/usuarios").
        then().log().all()
            .statusCode(409).
            body("statusText", is("Conflict")).
            body("message",is("Username {marcosdeandrade@email.com.br} já cadastrado"));    
	}
    @Test
    public void testCreateFaltandoParametro() {

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("username", "marcosdeandrade@email.com.br");

        given().log().all().contentType(ContentType.JSON).body(jsonAsMap).when().log().all()
                .post("http://localhost:8080/api/v1/usuarios").then().log().all()
                .statusCode(422)
                .body("statusText", is("Unprocessable Entity"))
                .body("message", is("Campo(s) invalido(s)"))
                .body("errors.size()", is(1))
                .body("errors.password", is("não deve estar em branco"));
    }

    @Test
    public void testUpadatePassword() {

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("senhaAtual", "333333");
        jsonAsMap.put("novaSenha", "333333");
        jsonAsMap.put("confirmaSenha", "333333");

        given().log().all().pathParam("id", 1).contentType(ContentType.JSON).body(jsonAsMap)
                .when().log().all()
                .patch("http://localhost:8080/api/v1/usuarios/{id}")
                .then().log().all()
                .statusCode(204);

    }
    
    @Test
    public void testUpadatePasswordInvalid() {

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("senhaAtual", "333334");
        jsonAsMap.put("novaSenha", "333333");
        jsonAsMap.put("confirmaSenha", "333333");

        given().log().all().pathParam("id", 1).contentType(ContentType.JSON).body(jsonAsMap)
                .when().log().all()
                .patch("http://localhost:8080/api/v1/usuarios/{id}")
                .then().log().all()
                .statusCode(400).body("statusText", is("Bad Request"))
                .body("message", is("Senha incorreta"));

    }

    @Test
    public void UpdatePasswordIdNotFound() {

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("senhaAtual", "333334");
        jsonAsMap.put("novaSenha", "333333");
        jsonAsMap.put("confirmaSenha", "333333");

        given().log().all().pathParam("id", 200).contentType(ContentType.JSON).body(jsonAsMap)
                .when().log().all()
                .patch("http://localhost:8080/api/v1/usuarios/{id}")
                .then().log().all()
                .statusCode(404)
                .body("statusText", is("Not Found"))
                .body("message", is("Usuario id=200 não encontrado"));

    }
    
}
