<h1 align="center">
:books: <br>Como subir um metodo handler na AWS Lambda
</h1>


## ⏯️ Requisitos

- Você precisará ter o [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11) instalado no seu computador;
- Instalar o AWS SAM CLI
- Configurar sua chave privada e security key da AWS com o comando ```aws configure```

---

## ✔️ Passo 01: Criar Projeto no java
Você precisará criar um projeto no java com maven ou gradle.

---

## ✔️ Passo 02: Inserir as dependências necessárias

Adicione as seguintes dependências no arquivo pom.xml para permitir a execução do código AWS Lambda:

### Maven

```
<dependencies>
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-lambda-java-core</artifactId>
            <version>1.2.2</version>
        </dependency>
        <dependency>
          <groupId>com.amazonaws</groupId>
          <artifactId>aws-lambda-java-events</artifactId>
          <version>3.11.0</version>
        </dependency>
        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.13.2</version>
          <scope>test</scope>
        </dependency>
    </dependencies>
```

### Gradle

```
dependencies {
    implementation 'com.amazonaws:aws-lambda-java-core:1.2.2'
    implementation 'com.amazonaws:aws-lambda-java-events:3.11.0'
    testImplementation 'junit:junit:4.13.2'
}

```

---

## ✔️ Passo 03: Criar a classe Java para a função Lambda

Crie uma classe Java que implemente a interface RequestHandler com o código da sua função Lambda.

```
package APILambdaAWS;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String numeroQueryOarameter = input.getQueryStringParameters().get("numero");
        if (numeroQueryOarameter == null) {
            return response
                    .withBody("{\"error\": \"Parâmetro 'numero' não encontrado na consulta\"}")
                    .withStatusCode(400);
        }

        String multiplicadorQueryOarameter = input.getQueryStringParameters().get("multiplicador");
        if (multiplicadorQueryOarameter == null) {
            return response
                    .withBody("{\"error\": \"Parâmetro 'multiplicador' não encontrado na consulta\"}")
                    .withStatusCode(400);
        }

        int numero, multiplicador;
        try {
            numero = Integer.parseInt(numeroQueryOarameter);
            multiplicador = Integer.parseInt(multiplicadorQueryOarameter);
        } catch (NumberFormatException e) {
            return response
                    .withBody("{\"error\": \"Valor de 'numero' ou 'multiplicador' inválido\"}")
                    .withStatusCode(400);
        }

        int result = numero * multiplicador;

        String output = String.format("{\n" +
                "   \"mensagem\":\"Resultado da multiplicação\",\n" +
                "   \"code\":\"200\",\n" +
                "   \"número\": " + "\""  + numero + "\", \n" +
                "   \"multiplicador\": " + "\""  + multiplicador + "\", \n" +
                "   \"resultado\": " + "\""  + result + "\", \n" +
                "}");

        return response
                .withStatusCode(200)
                .withBody(output);
    }
}
```

### Para definir os Path Params necessario para sua API Gateway usar o seguinte código: 

OBS: Alterar o nome do parametro do metodo get para o que você desejar.

```
String numeroQueryOarameter = input.getQueryStringParameters().get("numero");        
```

---

## ✔️ Passo 04: Compilar o projeto

Acessar o seu terminal de preferencia, entre na pasta no qual você tem o metodo handler e execute o seguinte comando:

OBS.: Para rodar esse comando é necessário ter o maven instalado na sua máquina.

```
mvn clean package
```

Depois que executar o comando vai ser gerato um .jar em sua pasta target.

---

## ✔️ Passo 05: Criar a função Lambda via AWS SAM CLI.

```
aws lambda create-function --function-name NOMEDAFUNCAOAWSLAMBDA \
    --zip-file fileb://target/NOMEDOARQUIVOJAR-1.0.jar \
    --handler PastaAnteriorAondeEstaOMetotoHandler.App::handleRequest \
    --runtime java11 \
    --role arn:aws:iam::123234345456:role/lambdaaws
```


---

## ✔️ Passo 06: Configurar a função Lambda

Para você fazer o teste acessar a console e ir na aba test e colocar em Event JSON o seguinte JSON para teste:

```
{
  "httpMethod": "GET",
  "queryStringParameters": {
    "numero": "10",
    "multiplicador": "5"
  }
}
```

Colocar depois de queryStringParameters os respectivo Path Params criados.

---

## ✔️ Passo 07: Configurando AWS API Gateway

Em sua Função Lambda clique em Add trigger e selecione API Gateway e sia os seguintes passos:

- Create a new API
- REST API
- Security -> Open

Depois só acessar o API Gateway e pegar o endpoint e colocar os path params e testar: 

```
?numero=10&multiplicador=5
```

---

## 👨‍💻 Autor

Nome: Guilherme Januário <br>Linkedin: https://www.linkedin.com/in/guilherme-janu%C3%A1rio/

---

<h4 align=center>Made with 💚 by <a href="https://github.com/guiijanuario">Guilherme Januário</a></h4>