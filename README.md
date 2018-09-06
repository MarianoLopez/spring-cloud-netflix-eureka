# spring-cloud-netflix-eureka
Spring-boot microservices first steps with: configuration server, eureka, zuul, feign, hystrix

Services:<br>
<ul>
  <li>config-service: Spring Cloud Config Server provides an HTTP resource-based API for external configuration (application.properties files). <b>NOTE</b>: config.server.git-repository (with the application.properties of each service) is ignored</li>
  <li>eureka-service: Eureka Service Discovery enables client-side load-balancing and decouples service providers from consumers without the need for DNS</li>
  <li>gateway-service: Intelligent Routing (Zuul) provides dynamic routing. e.g: gateway ip:gateway port/service name/service end-point. localhost:8080/user-service/user -> invoke user end-point from user-service</li>
  <li>user-service: A user-role api with mongodb, swagger, json-web-token, spring-boot-security. Provides crud & token generation/check</li>
  <li>
    test-service: Just a test of Ribbon (Client Side Load Balancer) - Feign (Feign is a Java to HTTP client binder) & hystrix (Circuit Breaker)
    <ul>
      <li>Has a secure /message end-point with fake data</li>
      <li>JWT middleware filter (which make a call to user-service to get username & authorities from a jwt token. Circuit Breaker pattern can allow a microservice to continue operating when a related service fails, preventing the failure from cascading and giving the failing service time to recover)</li>
    </ul>
  </li>
</ul>


