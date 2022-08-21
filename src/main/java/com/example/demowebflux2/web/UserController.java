package com.example.demowebflux2.web;

import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.reactivestreams.Publisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    static OkHttpClient client = new OkHttpClient();

    private Mono<User> findOneUser() {
        return Mono
                .fromCallable(() -> {
                    Thread.sleep(1000);
                    System.out.println("find one Thread " + Thread.currentThread().getId());
                    return User.builder()
                            .id("1234")
                            .age(14)
                            .name("eeeee")
                            .build();
                }).subscribeOn(Schedulers.boundedElastic());
    }

    public static Mono<String> query4() {
        return Mono.fromCallable(() -> {
            Request request = new Request.Builder()
                    .url("https://google.com")
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            System.out.println("thread call network " + Thread.currentThread().getId());

            int code = response.code();
            response.close();
            Thread.sleep(1000);
            return code + "dddddd";
        }).publishOn(Schedulers.boundedElastic());
    }


    @GetMapping("/test")
    public Mono<ResponseEntity<User>> getUserById() {
        long start = System.currentTimeMillis();
        System.out.println("receive request thread " + Thread.currentThread().getId());
        Mono<User> userMono = findOneUser();
        Mono<String> netWorkCall = query4();

        var rs = Mono.zip(List.of(userMono, netWorkCall), new Function<Object[], User>() {
            @Override
            public User apply(Object[] objects) {
                var rsQuery = (String) objects[1];
                var user = (User) objects[0];
                user.setDepartment(rsQuery);
                long end = System.currentTimeMillis();
                System.out.println(end - start);
                return user;
            }
        });


        return rs.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Flux<Object> findMultiUser() {
        return Flux
                .generate(() -> 1, (state, sink) -> {
                    System.out.printf("state : %d , thread : %d%n", state, Thread.currentThread().getId());
                    state++;
                    if (state < 5) {
                        var user = User.builder()
                                .id("1234eee")
                                .age(state)
                                .name("eeeee")
                                .build();
                        sink.next(user);
                    } else {
                        sink.complete();
                    }
                    return state;
                });
    }

    /**
     * when error exist :
     * <p>
     *     - return with empty list
     * </p>
     * <p>
     *     - return list success, list error
     * </p>
     * @return
     */
    @GetMapping("/search")
    public Mono<ListUserResponse> searchUsers() {
        return findMultiUser()
                .flatMap(x -> Flux.just(x)
                        .map(z -> (User) z)
                        .map(y -> {
                            System.out.println("Thread nested: " + Thread.currentThread().getId());
                            if (y.getAge() == 4) {
                                throw new RuntimeException("trree");
                            }
                            return y;
                        })
                        .subscribeOn(Schedulers.boundedElastic())
                ).map(x -> (User) x)
                .onErrorResume(throwable -> Flux.empty())
                .collectList()
                .map(ListUserResponse::new);
    }
}
