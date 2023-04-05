package br.com.rodrigo.nascimento.handler;

import br.com.rodrigo.nascimento.domain.Car;
import br.com.rodrigo.nascimento.domain.ResponseApi;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static br.com.rodrigo.nascimento.mapper.CarToItemMapper.mapper;

public class Handler implements RequestStreamHandler {

    private DynamoDB dynamoDb;

    private final String TABLE_NAME = "Car";

    private ObjectMapper mapper;

    private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_EAST_1));
        this.dynamoDb = new DynamoDB(client);
        this.mapper = new ObjectMapper();
    }

    private void persistData(Car car) {
        var item = mapper(car);
        this.dynamoDb.getTable(TABLE_NAME).putItem(item);
    }

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        System.out.println("Initializing...");

        this.initDynamoDbClient();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ResponseApi response;
        Map<String, String> headers = new HashMap<>();
        final String jsonResponse;

        try {
            final var car = buildNewCar(reader);

            this.persistData(car);

            System.out.println("Create items with sucess lambda dynamodb");
            headers.put("x-custom-header", "my custom header value");
            response = new ResponseApi(200, headers, mapper.writeValueAsString(car));

        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
            response = new ResponseApi(500, headers, "Internal Server Error");
        }

        jsonResponse = mapper.writeValueAsString(response);

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        writer.write(jsonResponse);

        writer.close();
    }

    private Car buildNewCar(BufferedReader reader) throws IOException {
        var car = mapper.readValue(reader, Car.class);
        car.setId(UUID.randomUUID().toString());
        return car;
    }
}
