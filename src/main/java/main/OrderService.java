package main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import lombok.Data;
import java.time.LocalDate;
import java.util.*;

public class OrderService {

    final String path = "C:\\Users\\maibo\\IdeaProjects\\JSON\\src\\main\\resources\\applications.json";
    ObjectMapper objectMapper = new ObjectMapper();

    private final Map<UUID, Application> storage = Collections.emptyMap();
    public OrderService() throws IOException {}

    //найти все заявки по clientId
    public List<Application> findApplicationsByClientId(String clientId) throws IOException {
        List<Application> registration = objectMapper.readValue(new File(path), new TypeReference<List<Application>>() {});
        return registration;
    }

    //найти всех заявки у которых нету продуктов
    public List<Application> findApplicationsWithoutProducts() {
        throw new UnsupportedOperationException();
    }

    //найти все действия которые были совершенны указанным сотрудником
    public List<Action> getActionsByEmployee(String employeeId) {
        throw new UnsupportedOperationException();
    }

    //найти все заявки у которых есть Action с указанным типом
    public List<Application> findApplicationsByActionType(ActionType actionType) {
        throw new UnsupportedOperationException();
    }

    //найти все продукты, у которых в описании (description) содержится указанное слово
    public List<Product> findProductsByDescription(String description) {
        throw new UnsupportedOperationException();
    }

    //найти всех клиентов, у которых отсутствует действие с типом DELETE и количество продуктов более одного
    public Set<String> findClientsByActionAndProducts() {
        throw new UnsupportedOperationException();
    }

    //найти все заявки, которые были созданы неделю назад (есть тип CREATE, но нет типа DElETE)
    public List<Application> findNewApplications() {
        throw new UnsupportedOperationException();
    }

    //найти все продукты клиента
    public List<Product> findClientProducts(String clientId) {
        throw new UnsupportedOperationException();
    }

    //найти список всех сотрудников и их самых частых действий (пример, если сотрудник 1 выполнил 2 UPDATE и 1 READ - то возвращаем 1 + UPDATE)
    public Map<String, ActionType> findEmployeeActions() {
        throw new UnsupportedOperationException();
    }

    //Информация о заявке клиента
    @Data
    private static class Application {
        private UUID id;
        private String clientId;
        private List<Action> actions;
        private List<Product> products;

        //generate getters, setters and constructors
    }

    //Информация о действиях совершенных над заявкой
    @Data
    private static class Action {
        private UUID actionId;
        private String employeeId;
        private ActionType actionType;
        private LocalDate executionTime;

        //generate getters, setters and constructors
    }

    //Информация о продуктах прикрепленных к заявке
    @Data
    private static class Product {
        private String productId;
        private String description;
        //generate getters, setters and constructors
    }

    private enum ActionType {
        READ,
        UPDATE,
        CREATE,
        DELETE
    }

    public static void main(String[] args) throws IOException {
        OrderService orderService = new OrderService();
        System.out.println(orderService.findApplicationsByClientId("333333"));
    }

}
