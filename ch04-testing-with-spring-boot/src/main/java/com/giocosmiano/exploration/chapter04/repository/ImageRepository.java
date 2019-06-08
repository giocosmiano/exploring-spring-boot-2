package com.giocosmiano.exploration.chapter04.repository;

import com.giocosmiano.exploration.chapter04.domain.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, String> {

    /*
     This interface extends ReactiveCrudRepository , which, as stated before, comes with a prepackaged set
     of reactive operations including save, findById, exists, findAll, count, delete, and deleteAll, all
     supporting Reactor types

     Custom finder named findByName that matches on `Image.name` based on parsing the name of the method (not the input argument)
     */

    Mono<Image> findByName(String name);

    /*
     Each of the operations inherited from ReactiveCrudRepository accepts direct arguments or a Reactor-friendly variant.
     This means, we can invoke either save(Image) or saveAll(Publisher<Image>). Since Mono and Flux both implement Publisher,
     saveAll() can be used to store either.

     ReactiveCrudRepository has ALL of its methods returning either a Mono or a Flux based on the situation.
     Some, like delete , simply return Mono<Void>, meaning, there is no data to return, but we need the
     operation's handle in order to issue the Reactive Streams' subscribe call. findById returns a Mono<Image>,
     because there can be only one. And findAll returns a Flux<Image>.
     */


}
