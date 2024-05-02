#!/usr/bin/env bash
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=catalog-service \
--package-name=com.library.catalog \
--groupId=com.library.catalog \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
catalog-service
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=fines-service \
--package-name=com.library.fines \
--groupId=com.library.fines \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
fines-service
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=loans-service \
--package-name=com.library.loans \
--groupId=com.library.loans \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
loans-service
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=patrons-service \
--package-name=com.library.patrons \
--groupId=com.library.patrons \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
patrons-service
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=api-gateway \
--package-name=com.library.apigateway \
--groupId=com.library.apigateway \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
api-gateway