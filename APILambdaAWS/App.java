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
