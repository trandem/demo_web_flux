//package com.example.demowebflux2.handler;
//
//import com.example.demowebflux2.web.User;
//import com.example.demowebflux2.web.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//@Component
//@RequiredArgsConstructor
//public class UserHandler {
//
//    private final UserService userService;
//
//    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
//        return ServerResponse
//                .ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(userService.getAllUser(), User.class);
//    }
//
//    public Mono<ServerResponse> getUserById(ServerRequest request) {
//        return userService
//                .findById(request.pathVariable("userId"))
//                .flatMap(user -> ServerResponse
//                        .ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(user, User.class))
//                .switchIfEmpty(ServerResponse.notFound().build());
//    }
//
//    public Mono<ServerResponse> create(ServerRequest request) {
//        Mono<User> userMono = request.bodyToMono(User.class);
//
//        return userMono.flatMap(user -> ServerResponse
//                .status(HttpStatus.CREATED)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(userService.createUser(user), User.class));
//    }
//
//    public Mono<ServerResponse> updateUserById(ServerRequest request) {
//        String id = request.pathVariable("userId");
//        Mono<User> updateUser = request.bodyToMono(User.class);
//
//        return updateUser.flatMap(user -> ServerResponse
//                .ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(userService.updateUser(id, user), User.class));
//
//    }
//
//    public Mono<ServerResponse> deleteUserById(ServerRequest request) {
//        final String id = request.pathVariable("userId");
//
//        return userService.deleteUser(id)
//                .flatMap(user -> ServerResponse.ok()
//                        .body(user, User.class)
//                        .switchIfEmpty(ServerResponse.notFound().build()));
//    }
//}
