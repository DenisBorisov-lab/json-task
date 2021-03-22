package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

import lombok.Data;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderService {

    private final String value = new String(Files.readAllBytes(Paths.get("C:\\Users\\maibo\\IdeaProjects\\JSON\\src\\main\\resources\\applications.json")));
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Map<UUID, Application> storage = Collections.emptyMap();
    private List<Application> registration = objectMapper.readValue(value, new TypeReference<List<Application>>() {});

    public OrderService() throws IOException {
    }

    //найти все заявки по clientId
    public List<Application> findApplicationsByClientId(String clientId) throws JsonProcessingException {

        return registration
                .stream()
                .filter(app -> app.clientId.equals(clientId))
                .collect(Collectors.toList());
    }

    //найти всех заявки у которых нету продуктов
    public List<Application> findApplicationsWithoutProducts() {
        return registration.stream()
                .filter(s -> s.getProducts().size() == 0)
                .collect(Collectors.toList());
    }

    //найти все действия которые были совершенны указанным сотрудником
    public List<Action> getActionsByEmployee(String employeeId) {
        List<Action> result  = new ArrayList<>();
        for (Application app : registration){
            for (Action action : app.getActions()){
                if (action.getEmployeeId().equals(employeeId)){
                    result.add(action);
                }
            }
        }

        return result;

    }

    //найти все заявки у которых есть Action с указанным типом
    public List<Application> findApplicationsByActionType(ActionType actionType) {
        List<Application> result = new ArrayList<>();
        for (Application app : registration){
            for (Action action : app.getActions()){
                if (action.getActionType().equals(actionType)){
                    result.add(app);
                }
            }
        }
        return result;
    }

    //найти все продукты, у которых в описании (description) содержится указанное слово
    public List<Product> findProductsByDescription(String description) {
        List<Product> result = new ArrayList<>();
        for (Application app : registration){
            for (Product product : app.getProducts()){
                if (product.getDescription().equals(description)){
                    result.add(product);
                }
            }
        }
        return result;
    }

    //найти всех клиентов, у которых отсутствует действие с типом DELETE и количество продуктов более одного
    public Set<String> findClientsByActionAndProducts() {
        Set<String> result = new HashSet<>();
        for (Application app : registration){
            int cnt = 0;
            if (app.getProducts().size() > 1){
                for (Action action : app.getActions()){
                    if (action.getActionType().equals(ActionType.DELETE)){
                        cnt++;
                    }
                }
                if (cnt == 0){
                    result.add(app.clientId);
                } else if (cnt > 0 && result.contains(app.getClientId())){
                    result.remove(app.getClientId());
                }
            }
        }
        return result;
    }

    //найти все заявки, которые были созданы неделю назад (есть тип CREATE, но нет типа DElETE)
    public List<Application> findNewApplications() {
        List<Application> result = new ArrayList<>();
        for (Application application : registration){
            int del = 0;
            int create = 0;
            for (Action action : application.getActions()){
                if (action.getActionType().equals(ActionType.DELETE)){
                    del++;
                }else if (action.getActionType().equals(ActionType.CREATE)){
                    create++;
                }
            }
            if (del == 0 && create >= 1){
                result.add(application);
            }
        }
        return result;
    }

    //найти все продукты клиента
    public List<Product> findClientProducts(String clientId) {
        List<Product> result = new ArrayList<>();
        for (Application app : registration){
            if (app.clientId.equals(clientId)){
                result.addAll(app.getProducts());
            }
        }
        return result;
    }

    //найти список всех сотрудников и их самых частых действий (пример, если сотрудник 1 выполнил 2 UPDATE и 1 READ - то возвращаем 1 + UPDATE)
    public Map<String, ActionType> findEmployeeActions() {
        Map<String, ActionType> result = new HashMap<>();
        Set<String> employees = new HashSet<>();
        for (Application app : registration){
            for (Action action : app.getActions()){
                employees.add(action.getEmployeeId());
            }
        }

        for (String employee : employees){
            List<ActionType> actionTypes = new ArrayList<>();
            for (Application application : registration){
                for (Action action : application.getActions()){
                    if (action.getEmployeeId().equals(employee)){
                        actionTypes.add(action.getActionType());
                    }
                }
            }
            result.put(employee, cnt(actionTypes));
        }
        return result;
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

    public ActionType cnt(List<ActionType> actionTypes){
        int delete = 0;
        int create = 0;
        int read = 0;
        int update = 0;
        for (ActionType actionType : actionTypes){
            switch (actionType){
                case READ: read++;
                    break;
                case UPDATE: update++;
                    break;
                case CREATE: create++;
                    break;
                case DELETE: delete++;
                    break;
            }
        }
        if (Math.max(delete, Math.max(create, Math.max(read, update))) == read){
            return ActionType.READ;
        }else if (Math.max(delete, Math.max(create, Math.max(read, update))) == create){
            return ActionType.CREATE;
        }else if (Math.max(delete, Math.max(create, Math.max(read, update))) == delete){
            return ActionType.DELETE;
        }else {
            return ActionType.UPDATE;
        }

    }

    @SneakyThrows
    public static void main(String[] args) throws JsonProcessingException {
        OrderService orderService = new OrderService();
        System.out.println("findApplicationsByClientId:");
        System.out.println(orderService.findApplicationsByClientId("333333"));
        System.out.println("-----------------------------------------------");
        System.out.println("findApplicationsWithoutProducts:");
        System.out.println(orderService.findApplicationsWithoutProducts());
        System.out.println("-----------------------------------------------");
        System.out.println("getActionsByEmployee:");
        System.out.println(orderService.getActionsByEmployee("second"));
        System.out.println("-----------------------------------------------");
        System.out.println("findApplicationsByActionType:");
        System.out.println(orderService.findApplicationsByActionType(ActionType.READ));
        System.out.println("-----------------------------------------------");
        System.out.println("findProductsByDescription");
        System.out.println(orderService.findProductsByDescription("Кредитная заявка"));
        System.out.println("-----------------------------------------------");
        System.out.println("findClientsByActionAndProducts:");
        System.out.println(orderService.findClientsByActionAndProducts());
        System.out.println("-----------------------------------------------");
        System.out.println("findNewApplications:");
        System.out.println(orderService.findNewApplications());
        System.out.println("-----------------------------------------------");
        System.out.println("findClientProducts");
        System.out.println(orderService.findClientProducts("333333"));
        System.out.println("-----------------------------------------------");
        System.out.println("findEmployeeActions:");
        System.out.println(orderService.findEmployeeActions());

    }

}
