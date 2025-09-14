package com.application.mybillbook;


import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Collections;

public class MongoConnection {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String DBNAME = "MyBillBook";
    private static final String user = "sundarrk";
    private static final String password = "Kanmani5";
    public static void main(String[] args) {
        MongoCredential credential = MongoCredential.createCredential(user, DBNAME, password.toCharArray());

        MongoClientSettings mongoSettings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(HOST, PORT))))
                .credential(credential).build();
        try(MongoClient mongoClient = MongoClients.create(mongoSettings)){
            MongoDatabase database = mongoClient.getDatabase(DBNAME);
            System.out.println("Connected to database successfully");
            for(String name: database.listCollectionNames()){
                System.out.println("Collection name: "+ name);
            }
        }catch(Exception e){
            System.out.println("Error connecting to database"+e.getMessage());
        }
    }
}
