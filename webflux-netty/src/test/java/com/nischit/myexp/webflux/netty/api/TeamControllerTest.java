package com.nischit.myexp.webflux.netty.api;

import com.nischit.myexp.webflux.domain.TeamDetails;
import com.nischit.myexp.webflux.netty.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @Mock
    private TeamService mockTeamService;

    @InjectMocks
    private TeamController teamController;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("When subscriber/publisher is the main thread")
    public void withSubscriberBeingMain() {
        when(mockTeamService.createTeam(any(TeamDetails.class))).thenReturn(Mono.just(new TeamDetails.Builder()
                .teamDesc("someteam")
                .teamId("id")
                .teamName("somename")
                .build()));
        final TeamDetails teamDetails = new TeamDetails.Builder().teamDesc("some team").teamId("US123").teamName("Phenoix").build();
        final Mono<ResponseEntity<Void>> voidResponseEntity = teamController.createTeam(teamDetails);
        voidResponseEntity.subscribe(response -> {
            System.out.println(String.format("Subscriber thread %s", Thread.currentThread()));
        });
    }

    @Test
    @DisplayName("When publisher is a different thread")
    public void withPublisherIsDifferentThread() throws InterruptedException {
        when(mockTeamService.createTeam(any(TeamDetails.class))).thenReturn(Mono.just(new TeamDetails.Builder()
                .teamDesc("someteam")
                .teamId("id")
                .teamName("somename")
                .build()));
        final TeamDetails teamDetails = new TeamDetails.Builder().teamDesc("some team").teamId("US123").teamName("Phenoix").build();
        final Mono<ResponseEntity<Void>> voidResponseEntity = Mono.just(teamDetails)
                .publishOn(Schedulers.elastic())
                .flatMap(details -> teamController.createTeam(teamDetails));
        voidResponseEntity.subscribe(response -> {
            System.out.println(String.format("Subscriber thread %s", Thread.currentThread()));
        });
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("When subscriber is a different thread")
    public void withSubscriberIsDifferentThread() throws InterruptedException {
        when(mockTeamService.createTeam(any(TeamDetails.class))).thenReturn(Mono.just(new TeamDetails.Builder()
                .teamDesc("someteam")
                .teamId("id")
                .teamName("somename")
                .build()));
        final TeamDetails teamDetails = new TeamDetails.Builder().teamDesc("some team").teamId("US123").teamName("Phenoix").build();
        final Mono<ResponseEntity<Void>> voidResponseEntity = Mono.just(teamDetails)
                .subscribeOn(Schedulers.elastic())
                .flatMap(details -> teamController.createTeam(teamDetails));
        voidResponseEntity.subscribe(response -> {
            System.out.println(String.format("Subscriber thread %s", Thread.currentThread()));
        });
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Understand subscribeOn/publishOn")
    public void withPublishOnAndSubscribeOn() throws InterruptedException {
        Flux.range(0, 2)
                .map(integer -> {
                    System.out.println(integer + " before doOnNext using thread: " + Thread.currentThread().getName());
                    return integer;
                })
                //.subscribeOn(Schedulers.parallel())
                .doOnNext(s -> System.out.println(s + " before publishOn using thread: " + Thread.currentThread().getName()))
                .publishOn(Schedulers.newSingle("first"))
                .map(integer -> {
                    System.out.println(integer + " after1 publishOn using thread: " + Thread.currentThread().getName());
                    return integer;
                })
                .publishOn(Schedulers.newSingle("second"))
                // the rest is influenced by publishOn
                .doOnNext(s -> System.out.println(s + " after2 publishOn using thread: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.single())
                .subscribe(integer -> {
                    System.out.println(" consumer processed "
                            + integer + " using thread: " + Thread.currentThread().getName());
                });

        Thread.sleep(5_000);
    }
}